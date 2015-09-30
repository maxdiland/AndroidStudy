package com.gmail.maxdiland.drebedengireports.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gmail.maxdiland.drebedengireports.db.entity.Currency;
import com.gmail.maxdiland.drebedengireports.db.entity.FinancialTarget;
import com.gmail.maxdiland.drebedengireports.util.sql.SqlUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.gmail.maxdiland.drebedengireports.db.DBSchemaNames.*;
import static com.gmail.maxdiland.drebedengireports.db.util.CursorOnObjectMapper.*;
import static com.gmail.maxdiland.drebedengireports.util.sql.SqlUtil.*;

/**
 * author Max Diland
 */
public class DrebedengiEntityDao {
    public static final int TYPE_CODE_EXPENSE = 3;
    public static final int TYPE_CODE_MONEY_PLACE = 4;
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

    public FinancialTarget[] getSortedExpenseCategories() {
        String whereClause = buildAndClause(
                buildEqualsClause(COLUMN_TARGET_TYPE, TYPE_CODE_EXPENSE),
                buildEqualsClause(COLUMN_TARGET_PARENT_ID, FinancialTarget.ROOT_PARENT_ID)
        );

        FinancialTarget[] rootCategories = getExpenseCategories(whereClause, COLUMN_TARGET_SORT);

        List<FinancialTarget> sortedCategories = new ArrayList<>();
        for (FinancialTarget rootCategory : rootCategories) {
            String childWhereClause = buildAndClause(
                    buildEqualsClause(COLUMN_TARGET_TYPE, TYPE_CODE_EXPENSE),
                    buildEqualsClause(COLUMN_TARGET_PARENT_ID, rootCategory.getServerId())
            );
            sortedCategories.add(rootCategory);
            sortedCategories.addAll(
                    Arrays.asList( getExpenseCategories(childWhereClause, COLUMN_TARGET_SORT) )
            );
        }

        return sortedCategories.toArray(new FinancialTarget[sortedCategories.size()]);
    }

    public FinancialTarget[] getExpenseCategories(String where, String orderBy) {
        Cursor cursor = sqliteDatabase.query(
                TABLE_TARGET,
                new String[]{
                        COLUMN_TARGET_ID, COLUMN_TARGET_NAME,
                        COLUMN_TARGET_PARENT_ID, COLUMN_TARGET_SERVER_ID
                },
                where, null, null, null, orderBy
        );
        FinancialTarget[] financialTargets = mapObjects(cursor, FinancialTarget.class);
        cursor.close();
        return financialTargets;
    }

    public FinancialTarget[] getMoneyPlaceCategories() {
        Cursor cursor = sqliteDatabase.query(
                TABLE_TARGET, new String[]{COLUMN_TARGET_ID, COLUMN_TARGET_NAME, COLUMN_TARGET_PARENT_ID},
                SqlUtil.buildEqualsClause(COLUMN_TARGET_TYPE, TYPE_CODE_MONEY_PLACE),
                null, null, null, null
        );
        FinancialTarget[] financialTargets = mapObjects(cursor, FinancialTarget.class);
        cursor.close();
        return financialTargets;
    }

    public void close() {
        sqliteDatabase.close();
    }
}
