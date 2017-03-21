package com.martinweigel.joptoforce.v13;

import com.martinweigel.joptoforce.OptoPackage;

public class OptoPackage13 extends OptoPackage {
    final static OptoPackage13 NO_BASELINE =
            new OptoPackage13(0, 0, 0, new int[]{0, 0, 0, 0}, new int[]{0, 0, 0, 0}, 0, 0, false);
    private OptoPackage13 baseline = NO_BASELINE;

    protected final int[] raw, compensated;
    protected final int temperature;

    public OptoPackage13(int x, int y, int z, int[] raw, int[] compensated, int temperature, int config, boolean isValid) {
        super(x, y, z, config, isValid);
        this.raw = raw;
        this.compensated = compensated;
        this.temperature = temperature;
    }

    public OptoPackage13 getBaseline() {
        return baseline;
    }

    public void setBaseline(OptoPackage13 baseline) {
        if (baseline != null)
            this.baseline = baseline;
        else
            this.baseline = NO_BASELINE;
    }

    protected int[] useBaseline(int[] values, int[] baseline) {
        assert (values.length == baseline.length);

        int[] result = values.clone();
        for (int i = 0; i < result.length; i++) {
            result[i] -= baseline[i];
        }
        return result;
    }

    public int[] getXYZ() {
        return useBaseline(super.xyz, baseline.xyz);
    }

    public int[] getRawReadings() {
        return useBaseline(raw, baseline.raw);
    }

    public int[] getCompensatedReadings() {
        return useBaseline(compensated, baseline.compensated);
    }

    public int getTemperature() {
        return temperature;
    }


    public boolean isCompensated() {
        return (config & 0b1) == 1;
    }

    public Status getStatus() {
        return Status.values()[(config & 0b11100000) >> 5];
    }

    public Speed getFrequency() {
        return Speed.values()[(config & 0b11000) >> 3];
    }

    public Filter getBandwidthFilter() {
        return Filter.values()[(config & 0b110) >> 1];
    }



    /**
     * Calculates the total squeeze force. Should be roughly equivalent to -z.
     * @return Amount of squeeze force
     */
    public int getSqueezeForce() {
        int[] readings = getCompensatedReadings();
        return (readings[0] + readings[1] + readings[2] + readings[3]) / 4;
    }

    public enum SqueezeDirection {
        HORIZONTAL,
        VERTICAL
    }

    /**
     * Finds out if the squeeze is vertical or horizontal by finding the side with the maximal deformation.
     * @return Direction of the maximum squeeze deformation
     */
    public SqueezeDirection getSqueezeDirection() {
        int[] readings = getCompensatedReadings();
        double hMax = Math.max(-readings[0], -readings[2]);
        double vMax = Math.max(-readings[1], -readings[3]);

        return (hMax > vMax) ? SqueezeDirection.HORIZONTAL : SqueezeDirection.VERTICAL;
    }
}
