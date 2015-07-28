package com.gmail.maxdiland.drebedengireports.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gmail.maxdiland.drebedengireports.entity.Currency;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import static com.gmail.maxdiland.drebedengireports.db.DBNames.*;
/**
 * author Max Diland
 */
public class DrebedengiDao {

    private static final String SQL_SELECT_CURRENCIES = "select id, name from currency";

    private final SQLiteDatabase sqliteDatabase;

    public DrebedengiDao(File dbFile) {
        sqliteDatabase = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
    }

    public Currency[] getCurrencies() {
        Cursor cursor = sqliteDatabase.query(
                TABLE_CURRENCY, new String[]{COLUMN_CURRENCY_ID, COLUMN_CURRENCY_NAME},
                null, null, null, null, null
        );

        return mapCurrencies(cursor);
    }


    private Currency[] mapCurrencies(Cursor cursor) {
        ArrayList<Currency> currencies = new ArrayList<>(cursor.getCount());
        int idColumnIndex = cursor.getColumnIndex(COLUMN_CURRENCY_ID);
        int nameColumnIndex = cursor.getColumnIndex(COLUMN_CURRENCY_NAME);
        while (cursor.moveToNext()) {
            currencies.add(
                    new Currency(
                            cursor.getInt(idColumnIndex), cursor.getString(nameColumnIndex)
                    )
            );
        }
        return currencies.toArray(new Currency[currencies.size()]);
    }


    public void close() {
        sqliteDatabase.close();
    }
}
