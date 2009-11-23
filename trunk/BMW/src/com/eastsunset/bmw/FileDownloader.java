package com.eastsunset.bmw;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import android.util.Log;
import android.widget.Toast;

public class FileDownloader {

	public String downloadText(String URL)
    {
	    
        try {
            URL = URLEncoder.encode(URL,"euc-kr");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        URL = URL.replace("%3A", ":");
        URL = URL.replace("%2F", "/");
        URL = URL.replace("%3F", "?");
        URL = URL.replace("%3D", "=");
        URL = URL.replace("%26", "&");
        URL = URL.replace("amp%3B", "");
	    
        int BUFFER_SIZE = 1000;
        InputStream in = null;
        try {
            in = OpenHttpConnection(URL);
        } catch (IOException e1) {
            e1.printStackTrace();
            return "error !!!!!!!!!!!!";
        }
        
        InputStreamReader isr = null;
		try {
			isr = new InputStreamReader(in,"EUC-KR");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NullPointerException e){
		    Log.v("myra", "conenction eerrror");
            Toast.makeText(BMW.bmw, "인터넷 연결이 필요합니다.", 5000).show();
            return null;
		}
        int charRead;
          String str = "";
          char[] inputBuffer = new char[BUFFER_SIZE];     
        try {
            while ((charRead = isr.read(inputBuffer)) > 0)
            {                    
                String readString = String.copyValueOf(inputBuffer, 0, charRead);                    
                str += readString;
                inputBuffer = new char[BUFFER_SIZE];
            }
            in.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "error !!!!!!!!!!!!";
        }    
        return str;        
    }


    
    private InputStream OpenHttpConnection(String urlString) throws IOException
    {
        InputStream in = null;
        int response = -1;
               
        URL url = new URL(urlString); 
        URLConnection conn = url.openConnection();
                 
        if (!(conn instanceof HttpURLConnection))                     
            throw new IOException("Not an HTTP connection");
        
        try{
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect(); 

            response = httpConn.getResponseCode();                 
            if (response == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();                                 
            }                     
        }
        catch (Exception ex)
        {

            throw new IOException("Error connecting");            
        }
        return in;     
    }
}
