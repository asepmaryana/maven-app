package com.ptba.dewatering.sulzer;

import com.serotonin.modbus4j.BatchRead;
import com.serotonin.modbus4j.BatchResults;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.locator.BaseLocator;
import com.serotonin.modbus4j.msg.ModbusResponse;
import com.serotonin.modbus4j.msg.WriteMaskRegisterRequest;

public class SulzerCommand 
{
    public static void main( String[] args )
    {
        if (args.length > 1) {
            System.out.println("Sending Command to ...");
            String host = args.length > 0 ? args[0] : "172.16.15.60";
            String cmd = args[1].toLowerCase();
            int port = 502;
            int slaveId = 1;

            System.out.println(String.format("Execute %s command to %s ...", cmd, host));
            
            IpParameters tcParameters = new IpParameters();
            tcParameters.setHost(host);
            tcParameters.setPort(port);

            ModbusFactory modbusFactory = new ModbusFactory();
            ModbusMaster modbusMaster = modbusFactory.createTcpMaster(tcParameters, false);
            modbusMaster.setTimeout(10000);
            modbusMaster.setRetries(3);

            try {
                // Read Param Awal
                BatchRead<String> batchRead = new BatchRead<String>();
                batchRead.addLocator("HH_VACUMM_PUMP", BaseLocator.holdingRegisterBit(slaveId, 10, 5));
                batchRead.addLocator("STAT_MAIN_PUMP_RUN", BaseLocator.holdingRegisterBit(slaveId, 10, 12));
                batchRead.addLocator("NOT_STAT_MAIN_PUMP_RUN", BaseLocator.holdingRegisterBit(slaveId, 10, 9));

                modbusMaster.init();
                BatchResults<String> results = modbusMaster.send(batchRead);

                String HH_VACUMM_PUMP = results.getValue("HH_VACUMM_PUMP").toString();
                String STAT_MAIN_PUMP_RUN = results.getValue("STAT_MAIN_PUMP_RUN").toString();
                String NOT_STAT_MAIN_PUMP_RUN = results.getValue("NOT_STAT_MAIN_PUMP_RUN").toString();

                StringBuffer sb = new StringBuffer();
                sb.append("-----------------------------\n");
                sb.append("Current Statuses : \n");
                sb.append("-----------------------------\n");
                sb.append("HH_VACUMM_PUMP = ").append(HH_VACUMM_PUMP).append("\n");
                sb.append("STAT_MAIN_PUMP_RUN = ").append(STAT_MAIN_PUMP_RUN).append("\n");
                sb.append("NOT_STAT_MAIN_PUMP_RUN = ").append(NOT_STAT_MAIN_PUMP_RUN).append("\n");
                sb.append("-----------------------------\n");
                System.out.println(sb.toString());
                
                WriteMaskRegisterRequest request = null;
                ModbusResponse response = null;

                if (cmd.equals("start")) {
                    if (STAT_MAIN_PUMP_RUN.equals("true")) System.out.println("Main Pump is already running.");
                    else {
                        // Pulse CMD_AUTO_SCADA
                        BaseLocator<Boolean> cmdAutoScada = BaseLocator.holdingRegisterBit(slaveId, 20, 0);
                        modbusMaster.setValue(cmdAutoScada, true);
                        Thread.sleep(1000);
                        modbusMaster.setValue(cmdAutoScada, false);
                        Thread.sleep(1000);

                        // Pulse CMD_SCADA_Vacumm_Pump
                        BaseLocator<Boolean> cmdScadaVacumPump = BaseLocator.holdingRegisterBit(slaveId, 20, 1);
                        modbusMaster.setValue(cmdScadaVacumPump, true);
                        Thread.sleep(1000);
                        modbusMaster.setValue(cmdScadaVacumPump, false);
                        Thread.sleep(1000);
                        /*
                        request = new WriteMaskRegisterRequest(slaveId, 10);
                        request.setBit(0, true);
                        response = modbusMaster.send(request);
                        
                        if (response.isException()) throw new Exception(response.getExceptionMessage());
                        else Thread.sleep(600);
                        
                        request.setBit(0, false);
                        response = modbusMaster.send(request);

                        if (response.isException()) throw new Exception(response.getExceptionMessage());
                        else Thread.sleep(600);

                        // Pulse CMD_SCADA_Vacumm_Pump
                        request.setBit(1, true);
                        response = modbusMaster.send(request);

                        if (response.isException()) throw new Exception(response.getExceptionMessage());
                        else Thread.sleep(600);

                        request.setBit(1, false);
                        response = modbusMaster.send(request);
                        */
                        System.out.println(String.format("Start command success.. Main Pump will be auto started after max 10 minutes."));
                    }
                } else {
                    //if (NOT_STAT_MAIN_PUMP_RUN.equals("true")) System.out.println("Main Pump is already stopped.");
                    //else {
                        // pulse CMD_SCADA_OFF_MAIN_PUMP
                        BaseLocator<Boolean> cmdScadaOffMainPump = BaseLocator.holdingRegisterBit(slaveId, 21, 11);
                        modbusMaster.setValue(cmdScadaOffMainPump, true);
                        Thread.sleep(1000);
                        
                        modbusMaster.setValue(cmdScadaOffMainPump, false);
                        Thread.sleep(1000);
                        /*
                        request = new WriteMaskRegisterRequest(slaveId, 11);
                        request.setBit(11, true);
                        response = modbusMaster.send(request);

                        if (response.isException()) throw new Exception(response.getExceptionMessage());
                        else Thread.sleep(600);

                        request.setBit(11, false);
                        response = modbusMaster.send(request);

                        if (response.isException()) throw new Exception(response.getExceptionMessage());
                        else Thread.sleep(600);
                        */
                        System.out.println(String.format("Stop command success.. Main Pump will be stopped immediately."));
                    //}
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                modbusMaster.destroy();
            }
        }
        else System.out.println("Required 2 arguments : [host] [command]");
    }
}
