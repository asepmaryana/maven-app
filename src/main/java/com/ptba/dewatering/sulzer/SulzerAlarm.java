package com.ptba.dewatering.sulzer;

import com.serotonin.modbus4j.BatchRead;
import com.serotonin.modbus4j.BatchResults;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.locator.BaseLocator;

public class SulzerAlarm {
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
            batchRead.addLocator(11, BaseLocator.holdingRegister(slaveId, 11, DataType.TWO_BYTE_INT_UNSIGNED));
            batchRead.addLocator(12, BaseLocator.holdingRegister(slaveId, 12, DataType.TWO_BYTE_INT_UNSIGNED));
            batchRead.addLocator(13, BaseLocator.holdingRegister(slaveId, 13, DataType.TWO_BYTE_INT_UNSIGNED));
            batchRead.addLocator(173, BaseLocator.holdingRegister(slaveId, 174, DataType.FOUR_BYTE_FLOAT_SWAPPED));
            batchRead.addLocator(171, BaseLocator.holdingRegister(slaveId, 172, DataType.FOUR_BYTE_FLOAT));
            BatchResults<Integer> results = modbusMaster.send(batchRead);
            
            //int alarm_11 = 120;
            int alarm_11 = results.getIntValue(11).intValue();
            int alarm_12 = results.getIntValue(12).intValue();
            int alarm_13 = results.getIntValue(13).intValue();

            System.out.println(String.format("Alarm 11 = %s, 12 = %d, 13 = %d\n", alarm_11, alarm_12, alarm_13));

            String alarm_org_11 = String.format("%16s", Integer.toBinaryString(alarm_11)).replace(" ", "0");
            String alarm_org_12 = String.format("%16s", Integer.toBinaryString(alarm_12)).replace(" ", "0");
            String alarm_org_13 = String.format("%16s", Integer.toBinaryString(alarm_13)).replace(" ", "0");

            String alarm_rev_11 = new StringBuffer(alarm_org_11).reverse().toString();
            String alarm_rev_12 = new StringBuffer(alarm_org_12).reverse().toString();
            String alarm_rev_13 = new StringBuffer(alarm_org_13).reverse().toString();

            String CMD_AUTO_SCADA           = alarm_rev_11.substring(0, 1);
            String CMD_SCADA_VACUMM_PUMP    = alarm_rev_11.substring(1, 2);
            String CLO_MAIN_PUMP_ON         = alarm_rev_11.substring(2, 3);
            String ALARM_GENERAL            = alarm_rev_11.substring(3, 4);
            String FAULT_VACUMM_PUMP        = alarm_rev_11.substring(4, 5);
            String HH_VACUMM_TANK           = alarm_rev_11.substring(5, 6);
            String CLO_VACUMM_PUMP          = alarm_rev_11.substring(6, 7);
            String REMOTE_TO_IDEM           = alarm_rev_11.substring(7, 8);
            String NOT_STAT_MAIN_PUMP_RUN   = alarm_rev_11.substring(9, 10);
            String EMERGENCY_STOP           = alarm_rev_11.substring(10, 11);
            String STAT_RUN_VACUMM_RUN      = alarm_rev_11.substring(11, 12);
            String STAT_MAIN_PUMP_RUN       = alarm_rev_11.substring(12, 13);
            String STAT_MAIN_PUMP_FAULT     = alarm_rev_11.substring(13, 14);

            String ALARM_UNDER_VOLTAGE_RS   = alarm_rev_12.substring(0, 1);
            String ALARM_UNDER_VOLTAGE_ST   = alarm_rev_12.substring(1, 2);
            String ALARM_UNDER_VOLTAGE_RT   = alarm_rev_12.substring(2, 3);
            String ALARM_WINDING_R          = alarm_rev_12.substring(3, 4);
            String ALARM_WINDING_S          = alarm_rev_12.substring(4, 5);
            String ALARM_WINDING_T          = alarm_rev_12.substring(5, 6);
            String ALARM_BEARING_TILT_X_P   = alarm_rev_12.substring(6, 7);
            String ALARM_BEARING_FRONT      = alarm_rev_12.substring(7, 8);
            String ALARM_BEARING_REAR       = alarm_rev_12.substring(8, 9);
            String ALARM_BEARING_TILT_Y_P   = alarm_rev_12.substring(9, 10);
            String ALARM_COMMON_2           = alarm_rev_12.substring(10, 11);
            String ALARM_OVER_VOLTAGE_RS    = alarm_rev_12.substring(12, 13);
            String ALARM_OVER_VOLTAGE_ST    = alarm_rev_12.substring(13, 14);
            String ALARM_OVER_VOLTAGE_RT    = alarm_rev_12.substring(14, 15);
            String ALARM_BEARING_TILT_X_M   = alarm_rev_12.substring(15, 16);
            String ALARM_BEARING_TILT_Y_M   = alarm_rev_13.substring(0, 1);

            StringBuffer sb = new StringBuffer();
            sb.append(":: ALARM 11 ::").append("\n");
            sb.append("CMD_AUTO_SCADA = ").append(CMD_AUTO_SCADA).append("\n");
            sb.append("CMD_SCADA_VACUMM_PUMP = ").append(CMD_SCADA_VACUMM_PUMP).append("\n");
            sb.append("CLO_MAIN_PUMP_ON = ").append(CLO_MAIN_PUMP_ON).append("\n");
            sb.append("ALARM_GENERAL = ").append(ALARM_GENERAL).append("\n");
            sb.append("FAULT_VACUMM_PUMP = ").append(FAULT_VACUMM_PUMP).append("\n");
            sb.append("HH_VACUMM_TANK = ").append(HH_VACUMM_TANK).append("\n");
            sb.append("CLO_VACUMM_PUMP = ").append(CLO_VACUMM_PUMP).append("\n");
            sb.append("REMOTE_TO_IDEM = ").append(REMOTE_TO_IDEM).append("\n");
            sb.append("NOT_STAT_MAIN_PUMP_RUN = ").append(NOT_STAT_MAIN_PUMP_RUN).append("\n");
            sb.append("EMERGENCY_STOP = ").append(EMERGENCY_STOP).append("\n");
            sb.append("STAT_RUN_VACUMM_RUN = ").append(STAT_RUN_VACUMM_RUN).append("\n");
            sb.append("STAT_MAIN_PUMP_RUN = ").append(STAT_MAIN_PUMP_RUN).append("\n");
            sb.append("STAT_MAIN_PUMP_FAULT = ").append(STAT_MAIN_PUMP_FAULT).append("\n");

            System.out.println(sb.toString());

            //clear
            sb.setLength(0);
            sb.append(":: ALARM 12 ::").append("\n");
            sb.append("ALARM_UNDER_VOLTAGE_RS = ").append(ALARM_UNDER_VOLTAGE_RS).append("\n");
            sb.append("ALARM_UNDER_VOLTAGE_ST = ").append(ALARM_UNDER_VOLTAGE_ST).append("\n");
            sb.append("ALARM_UNDER_VOLTAGE_RT = ").append(ALARM_UNDER_VOLTAGE_RT).append("\n");
            sb.append("ALARM_WINDING_R = ").append(ALARM_WINDING_R).append("\n");
            sb.append("ALARM_WINDING_S = ").append(ALARM_WINDING_S).append("\n");
            sb.append("ALARM_WINDING_T = ").append(ALARM_WINDING_T).append("\n");
            sb.append("ALARM_BEARING_TILT_X_P = ").append(ALARM_BEARING_TILT_X_P).append("\n");
            sb.append("ALARM_BEARING_TILT_Y_P = ").append(ALARM_BEARING_TILT_Y_P).append("\n");
            sb.append("ALARM_BEARING_FRONT = ").append(ALARM_BEARING_FRONT).append("\n");
            sb.append("ALARM_BEARING_REAR = ").append(ALARM_BEARING_REAR).append("\n");
            sb.append("ALARM_COMMON_2 = ").append(ALARM_COMMON_2).append("\n");
            sb.append("ALARM_OVER_VOLTAGE_RS = ").append(ALARM_OVER_VOLTAGE_RS).append("\n");
            sb.append("ALARM_OVER_VOLTAGE_ST = ").append(ALARM_OVER_VOLTAGE_ST).append("\n");
            sb.append("ALARM_OVER_VOLTAGE_RT = ").append(ALARM_OVER_VOLTAGE_RT).append("\n");
            sb.append("ALARM_BEARING_TILT_X_M = ").append(ALARM_BEARING_TILT_X_M).append("\n");
            sb.append("ALARM_BEARING_TILT_Y_M = ").append(ALARM_BEARING_TILT_Y_M).append("\n");

            System.out.println(sb.toString());
            System.out.println(String.format("FM Flowrate = %f --> %.3f", results.getFloatValue(173), (float)((float)results.getFloatValue(173)/(float)1)));
            System.out.println(String.format("FM TotalFlow = %f --> %.3f", results.getFloatValue(171), (float)((float)results.getFloatValue(171)/(float)1)));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            modbusMaster.destroy();
        }
    }
}
