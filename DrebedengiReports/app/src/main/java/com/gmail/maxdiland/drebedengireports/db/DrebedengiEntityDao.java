package com.gmail.maxdiland.drebedengireports.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gmail.maxdiland.drebedengireports.db.entity.Currency;
import com.gmail.maxdiland.drebedengireports.db.entity.FinancialTarget;

import java.io.File;

import static com.gmail.maxdiland.drebedengireports.db.DBNames.*;
import static com.gmail.maxdiland.drebedengireports.db.util.CursorOnObjectMapper.*;

/**
 * author Max Diland
 */
public class DrebedengiEntityDao {
    private final SQLiteDatabase sqliteDatabase;

    public DrebedengiEntityDao(File dbFile) {
        sqliteDatabase = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
    }

    public Currency[] getCurrencies() {
        Cursor cursor = sqliteDatabase.query(
                TABLE_CURRENCY, new String[]{COLUMN_CURRENCY_ID, COLUMN_CURRENCY_NAME},
                null, null, null, null, null
        );
        Currency[] currencies = mapObjects(cursor, Currency.class);
        cursor.close();
        return currencies;
    }

    public FinancialTarget[] getExpenseCategories() {
        Cursor cursor = sqliteDatabase.query(
                TABLE_TARGET,
                new String[]{
                        COLUMN_TARGET_ID, COLUMN_TARGET_NAME,
                        COLUMN_TARGET_PARENT_ID, COLUMN_TARGET_SERVER_ID
                },
                "type=3", null, null, null, null
        );
        FinancialTarget[] financialTargets = mapObjects(cursor, FinancialTarget.class);
        cursor.close();
        return financialTargets;
    }

    public FinancialTarget[] getMoneyPlaceCategories() {
        Cursor cursor = sqliteDatabase.query(
                TABLE_TARGET, new String[]{COLUMN_TARGET_ID, COLUMN_TARGET_NAME, COLUMN_TARGET_PARENT_ID},
                "type=4", null, null, null, null
        );
        FinancialTarget[] financialTargets = mapObjects(cursor, FinancialTarget.class);
        cursor.close();
        return financialTargets;
    }

    public void close() {
        sqliteDatabase.close();
    }
}
