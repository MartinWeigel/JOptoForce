package com.martinweigel.joptoforce.v14;

public enum Filter {
    NO_FILTER (0),
    Hz_500 (1),
    Hz_150 (2),
    Hz_50 (3),
    Hz_15 (4),
    Hz_5 (5),
    Hz_1_5 (6);

    private final int id;

    Filter(int id) {
        this.id = id;
    }

    public int getValue() {
        return id;
    }
}