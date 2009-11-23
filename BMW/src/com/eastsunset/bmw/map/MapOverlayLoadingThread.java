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
	    // �ɽ� Ȯ�� ���� ���� ����	
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
		    // DB�� ������ ���� ��츦 ����ؼ�.
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
			// ���ο� �����͸�
			if(!compareStringArrayList(stationList, data)) {
				// �ɽ� Ŭ����
				pointCache = null;
				// ���ο���� ����
				stationList = data;
				Log.v("myra", "1���ο� ������ ����");
				return true;
			}  else {
				//���ο� �����Ͱ� �ƴϸ�
				Log.v("myra", "2�ɽ� �̿�");
				return false;
			}
		} else {
			
			// ó�� ����, ���ο� ������ ����
			stationList = data;
			Log.v("myra", "3ó�� ���� ������ ����");
			return true;
		}
		// ���ο� �����Ͱ� �ƴ�
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
