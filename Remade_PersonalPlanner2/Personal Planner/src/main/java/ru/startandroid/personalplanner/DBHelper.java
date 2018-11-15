package ru.startandroid.personalplanner;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "eventsDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table events ("
                + "id integer primary key,"
                + "name text,"
                + "day text,"
                + "month text,"
                + "year text,"
                + "hourB text,"
                + "minuteB text,"
                + "hourE text,"
                + "minuteE text,"
                + "duration text,"
                + "durationB text,"
                + "durationE text"+ ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}