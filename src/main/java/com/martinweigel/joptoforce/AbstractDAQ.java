package com.martinweigel.joptoforce;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import java.util.function.Consumer;

public abstract class AbstractDAQ implements SerialPortEventListener {
    private MessageBuffer messageBuffer;
    protected SerialPort serialPort;
    protected OptoPackage lastPackage;
    public Consumer<OptoPackage> inform;


    public abstract void zeroValues();
    public abstract void unzeroValues();
    public abstract byte[] getMessageHeader();
    public abstract int getMessageLength();
    public abstract boolean isValidPackage(MessageBuffer message);
    public abstract OptoPackage createOptoPackage(MessageBuffer message);


    protected boolean checkHeader(MessageBuffer message, byte[] header) {
        for(int i=0; i<header.length; i++) {
            if(message.getValue(i) != header[i])
                return false;
        }
        return true;
    }

    public AbstractDAQ() {}
    public AbstractDAQ(String portname) throws SerialPortException {
        messageBuffer = new MessageBuffer(getMessageLength());

        serialPort = new SerialPort(portname);
        serialPort.openPort();
        serialPort.setParams(
                1000000,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);
        serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
        serialPort.addEventListener(this);
    }

    public boolean stop() throws SerialPortException {
        return serialPort.closePort();
    }

    public OptoPackage read() {
        return lastPackage;
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        try {
            byte[] buffer = serialPort.readBytes();
            for (byte b : buffer) {
                messageBuffer.addValue(b);
                if (isValidPackage(messageBuffer)) {
                    OptoPackage op = createOptoPackage(messageBuffer);

                    if (op != null) {
                        lastPackage = op;

                        // Trigger consumer
                        if (inform != null)
                            inform.accept(lastPackage);
                    }

                    // Remove package
                    messageBuffer.remove(getMessageLength());
                }
            }
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

    protected  void send(byte[] message) {
        for(byte b : message)
            send(b);
    }

    protected void send(byte byteMessage) {
        try {
            serialPort.writeByte(byteMessage);
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }
}
