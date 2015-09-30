package com.gmail.maxdiland.drebedengireports.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gmail.maxdiland.drebedengireports.db.util.CursorOnObjectMapper;
import com.gmail.maxdiland.drebedengireports.db.entity.FinancialOperation;
import com.gmail.maxdiland.drebedengireports.log.Tag;
import com.gmail.maxdiland.drebedengireports.request.SqlWhereClauseBuildable;

import java.io.File;

/**
 * author Maksim Diland (yc14md1)
 */
public class OperationDao {
    private static final String SELECT_OPERATION =
            "select r.sum, cr.name currency, r.operation_date, t1.name place, t.name target, r.comment from record r " +
                    "join currency cr on cr.client_id=r.currency_id " +
                    "join target t on t.client_id=r.target_id " +
                    "join target t1 on t1.client_id=r.place_id ";

    private final SQLiteDatabase sqliteDatabase;

    public OperationDao(File dbFile) {
        sqliteDatabase = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
    }

    public FinancialOperation[] findOperations(SqlWhereClauseBuildable request) {
      return findOperations(request.buildWhereClause());
    }

    public FinancialOperation[] findOperations(String whereClause) {
        String query = SELECT_OPERATION + whereClause;
        Log.d(Tag.SQL_QUERY, "Making request:\n" + query);
        Cursor cursor = sqliteDatabase.rawQuery(query, null);
        return CursorOnObjectMapper.mapObjects(cursor, FinancialOperation.class);
    }

    public void close() {
        sqliteDatabase.close();
    }

}

