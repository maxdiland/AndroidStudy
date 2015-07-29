package com.gmail.maxdiland.drebedengireports.entity;

import com.gmail.maxdiland.drebedengireports.db.util.annotation.Field;

/**
 * author Max Diland
 */
public class Currency {
    @Field("client_id")
    private int id;
    @Field("name")
    private String name;

    public Currency() {}

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
