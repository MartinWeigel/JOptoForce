package com.martinweigel.joptoforce.v13;

import com.martinweigel.joptoforce.AbstractDAQ;
import com.martinweigel.joptoforce.ByteHelper;
import com.martinweigel.joptoforce.MessageBuffer;
import com.martinweigel.joptoforce.OptoPackage;
import jssc.SerialPortException;

/**
 * DAQ for OptoForce 3-axis force sensor using protocol version 1.3.
 */
public class DAQSingle3Axis extends AbstractDAQ {
    private OptoPackage13 baseline = OptoPackage13.NO_BASELINE;
    private boolean requireBaseline;

    public DAQSingle3Axis() {}
    public DAQSingle3Axis(String portname) throws SerialPortException {
        super(portname);
    }

    @Override
    public byte[] getMessageHeader() {
        return new byte[] {55, 68};
    }

    @Override
    public int getMessageLength() {
        return 29;
    }

    @Override
    public boolean isValidPackage(MessageBuffer message) {
        if(message.size() >= getMessageLength() && checkHeader(message, getMessageHeader())) {
            int checksum = ByteHelper.unsignedWordFromArray(message, 27);
            int computedSum = ByteHelper.unsignedSum(message, 2, 26);
            return checksum == computedSum;
        }
        return false;
    }

    @Override
    public OptoPackage createOptoPackage(MessageBuffer message) {
        return createOptoPackage13(message);
    }

    public OptoPackage13 createOptoPackage13(MessageBuffer message) {
        int[] raw = new int[] {
            ByteHelper.unsignedWordFromArray(message, 11),
            ByteHelper.unsignedWordFromArray(message, 13),
            ByteHelper.unsignedWordFromArray(message, 15),
            ByteHelper.unsignedWordFromArray(message, 17)
        };
        int[] compensated = new int[] {
            ByteHelper.unsignedWordFromArray(message, 19),
            ByteHelper.unsignedWordFromArray(message, 21),
            ByteHelper.unsignedWordFromArray(message, 23),
            ByteHelper.unsignedWordFromArray(message, 25)
        };
        OptoPackage13 op = new OptoPackage13(
            ByteHelper.signedWordFromArray(message, 3),
            ByteHelper.signedWordFromArray(message, 5),
            ByteHelper.unsignedWordFromArray(message, 7),
            raw,
            compensated,
            ByteHelper.unsignedWordFromArray(message, 9),
            Byte.toUnsignedInt(message.getValue(2)),
            true);

        // If baseline is required, set it.
        if(requireBaseline) {
            baseline = op;
            requireBaseline = false;
        }

        op.setBaseline(baseline);
        return op;
    }

    public void configureSensor(Speed frequency, Filter filter) {
        send((byte)(0b10100000 | (frequency.getValue() << 3) | (filter.getValue() << 1)));
    }

    @Override
    public void unzeroValues() {
        baseline = null;
    }

    @Override
    public void zeroValues() {
        if(lastPackage != null)
            baseline = (OptoPackage13)lastPackage;
        else
            requireBaseline = true;
    }
}
