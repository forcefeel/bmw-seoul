package com.eastsunset.bmw.bus;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.eastsunset.bmw.BMW;

import android.app.Activity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ListView;
import android.widget.TextView;

public class SeoulBusDataRetriever {

	static enum Mode { busStop, busNumber, busStopName} 
	final static SeoulBusDataRetriever only = new SeoulBusDataRetriever(); 
		
	private final String searchingUrl = "http://m.seoul.go.kr/traffic/BusInfoSearch.do";
	private SeoulBusList seoulBusList = new SeoulBusList();
	
	public static SeoulBusDataRetriever getInstance() {
		return only;
	}
	
	public ArrayList<String> retrieveData(String url) {
		
		return null;
		
	}
	
	
	public void doSearch(View view, String url){
		
	}
	public void doSearch(ListView lv, String keyword){
		
		
		ArrayList<String> dataList = getBusSearchResult(keyword);
		SeoulBusListAdapter busListAdapter = new SeoulBusListAdapter(BMW.bmw, SeoulBus.gd, dataList);
		lv.setAdapter(busListAdapter);
		lv.setOnTouchListener(SeoulBus.otl);
		
	}
	
	private ArrayList<String> getBusSearchResult(String keyword){
		keyword = "Ω√√ª";
		keyword = keyword.trim();
		if(keyword.length() < 2) {
			//TOO SHORT
		}
		try {
			keyword = URLEncoder.encode(keyword,"euc-kr");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Check BusStop Number
		Pattern pattern = Pattern.compile("(\\d\\d\\d\\d\\d)");
		Matcher matches = pattern.matcher(keyword);
		if(matches.find()){
			matches.group(0);
			return seoulBusList.getData(searchingUrl + "?category=1&busSearch=" + keyword, Mode.busStop);
		}
		
		
		pattern = Pattern.compile("(\\%[A-Z][A-Z]++)");
		matches = pattern.matcher(keyword);
		if(matches.find()){
			matches.group(0);
			return seoulBusList.getData(searchingUrl + "?category=2&busSearch=" + keyword, Mode.busStopName);
		}
		
		
		// Check Bus Number
		pattern = Pattern.compile("(\\d++)");
		matches = pattern.matcher(keyword);
		if(matches.find()){
			matches.group(0);
			return seoulBusList.getData(searchingUrl + "?category=3&busSearch=" + keyword, Mode.busNumber);
		}
		
		// TODO: DO NOT NEED BELOW LINE
		// Check BusStop Name
		return seoulBusList.getData(searchingUrl + "?category=2&busSearch=" + keyword, Mode.busStopName);
		
		
	}

}
