package com.ptba.dewatering.sulzer;

import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.locator.BaseLocator;

public class SulzerCommand 
{
    public static void main( String[] args )
    {
        System.out.println("Sending Command to ...");
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

            // set vacum off
            BaseLocator<Boolean> vacumPump = BaseLocator.holdingRegisterBit(slaveId, 40011, 1);
            modbusMaster.setValue(vacumPump, 0);
            Thread.sleep(2000);

            // set main pump off
            BaseLocator<Boolean> mainPump = BaseLocator.holdingRegisterBit(slaveId, 40011, 2);
            modbusMaster.setValue(mainPump, 0);
            Thread.sleep(2000);

            System.out.println(modbusMaster.getValue(mainPump) == false ? "Sukses Turn OFF" : "Gagal Turn OFF");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            modbusMaster.destroy();
        }
    }
}
