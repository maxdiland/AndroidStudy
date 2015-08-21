package com.gmail.maxdiland.drebedengireports.util.sql;

/**
 * author Max Diland
 */
public enum  SqlKeyWord {

    SELECT("SELECT"), FROM("FROM"), WHERE("WHERE"), JOIN("JOIN"), GROUP_BY("GROUP BY"),
    HAVING("HAVING"), ORDER_BY("ORDER BY");

    private final String sqlRepresentation;

    SqlKeyWord(String sqlRepresentation) {
        this.sqlRepresentation = sqlRepresentation;
    }

    public String getSqlRepresentation() {
        return sqlRepresentation;
    }

    public String getSqlRepresentationWithSpaces() {
        return " " + sqlRepresentation + " ";
    }
}
