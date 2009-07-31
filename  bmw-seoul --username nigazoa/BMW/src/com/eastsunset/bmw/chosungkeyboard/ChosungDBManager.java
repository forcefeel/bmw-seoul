package com.eastsunset.bmw.chosungkeyboard;

import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ChosungDBManager
{
    
    SQLiteDatabase db;
    public ChosungDBManager(SQLiteDatabase db){
        this.db = db;
    }
    
    public ArrayList<String> searchChosung(String chosung, int wordCountLimit, int wordLengthLimit){
        
        ArrayList<String> arrayList = new ArrayList<String>();
        
        Cursor mCursor = db.rawQuery("select distinct nameKo from busstop where chosung like '%" + chosung +"%' order by length(chosung) limit 0, " + wordCountLimit, null);  
        mCursor.moveToFirst();
        
        while(mCursor.moveToNext())
        {
            if(mCursor.getString(0).length() > wordLengthLimit){
                arrayList.add(mCursor.getString(0).substring(0, wordLengthLimit));   
            } else {
                arrayList.add(mCursor.getString(0));
            }
            
        }
        
        mCursor.close();
        
        return arrayList;
          
      }
}
