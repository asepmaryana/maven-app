package com.ptba.dewatering.sykes;

import java.util.Date;

import com.serotonin.modbus4j.BatchRead;
import com.serotonin.modbus4j.BatchResults;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.locator.BaseLocator;

public class SykesCoil {
    public static void main( String[] args ) 
    {
        String host = args.length > 0 ? args[0] : "172.16.15.60";
        int port = args.length > 1 ? Integer.valueOf(args[1]) : 502;
        int slaveId = 1;

        System.out.println(String.format("Polling data from %s on port %d ...", host, port));
        
        IpParameters tcParameters = new IpParameters();
        tcParameters.setHost(host);
        tcParameters.setPort(port);

        ModbusFactory modbusFactory = new ModbusFactory();
        ModbusMaster modbusMaster = modbusFactory.createTcpMaster(tcParameters, false);
        modbusMaster.setTimeout(10000);
        modbusMaster.setRetries(1);

        BatchRead<String> batchRead = new BatchRead<String>();
        batchRead.addLocator("STATUS_RUN_VSD", BaseLocator.coilStatus(slaveId, 0));
        batchRead.addLocator("HEATER", BaseLocator.coilStatus(slaveId, 1));
        batchRead.addLocator("LUBRICANT", BaseLocator.coilStatus(slaveId, 2));
        batchRead.addLocator("A_PHASE_FAILURE", BaseLocator.coilStatus(slaveId, 3));
        batchRead.addLocator("A_EMERGENCY", BaseLocator.coilStatus(slaveId, 4));
        batchRead.addLocator("A_LUBRICANT", BaseLocator.coilStatus(slaveId, 5));
        batchRead.addLocator("A_FAULT_VSD", BaseLocator.coilStatus(slaveId, 6));
        batchRead.addLocator("A_FAULT_ROTATION", BaseLocator.coilStatus(slaveId, 7));
        batchRead.addLocator("A_OVER_CURRENT", BaseLocator.coilStatus(slaveId, 8));
        batchRead.addLocator("A_HIGH_PRESS_DISC", BaseLocator.coilStatus(slaveId, 9));
        batchRead.addLocator("A_HIGH_PRES_SUCT", BaseLocator.coilStatus(slaveId, 10));
        batchRead.addLocator("A_LOW_PRES_SUCT", BaseLocator.coilStatus(slaveId, 11));
        batchRead.addLocator("A_HIGH_UPPER_BEAR", BaseLocator.coilStatus(slaveId, 12));
        batchRead.addLocator("A_HIGH_LOWER_BEAR", BaseLocator.coilStatus(slaveId, 13));
        batchRead.addLocator("A_HIGH_WINDING_U", BaseLocator.coilStatus(slaveId, 14));
        batchRead.addLocator("A_HIGH_WINDING_V", BaseLocator.coilStatus(slaveId, 15));
        batchRead.addLocator("A_HIGH_WINDING_W", BaseLocator.coilStatus(slaveId, 16));
        batchRead.addLocator("A_FAULT_TILT", BaseLocator.coilStatus(slaveId, 17));

        batchRead.addLocator("PB_ON_RUN_VSD", BaseLocator.coilStatus(slaveId, 18));
        batchRead.addLocator("PB_OFF_RUN_VSD", BaseLocator.coilStatus(slaveId, 19));
        batchRead.addLocator("PB_ON_LUBRICANT", BaseLocator.coilStatus(slaveId, 20));
        batchRead.addLocator("PB_OFF_LUBRICANT", BaseLocator.coilStatus(slaveId, 21));
        batchRead.addLocator("PB_ON_HEATER", BaseLocator.coilStatus(slaveId, 22));
        batchRead.addLocator("PB_OFF_HEATER", BaseLocator.coilStatus(slaveId, 23));
        batchRead.addLocator("PB_START_AUTO", BaseLocator.coilStatus(slaveId, 24));
        batchRead.addLocator("PB_STOP_AUTO", BaseLocator.coilStatus(slaveId, 25));
        batchRead.addLocator("SILENT", BaseLocator.coilStatus(slaveId, 26));
        batchRead.addLocator("SET_FWD_PSD", BaseLocator.coilStatus(slaveId, 27));
        batchRead.addLocator("COM_PSD_LOSS", BaseLocator.coilStatus(slaveId, 28));
        batchRead.addLocator("RUN_FWD", BaseLocator.coilStatus(slaveId, 29));
        batchRead.addLocator("S_MODE_AUTO", BaseLocator.coilStatus(slaveId, 30));

        int counter = 0;
        try {
            modbusMaster.init();
            while (counter < 3) {
                counter++;
                batchRead.setContiguousRequests(false);
                BatchResults<String> results = modbusMaster.send(batchRead);
                System.out.println(new Date());
                System.out.println(String.format("STATUS_RUN_VSD = %s", results.getValue("STATUS_RUN_VSD")));
                System.out.println(String.format("HEATER = %s", results.getValue("HEATER")));
                System.out.println(String.format("LUBRICANT = %s", results.getValue("LUBRICANT")));
                System.out.println(String.format("A_PHASE_FAILURE = %s", results.getValue("A_PHASE_FAILURE")));
                System.out.println(String.format("A_EMERGENCY = %s", results.getValue("A_EMERGENCY")));
                System.out.println(String.format("A_LUBRICANT = %s", results.getValue("A_LUBRICANT")));
                System.out.println(String.format("A_FAULT_VSD = %s", results.getValue("A_FAULT_VSD")));
                System.out.println(String.format("A_FAULT_ROTATION = %s", results.getValue("A_FAULT_ROTATION")));
                System.out.println(String.format("A_OVER_CURRENT = %s", results.getValue("A_OVER_CURRENT")));
                System.out.println(String.format("A_HIGH_PRESS_DISC = %s", results.getValue("A_HIGH_PRESS_DISC")));
                System.out.println(String.format("A_HIGH_PRES_SUCT = %s", results.getValue("A_HIGH_PRES_SUCT")));
                System.out.println(String.format("A_LOW_PRES_SUCT = %s", results.getValue("A_LOW_PRES_SUCT")));
                System.out.println(String.format("A_HIGH_UPPER_BEAR = %s", results.getValue("A_HIGH_UPPER_BEAR")));
                System.out.println(String.format("A_HIGH_LOWER_BEAR = %s", results.getValue("A_HIGH_LOWER_BEAR")));
                System.out.println(String.format("A_HIGH_WINDING_U = %s", results.getValue("A_HIGH_WINDING_U")));
                System.out.println(String.format("A_HIGH_WINDING_V = %s", results.getValue("A_HIGH_WINDING_V")));
                System.out.println(String.format("A_HIGH_WINDING_W = %s", results.getValue("A_HIGH_WINDING_W")));
                System.out.println(String.format("A_FAULT_TILT = %s", results.getValue("A_FAULT_TILT")));

                System.out.println(String.format("PB_ON_RUN_VSD = %s", results.getValue("PB_ON_RUN_VSD")));
                System.out.println(String.format("PB_OFF_RUN_VSD = %s", results.getValue("PB_OFF_RUN_VSD")));
                System.out.println(String.format("PB_ON_LUBRICANT = %s", results.getValue("PB_ON_LUBRICANT")));
                System.out.println(String.format("PB_OFF_LUBRICANT = %s", results.getValue("PB_OFF_LUBRICANT")));
                System.out.println(String.format("PB_ON_HEATER = %s", results.getValue("PB_ON_HEATER")));
                System.out.println(String.format("PB_OFF_HEATER = %s", results.getValue("PB_OFF_HEATER")));
                System.out.println(String.format("PB_START_AUTO = %s", results.getValue("PB_START_AUTO")));
                System.out.println(String.format("PB_STOP_AUTO = %s", results.getValue("PB_STOP_AUTO")));
                System.out.println(String.format("SILENT = %s", results.getValue("SILENT")));
                System.out.println(String.format("SET_FWD_PSD = %s", results.getValue("SET_FWD_PSD")));
                System.out.println(String.format("COM_PSD_LOSS = %s", results.getValue("COM_PSD_LOSS")));
                System.out.println(String.format("RUN_FWD = %s", results.getValue("RUN_FWD")));
                System.out.println(String.format("S_MODE_AUTO = %s", results.getValue("S_MODE_AUTO")));
                System.out.println("----------------------------------------------------");

                Thread.sleep(2000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            modbusMaster.destroy();
        }
    }
}
