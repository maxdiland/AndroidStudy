package com.gmail.maxdiland.drebedengireports.entity;

/**
 * author Max Diland
 */
public class Currency {
    private final int id;
    private final String name;

    public Currency(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
