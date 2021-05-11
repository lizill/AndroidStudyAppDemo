package com.example.studyapp.ui.group;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbOpenHelper {

    private static final String DATABASE_NAME = "innerDatabase(SQLite).db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    private DatabaseHelper mDBHelper;
    private Context mCtx;

    private class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(Database.CreateDB._CREATE0);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

        @Override
        public void onConfigure(SQLiteDatabase db) {
            super.onConfigure(db);
            db.disableWriteAheadLogging();
        }
    }

    public DbOpenHelper(Context context) {
        this.mCtx = context;
    }

    public DbOpenHelper open() throws SQLException {
        mDBHelper = new DatabaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }

    public void create() {
        mDBHelper.onCreate(mDB);
    }

    public void close() {
        mDB.close();
    }

    public long insertColumn(String room_name, String type, String from, String to, String content, String send_time) {
        ContentValues values = new ContentValues();
        values.put(Database.CreateDB.ROOM_NAME, room_name);
        values.put(Database.CreateDB.TYPE, type);
        values.put(Database.CreateDB.FROM, from);
        values.put(Database.CreateDB.TO, to);
        values.put(Database.CreateDB.CONTENT, content);
        values.put(Database.CreateDB.SEND_TIME, send_time);
        return mDB.insert(Database.CreateDB._TABLENAME0, null, values);
    }

    public Cursor selectColumns(String where) {
        Cursor c = mDB.rawQuery( "SELECT * FROM chat_table WHERE room_name = '" + where + "';", null);
        return c;
    }

}
