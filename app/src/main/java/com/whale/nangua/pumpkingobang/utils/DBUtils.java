package com.whale.nangua.pumpkingobang.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by nangua on 2016/5/19.
 */
public class DBUtils  {
    //数据库版本号
    final static int VERSION = 1;
    //声明数据库
    private SQLiteDatabase db;
    public DBUtils(Context context) {
        DbHelper helper = new DbHelper(context,"Rank",null,VERSION);
        //获得数据库
        db = helper.getWritableDatabase();
    }

    public boolean insertTime(int time,String name) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("time", time);
        long result = db.insert("Rank",null,values);
        if (result!=-1) {
            return true ;
        } else {
            return false ;
        }

    };

    public  ArrayList<String[]>  queryRank() {
        ArrayList<String[]> ranklist = new ArrayList<>();
        //查询Device表中所有的数据
        Cursor cursor = db.query("Rank",new String[]{"name,time"},null,null,null,null,"time asc");
        if (cursor.moveToFirst()) {
            do{
                String[] ranks = new String[2];
                ranks[0] = cursor.getString(0);
                ranks[1] = String.valueOf(cursor.getInt(1));
                ranklist.add(ranks);
            } while (cursor.moveToNext());
        }
        return ranklist;
    }

    public void qingkong() {
        db.execSQL("DELETE FROM Rank");
    }


}