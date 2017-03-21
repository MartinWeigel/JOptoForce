package com.martinweigel.joptoforce.v14;

import com.martinweigel.joptoforce.ByteHelper;
import com.martinweigel.joptoforce.MessageBuffer;
import com.martinweigel.joptoforce.OptoPackage;
import jssc.SerialPortException;

/**
 * DAQ for OptoForce multi-channel 3-axis force sensor using protocol version 1.4.
 * TODO: Only first sensor is zerod
 */
public class DAQMulti3Axis extends AbstractDAQv14 {
    private OptoPackage14[] channels;

    public DAQMulti3Axis() {}
    public DAQMulti3Axis(String portname) throws SerialPortException {
        super(portname);
    }

    public OptoPackage[] getChannels() {
        return channels;
    }

    @Override
    public byte[] getMessageHeader() {
        return new byte[] {(byte)170, 7, 8, 28};
    }

    @Override
    public int getMessageLength() {
        return 34;
    }

    @Override
    public boolean isValidPackage(MessageBuffer message) {
        if(message.size() >= getMessageLength() && checkHeader(message, getMessageHeader())) {
            int checksum = ByteHelper.unsignedWordFromArray(message, 32);
            int computedSum = ByteHelper.unsignedSum(message, 0, 31);
            return checksum == computedSum;
        }
        return false;
    }

    @Override
    public OptoPackage createOptoPackage(MessageBuffer message) {
        return createOptoPackages(message)[0];
    }

    public OptoPackage14[] createOptoPackages(MessageBuffer message) {
        // Extract values that apply for all data points
        int sampleCounter = ByteHelper.unsignedWordFromArray(message, 4);
        int status = ByteHelper.unsignedWordFromArray(message, 6);

        int checksum = ByteHelper.unsignedWordFromArray(message, 14);
        int computedSum = ByteHelper.unsignedSum(message, 0, 31);
        boolean valid = checksum == computedSum;

        // Create channels
        channels = new OptoPackage14[] {
                createSensor(0, message, sampleCounter, status, valid),
                createSensor(1, message, sampleCounter, status, valid),
                createSensor(2, message, sampleCounter, status, valid),
                createSensor(3, message, sampleCounter, status, valid)
        };
        return channels;
    }

    private OptoPackage14 createSensor(int number, MessageBuffer message, int counter, int status, boolean valid) {
        return new OptoPackage14(
                counter,
                ByteHelper.signedWordFromArray(message, number*3 + 8),
                ByteHelper.signedWordFromArray(message, number*3 + 10),
                ByteHelper.signedWordFromArray(message, number*3 + 12),
                status,
                valid
        );
    }
}
