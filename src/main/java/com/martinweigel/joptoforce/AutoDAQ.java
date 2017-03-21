package com.martinweigel.joptoforce;

import jssc.SerialPortException;

/**
 * Listens to port and automatically chooses the right supported DAQ.
 * Currently supported:
 * - Version 1.3: Single-Channel Three-Axis DAQ
 * - Version 1.4: Single-Channel Three-Axis DAQ
 * - Version 1.4: Multi-channel Three-Axis DAQ
 */
public class AutoDAQ extends AbstractDAQ {
    private AbstractDAQ[] SUPPORTED_DAQS = new AbstractDAQ[] {
            new com.martinweigel.joptoforce.v14.DAQMulti3Axis(),
            new com.martinweigel.joptoforce.v14.DAQSingle3Axis(),
            new com.martinweigel.joptoforce.v13.DAQSingle3Axis()
    };
    private final int MAX_MSG_LENGTH = 35;
    private AbstractDAQ choosenDAQ;
    private boolean requireZero;

    public AutoDAQ(String portname) throws SerialPortException {
        super(portname);
    }

    @Override
    /**
     * The header of the chosen DAQ or null.
     */
    public byte[] getMessageHeader() {
        if(choosenDAQ == null)
            return new byte[0];
        else
            return choosenDAQ.getMessageHeader();
    }

    /**
     * Length of the message.
     * Before a DAQ is chosen the maximum of all DAQ message lengths.
     * After a DAQ is chosen, the message length of the DAQ.
     */
    @Override
    public int getMessageLength() {
        if(choosenDAQ == null)
            return MAX_MSG_LENGTH;
        else
            return choosenDAQ.getMessageLength();
    }

    @Override
    public boolean isValidPackage(MessageBuffer message) {
        if(choosenDAQ != null) {
            return choosenDAQ.isValidPackage(message);
        } else {
            if(SUPPORTED_DAQS != null) {
                for (AbstractDAQ daq : SUPPORTED_DAQS) {
                    if (daq.isValidPackage(message)) {
                        choosenDAQ = daq;
                        choosenDAQ.serialPort = serialPort;
                        SUPPORTED_DAQS = null;
                        return true;
                    }
                }
            }
            return false;
        }
    }

    @Override
    public OptoPackage createOptoPackage(MessageBuffer message) {
        if (choosenDAQ == null)
            return null;
        else {
            if(requireZero) {
                choosenDAQ.zeroValues();
                requireZero = false;
            }
            return choosenDAQ.createOptoPackage(message);
        }
    }

    public AbstractDAQ getDAQ() {
        return choosenDAQ;
    }

    public void zeroValues() {
        requireZero = true;
    }

    @Override
    public void unzeroValues() {
        if(choosenDAQ != null)
            choosenDAQ.unzeroValues();
    }
}
