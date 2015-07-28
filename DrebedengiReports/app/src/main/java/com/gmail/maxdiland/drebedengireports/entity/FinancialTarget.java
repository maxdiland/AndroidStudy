package com.gmail.maxdiland.drebedengireports.entity;

/**
 * author Maksim Diland (yc14md1)
 */
public class FinancialTarget {
    private int id;
    private String name;
    private boolean hasParent;

    public FinancialTarget(int id, String name, boolean hasParent) {
        this.id = id;
        this.name = name;
        this.hasParent = hasParent;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isHasParent() {
        return hasParent;
    }

    @Override
    public String toString() {
        return name;
    }
}
