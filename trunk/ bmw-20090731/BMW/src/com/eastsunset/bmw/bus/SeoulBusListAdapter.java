package com.eastsunset.bmw.bus;

import java.util.ArrayList;

import com.eastsunset.bmw.R;
import com.eastsunset.bmw.R.drawable;
import com.eastsunset.bmw.R.id;
import com.eastsunset.bmw.R.layout;

import android.app.Activity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SeoulBusListAdapter extends BaseAdapter implements OnCheckedChangeListener, OnTouchListener {
	enum Mode {  busStopList;	}	
	private Mode mode;
	
	private ArrayList<String> titleList;
	private View[] viewCache;
	private Activity bmw;

	private GestureDetector gd;

    
	
	public SeoulBusListAdapter(Activity bmw, GestureDetector gd, ArrayList<String> busStopList){
		// 이 경우 버스리스트를 이야기함
		mode = Mode.busStopList;
		titleList = busStopList;
		this.gd = gd;
		this.bmw = bmw;
		this.viewCache = new View[busStopList.size() / 3];
		
	}
	

	@Override
	public int getCount() {
		if(mode == Mode.busStopList) return titleList.size() / 3;
		return 2;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	// convertView 찾아보자
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    
	    if(viewCache[position] != null) {
	        Log.v("myra", "viewCache");
	        return viewCache[position];
	    }

		if(mode == Mode.busStopList) return getBusStopListView(position);
		
		return null;
	}


	private View getBusStopListView(int position) {
	    Log.v("myra", "getBusStopListView");
		RelativeLayout rl = (RelativeLayout) bmw.getLayoutInflater().inflate(R.layout.buslistrowbusstop, null);
		rl.setFocusable(true);
		TextView tv = (TextView)rl.findViewById(R.id.title);
		tv.setText(titleList.get(position * 3 + 1) + "[" + titleList.get(position * 3 + 2) + "]");
		tv.setOnTouchListener(this);
		//tv.setOnClickListener(this);
		CheckBox cb = (CheckBox)rl.findViewById(R.id.favorite);
		
		// TODO: db 연결
		//check titleList.get(position);
		if (false) cb.setButtonDrawable(android.R.drawable.btn_star_big_off);
			  else cb.setButtonDrawable(android.R.drawable.btn_star_big_on);
		
		cb.setOnCheckedChangeListener(this);
		cb.setTag(position);

		viewCache[position] = rl;
		return rl;
	}


	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		CheckBox cb = (CheckBox)buttonView;
		if(cb.isChecked()){
			cb.setButtonDrawable(android.R.drawable.btn_star_big_off);
			// db 삭제
		} else {
			cb.setButtonDrawable(android.R.drawable.btn_star_big_on);
			// db 추가
		}
	}

	
	float yCoord;
	View selectedView;

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			RelativeLayout rl = (RelativeLayout)v.getParent();
			rl.setBackgroundResource(R.drawable.alllist_listview_selected_item_bg_gradient);
			
			selectedView = v;
			yCoord = event.getY();
			
			return true;
		} else if(event.getAction() == MotionEvent.ACTION_MOVE){
			
			
			if(selectedView != null && Math.abs(yCoord - event.getY()) > v.getMeasuredHeight() / 2 ){

				RelativeLayout rl = (RelativeLayout)v.getParent();
				rl.setBackgroundDrawable(null);
				selectedView = null;
			}
			
		}
		gd.onTouchEvent(event);
			
		return true;
	}

}
