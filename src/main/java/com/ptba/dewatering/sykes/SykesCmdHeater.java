package com.ptba.dewatering.sykes;

import java.util.Arrays;

import com.serotonin.modbus4j.BatchRead;
import com.serotonin.modbus4j.BatchResults;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.locator.BaseLocator;
import com.serotonin.modbus4j.msg.WriteCoilsRequest;
import com.serotonin.modbus4j.msg.WriteCoilsResponse;

public class SykesCmdHeater {
    public static void main( String[] args ) 
    {
        if (args.length < 2) {
            System.out.println("Required 2 arguments : [host] [command]");
        }
        else if(!Arrays.asList("start", "stop").contains(args[1].toLowerCase())) {
            System.out.println("Invalid command ! its start or stop.");
        }
        else {
            String host = args.length > 0 ? args[0] : "172.16.17.30";
            String command = args[1].toLowerCase();
            int port = 502;
            int slaveId = 1;

            System.out.println(String.format("%s command on %s ... ", command, host));
            
            IpParameters tcParameters = new IpParameters();
            tcParameters.setHost(host);
            tcParameters.setPort(port);

            ModbusFactory modbusFactory = new ModbusFactory();
            ModbusMaster modbusMaster = modbusFactory.createTcpMaster(tcParameters, false);
            modbusMaster.setTimeout(10000);
            modbusMaster.setRetries(1);
            
            // check mode is auto
            BatchRead<String> modeRead = new BatchRead<String>();
            

            try {
                modbusMaster.init();

                modeRead.addLocator("PB_ON_HEATER", BaseLocator.coilStatus(slaveId, 22));
                modeRead.addLocator("PB_OFF_HEATER", BaseLocator.coilStatus(slaveId, 23));

                BatchResults<String> modeResult = modbusMaster.send(modeRead);
                String onString = modeResult.getValue("PB_ON_HEATER").toString();
                String offString = modeResult.getValue("PB_OFF_HEATER").toString();

                if (command.equals("start") && onString.equals("true")) 
                    System.out.println("Heater is already ON !");
                else if (command.equals("stop") && offString.equals("true"))
                    System.out.println("Heater is already OFF !");
                else {
                    
                    WriteCoilsRequest request = new WriteCoilsRequest(slaveId, 22, command.equals("start") ? new boolean[]{true, false} : new boolean[]{false, true});
                    WriteCoilsResponse response = (WriteCoilsResponse) modbusMaster.send(request);

                    if (response.isException()) System.out.println(String.format("Command %s heater failed : %s", command, response.getExceptionMessage()));
                    else System.out.println(String.format("Command %s heater succeed.", command));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                modbusMaster.destroy();
            }
        }

    }
}
