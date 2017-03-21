package com.martinweigel.joptoforce.v13;

public enum Filter {
    NO_FILTER (0b00),
    Hz_150 (0b01),
    Hz_50 (0b10),
    Hz_15 (0b11);

    private final int id;

    Filter(int id) {
        this.id = id;
    }

    public int getValue() {
        return id;
    }
}