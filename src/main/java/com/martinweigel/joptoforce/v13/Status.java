package com.martinweigel.joptoforce.v13;

public enum Status {
    NO_SENSOR (0b000),
    OVERLOAD_FX (0b001),
    OVERLOAD_FY (0b010),
    OVERLOAD_FZ (0b011),
    SENSOR_FAILURE (0b100),
    SENSOR_OK (0b101);

    private final int id;

    Status(int id) {
        this.id = id;
    }

    public int getValue() {
        return id;
    }
}