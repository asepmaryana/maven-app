package com.ptba.dewatering.sulzer;

import java.util.Date;

import com.serotonin.modbus4j.BatchRead;
import com.serotonin.modbus4j.BatchResults;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.locator.BaseLocator;

public class SulzerReadSp {
    public static void main( String[] args ) 
    {
        String host = args.length > 0 ? args[0] : "172.16.15.60";
        int port = args.length > 1 ? Integer.valueOf(args[1]) : 502;
        int slaveId = 1;
        
        System.out.println(String.format("Read SetPoint from %s on port %d ...", host, port));
        
        IpParameters tcParameters = new IpParameters();
        tcParameters.setHost(host);
        tcParameters.setPort(port);

        ModbusFactory modbusFactory = new ModbusFactory();
        ModbusMaster modbusMaster = modbusFactory.createTcpMaster(tcParameters, false);
        modbusMaster.setTimeout(10000);
        modbusMaster.setRetries(1);

        BatchRead<String> batchRead = new BatchRead<String>();
        batchRead.addLocator("SP_TEMPERATURE", BaseLocator.holdingRegister(slaveId, 90, DataType.TWO_BYTE_INT_SIGNED));
        batchRead.addLocator("SP_BEARING", BaseLocator.holdingRegister(slaveId, 91, DataType.TWO_BYTE_INT_SIGNED));
        batchRead.addLocator("SP_TILT_X", BaseLocator.holdingRegister(slaveId, 92, DataType.TWO_BYTE_INT_SIGNED));
        batchRead.addLocator("SP_FREQ_AUTO", BaseLocator.holdingRegister(slaveId, 94, DataType.TWO_BYTE_INT_SIGNED));
        batchRead.addLocator("SP_TILT_Y", BaseLocator.holdingRegister(slaveId, 95, DataType.TWO_BYTE_INT_SIGNED));
        batchRead.addLocator("SP_UV", BaseLocator.holdingRegister(slaveId, 96, DataType.TWO_BYTE_INT_SIGNED));
        batchRead.addLocator("SP_OV", BaseLocator.holdingRegister(slaveId, 97, DataType.TWO_BYTE_INT_SIGNED));
        

        int counter = 0;
        try {
            modbusMaster.init();
            while (counter < 3) {
                counter++;
                batchRead.setContiguousRequests(false);
                BatchResults<String> results = modbusMaster.send(batchRead);
                System.out.println(new Date());
                System.out.println(String.format("Sp Temperature = %d --> %.2f C", results.getValue("SP_TEMPERATURE"), (((Short)results.getValue("SP_TEMPERATURE")).floatValue()/(float)1)));
                System.out.println(String.format("Sp Bearing = %d --> %.2f C", results.getValue("SP_BEARING"), (((Short)results.getValue("SP_BEARING")).floatValue()/(float)1)));
                System.out.println(String.format("Sp Tilt X = %d --> %.2f ", results.getValue("SP_TILT_X"), (((Short)results.getValue("SP_TILT_X")).floatValue()/(float)1)));
                System.out.println(String.format("Sp Frequency Auto = %d --> %.2f Hz", results.getValue("SP_FREQ_AUTO"), (((Short)results.getValue("SP_FREQ_AUTO")).floatValue()/(float)1)));
                System.out.println(String.format("Sp Tilt Y = %d --> %.2f", results.getValue("SP_TILT_Y"), (((Short)results.getValue("SP_TILT_Y")).floatValue()/(float)1)));
                System.out.println(String.format("Sp Under Voltage = %d --> %.2f V", results.getValue("SP_UV"), (((Short)results.getValue("SP_UV")).floatValue()/(float)1)));
                System.out.println(String.format("Sp Over Voltage = %d --> %.2f V", results.getValue("SP_OV"), (((Short)results.getValue("SP_OV")).floatValue()/(float)1)));
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
