package com.martinweigel.joptoforce.v13;

public enum Speed {
    Hz_1000 (0b00),
    Hz_333 (0b01),
    Hz_100 (0b10),
    Hz_30 (0b11);

    private final int id;

    Speed(int id) {
        this.id = id;
    }

    public int getValue() {
        return id;
    }
}