package examples.console;

import com.martinweigel.joptoforce.OptoPackage;
import com.martinweigel.joptoforce.v14.DAQMulti3Axis;
import jssc.SerialPortList;

public class MultiSensorConsolePrinter {
    public static void main(String[] args) {
        try {
            String serialPort = SerialPortList.getPortNames()[0];

            DAQMulti3Axis optoforce = new DAQMulti3Axis(serialPort);
            optoforce.zeroValues();

            while (true) {
                OptoPackage[] op = optoforce.getChannels();
                if(op != null) {
                    System.out.println(
                            String.format("Package %d:\t%d\t%d\t%d\t%d\t%d\t%d",
                                    op[0].timestamp.toEpochMilli(), op[0].getX(), op[0].getY(), op[0].getZ(), op[1].getX(), op[1].getY(), op[1].getZ()));
                }
                Thread.sleep(10);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}