package com.ptba.dewatering.sulzer;

import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.msg.WriteRegisterRequest;
import com.serotonin.modbus4j.msg.WriteRegisterResponse;

public class SulzerWriteSp {
    public static void main( String[] args ) 
    {
        if (args.length > 2) {
            String host = args.length > 0 ? args[0] : "172.16.15.60";
            String cmd = args[1];
            String val = args[2];
            int value = Integer.valueOf(val);
            int port = 502;
            int slaveId = 1;
            int offset = -1;

            System.out.println(String.format("Write SetPoint '%s' the params '%s' with value '%s' ... ", host, cmd, val));
            
            IpParameters tcParameters = new IpParameters();
            tcParameters.setHost(host);
            tcParameters.setPort(port);

            ModbusFactory modbusFactory = new ModbusFactory();
            ModbusMaster modbusMaster = modbusFactory.createTcpMaster(tcParameters, false);
            modbusMaster.setTimeout(10000);
            modbusMaster.setRetries(1);

            try {
                switch(cmd) {
                    case "SP_TEMPERATURE":
                        offset = 90;
                    break;
                    case "SP_BEARING":
                        offset = 91;
                    break;
                    case "SP_TILT_X":
                        offset = 92;
                    break;
                    case "SP_FREQ_AUTO":
                        offset = 94;
                    break;
                    case "SP_TILT_Y":
                        offset = 95;
                    break;
                    case "SP_UV":
                        offset = 96;
                    break;
                    case "SP_OV":
                        offset = 97;
                    break;
                }

                modbusMaster.init();

                WriteRegisterRequest request = new WriteRegisterRequest(slaveId, offset, value);
                WriteRegisterResponse response = (WriteRegisterResponse) modbusMaster.send(request);

                if (response.isException()) System.out.println("Exception response: message=" + response.getExceptionMessage());
                else System.out.println(String.format("SET %s => %s ... Success.", cmd, val));

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                modbusMaster.destroy();
            }
        }
        else System.out.println("Required 3 arguments : [host] [command] [value]");
    }
}
