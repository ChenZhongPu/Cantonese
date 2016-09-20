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

    private static final String SQL_CREATE_WordInfo =
            "CREATE TABLE " + WordInfoItem.WordInfoEntry.TABLE_NAME + " (" +
                    WordInfoItem.WordInfoEntry.COLUMN_BIG5 + " text not null" + COMMA_SEP +
                    WordInfoItem.WordInfoEntry.COLUMN_CANJIE + " text" + COMMA_SEP +
                    WordInfoItem.WordInfoEntry.COLUMN_english + " text" + COMMA_SEP +
                    "FOREIGN KEY(" + WordInfoItem.WordInfoEntry.COLUMN_BIG5 + ") " + " REFERENCES words(big5) ON DELETE CASCADE )";

    private static final String SQL_CREATE_WordMean =
            "CREATE TABLE " + WordMeanItem.WordMeanEntry.TABLE_NAME + " (" +
                    WordMeanItem.WordMeanEntry.COLUM_BIG5 + " text not null" + COMMA_SEP +
                    WordMeanItem.WordMeanEntry.COLUNN_MEANING + " text" + COMMA_SEP +
                    WordMeanItem.WordMeanEntry.COLUMN_SOUND + " text not null" + COMMA_SEP +
                    "FOREIGN KEY(" + WordMeanItem.WordMeanEntry.COLUM_BIG5 + ") " + " REFERENCES words(big5) ON DELETE CASCADE )";


    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + WordItem.WordEntry.TABLE_NAME;

    private static final String SQL_DELETE_WordInfo =
            "DROP TABLE IF EXISTS " + WordInfoItem.WordInfoEntry.TABLE_NAME;

    public static final String SQL_DELETE_WordMeans =
            "DROP TABLE IF EXISTS " + WordMeanItem.WordMeanEntry.TABLE_NAME;

    public WordDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_WordInfo);
        db.execSQL(SQL_CREATE_WordMean);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_DELETE_WordInfo);
        db.execSQL(SQL_DELETE_WordMeans);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    @Override
    public void onConfigure(SQLiteDatabase db){
        db.setForeignKeyConstraintsEnabled(true);
    }
}
