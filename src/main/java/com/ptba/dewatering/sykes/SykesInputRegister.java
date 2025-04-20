package com.ptba.dewatering.sykes;

import java.util.Date;

import com.serotonin.modbus4j.BatchRead;
import com.serotonin.modbus4j.BatchResults;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.locator.BaseLocator;

public class SykesInputRegister {
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
        batchRead.addLocator("STATUS_RUN_VSD", BaseLocator.holdingRegister(slaveId, 1, DataType.BINARY));
        /*
        batchRead.addLocator("HEATER", BaseLocator.inputRegisterBit(slaveId, 2, 0));
        batchRead.addLocator("LUBRICANT", BaseLocator.inputRegisterBit(slaveId, 3, 0));
        batchRead.addLocator("A_PHASE_FAILURE", BaseLocator.inputRegisterBit(slaveId, 4, 0));
        batchRead.addLocator("A_EMERGENCY", BaseLocator.inputRegisterBit(slaveId, 5, 0));
        batchRead.addLocator("A_LUBRICANT", BaseLocator.inputRegisterBit(slaveId, 6, 0));
        batchRead.addLocator("A_FAULT_VSD", BaseLocator.inputRegisterBit(slaveId, 7, 0));
        batchRead.addLocator("A_FAULT_ROTATION", BaseLocator.inputRegisterBit(slaveId, 8, 0));
        batchRead.addLocator("A_OVER_CURRENT", BaseLocator.inputRegisterBit(slaveId, 9, 0));
        batchRead.addLocator("A_HIGH_PRESS_DISC", BaseLocator.inputRegisterBit(slaveId, 10, 0));
        batchRead.addLocator("A_HIGH_PRES_SUCT", BaseLocator.inputRegisterBit(slaveId, 11, 0));
        batchRead.addLocator("A_LOW_PRES_SUCT", BaseLocator.inputRegisterBit(slaveId, 12, 0));
        batchRead.addLocator("A_HIGH_UPPER_BEAR", BaseLocator.inputRegisterBit(slaveId, 13, 0));
        batchRead.addLocator("A_HIGH_LOWER_BEAR", BaseLocator.inputRegisterBit(slaveId, 14, 0));
        batchRead.addLocator("A_HIGH_WINDING_U", BaseLocator.inputRegisterBit(slaveId, 15, 0));
        batchRead.addLocator("A_HIGH_WINDING_V", BaseLocator.inputRegisterBit(slaveId, 16, 0));
        batchRead.addLocator("A_HIGH_WINDING_W", BaseLocator.inputRegisterBit(slaveId, 17, 0));
        batchRead.addLocator("A_FAULT_TILT", BaseLocator.inputRegisterBit(slaveId, 18, 0));
        */
        int counter = 0;
        try {
            modbusMaster.init();
            while (counter < 3) {
                counter++;
                batchRead.setContiguousRequests(false);
                BatchResults<String> results = modbusMaster.send(batchRead);
                System.out.println(new Date());
                System.out.println(String.format("STATUS_RUN_VSD = %s", results.getValue("STATUS_RUN_VSD")));
                /*
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
                */
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
