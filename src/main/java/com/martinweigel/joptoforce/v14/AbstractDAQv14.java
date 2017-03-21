package com.martinweigel.joptoforce.v14;

import com.martinweigel.joptoforce.AbstractDAQ;
import com.martinweigel.joptoforce.ByteHelper;
import jssc.SerialPortException;

public abstract  class AbstractDAQv14 extends AbstractDAQ {
    public AbstractDAQv14() {}
    public AbstractDAQv14(String portname) throws SerialPortException {
        super(portname);
    }

    public void configureSensor(Speed speed, Filter filter, boolean zero) {
        byte[] message = new byte[] {
                (byte)170, 0, 50, 3,
                (byte)speed.getValue(),
                (byte)filter.getValue(),
                (byte)(zero ? 255 : 0)
        };
        int checksum = ByteHelper.unsignedSum(message);
        send(message);
        send((byte)((checksum & 0xff00) >> 8));
        send((byte)(checksum & 0x00ff));
    }

    @Override
    public void zeroValues() {
        configureSensor(Speed.Hz_100, Filter.Hz_15, true);
    }

    @Override
    public void unzeroValues() {
        configureSensor(Speed.Hz_100, Filter.Hz_15, false);
    }
}
