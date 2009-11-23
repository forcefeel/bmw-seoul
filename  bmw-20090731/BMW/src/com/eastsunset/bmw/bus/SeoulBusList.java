package com.eastsunset.bmw.bus;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;

import com.eastsunset.bmw.FileDownloader;
import com.eastsunset.bmw.bus.SeoulBusDataRetriever.Mode;

public class SeoulBusList {
	
	private boolean updateFinished;
	private ArrayList<String> dataArray;
    //private ArrayAdapter<String> dataAdapter;
	private Thread updateThread;
    
    public SeoulBusList(){
    	//this.updateThread = updateThread;
    }
	
    public synchronized void updateFinished(){
		updateFinished = true;
	}
	
//	 public ArrayAdapter<String> start(String a) {
//		 dataArray = getList(a);
//		 return null;
//	 }
	    
	 public void update(String url, Mode mode) {
		 dataArray.addAll(generateList(url, getDataPattern(mode)));
	 }
	 
	public ArrayList<String> getData(String url, Mode mode) {
		Log.v("myra", "buslist getData uRL "+  url);
		ArrayList<String> dataArray = generateList(url, getDataPattern(mode));
		return dataArray; 
	}  
	
	private Pattern getDataPattern(Mode mode){
		if(mode == Mode.busNumber)
			return Pattern.compile("<a title=\"확인\" href=\"(.+)\" accesskey=\"[0-9]\">(.+)[ ]+\\[([0-9]+)[ ]+\\]</a></div>");
		if(mode == Mode.busStop)
			return Pattern.compile("<a title=\"확인\" href=\"(.+)\" accesskey=\"[0-9]\">(.+)[ ]+\\[([0-9]+)[ ]+\\]</a></div>");
		else {
			Log.v("myra", "BUSSTOPNAME MODE");
			return Pattern.compile("<a title=\"확인\" href=\"(.+)\" accesskey=\"[0-9]\">(.+)[ ]+\\[([0-9]+)\\]</a></div>");
		}
			
	}
		
	 private ArrayList<String> generateList(String downloadUrl, Pattern busSearchPattern) {
		 
		 int pageNumber = 1;
		 
		 FileDownloader fdn = new FileDownloader();
		 
		 Log.v("myra", "buslist generateList uRL "+  downloadUrl);
		 String data = fdn.downloadText(downloadUrl);
		 Log.v("myra", "data "+  data);
		 ArrayList<String> dataArray = new ArrayList<String>();
		 
		 Pattern nextPagePattern = Pattern.compile("\\[<span style=\"color:#FF0000;\">");
		 Matcher nextPagePatternMatches = nextPagePattern.matcher(data);
		 Matcher matches = busSearchPattern.matcher(data);
		   
		 while (matches.find()) {  
			 Log.v("myra", "we got one ");
			 dataArray.add(matches.group(1).replace("&amp;", "&"));
			 dataArray.add(matches.group(2).trim());
			 dataArray.add(matches.group(3));
		 }
		 
		 while(nextPagePatternMatches.find()){
			 Log.v("myra","WHILE 2");
			 matches = busSearchPattern.matcher(data);  
			 while (matches.find()) {
				 dataArray.add(matches.group(1).replace("&amp;", "&"));
				 dataArray.add(matches.group(2).trim());
				 dataArray.add(matches.group(3));
			 }
			 data = fdn.downloadText(downloadUrl + "&cpage=" + pageNumber++);
			 nextPagePatternMatches = nextPagePattern.matcher(data);
		 }
			return dataArray;
	 }


}
