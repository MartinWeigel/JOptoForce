package com.martinweigel.joptoforce.v14;

import com.martinweigel.joptoforce.ByteHelper;
import com.martinweigel.joptoforce.MessageBuffer;
import com.martinweigel.joptoforce.OptoPackage;
import jssc.SerialPortException;

/**
 * DAQ for OptoForce for 3-axis force sensors using protocol version 1.4.
 */
public class DAQSingle3Axis extends AbstractDAQv14 {

    public DAQSingle3Axis() {}
    public DAQSingle3Axis(String portname) throws SerialPortException {
        super(portname);
    }

    @Override
    public byte[] getMessageHeader() {
        return new byte[] {(byte)170, 7, 8, 10};
    }

    @Override
    public int getMessageLength() {
        return 16;
    }

    @Override
    public boolean isValidPackage(MessageBuffer message) {
        if(message.size() >= getMessageLength() && checkHeader(message, getMessageHeader())) {
            int checksum = ByteHelper.unsignedWordFromArray(message, 14);
            int computedSum = ByteHelper.unsignedSum(message, 0, 13);
            return checksum == computedSum;
        }
        return false;
    }

    @Override
    public OptoPackage createOptoPackage(MessageBuffer message) {
        return createOptoPackage14(message);
    }

    public OptoPackage14 createOptoPackage14(MessageBuffer message) {
        if(isValidPackage(message)) {
            return new OptoPackage14(
                ByteHelper.unsignedWordFromArray(message, 4),
                ByteHelper.signedWordFromArray(message, 8),
                ByteHelper.signedWordFromArray(message, 10),
                ByteHelper.signedWordFromArray(message, 12),
                ByteHelper.unsignedWordFromArray(message, 6),
                true);
        }
        return null;
    }
}
