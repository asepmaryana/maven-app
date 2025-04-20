package com.ptba.dewatering.sykes;

import java.util.Date;

import com.serotonin.modbus4j.BatchRead;
import com.serotonin.modbus4j.BatchResults;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.locator.BaseLocator;

public class SykesData {
    public static void main( String[] args )
    {
        //System.out.println( ":: SykesData :: " );
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
        batchRead.addLocator("SP_SPEED", BaseLocator.holdingRegister(slaveId, 1, DataType.FOUR_BYTE_FLOAT));
        batchRead.addLocator("ACCELERATION", BaseLocator.holdingRegister(slaveId, 3, DataType.FOUR_BYTE_FLOAT));
        batchRead.addLocator("DECLARATION", BaseLocator.holdingRegister(slaveId, 5, DataType.FOUR_BYTE_FLOAT));
        batchRead.addLocator("FB_SPEED_REF", BaseLocator.holdingRegister(slaveId, 7, DataType.FOUR_BYTE_FLOAT));
        batchRead.addLocator("CURRENT_METER", BaseLocator.holdingRegister(slaveId, 9, DataType.FOUR_BYTE_FLOAT));
        batchRead.addLocator("OUTPUT_VOLTAGE", BaseLocator.holdingRegister(slaveId, 11, DataType.FOUR_BYTE_FLOAT));
        batchRead.addLocator("DC_BUS", BaseLocator.holdingRegister(slaveId, 13, DataType.FOUR_BYTE_FLOAT));
        batchRead.addLocator("FAULT_VSD", BaseLocator.holdingRegister(slaveId, 15, DataType.TWO_BYTE_INT_SIGNED));
        batchRead.addLocator("PRES_DISC", BaseLocator.holdingRegister(slaveId, 16, DataType.FOUR_BYTE_FLOAT));
        batchRead.addLocator("PRES_SUCT", BaseLocator.holdingRegister(slaveId, 18, DataType.FOUR_BYTE_FLOAT));
        batchRead.addLocator("FLOW_RATE", BaseLocator.holdingRegister(slaveId, 20, DataType.FOUR_BYTE_FLOAT));
        batchRead.addLocator("UPPER_BEARING", BaseLocator.holdingRegister(slaveId, 22, DataType.FOUR_BYTE_FLOAT));
        batchRead.addLocator("LOWER_BEARING", BaseLocator.holdingRegister(slaveId, 24, DataType.FOUR_BYTE_FLOAT));
        batchRead.addLocator("WINDING_U", BaseLocator.holdingRegister(slaveId, 26, DataType.FOUR_BYTE_FLOAT));
        batchRead.addLocator("WINDING_V", BaseLocator.holdingRegister(slaveId, 28, DataType.FOUR_BYTE_FLOAT));
        batchRead.addLocator("WINDING_W", BaseLocator.holdingRegister(slaveId, 30, DataType.FOUR_BYTE_FLOAT));
        batchRead.addLocator("MIN_FLOW_SENSOR", BaseLocator.holdingRegister(slaveId, 32, DataType.FOUR_BYTE_FLOAT));
        batchRead.addLocator("MAX_FLOW_SENSOR", BaseLocator.holdingRegister(slaveId, 34, DataType.FOUR_BYTE_FLOAT));
        batchRead.addLocator("MIN_PRES_SUCT", BaseLocator.holdingRegister(slaveId, 36, DataType.FOUR_BYTE_FLOAT));
        batchRead.addLocator("MAX_PRES_SUCT", BaseLocator.holdingRegister(slaveId, 38, DataType.FOUR_BYTE_FLOAT));
        batchRead.addLocator("SP_HIGH_PRES_SUCT", BaseLocator.holdingRegister(slaveId, 40, DataType.FOUR_BYTE_FLOAT));
        batchRead.addLocator("MIN_PRES_DISC", BaseLocator.holdingRegister(slaveId, 42, DataType.FOUR_BYTE_FLOAT));
        batchRead.addLocator("MAX_PRES_DISC", BaseLocator.holdingRegister(slaveId, 44, DataType.FOUR_BYTE_FLOAT));
        batchRead.addLocator("SP_HIGH_PRES_DISC", BaseLocator.holdingRegister(slaveId, 46, DataType.FOUR_BYTE_FLOAT));
        batchRead.addLocator("SP_HIGH_UPPER_BEARING", BaseLocator.holdingRegister(slaveId, 48, DataType.FOUR_BYTE_FLOAT));
        batchRead.addLocator("SP_HIGH_LOWER_BEARING", BaseLocator.holdingRegister(slaveId, 50, DataType.FOUR_BYTE_FLOAT));
        batchRead.addLocator("SP_HIGH_WINDING_U", BaseLocator.holdingRegister(slaveId, 52, DataType.FOUR_BYTE_FLOAT));
        batchRead.addLocator("SP_HIGH_WINDING_V", BaseLocator.holdingRegister(slaveId, 54, DataType.FOUR_BYTE_FLOAT));
        batchRead.addLocator("SP_HIGH_WINDING_W", BaseLocator.holdingRegister(slaveId, 56, DataType.FOUR_BYTE_FLOAT));
        //batchRead.addLocator("SP_OVER_CURRENT", BaseLocator.holdingRegister(slaveId, 58, DataType.FOUR_BYTE_FLOAT));
        
        int counter = 0;
        try {
            modbusMaster.init();
            while (counter < 3) {
                counter++;
                batchRead.setContiguousRequests(false);
                BatchResults<String> results = modbusMaster.send(batchRead);
                System.out.println(new Date());
                //System.out.println(results);
                System.out.println(String.format("Sp Speed = %f --> %.2f Hz", results.getValue("SP_SPEED"), (((Float)results.getValue("SP_SPEED")).floatValue()/(float)1)));
                System.out.println(String.format("Acceleration = %f --> %.2f sec", results.getValue("ACCELERATION"), (((Float)results.getValue("ACCELERATION")).floatValue()/(float)1)));
                System.out.println(String.format("Declaration = %f --> %.2f sec", results.getValue("DECLARATION"), (((Float)results.getValue("DECLARATION")).floatValue()/(float)1)));
                System.out.println(String.format("Fb Speed Ref = %f --> %.2f", results.getValue("FB_SPEED_REF"), (((Float)results.getValue("FB_SPEED_REF")).floatValue()/(float)1)));
                System.out.println(String.format("Current Meter = %f --> %.2f A", results.getValue("CURRENT_METER"), (((Float)results.getValue("CURRENT_METER")).floatValue()/(float)1)));
                System.out.println(String.format("Output Voltage = %f --> %.2f V", results.getValue("OUTPUT_VOLTAGE"), (((Float)results.getValue("OUTPUT_VOLTAGE")).floatValue()/(float)1)));
                System.out.println(String.format("DC Bus Voltage = %f --> %.2f V", results.getValue("DC_BUS"), (((Float)results.getValue("DC_BUS")).floatValue()/(float)1)));
                System.out.println(String.format("Fault VSD = %d --> %.2f", results.getValue("FAULT_VSD"), (((Short)results.getValue("FAULT_VSD")).floatValue()/(float)1)));
                System.out.println(String.format("Pressure Discharge = %f --> %.2f kPa", results.getValue("PRES_DISC"), (((Float)results.getValue("PRES_DISC")).floatValue()/(float)1)));
                System.out.println(String.format("Pressure Suction = %f --> %.2f kPa", results.getValue("PRES_SUCT"), (((Float)results.getValue("PRES_SUCT")).floatValue()/(float)1)));
                System.out.println(String.format("Flowrate = %f --> %.2f m3/H", results.getValue("FLOW_RATE"), (((Float)results.getValue("FLOW_RATE")).floatValue()/(float)1)));
                System.out.println(String.format("Upper Bearing = %f --> %.2f C", results.getValue("UPPER_BEARING"), (((Float)results.getValue("UPPER_BEARING")).floatValue()/(float)1)));
                System.out.println(String.format("Lower Bearing = %f --> %.2f C", results.getValue("LOWER_BEARING"), (((Float)results.getValue("LOWER_BEARING")).floatValue()/(float)1)));
                System.out.println(String.format("Winding U = %f --> %.2f C", results.getValue("WINDING_U"), (((Float)results.getValue("WINDING_U")).floatValue()/(float)1)));
                System.out.println(String.format("Winding V = %f --> %.2f C", results.getValue("WINDING_V"), (((Float)results.getValue("WINDING_V")).floatValue()/(float)1)));
                System.out.println(String.format("Winding W = %f --> %.2f C", results.getValue("WINDING_W"), (((Float)results.getValue("WINDING_W")).floatValue()/(float)1)));
                System.out.println(String.format("Min Flow Sensor = %f --> %.2f", results.getValue("MIN_FLOW_SENSOR"), (((Float)results.getValue("MIN_FLOW_SENSOR")).floatValue()/(float)1)));
                System.out.println(String.format("Max Flow Sensor = %f --> %.2f", results.getValue("MAX_FLOW_SENSOR"), (((Float)results.getValue("MAX_FLOW_SENSOR")).floatValue()/(float)1)));
                System.out.println(String.format("Min Pressure Suction = %f --> %.2f", results.getValue("MIN_PRES_SUCT"), (((Float)results.getValue("MIN_PRES_SUCT")).floatValue()/(float)1)));
                System.out.println(String.format("Max Pressure Suction = %f --> %.2f", results.getValue("MAX_PRES_SUCT"), (((Float)results.getValue("MAX_PRES_SUCT")).floatValue()/(float)1)));
                System.out.println(String.format("Sp High Pressure Suction = %f --> %.2f", results.getValue("SP_HIGH_PRES_SUCT"), (((Float)results.getValue("SP_HIGH_PRES_SUCT")).floatValue()/(float)1)));
                System.out.println(String.format("Min Pressure Discharge = %f --> %.2f", results.getValue("MIN_PRES_DISC"), (((Float)results.getValue("MIN_PRES_DISC")).floatValue()/(float)1)));
                System.out.println(String.format("Max Pressure Discharge = %f --> %.2f", results.getValue("MAX_PRES_DISC"), (((Float)results.getValue("MAX_PRES_DISC")).floatValue()/(float)1)));
                System.out.println(String.format("Sp High Pressure Discharge = %f --> %.2f", results.getValue("SP_HIGH_PRES_DISC"), (((Float)results.getValue("SP_HIGH_PRES_DISC")).floatValue()/(float)1)));
                System.out.println(String.format("Sp High Upper Bearing = %f --> %.2f", results.getValue("SP_HIGH_UPPER_BEARING"), (((Float)results.getValue("SP_HIGH_UPPER_BEARING")).floatValue()/(float)1)));
                System.out.println(String.format("Sp High Lower Bearing = %f --> %.2f", results.getValue("SP_HIGH_LOWER_BEARING"), (((Float)results.getValue("SP_HIGH_LOWER_BEARING")).floatValue()/(float)1)));
                System.out.println(String.format("Sp High Lower Bearing = %f --> %.2f", results.getValue("SP_HIGH_WINDING_U"), (((Float)results.getValue("SP_HIGH_WINDING_U")).floatValue()/(float)1)));
                System.out.println(String.format("Sp High Winding U = %f --> %.2f", results.getValue("SP_HIGH_WINDING_U"), (((Float)results.getValue("SP_HIGH_WINDING_U")).floatValue()/(float)1)));
                System.out.println(String.format("Sp High Winding V = %f --> %.2f", results.getValue("SP_HIGH_WINDING_V"), (((Float)results.getValue("SP_HIGH_WINDING_V")).floatValue()/(float)1)));
                System.out.println(String.format("Sp High Winding W = %f --> %.2f", results.getValue("SP_HIGH_WINDING_W"), (((Float)results.getValue("SP_HIGH_WINDING_W")).floatValue()/(float)1)));
                //System.out.println(String.format("Sp Over Current = %f --> %.2f", results.getValue("SP_OVER_CURRENT"), (((Float)results.getValue("SP_OVER_CURRENT")).floatValue()/(float)1)));
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
