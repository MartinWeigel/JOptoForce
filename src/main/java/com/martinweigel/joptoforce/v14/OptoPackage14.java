package com.martinweigel.joptoforce.v14;

import com.martinweigel.joptoforce.OptoPackage;

public class OptoPackage14 extends OptoPackage {
    protected final int messageCounter;

    public OptoPackage14(int messageCounter, int x, int y, int z, int config, boolean isValid) {
        super(x, y, z, config, isValid);
        this.messageCounter = messageCounter;
    }

    public int getCounter() {
        return messageCounter;
    };
}
