package com.whale.nangua.pumpkingobang.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by nangua on 2016/6/5.
 */
public class DbHelper extends SQLiteOpenHelper  {

    public static final String CREATE_DEVICE = "create table Rank ("
            + "id integer primary key autoincrement, "
            + "name text,"
            + "time integer)";

    private Context context;
    public DbHelper(Context context,String name,SQLiteDatabase.CursorFactory factory,int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //这里应该检测数据库是否已经创建
        db.execSQL(CREATE_DEVICE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}