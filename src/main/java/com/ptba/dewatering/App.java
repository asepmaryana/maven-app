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
            batchRead.addLocator(121, BaseLocator.holdingRegister(slaveId, 121, DataType.TWO_BYTE_INT_UNSIGNED));
            batchRead.addLocator(123, BaseLocator.holdingRegister(slaveId, 123, DataType.TWO_BYTE_INT_UNSIGNED));
            batchRead.addLocator(125, BaseLocator.holdingRegister(slaveId, 125, DataType.TWO_BYTE_INT_UNSIGNED));

            BatchResults<Integer> results = modbusMaster.send(batchRead);
            System.out.println(String.format("Voltage R = %.2f", (double)(results.getIntValue(121)/10)));
            System.out.println(String.format("Voltage S = %.2f", (double)(results.getIntValue(123)/10)));
            System.out.println(String.format("Voltage T = %.2f", (double)(results.getIntValue(125)/10)));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            modbusMaster.destroy();
        }
    }
}
