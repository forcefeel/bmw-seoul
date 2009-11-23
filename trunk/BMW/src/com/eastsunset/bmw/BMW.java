package com.eastsunset.bmw;

import java.util.ArrayList;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TabHost;

import com.eastsunset.bmw.bus.SeoulBus;
import com.eastsunset.bmw.map.MapMain;
import com.eastsunset.bmw.weather.WeatherMain;

public class BMW extends TabActivity implements TabHost.OnTabChangeListener{

	public static Activity bmw;
	private SeoulBus seoulBus;
	
	public static final int BUSSTOP_SEARCH = 0;

	public static final String BACKGROUND_DRAWABLE_BUS		= "#93b900";
	public static final String BACKGROUND_DRAWABLE_MAP		= "#0086a8";
	public static final String BACKGROUND_DRAWABLE_WEATHER 	= "#A00331";
	
	ArrayList<View> busContentViews = new ArrayList<View>();
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getConfiguration().keyboardHidden == Configuration.KEYBOARDHIDDEN_YES){
            Log.v("score", "aodsihjqaw ojdioqwj doiwqjdioqjwd qwd ");
        }
        
        
        bmw = this;
        DBCopy dbCopy = new DBCopy();
        dbCopy.doCopy();
        DBAdapter.getInstance().open();
        seoulBus = new SeoulBus(getLayoutInflater().inflate(R.layout.seoulbus, null));
        
        getWindow().setBackgroundDrawable( new ColorDrawable(Color.TRANSPARENT) ); 
        TabHost tabHost = getTabHost();	
        tabHost.setOnTabChangedListener(this);
        setupTabs(tabHost);
    }
    

	private void setupTabs(TabHost tabHost) {
		
		tabHost.addTab(tabHost.newTabSpec("tab1_bus")
	    		.setIndicator("버스", this.getResources().getDrawable(R.drawable.ic_menu_myplaces))
	    		.setContent(new TabHost.TabContentFactory(){
	    			public View createTabContent(String tag){
	                	return seoulBus.getBusView();
	                }  
	    		}));
		tabHost.addTab(tabHost.newTabSpec("tab2_map")
	    		.setIndicator("지도", this.getResources().getDrawable(R.drawable.ic_menu_compass))
	    		.setContent(new Intent(BMW.bmw, MapMain.class))); 
		
		tabHost.addTab(tabHost.newTabSpec("tab3_weather")
	    		.setIndicator("날씨", this.getResources().getDrawable(R.drawable.ic_menu_gallery))
	    		.setContent(new Intent(BMW.bmw, WeatherMain.class)));
	}
	
	public void onTabChanged(String tabId) {

	    if(tabId.equals("tab1_bus")) getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor(BACKGROUND_DRAWABLE_BUS)));
	    else if(tabId.equals("tab2_map"))  getWindow().setBackgroundDrawable( new ColorDrawable(Color.parseColor(BACKGROUND_DRAWABLE_MAP)));
	    else if(tabId.equals("tab3_weather"))  getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor(BACKGROUND_DRAWABLE_WEATHER)));
	}
	
	
    @Override
    public void onDestroy() 
    {   
        super.onDestroy();
    }
}