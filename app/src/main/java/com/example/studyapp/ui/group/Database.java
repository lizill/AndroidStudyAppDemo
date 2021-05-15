package com.example.studyapp.ui.group;

import android.provider.BaseColumns;

public final class Database {
    public static final class CreateDB implements BaseColumns {
        public static final String ROOM_NAME = "room_name";
        public static final String TYPE = "type";
        public static final String FROM = "f_rom";
        public static final String TO = "t_o";
        public static final String CONTENT = "content";
        public static final String SEND_TIME = "sendTime";
        public static final String _TABLENAME0 = "chat_table";
        public static final String _CREATE0 = "create table if not exists " + _TABLENAME0 + "("
                + _ID + " integer primary key autoincrement, "
                + ROOM_NAME + " text not null , "
                + TYPE + " text not null , "
                + FROM + " text not null , "
                + TO + " text not null, "
                + CONTENT + " text not null , "
                + SEND_TIME + " text not null );";
    }
}
