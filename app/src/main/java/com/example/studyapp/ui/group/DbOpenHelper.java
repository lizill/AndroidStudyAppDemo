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
            db.execSQL(InnerDatabase.CreateDB._CREATE0);
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

    public long insertColumn(MessageData data) {
        ContentValues values = new ContentValues();
        values.put(InnerDatabase.CreateDB.ROOM_NAME, data.getRoomName());
        values.put(InnerDatabase.CreateDB.TYPE, data.getType());
        values.put(InnerDatabase.CreateDB.FROM, data.getFrom());
        values.put(InnerDatabase.CreateDB.TO, data.getTo());
        values.put(InnerDatabase.CreateDB.CONTENT, data.getContent());
        values.put(InnerDatabase.CreateDB.SEND_TIME, String.valueOf(data.getSendTime()));
        return mDB.insert(InnerDatabase.CreateDB._TABLENAME0, null, values);
    }

    public Cursor selectColumns(String where) {
        Cursor c = mDB.rawQuery( "SELECT * FROM chat_table WHERE room_name = '" + where + "';", null);
        return c;
    }

}
