package com.gmail.maxdiland.drebedengireports.util.sql;

/**
 * author Maksim Diland
 */
public enum SqlComparisonOperator {
    EQUAL("="),
    NOT_EQUAL("<>"),
    GREATER_THAN(">"),
    LESS_THAN("<"),
    GREATER_EQUAL(">="),
    LESS_EQUAL("<="),
    LIKE("LIKE");

    private final String sqlRepresentation;

    SqlComparisonOperator(String sqlRepresentation) {
        this.sqlRepresentation = sqlRepresentation;
    }

    public String getSqlRepresentation() {
        return sqlRepresentation;
    }

    public String getSqlRepresentationWithSpaces() {
        return " " + sqlRepresentation + " ";
    }

    public static SqlComparisonOperator getDefaultOperator() {
        return EQUAL;
    }
}
