package com.gmail.maxdiland.drebedengireports.db.entity;

import com.gmail.maxdiland.drebedengireports.db.util.annotation.Field;

/**
 * author Maksim Diland (yc14md1)
 */
public class FinancialTarget {
    @Field("client_id")
    private int id;
    @Field("name")
    private String name;
    @Field("server_id")
    private Integer serverId;
    @Field("parent_id")
    private int parentId;

    public FinancialTarget() {}

    public FinancialTarget(int id, String name, int parentId, Integer serverId) {
        this.id = id;
        this.name = name;
        this.serverId = serverId;
        this.parentId = parentId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getParentId() {
        return parentId;
    }

    public Integer getServerId() {
        return serverId;
    }

    @Override
    public String toString() {
        return name;
    }
}
