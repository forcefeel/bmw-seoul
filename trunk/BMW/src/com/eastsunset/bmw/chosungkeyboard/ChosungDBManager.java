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
    
    public ArrayList<String> getWords(String chosung, int wordCountLimit, int wordLengthLimit){
        
        ArrayList<String> arrayList = new ArrayList<String>();
        
        Cursor mCursor = db.rawQuery("select distinct nameKo from busstop where chosung like '%" + chosung +"%' order by length(nameKo) limit 0, " + wordCountLimit, null);  
        mCursor.moveToFirst();
        
        if(mCursor.getCount() != 0){
            do{
                if(mCursor.getString(0).length() > wordLengthLimit){
                    arrayList.add(mCursor.getString(0).substring(0, wordLengthLimit));   
                } else {
                    arrayList.add(mCursor.getString(0));
                }
            } while(mCursor.moveToNext());
        }
        mCursor.close();
        return arrayList;
   }
    
    public String[] getCoordinate(String nameKo){
        
        String[] coordinate = new String[2];
        
        Cursor mCursor = db.rawQuery("select TM128_X, TM128_Y  from busstop where nameKo like '" + nameKo +"%'", null);  
        mCursor.moveToFirst();
        
        if(mCursor.getCount() != 0){
                coordinate[0] = String.valueOf(mCursor.getFloat(0));
                coordinate[1] = String.valueOf(mCursor.getFloat(1));
        }
        mCursor.close();
        return coordinate;
   }
    
    public ArrayList<String> searchWord(String word, int wordCountLimit, int wordLengthLimit){
        
        ArrayList<String> arrayList = new ArrayList<String>();
        
        Cursor mCursor = db.rawQuery("select stationnumber, nameKo from busstop where nameKo like '" + word +"%' order by length(nameKo) limit 0, " + wordCountLimit, null);  
        mCursor.moveToFirst();
        
        if(mCursor.getCount() != 0){
            do{
                if(mCursor.getString(0).length() > wordLengthLimit){
                    arrayList.add(mCursor.getString(0).substring(0, wordLengthLimit));   
                } else {
                    arrayList.add(mCursor.getString(0));
                }
            } while(mCursor.moveToNext());
        }
        mCursor.close();
        return arrayList;
   }
    
   public int countChosung(String chosung) {
       Cursor mCursor = db.rawQuery("select count(distinct nameKo) from busstop where chosung like '%" + chosung +"%'", null);
       mCursor.moveToFirst();
       int count = mCursor.getInt(0);
       mCursor.close();
       return count;
   }
    
}
