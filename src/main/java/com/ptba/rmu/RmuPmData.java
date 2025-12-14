package com.ptba.rmu;

import java.util.Date;

import com.serotonin.modbus4j.BatchRead;
import com.serotonin.modbus4j.BatchResults;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.locator.BaseLocator;

public class RmuPmData {
    public static void main(String[] args) {
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

        BatchRead<Integer> batchRead = new BatchRead<Integer>();
        // Voltage A-B
        batchRead.addLocator(3020, BaseLocator.holdingRegister(slaveId, 3019, DataType.FOUR_BYTE_FLOAT));
        // Voltage B-C
        batchRead.addLocator(3022, BaseLocator.holdingRegister(slaveId, 3021, DataType.FOUR_BYTE_FLOAT));
        // Voltage C-A
        batchRead.addLocator(3024, BaseLocator.holdingRegister(slaveId, 3023, DataType.FOUR_BYTE_FLOAT));
        // Voltage A-N
        batchRead.addLocator(3028, BaseLocator.holdingRegister(slaveId, 3027, DataType.FOUR_BYTE_FLOAT));
        // Voltage B-N
        batchRead.addLocator(3030, BaseLocator.holdingRegister(slaveId, 3029, DataType.FOUR_BYTE_FLOAT));
        // Voltage C-N
        batchRead.addLocator(3032, BaseLocator.holdingRegister(slaveId, 3031, DataType.FOUR_BYTE_FLOAT));
        // Voltage L-L AVG
        batchRead.addLocator(3026, BaseLocator.holdingRegister(slaveId, 3025, DataType.FOUR_BYTE_FLOAT));
        // Voltage L-N AVG
        batchRead.addLocator(3036, BaseLocator.holdingRegister(slaveId, 3035, DataType.FOUR_BYTE_FLOAT));
        // Current AVG
        batchRead.addLocator(3010, BaseLocator.holdingRegister(slaveId, 3009, DataType.FOUR_BYTE_FLOAT));
        // Frequency
        batchRead.addLocator(3110, BaseLocator.holdingRegister(slaveId, 3109, DataType.FOUR_BYTE_FLOAT));

        int counter = 0;
        try {
            modbusMaster.init();
            while (counter < 3) {
                counter++;

                batchRead.setContiguousRequests(false);
                BatchResults<Integer> results = modbusMaster.send(batchRead);
                System.out.println(new Date());
                System.out.println("-------------------------------------");
                System.out.println("          Power Meter                ");
                System.out.println("-------------------------------------");
                System.out.println(String.format("Voltage A-B = %f --> %f Volt", results.getValue(3020), (((Float)results.getValue(3020)).floatValue()/(float)1)));
                System.out.println(String.format("Voltage B-C = %f --> %f Volt", results.getValue(3022), (((Float)results.getValue(3022)).floatValue()/(float)1)));
                System.out.println(String.format("Voltage C-A = %f --> %f Volt", results.getValue(3024), (((Float)results.getValue(3024)).floatValue()/(float)1)));
                System.out.println(String.format("Voltage A-N = %f --> %f Volt", results.getValue(3028), (((Float)results.getValue(3028)).floatValue()/(float)1)));
                System.out.println(String.format("Voltage B-N = %f --> %f Volt", results.getValue(3030), (((Float)results.getValue(3030)).floatValue()/(float)1)));
                System.out.println(String.format("Voltage C-N = %f --> %f Volt", results.getValue(3032), (((Float)results.getValue(3032)).floatValue()/(float)1)));
                System.out.println(String.format("Voltage L-L AVG = %f --> %.1f Volt", results.getValue(3026), (((Float)results.getValue(3026)).floatValue()/(float)1)));
                System.out.println(String.format("Voltage L-N AVG = %f --> %.1f Volt", results.getValue(3036), (((Float)results.getValue(3036)).floatValue()/(float)1)));
                System.out.println(String.format("Current AVG = %f --> %.1f A", results.getValue(3010), (((Float)results.getValue(3010)).floatValue()/(float)1)));
                System.out.println(String.format("Frequency = %f --> %.2f Hz", results.getValue(3110), (((Float)results.getValue(3110)).floatValue()/(float)1)));
                System.out.println("-------------------------------------\n");
                Thread.sleep(2000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            modbusMaster.destroy();
        }
    }
}
