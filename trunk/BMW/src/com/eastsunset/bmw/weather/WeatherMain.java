package com.eastsunset.bmw.weather;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.eastsunset.bmw.HistoryManager;
import com.eastsunset.bmw.R;


public class WeatherMain extends Activity  implements OnClickListener {

	public static HistoryManager historyManager;
	private View newView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v("myra", "oncreate");
		setContentView(R.layout.weathermain);
		
		if(historyManager == null ) {
			historyManager = new HistoryManager((LinearLayout)findViewById(R.id.contents));

			WeatherCity weatherSeoul = new WeatherCity("seoul"); 
			newView = weatherSeoul.getWeatherView();
			historyManager.add(newView);
      
		} else {
			historyManager.setNewParent((LinearLayout)findViewById(R.id.contents));
		}
		setTouchListener();
		setupButtons();
	}
	
	public static OnTouchListener otl;
	public static GestureDetector gd;
	
	private void setTouchListener() {
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
      findViewById(R.id.weather_main).setOnTouchListener(otl);
	}


	public void setupButtons() {
		
		((Button)findViewById(R.id.seoul_button)).setOnClickListener(this);
		((Button)findViewById(R.id.daejeon_button)).setOnClickListener(this);
		((Button)findViewById(R.id.taegu_button)).setOnClickListener(this);
		((Button)findViewById(R.id.pusan_button)).setOnClickListener(this);
	}
	
	public void onClick(View v) 
	{
		if (v == findViewById(R.id.seoul_button)) {
			WeatherCity weatherSeoul = new WeatherCity("seoul"); 
			newView = weatherSeoul.getWeatherView();
		} else if (v == findViewById(R.id.daejeon_button)) {
			WeatherCity weatherSeoul = new WeatherCity("daejeon"); 
			newView = weatherSeoul.getWeatherView();
		} else if (v == findViewById(R.id.taegu_button)) {
			WeatherCity weatherSeoul = new WeatherCity("taegu"); 
			newView = weatherSeoul.getWeatherView();
		} else if (v == findViewById(R.id.pusan_button)) {
			WeatherCity weatherSeoul = new WeatherCity("pusan"); 
			newView = weatherSeoul.getWeatherView();
		}
		
		historyManager.add(newView);	
		historyManager.nextView();
	}
	
}
