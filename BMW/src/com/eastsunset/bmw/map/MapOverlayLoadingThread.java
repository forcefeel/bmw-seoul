package com.eastsunset.bmw.map;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.util.Log;

public class MapOverlayLoadingThread extends Thread {
	/** Nested class that performs progress calculations (counting) */
       
	ArrayList<String> stationList;
	ArrayList<String[]> pointCache;
	
	MapControlHelper mapControlHelper;
	private ProgressDialog progressDialog;
	private String startStation;
	private String endStation;
	private float position;
	private boolean isStarted = false;
    	
	MapOverlayLoadingThread(MapControlHelper mapControlHelper) {
		this.mapControlHelper = mapControlHelper;
	}
    
	@Override
	public synchronized void run() {
		isStarted = true;
		
		while(true) {
				mapControlHelper.clearItemizedOverlay();
				addStationList(stationList);
				
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		}
	}
    	
	private void addStationList(ArrayList<String> list){
    	
		if(!isSameList(list)) retriveDataFromDataBase(list);
		else retriveDataFromCache(list);

		if(startStation != null) {
		    mapControlHelper.currentPosition(startStation, endStation, position);
		} else {
		    mapControlHelper.moveToCenter();
		}
		progressDialog.dismiss();
	}
	
	private boolean isSameList(ArrayList<String> list) {
	    // 케쉬 확인 유무 위한 절차	
	    if(pointCache == null) return false;
	    if(list.size() != pointCache.size()) return false;
	    
        return true;
    }

    private void retriveDataFromDataBase(ArrayList<String> list) {
		pointCache = new ArrayList<String[]>();
		
		for(int i = 0; i < list.size(); i++){ 
			pointCache.add(mapControlHelper.addStation(list.get(i)));
			progressDialog.setProgress(i + 1);
		}
	}
	private void retriveDataFromCache(ArrayList<String> list) {
		
		for(int i = 0; i < list.size(); i++){
		    // DB에 버스가 없을 경우를 대비해서.
		    if(pointCache.get(i) != null ) {
		        mapControlHelper.addStationPoint(pointCache.get(i), list.get(i));
		    }
		}
	}
	
	public void setCurrentPosition(String startStation, String endStation, float position) {
		this.startStation = startStation;
		this.endStation = endStation;

	}

	public boolean setStationList(ArrayList<String> data) {
			
		if(stationList != null) {
			// 새로운 데이터면
			if(!compareStringArrayList(stationList, data)) {
				// 케쉬 클리어
				pointCache = null;
				// 새로운데이터 적용
				stationList = data;
				Log.v("myra", "1새로운 데이터 적용");
				return true;
			}  else {
				//새로운 데이터가 아니면
				Log.v("myra", "2케쉬 이용");
				return false;
			}
		} else {
			
			// 처음 실행, 새로운 데이터 설정
			stationList = data;
			Log.v("myra", "3처음 실행 데이터 적용");
			return true;
		}
		// 새로운 데이터가 아님
	}
	
	private boolean compareStringArrayList(ArrayList<String> xList, ArrayList<String> yList){
		if(xList.size() != yList.size()) return false;
		
		for(int i = 0; i < xList.size(); i++) {
			if(!xList.get(i).equals(yList.get(i))) return false;
		}
		
		return true;
	}
	
	public synchronized void process() {
		if(isStarted) this.notify();
		else start();
	}

	public void setProgressDialog(ProgressDialog progressDialog) {
		this.progressDialog = progressDialog;
	}
	
}
