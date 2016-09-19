package com.sjtu.chenzhongpu.cantonese.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by chenzhongpu on 9/19/16.
 */
public class WordDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "worditem.db";

    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + WordItem.WordEntry.TABLE_NAME + " (" +
                    WordItem.WordEntry.COLUMN_BIG5 + " text primary key" + COMMA_SEP +
                    WordItem.WordEntry.COLUMN_CHRACTER + " text not null)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + WordItem.WordEntry.TABLE_NAME;

    public WordDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
