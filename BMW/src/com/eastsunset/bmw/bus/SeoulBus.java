package com.eastsunset.bmw.bus;

import android.app.Activity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eastsunset.bmw.BMW;
import com.eastsunset.bmw.HistoryManager;
import com.eastsunset.bmw.R;

public class SeoulBus extends Activity implements OnClickListener {

	private View newView;
	public static HistoryManager historyManager;
	private View busView;
    
	public SeoulBus(View busView){
		this.busView = busView;
		
		historyManager = new HistoryManager((LinearLayout)busView.findViewById(R.id.contents));
		newView = (RelativeLayout) BMW.bmw.getLayoutInflater().inflate(R.layout.busstart, null);
		historyManager.add(newView);
        
		setListener();
    	setupButtons();
	}


	public static OnTouchListener otl;
	public static GestureDetector gd;
	
	private void setListener() {
        gd = new GestureDetector(new GestureDetector.SimpleOnGestureListener(){
            
            @Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            	if ( e1.getX() - e2.getX() > 50) {
            	    historyManager.nextView();
            	    return true;
            	} else if ( e1.getX() - e2.getX() < -50) {
            	    historyManager.previousView();
            	    return true;
            	}    
            	return false;
	        }
       });
    		   
        otl = new OnTouchListener(){
            public boolean onTouch(View v, MotionEvent ev) { 
                gd.onTouchEvent(ev);
            	v.onTouchEvent(ev);
                return true;
            }
        };
        busView.setOnTouchListener(otl);
	}


	public View getBusView(){
		return busView;
	}
	
	public void setupButtons() {
		((Button)busView.findViewById(R.id.busStopSearchButton)).setOnClickListener(this);
		((Button)busView.findViewById(R.id.busLineSearchButton)).setOnClickListener(this);
		((Button)busView.findViewById(R.id.busMyBusStopButton)).setOnClickListener(this);
		((Button)busView.findViewById(R.id.busRouteSearchButton)).setOnClickListener(this);
	}
	
	public void onClick(View v) 
	{
		if (v == busView.findViewById(R.id.busStopSearchButton))
		{
		    SeoulBusStopSearch seoulBusStopSearch = new SeoulBusStopSearch();
			newView = seoulBusStopSearch.getView();
			((TextView)newView.findViewById(R.id.main_title)).setText("버스정류장 검색");
		}
		else if (v == busView.findViewById(R.id.busMyBusStopButton))
		{
			newView = (RelativeLayout) BMW.bmw.getLayoutInflater().inflate(R.layout.busstart, null);
			
		}
		else if (v == busView.findViewById(R.id.busLineSearchButton))
		{
		    SeoulBusLineSearch seoulBusLine = new SeoulBusLineSearch();
            newView = seoulBusLine.getView();
            ((TextView)newView.findViewById(R.id.main_title)).setText("버스 노선 검색");
		}
		else if (v == busView.findViewById(R.id.busRouteSearchButton))
		{
		    SeoulBusTrackSearch seoulBusTrackSearch = new SeoulBusTrackSearch();
	        newView = seoulBusTrackSearch.getView();
            ((TextView)newView.findViewById(R.id.main_title)).setText("버스 최단노선 검색");
	    }
		
		historyManager.add(newView);	
		historyManager.nextView();
	}
	
	}
