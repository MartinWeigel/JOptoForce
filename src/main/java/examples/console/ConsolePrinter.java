package examples.console;

import com.martinweigel.joptoforce.AutoDAQ;
import jssc.SerialPortList;

public class ConsolePrinter {
    public static void main(String[] args) {
        try {
            String serialPort = SerialPortList.getPortNames()[0];

            AutoDAQ optoforce = new AutoDAQ(serialPort);
            optoforce.zeroValues();

            optoforce.inform = op -> System.out.println(
                    String.format("Package %d: %d %d %d",
                            op.timestamp.toEpochMilli(), op.getX(), op.getY(), op.getZ()));
            while (true)
                Thread.sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}