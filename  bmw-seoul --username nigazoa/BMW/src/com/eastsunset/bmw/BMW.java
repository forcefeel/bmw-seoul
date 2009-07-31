package com.eastsunset.bmw;

import java.util.ArrayList;

import com.eastsunset.bmw.bus.SeoulBus;

import android.app.Activity;
import android.app.TabActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;

public class BMW extends TabActivity implements TabHost.OnTabChangeListener{

//	private	ListView busList;
	
	//private TabLayoutHelper helper = new TabLayoutHelper();
	public static Activity bmw;
	private SeoulBus seoulBus;
	
	private static int TAB1_BUS = 0;
	private static int TAB2_METRO = TAB1_BUS ++;
	private static int TAB3_MAP = TAB2_METRO ++;
	private static int TAB4_WEATHER = TAB3_MAP ++;
	
	ArrayList<View> busContentViews = new ArrayList<View>();
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bmw = this;
        DBCopy dbCopy = new DBCopy();
        dbCopy.doCopy();
        DBAdapter.getInstance().open();
        seoulBus = new SeoulBus(getLayoutInflater().inflate(R.layout.seoulbus, null));
        
        TabHost tabHost = getTabHost();	
        tabHost.setOnTabChangedListener(this);
        setupTabs(tabHost);
    }
    

	private void setupTabs(TabHost tabHost) {
		
		//busList = (ListView) seoulBus.getBusView().findViewById(R.id.buslist);
		
		tabHost.addTab(tabHost.newTabSpec("tab1_bus")
	    		.setIndicator("버스")	
	    		.setContent(new TabHost.TabContentFactory(){
	    			public View createTabContent(String tag){
	                	return seoulBus.getBusView();
	                }  
	                }));
		
		tabHost.addTab(tabHost.newTabSpec("tab1_bus")
	    		.setIndicator("바바")	
	    		.setContent(new TabHost.TabContentFactory(){
	    			public View createTabContent(String tag){
	                	return seoulBus.getBusView();
	                }
	                }));
	}
	
	@Override
	public void onTabChanged(String tabId) {
//		else if(tabId.equals("tab2_metro")) helper.setCurrentTab(TAB2_METRO);
//		if(tabId.equals("tab1_bus")) helper.setCurrentTab(TAB1_BUS);
//		else if(tabId.equals("tab3_map")) helper.setCurrentTab(TAB3_MAP);
//		else if(tabId.equals("tab4_weather")) helper.setCurrentTab(TAB4_WEATHER);
	}
	
    @Override
    public void onDestroy() 
    {
        DBAdapter.getInstance().close();
    }
}