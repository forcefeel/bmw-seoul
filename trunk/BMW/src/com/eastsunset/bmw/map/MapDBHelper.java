package com.eastsunset.bmw.map;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MapDBHelper
{
    
    SQLiteDatabase db;
    public MapDBHelper(SQLiteDatabase db){
        this.db = db;
    }
    
    
    public String[] getCoordinate(String stationNumber){
        
    	String[] data = new String[4];
        
        Cursor mCursor = db.rawQuery("select WGS84_X, WGS84_Y, nameKo from busstop where stationnumber like " + stationNumber, null);
        mCursor.moveToFirst();
        
        if(mCursor.getCount() != 0){
                data[0] = mCursor.getString(0);
                data[1] = mCursor.getString(1);
                data[2] = mCursor.getString(2);
                data[3] = stationNumber;
        }
        mCursor.close();
        return data;
   }
    
}
