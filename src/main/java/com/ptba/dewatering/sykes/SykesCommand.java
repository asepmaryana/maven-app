package com.ptba.dewatering.sykes;

import java.util.Arrays;

import com.serotonin.modbus4j.BatchRead;
import com.serotonin.modbus4j.BatchResults;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.locator.BaseLocator;
import com.serotonin.modbus4j.msg.WriteCoilRequest;
import com.serotonin.modbus4j.msg.WriteCoilResponse;
import com.serotonin.modbus4j.msg.WriteCoilsRequest;
import com.serotonin.modbus4j.msg.WriteCoilsResponse;

public class SykesCommand {
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
            modeRead.addLocator("S_MODE_AUTO", BaseLocator.coilStatus(slaveId, 30));

            try {
                modbusMaster.init();
                BatchResults<String> modeResult = modbusMaster.send(modeRead);
                String autoString = modeResult.getValue("S_MODE_AUTO").toString();
                if (autoString.equals("true")) {
                    // jika command start
                    if (command.equals("start")) {
                        boolean values[] = {true, false};
                        WriteCoilsRequest request = new WriteCoilsRequest(slaveId, 18, values);
                        WriteCoilsResponse response = (WriteCoilsResponse) modbusMaster.send(request);
                        //WriteCoilRequest request = new WriteCoilRequest(slaveId, 18, true);
                        //WriteCoilResponse response = (WriteCoilResponse) modbusMaster.send(request);
                        
                        //WriteCoilRequest request2 = new WriteCoilRequest(slaveId, 19, true);
                        //WriteCoilResponse response2 = (WriteCoilResponse) modbusMaster.send(request);

                        if (response.isException()) {
                            System.out.println(String.format("Command %s failed : %s", command, response.getExceptionMessage()));
                        } else {
                            System.out.println("Command is processing, please wait ...");
                            Thread.sleep(10000);
                            // read current meter
                            BatchRead<String> checkRead = new BatchRead<String>();
                            checkRead.addLocator("CURRENT_METER", BaseLocator.holdingRegister(slaveId, 9, DataType.FOUR_BYTE_FLOAT));
                            BatchResults<String> checkResult = modbusMaster.send(checkRead);
                            System.out.println(String.format("Current Meter = %f --> %.2f A", checkResult.getValue("CURRENT_METER"), (((Float)checkResult.getValue("CURRENT_METER")).floatValue()/(float)1)));
                            if (((Float)checkResult.getValue("CURRENT_METER")).floatValue() > 0) {
                                System.out.println(String.format("%s %s ... succeed.", command, host));
                            } else {
                                System.out.println(String.format("%s %s ... failed.", command, host));
                            }
                        }
                    } else {
                        boolean values[] = {false, true};
                        //WriteCoilRequest request = new WriteCoilRequest(slaveId, 18, false);
                        //WriteCoilResponse response = (WriteCoilResponse) modbusMaster.send(request);
                        WriteCoilsRequest request = new WriteCoilsRequest(slaveId, 18, values);
                        WriteCoilsResponse response = (WriteCoilsResponse) modbusMaster.send(request);
                        
                        if (response.isException()) {
                            System.out.println(String.format("Command %s failed : %s", command, response.getExceptionMessage()));
                        } else {
                            System.out.println("Command is processing, please wait ...");
                            Thread.sleep(10000);
                            // read current meter
                            BatchRead<String> checkRead = new BatchRead<String>();
                            checkRead.addLocator("CURRENT_METER", BaseLocator.holdingRegister(slaveId, 9, DataType.FOUR_BYTE_FLOAT));
                            BatchResults<String> checkResult = modbusMaster.send(checkRead);
                            if (((Float)checkResult.getValue("CURRENT_METER")).floatValue() == 0) {
                                System.out.println(String.format("%s %s ... succeed.", command, host));
                            } else {
                                System.out.println(String.format("%s %s ... failed.", command, host));
                            }
                        }
                    }
                } else {
                    System.out.println("Current Mode on Device is Local/Manual, Please Switched it to Remote/Auto !");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                modbusMaster.destroy();
            }
        }

    }
}
