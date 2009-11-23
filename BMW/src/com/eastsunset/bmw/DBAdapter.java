
package com.eastsunset.bmw;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter 
{
    private static final String DATABASE_NAME = "bmw.db";
    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_TABLES = "CREATE TABLE busstop(stationnumber,nameKo,chosung,WGS84_X,WGS84_Y);";
    
    private DatabaseHelper DBHelper;
    public SQLiteDatabase db;
    public static final DBAdapter only = new DBAdapter();
    
    
    public SQLiteDatabase getDB(){
        return db;
    }
	
    public static DBAdapter getInstance(){
        return only;
    }

    public DBAdapter() 
    {
        DBHelper = new DatabaseHelper(BMW.bmw);
    }
    

    public DBAdapter open() throws SQLException 
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close() 
    {
        DBHelper.close();
    }
        
    private static class DatabaseHelper extends SQLiteOpenHelper 
    {
        DatabaseHelper(Context context) 
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) 
        {
//                db.execSQL(CREATE_TABLES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, 
        int newVersion) 
        {
            Log.w("myra", "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS titles");
            onCreate(db);
        }
    }    
    
}