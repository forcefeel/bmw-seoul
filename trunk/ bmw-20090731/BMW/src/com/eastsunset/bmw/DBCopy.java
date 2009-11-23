package com.eastsunset.bmw;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.util.Log;

public class DBCopy
{
    public void doCopy(){
    try {
        String copyTo = "/data/data/com.eastsunset.bmw/databases/bmw.db";
        InputStream in = BMW.bmw.getAssets().open("bmw.db");
        OutputStream out = new FileOutputStream(copyTo);

        byte[] buf = new byte[1024];
        
        int length;
        while ((length = in.read(buf)) > 0) {
            out.write(buf, 0, length);
        }

        out.close();
        in.close();
    } catch (IOException e) {
        Log.v("myra", "DBCOPY EXCEPTION: " + e.toString());
    }
   }
    
}
