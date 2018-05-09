package com.example.lattitude.w2d2e1;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Lattitude on 5/8/2018.
 */

public class usersDB extends SQLiteOpenHelper {
    private final String USERS_TABLE_NAME = "users";
    private final String COLUMN_1 = "id";
    private final String COLUMN_2 = "name";
    private final String COLUMN_3 = "lastname";
    private final String COLUMN_4 = "email";
    private final String COLUMN_5 = "imagepath";
    private String sqlCreate =
            "CREATE TABLE IF NOT EXISTS "+USERS_TABLE_NAME+" ("+COLUMN_1+
                    " INTEGER PRIMARY KEY AUTOINCREMENT, "+COLUMN_2+
                    " VARCHAR NOT NULL, "+COLUMN_3+
                    " VARCHAR NOT NULL, "+COLUMN_4+
                    " VARCHAR,"+COLUMN_5+
                    " VARCHAR)";
    private String sqlDrop = "DROP TABLE IF EXISTS "+USERS_TABLE_NAME;

    public usersDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public String getCOLUMN_1() {
        return COLUMN_1;
    }

    public String getCOLUMN_2() {
        return COLUMN_2;
    }

    public String getCOLUMN_3() {
        return COLUMN_3;
    }

    public String getCOLUMN_4() {
        return COLUMN_4;
    }

    public String getCOLUMN_5() {
        return COLUMN_5;
    }

    public String getUSERS_TABLE_NAME() {
        return USERS_TABLE_NAME;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(sqlDrop);
        sqLiteDatabase.execSQL(sqlCreate);

    }

}
