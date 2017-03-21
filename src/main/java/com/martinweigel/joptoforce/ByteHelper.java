package com.martinweigel.joptoforce;

public abstract class ByteHelper {
    public static int signedWord(byte byte1, byte byte2) {
        return byte1 << 8 | Byte.toUnsignedInt(byte2);
    }

    public static int unsignedWord(byte byte1, byte byte2) {
        return Byte.toUnsignedInt(byte1) << 8 | Byte.toUnsignedInt(byte2);
    }


    public static int signedWordFromArray(MessageBuffer buffer, int indexOfFirstByte) {
        return signedWord(buffer.getValue(indexOfFirstByte), buffer.getValue(indexOfFirstByte + 1));
    }

    public static int unsignedWordFromArray(MessageBuffer buffer, int indexOfFirstByte) {
        return unsignedWord(buffer.getValue(indexOfFirstByte), buffer.getValue(indexOfFirstByte + 1));
    }


    public static int signedSum(MessageBuffer buffer, int minIndex, int maxIndex) {
        int result = 0;
        for (int i = minIndex; i <= maxIndex; i++) {
            result += buffer.getValue(i);
        }
        return result;
    }

    public static int unsignedSum(MessageBuffer buffer, int minIndex, int maxIndex) {
        int result = 0;
        for (int i = minIndex; i <= maxIndex; i++) {
            result += Byte.toUnsignedInt(buffer.getValue(i));
        }
        return result;
    }


    public static int unsignedSum(byte[] buffer) {
        return unsignedSum(buffer, 0, buffer.length-1);
    }
    public static int unsignedSum(byte[] buffer, int minIndex, int maxIndex) {
        int result = 0;
        for (int i = minIndex; i <= maxIndex; i++) {
            result += Byte.toUnsignedInt(buffer[i]);
        }
        return result;
    }
}
