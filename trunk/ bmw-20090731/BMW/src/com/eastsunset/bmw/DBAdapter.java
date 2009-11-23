
package com.eastsunset.bmw;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
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
                db.execSQL(CREATE_TABLES);
//                DBDataPackage dataPackage = new DBDataPackage();
//                for(int i = 0; i < dataPackage.getData().length; i++){
//                    db.execSQL("INSERT INTO busstop VALUES(" + dataPackage.getData()[i] + ");");
//                }
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
//    public int getUserID()
//    {
//    	return id;
//    }
//
//    public void setupPackage(String[] list) {
//    	
//    	for(int i=0;i<list.length;i++)
//    	{
//    	    DBAdapter.getInstance().insertNewPackage("category_"+list[i]);
//    	}
//
//    	DBAdapter.getInstance().insertRandomPackage();
//    	
//    }



    
    
//    public void setID(int id){
//    	this.id = id;
//    }
//    

//    // USER - ADD
//    public void insertNewUser(int id, String name, String picture)
//    {
//        ContentValues initialValues = new ContentValues();
//        initialValues.put("_id", id);
//        initialValues.put("name", name);
//        initialValues.put("picture", picture);
//        initialValues.put("average", 0);
//        initialValues.put("package_finished", 0);
//
//        db.insert("user", null, initialValues);
//    }
//    
//    public void updateUser(int id, String name, String picture){
//    	ContentValues args = new ContentValues();
//        args.put("name", name);
//        args.put("picture", picture);
//        db.update("user", args, "_id =" + id, null);
//        
//    }
//        
//
//    //	USER - REMOVE
//    public void removeUser(int id)
//    {
//    	db.delete("user", "_id =" + id, null);
//    	Object[] packageList = getPackageList();
//
//    	ContentValues args;
//    	 
//    	// userdata 점수와 레벨 초기화
//    	for(int i = 0; i < packageList.length; i++)
//    	{
//            args = new ContentValues();
//            args.put("score_id" + id, 0);
//            args.put("level_id" + id, 1);
//            db.update("userdata", args, "package_name = '" + packageList[i].toString() +"'", null);
//    	}
//    	
//    }
//    
//    public int getUserCount()
//    {
//    	Cursor mCursor = db.query("user", new String[] {"_id"},
//				null , null, null, null, null, null);
//    	int userCount = mCursor.getCount();
//    	mCursor.close(); 
//    	return userCount;
//    }
//    
//    public int getPackageCount()
//    {
//    	Cursor mCursor = db.query("userdata", new String[] {"package_name"},
//				null , null, null, null, null, null);
//    	int packageCount = mCursor.getCount();
//    	mCursor.close();
//    	
//    	// dhchang -1 은 랜덤 페키지를 제외시키기 위해
//    	return packageCount - 1;
//    }
//
//    //	USER - UPDATE
//    private boolean updateAverage(int average)
//    {
//        ContentValues args = new ContentValues();
//        args.put("average", average);
//        return db.update("user", args, "_id =" + id, null) > 0;
//    }
//    
//    private boolean updatePackageFinished(int packageFinishedNumber)
//    {
//        ContentValues args = new ContentValues();
//        args.put("package_finished", packageFinishedNumber);
//        return db.update("user", args, "_id =" + id, null) > 0;
//    }
//    
//    private boolean updatePackageFinishedAddOne()
//    {
//    	return updatePackageFinished(getPackageFinished(id) + 1);
//    }
//    
//	public boolean updatePicture(String picture)
//	{
//		ContentValues args = new ContentValues();
//        args.put("picture", picture);
//        return db.update("user", args, "_id =" + id, null) > 0;
//	}
//
//    //	USER - FETCH
//    private Cursor getUser(int id) {
//    	Cursor mCursor = db.query("user", new String[] {"_id", "name", "picture", "average", "package_finished"},
//    				"_id = " + id , null, null, null, null, null);
//    	mCursor.moveToFirst();
//    	return mCursor;
//    }
//    
//    
//
//    public String getName(int id) 
//    {
//    	Cursor mCursor = getUser(id);
//    	
//    	// 이름이 검색되지 않았다면.
//    	if (mCursor.getCount() < 1) {
//    		mCursor.close();	
//    		return null;
//    	}
//    	String name = mCursor.getString(1);
//    	mCursor.close();
//    	return name;
//    }
//    
//    public String getPicture(int id) 
//    {
//    	Cursor mCursor =  getUser(id);
//    	String picture = mCursor.getString(2);
//    	mCursor.close();
//    	return picture;
//    }
//    
//    public int getAverage(int id) 
//    {
//    	Cursor mCursor =  getUser(id);
//    	int average = mCursor.getInt(3);
//    	mCursor.close();
//    	return average;
//    }
//    public int getPackageFinished(int id)
//    {
//    	Cursor mCursor = getUser(id);
//    	int packageFinished = mCursor.getInt(4);
//    	mCursor.close();
//    	return packageFinished;
//    }
//    
//    // PACKAGE - ADD
//    public void insertNewPackage(String packageName)
//    {
//
//    	// 페키지 이름이 이미 DB에 있으면 리턴
//    	if(isPackageExist(packageName)) return;
//    	
//        ContentValues initialValues = new ContentValues();
//    	
//        initialValues = new ContentValues();
//        initialValues.put("package_name", packageName);
//        initialValues.put("level_id0", 1);
//        initialValues.put("level_id1", 1);
//        initialValues.put("level_id2", 1);
//        initialValues.put("score_id0", -1);
//        initialValues.put("score_id1", -1);
//        initialValues.put("score_id2", -1);
//        db.insert("userdata", null, initialValues);
//        
//    }
//    
//    private void insertRandomPackage() 
//    {
//    	if(isPackageExist("category_random")) return;
//    	
//        ContentValues initialValues = new ContentValues();
//    	
//        initialValues = new ContentValues();
//        initialValues.put("package_name", "category_random");
//        initialValues.put("level_id0", 5);
//        initialValues.put("level_id1", 5);
//        initialValues.put("level_id2", 5);
//        initialValues.put("score_id0", -1);
//        initialValues.put("score_id1", -1);
//        initialValues.put("score_id2", -1);
//        db.insert("userdata", null, initialValues);
//	}
//    
//    // PACKAGE - CHECK    (DB에 입력되있는지 체크)
//    public boolean isPackageExist(String packageName)
//    {
//    	Cursor mCursor = db.query("userdata", new String[] {},
//			"package_name = '" + packageName +"'", null, null, null, null, null);
//    	boolean exist = mCursor.getCount() > 0;
//    	mCursor.close();
//    	return exist;
//    }
//    
//    
//    //	PACKAGE - REMOVE
//    public void removePackage(String packageName)
//    {
//    	db.delete("userdata", "package_name = '" + packageName +"'", null);
//    }
//    
//    // USERDATA - UPDATE
//    public boolean updatePackageScore(String packageName, int score)
//    {
//        ContentValues args = new ContentValues();
//        args.put("score_id" + id, score);
//        db.update("userdata", args, "package_name = '" + packageName +"'", null);
//        return updateAverage() > -1;
//    }
//    
//    public boolean updateNextLevel(String packageName)
//    {
//    	int newLevel = getLevel(packageName) + 1; 
//    	if ( newLevel == 5) updatePackageFinishedAddOne();
//    	if ( newLevel == 6) return false;
//        ContentValues args = new ContentValues();
//        args.put("level_id" + id, newLevel);
//        return db.update("userdata", args, "package_name = '" + packageName +"'", null) > 0;
//    }
//    
//    // USERDATA - FETCH
//    private Cursor getUserData(String packageName) 
//    {
//    	Cursor mCursor = db.query("userdata", new String[] {"package_name", "level_id" + id, "score_id" + id},
//    			"package_name = '" + packageName +"'" , null, null, null, null, null);
//    	mCursor.moveToFirst();
//    	
//    	//mCursor.get
//    	return mCursor;
//    }
//    
//    public int getLevel(String packageName)
//    {
//    	if (id < 0) return 1;
//    	Cursor mCursor = getUserData(packageName); 
//    	int level = mCursor.getInt(1);
//    	mCursor.close();
//    	return level;
//    }
//    
//    public int getScore(String packageName)
//    {
//    	Cursor mCursor = getUserData(packageName); 
//    	int score = mCursor.getInt(2);
//    	mCursor.close();
//    	return score;
//    }
//    
//    public Object[] getPackageList(){
//    	
//    	ArrayList<String> arrayList = new ArrayList<String>();
//    	Cursor mCursor = db.query("userdata", new String[] {"package_name"},
//				null , null, null, null, null, null);
//    	mCursor.moveToFirst();
//    	
//    	boolean newLine = true;
//    	while(newLine)
//    	{
//    		arrayList.add(mCursor.getString(0));
//    		newLine = mCursor.moveToNext();
//    	}
//    	
//    	mCursor.close();
//    	return arrayList.toArray();
//    }
//    
//    private int updateAverage()
//    {
//    	int scoredPackageNumber =0;
//    	int totalScore = 0;
//    	int packageScore = 0;
//    	
//    	Cursor mCursor = db.query("userdata", new String[] {"score_id" + id},
//				null , null, null, null, null, null);
//    	mCursor.moveToFirst();
//    	
//    	boolean newLine = true;
//    	while(newLine)
//    	{
//    		packageScore = mCursor.getInt(0); 
//    		if(packageScore >= 0) 
//    		{
//    			totalScore += packageScore;
//    			scoredPackageNumber ++;
//    		}
//    		 newLine = mCursor.moveToNext();
//    	}
//    	int average = totalScore / scoredPackageNumber;
//    	updateAverage(average);
//    	mCursor.close();
//    	return average;
//    }
//    
//    // SETTING
//    private Cursor getSettingCursor() 
//    {
//        Cursor mCursor = db.query("setting", new String[] {"_id", "sound", "vibrate"},
//                    "_id =" + 0, null, null, null, null, null);
//
//        return mCursor;
//    }
//    
//    // GET SOUND SETTING INFO
//    public int getSoundSettingInfo()
//    {
//        Cursor mCursor = getSettingCursor();
//        mCursor.moveToFirst();
//        
//        int settingInfo = mCursor.getInt(1);
//        mCursor.close();
//        return settingInfo;
//    }
//    
//    // GET VIBRATE SETTING INFO
//    public int getVibrateSettingInfo()
//    {
//        Cursor mCursor = getSettingCursor();
//        mCursor.moveToFirst();
//        
//        int settingInfo = mCursor.getInt(2);
//        mCursor.close();
//        return settingInfo;
//    }
//    
//    // UPDATE SOUND SETTING INFO
//    public boolean updateSoundSettingInfo(int soundStatus)
//    {
//        ContentValues args = new ContentValues();
//        args.put("sound", soundStatus);
//        
//        return db.update("setting", args, "_id =" + 0, null) > 0;
//    }
//    
//    // UPDATE VIBRATE SETTING INFO
//    public boolean updateVibrateSettingInfo(int vibrateStatus)
//    {
//        ContentValues args = new ContentValues();
//        args.put("vibrate", vibrateStatus);
//        
//        return db.update("setting", args, "_id =" + 0, null) > 0;
//    }
//    
//    // INIT VIBRATE SETTING INFO
//    public void initSettingInfo()
//    {
//        Cursor mCursor = getSettingCursor();
//        
//        if(mCursor.getCount() == 0) // row 개수가 0이면 최초로 저장함.
//        {
//            ContentValues value = new ContentValues();
//            
//            value.put("_id", 0);
//            value.put("sound", SettingMenuActivity.SETTING_ON_STATUS);
//            value.put("vibrate", SettingMenuActivity.SETTING_ON_STATUS);
//            
//            db.insert("setting", null, value);   
//        }
//        mCursor.close();
//    }
    
}