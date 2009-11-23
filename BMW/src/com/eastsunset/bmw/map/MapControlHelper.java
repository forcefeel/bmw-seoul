package com.eastsunset.bmw.map;

import android.location.Location;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

import com.eastsunset.bmw.BMW;
import com.eastsunset.bmw.DBAdapter;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class MapControlHelper {

	MapItemizedOverlay itemizedOverlay;
    MapDBHelper mapDBHelper;
	MapController mapController;
	
	private MapView mapView;

	public MapControlHelper(MapView mapView) {
		this.mapController = mapView.getController();
		this.mapView = mapView;
		
		itemizedOverlay = new MapItemizedOverlay();
		mapView.getOverlays().add(itemizedOverlay);
		mapDBHelper = new MapDBHelper(DBAdapter.getInstance().db);
		
	}

	public void moveTo(Double latitude, Double longitude, int zoom) {
		if(latitude == null) {
			Toast.makeText(BMW.bmw, "검색된 좌표가 없습니다.", 5000);
			mapView.getOverlays().remove(0);
			return;
		}
		//이동하기
        latitude = latitude * 1E6;
        longitude = longitude * 1E6;
        
        GeoPoint point = new GeoPoint(latitude.intValue(), longitude.intValue());
        mapController.animateTo(point);
        if(zoom > 0 ) mapController.setZoom(zoom);
	}
	
	public void moveTo(Location location) {
		
		Double lat = location.getLatitude() * 1E6;
		Double lng = location.getLongitude() * 1E6;
		
		GeoPoint point = new GeoPoint(lat.intValue(), lng.intValue());
		mapController.animateTo(point);
		
	}
	
	public void setZoom(int z) {
		mapController.setZoom(z);
	}
	
	public void clearItemizedOverlay() {
		itemizedOverlay = new MapItemizedOverlay();
		mapView.getOverlays().remove(0);
		mapView.getOverlays().add(itemizedOverlay);
	}
	
	public void updateGpsPosition(Location location) {
		
		itemizedOverlay.updateCurrentPoint(location.getLatitude(), location.getLongitude(), "me", null);
		moveTo(location);
	}
	
	public String[] addStation(String stationNumber){
		String[] point = mapDBHelper.getCoordinate(stationNumber);
		if(point[0] == null | stationNumber.length() != 5) {
		    Log.v("myra", "cannot find!! " + stationNumber);
			return null;
		}
		itemizedOverlay.addPoint(Double.parseDouble(point[1]), Double.parseDouble(point[0]), point[3], point[2]);
		
		return point;
	}
	
	public void addStationPoint(String[] point, String stationNumber) {
		itemizedOverlay.addPoint(Double.parseDouble(point[1]), Double.parseDouble(point[0]), point[3], point[2]);
	}
	
	public void moveToCenter() {
		mapController.animateTo(itemizedOverlay.getCenter());
	}
	
	public void currentPosition(String start, String end, float position) {
		String[] startArray = mapDBHelper.getCoordinate(start);
		String[] endArray = mapDBHelper.getCoordinate(end);
		
		
		if(position >= 1) {
			position = 1f;
		} else if (position < 0) {
			position = 0f;
		} else if (position == 0) {
			position = 0.3f;
		} else if (position == 1) {
			position = 0.7f;
		}
		
		Double[] centerArray = new Double[2];
		try {
			
		centerArray[0] = Double.parseDouble(startArray[0]) + (Double.parseDouble(endArray[0]) - Double.parseDouble(startArray[0])) * position;
		centerArray[1] = Double.parseDouble(startArray[1]) + (Double.parseDouble(endArray[1]) - Double.parseDouble(startArray[1])) * position;
		
		
		
		itemizedOverlay.updateCurrentPoint(centerArray[1], centerArray[0], "Me!", null);
		moveTo(centerArray[1], centerArray[0], -1);
		} catch(NullPointerException e) {
			Log.v("myra", "NULLPOINTER EXCEPOTEIONOENOITNE ");
		}
		
	}
	
}
