package com.gmail.maxdiland.drebedengireports.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.gmail.maxdiland.drebedengireports.db.search.FinancialOperationSearchRequest;
import com.gmail.maxdiland.drebedengireports.entity.FinancialOperation;

import java.io.File;

/**
 * author Maksim Diland (yc14md1)
 */
public class OperationDao {
    private static final String SELECT_OPERATION =
            "select r.sum, cr.name currency, r.operation_date, t.name, t1.name, r.comment from record r" +
                    "join currency cr on cr.client_id=r.currency_id" +
                    "join target t on t.client_id=r.target_id" +
                    "join target t1 on t1.client_id=r.place_id";

    private final SQLiteDatabase sqliteDatabase;

    public OperationDao(File dbFile) {
        sqliteDatabase = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
    }

    public FinancialOperation[] findOperations(FinancialOperationSearchRequest request) {
        String query = SELECT_OPERATION + " " + request.buildWhereClause();

        Cursor cursor = sqliteDatabase.rawQuery(query, null);
        return null;
    }

}

