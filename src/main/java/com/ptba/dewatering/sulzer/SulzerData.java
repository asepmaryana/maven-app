package com.ptba.dewatering.sulzer;

import java.util.Date;

import com.serotonin.modbus4j.BatchRead;
import com.serotonin.modbus4j.BatchResults;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.locator.BaseLocator;

public class SulzerData {
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
        modbusMaster.setRetries(1);

        BatchRead<Integer> batchRead = new BatchRead<Integer>();
        batchRead.addLocator(121, BaseLocator.holdingRegister(slaveId, 121, DataType.TWO_BYTE_INT_SIGNED));
        batchRead.addLocator(123, BaseLocator.holdingRegister(slaveId, 123, DataType.TWO_BYTE_INT_SIGNED));
        batchRead.addLocator(125, BaseLocator.holdingRegister(slaveId, 125, DataType.TWO_BYTE_INT_SIGNED));
        batchRead.addLocator(127, BaseLocator.holdingRegister(slaveId, 127, DataType.TWO_BYTE_INT_SIGNED));
        batchRead.addLocator(129, BaseLocator.holdingRegister(slaveId, 129, DataType.TWO_BYTE_INT_SIGNED));
        batchRead.addLocator(131, BaseLocator.holdingRegister(slaveId, 131, DataType.TWO_BYTE_INT_SIGNED));
        batchRead.addLocator(133, BaseLocator.holdingRegister(slaveId, 133, DataType.TWO_BYTE_INT_SIGNED));
        batchRead.addLocator(134, BaseLocator.holdingRegister(slaveId, 134, DataType.TWO_BYTE_INT_SIGNED));

        batchRead.addLocator(107, BaseLocator.holdingRegister(slaveId, 107, DataType.TWO_BYTE_INT_SIGNED));
        batchRead.addLocator(108, BaseLocator.holdingRegister(slaveId, 108, DataType.TWO_BYTE_INT_SIGNED));
        batchRead.addLocator(109, BaseLocator.holdingRegister(slaveId, 109, DataType.TWO_BYTE_INT_SIGNED));

        batchRead.addLocator(140, BaseLocator.holdingRegister(slaveId, 140, DataType.TWO_BYTE_INT_SIGNED));
        batchRead.addLocator(141, BaseLocator.holdingRegister(slaveId, 141, DataType.TWO_BYTE_INT_SIGNED));
        batchRead.addLocator(142, BaseLocator.holdingRegister(slaveId, 142, DataType.TWO_BYTE_INT_SIGNED));
        batchRead.addLocator(143, BaseLocator.holdingRegister(slaveId, 143, DataType.TWO_BYTE_INT_SIGNED));
        batchRead.addLocator(144, BaseLocator.holdingRegister(slaveId, 144, DataType.TWO_BYTE_INT_SIGNED));
        batchRead.addLocator(145, BaseLocator.holdingRegister(slaveId, 145, DataType.TWO_BYTE_INT_SIGNED));
        batchRead.addLocator(146, BaseLocator.holdingRegister(slaveId, 146, DataType.TWO_BYTE_INT_SIGNED));

        batchRead.addLocator(171, BaseLocator.holdingRegister(slaveId, 172, DataType.FOUR_BYTE_FLOAT));
        batchRead.addLocator(173, BaseLocator.holdingRegister(slaveId, 174, DataType.FOUR_BYTE_FLOAT_SWAPPED));

        batchRead.addLocator(113, BaseLocator.holdingRegister(slaveId, 113, DataType.TWO_BYTE_INT_SIGNED));
        batchRead.addLocator(114, BaseLocator.holdingRegister(slaveId, 114, DataType.TWO_BYTE_INT_SIGNED));
        batchRead.addLocator(116, BaseLocator.holdingRegister(slaveId, 116, DataType.TWO_BYTE_INT_SIGNED));

        batchRead.addLocator(1111, BaseLocator.holdingRegisterBit(slaveId, 11, 12));
        batchRead.addLocator(1104, BaseLocator.holdingRegisterBit(slaveId, 11, 5));
        batchRead.addLocator(1105, BaseLocator.holdingRegisterBit(slaveId, 11, 6));

        batchRead.addLocator(1100, BaseLocator.holdingRegisterBit(slaveId, 11, 0));
        batchRead.addLocator(1200, BaseLocator.holdingRegisterBit(slaveId, 12, 0));

        int counter = 0;
        try { 
            modbusMaster.init();
            while (counter < 3) {
                counter++;
                batchRead.setContiguousRequests(false);
                BatchResults<Integer> results = modbusMaster.send(batchRead);
                System.out.println(new Date());
                //System.out.println(results);
                System.out.println("-------------------------------------");
                System.out.println("          Power Motor                ");
                System.out.println("-------------------------------------");
                System.out.println(String.format("Voltage R = %d --> %.2f V", results.getValue(121), (((Short)results.getValue(121)).floatValue()/(float)10)));
                System.out.println(String.format("Voltage S = %d --> %.2f V", results.getValue(123), (((Short)results.getValue(123)).floatValue()/(float)10)));
                System.out.println(String.format("Voltage T = %d --> %.2f V\n", results.getValue(125), (((Short)results.getValue(125)).floatValue()/(float)10)));
                System.out.println(String.format("Current R = %d --> %.2f A", results.getValue(127), (((Short)results.getValue(127)).floatValue()/(float)100)));
                System.out.println(String.format("Current S = %d --> %.2f A", results.getValue(129), (((Short)results.getValue(129)).floatValue()/(float)100)));
                System.out.println(String.format("Current T = %d --> %.2f A\n", results.getValue(131), (((Short)results.getValue(131)).floatValue()/(float)100)));
                System.out.println(String.format("Power Total = %d --> %.3f kW", results.getValue(133), (((Short)results.getValue(133)).floatValue()/(float)1000)));
                System.out.println(String.format("Frequency = %s --> %.2f Hz", results.getValue(134), (((Short)results.getValue(134)).floatValue()/(float)100)));

                System.out.println("-------------------------------------");
                System.out.println("            Main Pump                ");
                System.out.println("-------------------------------------");
                System.out.println(String.format("Winding R = %d --> %.2f C", results.getValue(107), (((Short)results.getValue(107)).floatValue()/(float)1)));
                System.out.println(String.format("Winding S = %d --> %.2f C", results.getValue(108), (((Short)results.getValue(108)).floatValue()/(float)1)));
                System.out.println(String.format("Winding T = %d --> %.2f C", results.getValue(109), (((Short)results.getValue(109)).floatValue()/(float)1)));

                System.out.println("-------------------------------------");
                System.out.println("            Flow Meter               ");
                System.out.println("-------------------------------------");
                System.out.println(String.format("Flowrate = %f --> %.3f m3/H", results.getValue(173), (((Float)results.getValue(173)).floatValue()/(float)1)));
                System.out.println(String.format("Totalizer = %f --> %.3f m3", results.getValue(171), (((Float)results.getValue(171)).floatValue()/(float)1)));

                System.out.println("-------------------------------------");
                System.out.println("            VSD Monitoring           ");
                System.out.println("-------------------------------------");
                System.out.println(String.format("Motor Speed Used = %d --> %.2f Rpm", results.getValue(140), (((Short)results.getValue(140)).floatValue()/(float)10)));
                System.out.println(String.format("Motor Speed Perc = %d --> %.2f", results.getValue(141), (((Short)results.getValue(141)).floatValue()/(float)10)));
                System.out.println(String.format("Output Frequency = %d --> %.2f Hz", results.getValue(142), (((Short)results.getValue(142)).floatValue()/(float)10)));
                System.out.println(String.format("Motor Current = %d --> %.2f A", results.getValue(143), (((Short)results.getValue(143)).floatValue()/(float)10)));
                System.out.println(String.format("Motor Torque = %d --> %.2f", results.getValue(144), (((Short)results.getValue(144)).floatValue()/(float)10)));
                System.out.println(String.format("DC Voltage = %d --> %.2f V", results.getValue(145), (((Short)results.getValue(145)).floatValue()/(float)10)));
                System.out.println(String.format("Output Power = %d --> %.2f kW", results.getValue(146), (((Short)results.getValue(146)).floatValue()/(float)10)));

                System.out.println("-------------------------------------");
                System.out.println("            Pressure Meter           ");
                System.out.println("-------------------------------------");
                System.out.println(String.format("Pressure = %d --> %.2f Bar", results.getValue(114), (((Short)results.getValue(114)).floatValue()/(float)10)));

                System.out.println("-------------------------------------");
                System.out.println("            Tilt Sensor             ");
                System.out.println("-------------------------------------");
                System.out.println(String.format("Tilt X = %d --> %.2f", results.getValue(113), (((Short)results.getValue(113)).floatValue()/(float)1)));
                System.out.println(String.format("Tilt Y = %d --> %.2f", results.getValue(116), (((Short)results.getValue(116)).floatValue()/(float)1)));

                System.out.println("-------------------------------------");
                System.out.println("            Vacumm Pump              ");
                System.out.println("-------------------------------------");
                System.out.println(String.format("Vacumm Pump Off = %s", results.getValue(1111)));
                System.out.println(String.format("Vacumm Pump Normal = %s", results.getValue(1104)));
                System.out.println(String.format("Vacumm Water Tank = %s", results.getValue(1105)));

                System.out.println("-------------------------------------");
                System.out.println("            Control Mode              ");
                System.out.println("-------------------------------------");
                System.out.println(String.format("Auto = %s", results.getValue(1100)));
                System.out.println(String.format("Shutdown & Manual = %s", results.getValue(1200)));

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
