package com.martinweigel.joptoforce.v14;

public enum Speed {
    STOP (0),
    Hz_1000 (1),
    Hz_333 (3),
    Hz_100 (10),
    Hz_30 (100);

    private final int id;

    Speed(int id) {
        this.id = id;
    }

    public int getValue() {
        return id;
    }
}