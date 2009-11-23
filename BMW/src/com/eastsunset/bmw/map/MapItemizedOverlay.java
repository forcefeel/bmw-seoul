package com.eastsunset.bmw.map;

import java.util.ArrayList;
import java.util.List;

import android.widget.Toast;

import com.eastsunset.bmw.BMW;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.ItemizedOverlay.OnFocusChangeListener;

public class MapItemizedOverlay extends ItemizedOverlay implements OnFocusChangeListener {
	
	
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	OverlayItem currentPosition;
	List<Overlay> overlays;
	
	public MapItemizedOverlay() {
		super(boundCenterBottom(BMW.bmw.getResources().getDrawable(android.R.drawable.checkbox_off_background)));
        setOnFocusChangeListener(this);
	}

	public MapItemizedOverlay(List<Overlay> overlays) {
		super(boundCenterBottom(BMW.bmw.getResources().getDrawable(android.R.drawable.checkbox_off_background)));
		this.overlays = overlays; 
		setOnFocusChangeListener(this);
	}
	
	public void focusMyPosition() {
		if(currentPosition != null)	{
			setFocus(currentPosition);
			this.populate();
		}
	}

	@Override
	protected OverlayItem createItem(int i) {
	  return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}
	public void addOverlay(OverlayItem overlay) {
	    mOverlays.add(overlay);
	    populate();
	}
	
	public void removeAll(){
		while( mOverlays.size() > 0) {
			mOverlays.remove(0);
		}
	}
	
	public void addPoint(Double latitude, Double longitude, String title, String snippet){
		
		if(latitude == null) {
			Toast.makeText(BMW.bmw, "해당 좌표를 찾을 수 없습니다.", 5000).show();
			return;
		}
		
		latitude = latitude*1E6;
        longitude = longitude*1E6;
        mOverlays.add(new OverlayItem(new GeoPoint(latitude.intValue(), longitude.intValue()), title, snippet));
	    populate();
	}
	
	public void updateCurrentPoint(Double latitude, Double longitude, String title, String snippet){
		latitude = latitude*1E6;
        longitude = longitude*1E6;
        
        // 예전 포인트 삭제
        if(currentPosition != null) mOverlays.remove(mOverlays.size() - 1);
        
    	currentPosition = new OverlayItem(new GeoPoint(latitude.intValue(), longitude.intValue()), title, "snippet");
    	currentPosition.setMarker(boundCenterBottom(BMW.bmw.getResources().getDrawable(android.R.drawable.star_on)));
    	mOverlays.add(currentPosition);
	    populate();
	    setFocus(currentPosition);
	}
	
	public void checkPoint(String title){
		
		for(int i = 0; i < size(); i++) {
			if(mOverlays.get(i).getTitle().equals(title)) {
				mOverlays.get(i).setMarker(boundCenterBottom(BMW.bmw.getResources().getDrawable(android.R.drawable.checkbox_on_background)));
				populate();
				break;
			}
		}
	}

	public void onFocusChanged(ItemizedOverlay overlay, OverlayItem newFocus) {
		if(currentPosition != null)	{
			setFocus(currentPosition);		
		}
		if(newFocus == null) {
			return;
		}		
		if(newFocus.getTitle().toString().length() == 5) {
			Toast.makeText(BMW.bmw, newFocus.getSnippet() + " (" + newFocus.getTitle() + ")", 1500).show();
		}
	}
}
