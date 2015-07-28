package com.gmail.maxdiland.drebedengireports.db;

import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;

//import org.sqlite.JDBC;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * author Max Diland
 */
public class DBConnectionResolver {
    private static final String SQL_LITE_JDBC_URL_PATTERN = "jdbc:sqlite:%s";


    static {
        registerSqliteDriver();
    }

    public static Connection getConnection(File dbFile) {
        SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        String jdbcUrl = String.format(SQL_LITE_JDBC_URL_PATTERN, dbFile.getAbsolutePath());
        try {
            return DriverManager.getConnection(jdbcUrl);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to open connection to the database: " + jdbcUrl);
        }
    }

    private static void registerSqliteDriver() {

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
//        try {
//            DriverManager.registerDriver(new JDBC());
//        } catch (SQLException e) {
//            throw new RuntimeException("Unable to load SQLite driver");
//        }
    }
}
