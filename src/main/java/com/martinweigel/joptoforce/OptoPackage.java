package com.martinweigel.joptoforce;

import java.time.Clock;
import java.time.Instant;

public class OptoPackage {
    private static final Clock clock = Clock.systemDefaultZone();
    public final Instant timestamp;
    protected final int config;
    protected final int[] xyz;
    protected final boolean isValid;

    public OptoPackage(int x, int y, int z, int config, boolean isValid) {
        this.timestamp = clock.instant();
        this.xyz = new int[] {x, y, z};
        this.config = config;
        this.isValid = isValid;
    }

    public boolean isValid() {
        return isValid;
    };
    public int[] getXYZ() {
        return xyz;
    }
    public int getX() {
        return getXYZ()[0];
    }
    public int getY() {
        return getXYZ()[1];
    }
    public int getZ() {
        return getXYZ()[2];
    }
}
