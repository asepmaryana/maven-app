package com.ptba.dewatering;

import com.serotonin.modbus4j.BatchRead;
import com.serotonin.modbus4j.BatchResults;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.locator.BaseLocator;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        //System.out.println( "Hello World!" );
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
        modbusMaster.setRetries(3);

        try {
            modbusMaster.init();

            BatchRead<Integer> batchRead = new BatchRead<Integer>();
            
            // Voltage RS
            batchRead.addLocator(121, BaseLocator.holdingRegister(slaveId, 121, DataType.TWO_BYTE_INT_UNSIGNED));
            // Voltage ST
            batchRead.addLocator(123, BaseLocator.holdingRegister(slaveId, 123, DataType.TWO_BYTE_INT_UNSIGNED));
            // Voltage RT
            batchRead.addLocator(125, BaseLocator.holdingRegister(slaveId, 125, DataType.TWO_BYTE_INT_UNSIGNED));
            // Current R
            batchRead.addLocator(127, BaseLocator.holdingRegister(slaveId, 127, DataType.TWO_BYTE_INT_UNSIGNED));
            // Current S
            batchRead.addLocator(129, BaseLocator.holdingRegister(slaveId, 129, DataType.TWO_BYTE_INT_UNSIGNED));
            // Current T
            batchRead.addLocator(131, BaseLocator.holdingRegister(slaveId, 131, DataType.TWO_BYTE_INT_UNSIGNED));
            // Bearing Front
            batchRead.addLocator(101, BaseLocator.holdingRegister(slaveId, 101, DataType.ONE_BYTE_INT_UNSIGNED_LOWER));
            // Bearing Rear
            batchRead.addLocator(102, BaseLocator.holdingRegister(slaveId, 102, DataType.ONE_BYTE_INT_UNSIGNED_LOWER));
            // PTC Pompa
            batchRead.addLocator(103, BaseLocator.holdingRegister(slaveId, 103, DataType.ONE_BYTE_INT_UNSIGNED_LOWER));
            // Sensor Wending R
            batchRead.addLocator(108, BaseLocator.holdingRegister(slaveId, 108, DataType.ONE_BYTE_INT_UNSIGNED_LOWER));
            // Sensor Wending S
            batchRead.addLocator(109, BaseLocator.holdingRegister(slaveId, 109, DataType.ONE_BYTE_INT_UNSIGNED_LOWER));
            // Sensor Wending T
            batchRead.addLocator(110, BaseLocator.holdingRegister(slaveId, 110, DataType.ONE_BYTE_INT_UNSIGNED_LOWER));
            // Tilt Sensor X Axis
            batchRead.addLocator(114, BaseLocator.holdingRegister(slaveId, 114, DataType.ONE_BYTE_INT_UNSIGNED_LOWER));
            // Pressure Meter
            batchRead.addLocator(115, BaseLocator.holdingRegister(slaveId, 115, DataType.ONE_BYTE_INT_UNSIGNED_LOWER));
            // Flow Rate Flow Meter
            batchRead.addLocator(116, BaseLocator.holdingRegister(slaveId, 116, DataType.ONE_BYTE_INT_UNSIGNED_LOWER));
            // Tilt Sensor Y Axis
            batchRead.addLocator(117, BaseLocator.holdingRegister(slaveId, 117, DataType.ONE_BYTE_INT_UNSIGNED_LOWER));
            // Active Power Total
            batchRead.addLocator(133, BaseLocator.holdingRegister(slaveId, 133, DataType.TWO_BYTE_INT_UNSIGNED));
            // PM Frequency
            batchRead.addLocator(135, BaseLocator.holdingRegister(slaveId, 135, DataType.TWO_BYTE_INT_UNSIGNED));
            // Motor Speed Used
            batchRead.addLocator(141, BaseLocator.holdingRegister(slaveId, 141, DataType.ONE_BYTE_INT_UNSIGNED_LOWER));
            // Motor Speed Percent
            batchRead.addLocator(142, BaseLocator.holdingRegister(slaveId, 142, DataType.ONE_BYTE_INT_UNSIGNED_LOWER));
            // Output Frequency
            batchRead.addLocator(143, BaseLocator.holdingRegister(slaveId, 143, DataType.ONE_BYTE_INT_UNSIGNED_LOWER));
            // Motor Current
            batchRead.addLocator(144, BaseLocator.holdingRegister(slaveId, 144, DataType.ONE_BYTE_INT_UNSIGNED_LOWER));
            // Motor Torque
            batchRead.addLocator(145, BaseLocator.holdingRegister(slaveId, 145, DataType.ONE_BYTE_INT_UNSIGNED_LOWER));
            // DC Voltage
            batchRead.addLocator(146, BaseLocator.holdingRegister(slaveId, 146, DataType.ONE_BYTE_INT_UNSIGNED_LOWER));
            // Output Power
            batchRead.addLocator(147, BaseLocator.holdingRegister(slaveId, 147, DataType.ONE_BYTE_INT_UNSIGNED_LOWER));
            // Temperature
            batchRead.addLocator(701, BaseLocator.holdingRegister(slaveId, 701, DataType.ONE_BYTE_INT_UNSIGNED_LOWER));
            // SP Temperature
            batchRead.addLocator(91, BaseLocator.holdingRegister(slaveId, 91, DataType.ONE_BYTE_INT_UNSIGNED_LOWER));
            // SP Bearing
            batchRead.addLocator(92, BaseLocator.holdingRegister(slaveId, 92, DataType.ONE_BYTE_INT_UNSIGNED_LOWER));
            // SP Tilt X
            batchRead.addLocator(93, BaseLocator.holdingRegister(slaveId, 93, DataType.ONE_BYTE_INT_UNSIGNED_LOWER));
            // SP Tilt Y
            batchRead.addLocator(96, BaseLocator.holdingRegister(slaveId, 96, DataType.ONE_BYTE_INT_UNSIGNED_LOWER));
            // SP Frequency Auto
            batchRead.addLocator(95, BaseLocator.holdingRegister(slaveId, 95, DataType.ONE_BYTE_INT_UNSIGNED_LOWER));
            // SP UV
            batchRead.addLocator(97, BaseLocator.holdingRegister(slaveId, 97, DataType.ONE_BYTE_INT_UNSIGNED_LOWER));
            // SP OV
            batchRead.addLocator(98, BaseLocator.holdingRegister(slaveId, 98, DataType.ONE_BYTE_INT_UNSIGNED_LOWER));
            // FM Flowrate
            batchRead.addLocator(171, BaseLocator.holdingRegister(slaveId, 171, DataType.FOUR_BYTE_FLOAT_SWAPPED));
            
            // FM Totalizer
            batchRead.addLocator(173, BaseLocator.holdingRegister(slaveId, 173, DataType.FOUR_BYTE_FLOAT_SWAPPED));

            BatchResults<Integer> results = modbusMaster.send(batchRead);
            
            System.out.println(String.format("Voltage R = %d --> %.2f", results.getIntValue(121), (float)((float)results.getIntValue(121)/(float)10)));
            System.out.println(String.format("Voltage S = %d --> %.2f", results.getIntValue(123), (float)((float)results.getIntValue(123)/(float)10)));
            System.out.println(String.format("Voltage T = %d --> %.2f\n", results.getIntValue(125), (float)((float)results.getIntValue(125)/(float)10)));

            System.out.println(String.format("Current R = %d --> %.2f", results.getIntValue(127), (float)((float)results.getIntValue(127)/(float)100)));
            System.out.println(String.format("Current S = %d --> %.2f", results.getIntValue(129), (float)((float)results.getIntValue(129)/(float)100)));
            System.out.println(String.format("Current T = %d --> %.2f\n", results.getIntValue(131), (float)((float)results.getIntValue(131)/(float)100)));

            System.out.println(String.format("Bearing Front = %d --> %.2f", results.getIntValue(101), (float)((float)results.getIntValue(101)/(float)1)));
            System.out.println(String.format("Bearing Rear = %d --> %.2f", results.getIntValue(102), (float)((float)results.getIntValue(102)/(float)1)));
            System.out.println(String.format("PTC Pompa = %d --> %.2f\n", results.getIntValue(103), (float)((float)results.getIntValue(103)/(float)1)));

            System.out.println(String.format("Wending R = %d --> %.2f", results.getIntValue(108), (float)((float)results.getIntValue(108)/(float)1)));
            System.out.println(String.format("Wending S = %d --> %.2f", results.getIntValue(109), (float)((float)results.getIntValue(109)/(float)1)));
            System.out.println(String.format("Wending T = %d --> %.2f\n", results.getIntValue(110), (float)((float)results.getIntValue(110)/(float)1)));
            
            System.out.println(String.format("Tilt Sensor X Axis = %d --> %.2f", results.getIntValue(114), (float)((float)results.getIntValue(114)/(float)1)));
            System.out.println(String.format("Pressure Meter = %d --> %.2f", results.getIntValue(115), (float)((float)results.getIntValue(115)/(float)1)));
            System.out.println(String.format("Flowrate Flow Meter = %d --> %.2f", results.getIntValue(116), (float)((float)results.getIntValue(116)/(float)1)));
            System.out.println(String.format("Tilt Sensor Y Axis = %d --> %.2f", results.getIntValue(117), (float)((float)results.getIntValue(117)/(float)1)));
            System.out.println(String.format("Active Power Total = %d --> %.2f", results.getIntValue(133), (float)((float)results.getIntValue(133)/(float)100)));
            System.out.println(String.format("PM Frequency = %d --> %.2f\n", results.getIntValue(135), (float)((float)results.getIntValue(135)/(float)1)));

            System.out.println(String.format("Motor Speed Used = %d --> %.2f", results.getIntValue(141), (float)((float)results.getIntValue(141)/(float)1)));
            System.out.println(String.format("Motor Speed Percent = %d --> %.2f", results.getIntValue(142), (float)((float)results.getIntValue(142)/(float)1)));
            System.out.println(String.format("Output Frequency = %d --> %.2f", results.getIntValue(143), (float)((float)results.getIntValue(143)/(float)1)));
            System.out.println(String.format("Motor Current = %d --> %.2f", results.getIntValue(144), (float)((float)results.getIntValue(144)/(float)1)));
            System.out.println(String.format("Motor Torque = %d --> %.2f", results.getIntValue(145), (float)((float)results.getIntValue(145)/(float)1)));
            System.out.println(String.format("DC Voltage = %d --> %.2f", results.getIntValue(146), (float)((float)results.getIntValue(146)/(float)1)));
            System.out.println(String.format("Output Power = %d --> %.2f", results.getIntValue(147), (float)((float)results.getIntValue(147)/(float)1)));
            System.out.println(String.format("Temperature = %d --> %.2f\n", results.getIntValue(701), (float)((float)results.getIntValue(701)/(float)1)));

            System.out.println(String.format("SP Temperature = %d --> %.2f", results.getIntValue(91), (float)((float)results.getIntValue(91)/(float)1)));
            System.out.println(String.format("SP Bearing = %d --> %.2f", results.getIntValue(92), (float)((float)results.getIntValue(92)/(float)1)));
            System.out.println(String.format("SP Tilt X = %d --> %.2f", results.getIntValue(93), (float)((float)results.getIntValue(93)/(float)1)));
            System.out.println(String.format("SP Tilt Y = %d --> %.2f", results.getIntValue(96), (float)((float)results.getIntValue(96)/(float)1)));
            System.out.println(String.format("SP Freq Auto = %d --> %.2f", results.getIntValue(95), (float)((float)results.getIntValue(95)/(float)1)));
            System.out.println(String.format("SP UV = %d --> %.2f", results.getIntValue(97), (float)((float)results.getIntValue(97)/(float)1)));
            System.out.println(String.format("SP OV = %d --> %.2f\n", results.getIntValue(98), (float)((float)results.getIntValue(98)/(float)1)));

            System.out.println(String.format("FM Flowrate = %f --> %.2f", results.getFloatValue(171), (float)((float)results.getFloatValue(171)/(float)1)));
            System.out.println(String.format("FM Totalizer = %f --> %.2f", results.getFloatValue(173), (float)((float)results.getFloatValue(173)/(float)1)));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            modbusMaster.destroy();
        }
    }
}
