package com.eastsunset.bmw.bus;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.eastsunset.bmw.FileDownloader;

import android.view.View;

public class SeoulBusStop {
	
	
	
	public SeoulBusStop(){
		
	}
	
	public View getView(String url) {
		
		ArrayList<String> busStop = getBusStop(url);
		
		
		
		return null;
		
	}
	
	public ArrayList<String> getBusStop(String downloadUrl){
		ArrayList<String> busStop = new ArrayList<String>();
		
		 FileDownloader fdn = new FileDownloader();
		 String data = fdn.downloadText(downloadUrl);
		 
		 Pattern busStopNamePattern = Pattern.compile("<div><b>(.*) 노선번호</b></div>");
		 Matcher busStopNameMacher = busStopNamePattern.matcher(data);
		 
		 Pattern busLinePattern = Pattern.compile(" accesskey=\"[0-9]\">([0-9]+)      &nbsp;:&nbsp;(.+)</a></div>");
		 Matcher busLineMatcher = busLinePattern.matcher(data);
		 
		 busStop.add(busStopNameMacher.group(1));

		 while (busLineMatcher.find()) {  
			 busStop.add(busLineMatcher.group(1));
			 busStop.add(busLineMatcher.group(2));
		 }
		 
		 return busStop;
	}

}