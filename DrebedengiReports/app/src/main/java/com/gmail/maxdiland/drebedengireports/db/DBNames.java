package com.gmail.maxdiland.drebedengireports.db;

/**
 * author Max Diland
 */
public class DBNames {
    public static final String TABLE_CURRENCY = "currency";
    public static final String COLUMN_CURRENCY_ID = "client_id";
    public static final String COLUMN_CURRENCY_NAME = "name";

    public static final String TABLE_TARGET = "target";
    public static final String COLUMN_TARGET_ID = "client_id";
    public static final String COLUMN_TARGET_NAME = "name";
    public static final String COLUMN_TARGET_PARENT_ID = "parent_id";

    public static final String TABLE_RECORD = "record";
    public static final String COLUMN_RECORD_ID = "client_id";
    public static final String COLUMN_RECORD_SUM = "sum";
    public static final String COLUMN_RECORD_CURRENCY_ID = "currency_id";
    public static final String COLUMN_RECORD_TARGET_ID = "target_id";
    public static final String COLUMN_RECORD_PLACE_ID = "place_id";
    public static final String COLUMN_RECORD_DATE = "operation_date";
    public static final String COLUMN_RECORD_COMMENT = "comment";
}