package com.eastsunset.bmw.bus;
//package com.eastsunset.bmw;
//
//import java.util.ArrayList;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import android.util.Log;
//
//public class SeoulBusSearch {
//	
//	static enum Mode { busStop, busNumber, busStopName} 
//		
//	private final String searchingUrl = "http://m.seoul.go.kr/traffic/BusInfoSearch.do";
//	private SeoulBusList seoulBusList = new SeoulBusList();
//	
//	public ArrayList<String> doSearch(String keyword){
//		
//		Log.v("myra", "keyword " + keyword);
//		keyword = keyword.trim();
//		
//		if(keyword.length() < 2) {
//			//TOO SHORT
//		}
//		
//		// Check BusStop Number
//		Pattern pattern = Pattern.compile("(\\d\\d\\d\\d\\d)");
//		Matcher matches = pattern.matcher(keyword);
//		if(matches.find()){
//			matches.group(0);
//			Log.v("myra", " BusStop matches.group(0) " + matches.group(0));
//			return seoulBusList.getData(searchingUrl + "?category=1&busSearch=" + keyword, Mode.busStop);
//		}
//		
//		// Check Bus Number
//		pattern = Pattern.compile("(\\d++)");
//		matches = pattern.matcher(keyword);
//		if(matches.find()){
//			matches.group(0);
//			Log.v("myra", "Bus matches.group(0) " + matches.group(0));
//			return seoulBusList.getData(searchingUrl + "?category=1&busSearch=" + keyword, Mode.busNumber);
//			
//		}
//		
//		// Check BusStop Name
//		return seoulBusList.getData(searchingUrl + "?category=2&busSearch=" + keyword, Mode.busStopName);
//		
//		
//	}
//
//	
//}
