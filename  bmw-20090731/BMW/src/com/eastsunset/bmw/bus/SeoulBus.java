package com.eastsunset.bmw.bus;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eastsunset.bmw.BMW;
import com.eastsunset.bmw.DBAdapter;
import com.eastsunset.bmw.HistoryManager;
import com.eastsunset.bmw.R;
import com.eastsunset.bmw.chosungkeyboard.ChosungKeyboard;

public class SeoulBus implements OnClickListener {

//    private Thread updateThread;
//    private SeoulBusList seoulBusList;
//	private TabLayoutHelper helper;
	RelativeLayout newView;
	HistoryManager historyManager;
	private View busView;
    
	public SeoulBus(View busView){
		this.busView = busView;
		
		historyManager = new HistoryManager((LinearLayout)busView.findViewById(R.id.contents));
		newView = (RelativeLayout) BMW.bmw.getLayoutInflater().inflate(R.layout.busrecent, null);
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
            	
            	if ( e1.getX() - e2.getX() > 50) historyManager.nextView();
				else if ( e1.getX() - e2.getX() < -50) {
					historyManager.previousView();
					Log.v("myra", "fling~~~");
				}
	        	return true;
	        }
       });
    		   
        otl = new OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent ev) { 

            	ListView lv = (ListView) historyManager.getCurrentView().findViewById(R.id.buslist);
            	if(lv !=null) lv.onTouchEvent(ev);
            	
            	//if (ev.getAction() == MotionEvent.ACTION_UP) onClickCheckTag(v);
            	
            	gd.onTouchEvent(ev);
                return true;
            }
        };
        busView.setOnTouchListener(otl);
	}


	public View getBusView(){
		return busView;
	}
	
	
	public void setupButtons() {
		
		((Button)busView.findViewById(R.id.busSearchButton)).setOnClickListener(this);
		((Button)busView.findViewById(R.id.busMyBusButton)).setOnClickListener(this);
		((Button)busView.findViewById(R.id.busMyBusStopButton)).setOnClickListener(this);
		((Button)busView.findViewById(R.id.busRecentButton)).setOnClickListener(this);
		//((Button)busView.findViewById(R.id.busSearchConfirmButton)).setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		
		if (v == busView.findViewById(R.id.busSearchButton)){
			newView = (RelativeLayout) BMW.bmw.getLayoutInflater().inflate(R.layout.bussearch, null);
//			Button bt = (Button) newView.find10ViewById(R.id.busSearchConfirmButton);
//			bt.setTag("busSearchConfirmButton");
//			bt.setOnTouchListener(otl);
			ChosungKeyboard ck = new ChosungKeyboard(BMW.bmw, null, this, DBAdapter.getInstance().getDB());
			newView.addView(ck.getKeyboard(ChosungKeyboard.Keyboard.han));
		}
		else if (v == busView.findViewById(R.id.busRecentButton))
			newView = (RelativeLayout) BMW.bmw.getLayoutInflater().inflate(R.layout.busrecent, null);
		else if (v == busView.findViewById(R.id.busMyBusStopButton))
			newView = (RelativeLayout) BMW.bmw.getLayoutInflater().inflate(R.layout.busmybusstop, null);
		else if (v == busView.findViewById(R.id.busMyBusButton))
			newView = (RelativeLayout) BMW.bmw.getLayoutInflater().inflate(R.layout.busmybus, null);
		else if (v.getTag().equals("busSearchConfirmButton")) {
            Log.v("myra", "onclick check tag");
            newView = (RelativeLayout) BMW.bmw.getLayoutInflater().inflate(R.layout.buslist, null);
            ListView lv = (ListView) newView.findViewById(R.id.buslist);
            SeoulBusDataRetriever.getInstance().doSearch(lv, ((TextView)historyManager.getCurrentView().findViewById(R.id.busSearchTextBox)).getText().toString());
        }
		
		historyManager.add(newView);	
		historyManager.nextView();
	}
	
//	public void onClickCheckTag(View v){
//		
//		if(v.getTag() == null) return; // 지워도 되지 안나?
//
//		// 버스 검색 결과
// else return;
//		
//		historyManager.add(newView);	
//		historyManager.nextView();
//	}
}
