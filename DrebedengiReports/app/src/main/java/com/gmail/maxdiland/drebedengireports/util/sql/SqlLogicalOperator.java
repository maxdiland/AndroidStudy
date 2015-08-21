package com.gmail.maxdiland.drebedengireports.util.sql;

/**
 * author Maksim Diland
 */
public enum SqlLogicalOperator {
    AND("AND"),
    OR("OR");

    private final String sqlRepresentation;

    SqlLogicalOperator(String sqlRepresentation) {
        this.sqlRepresentation = sqlRepresentation;
    }

    public String getSqlRepresentation() {
        return sqlRepresentation;
    }

    public String getSqlRepresentationWithSpaces() {
        return " " + sqlRepresentation + " ";
    }
}
