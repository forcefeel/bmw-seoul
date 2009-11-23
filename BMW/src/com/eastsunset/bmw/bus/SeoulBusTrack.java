package com.eastsunset.bmw.bus;

import java.util.ArrayList;
import java.util.Calendar;

import com.eastsunset.bmw.TrackData;

public class SeoulBusTrack {
    
    SeoulBusDataRetriever seoulBusDataRetriever;
    
    ArrayList<String> stationNameList = new ArrayList<String>();
    ArrayList<String> stationNumberList = new ArrayList<String>();
    
    ArrayList<String> busUrlList = new ArrayList<String>();
    ArrayList<String> busStationNumberList = new ArrayList<String>();

    ArrayList<String> routeStationName = new ArrayList<String>();
    ArrayList<String> routeStationNumber = new ArrayList<String>();
    
    private String url;
    private String start;
    private String end;
    
    private final String targetSiteUrl = "http://210.96.13.82/bms/web/realtime_bus/";
    
    public SeoulBusTrack() {
        seoulBusDataRetriever = new SeoulBusDataRetriever();
    }
    
    public int getRouteLenght() {
        return routeStationName.size();
    }
    
    public String getRouteStationName(int position) {
        return routeStationName.get(position);
    }
    
    public String getRouteStationNumber(int position) {
        return routeStationNumber.get(position);
    }
    
    public void add(String lineData) {
        // 버스
       if(lineData.charAt(0) == '<') {
           int startIndex = lineData.indexOf("3('") + 3;
           int endIndex = lineData.indexOf("','");
           busUrlList.add(lineData.substring(startIndex, endIndex));
           busStationNumberList.add(stationNumberList.get(stationNumberList.size() - 1));
       } else {
       // 정류장
           int startIndex = lineData.indexOf(" ");
           stationNameList.add(lineData);
           stationNumberList.add(lineData.substring(startIndex + 2, lineData.length() - 1));
       }
    }
    

    public String getStartStation() {
        return start;
    }
    
    public String getStartStationName() {
        return routeStationName.get(0);
    }
    
    public String getEndStation() {
        return end;
    }
    
    public String getEndStationName() {
        return routeStationName.get(routeStationName.size() - 1);
    }
    
    public void setUrl(String url) {
        this.url = url;
        start = url.substring(url.length() - 10, url.length() - 5);
        end = url.substring(url.length() - 5, url.length());
        
        initRoute();
    }
    
    
    public String getUrl(){
        return url;
    }

    public String getMyBus() {
        // 버스가 리스트에 하나도 없을때
        if(busUrlList.size() == 0 ) return null;
        int location;
        for(location = 0; location < busStationNumberList.size(); location++) {
            //  버스 위치가 start 지점을 지나가면 break
            if (stationNumberList.indexOf(busStationNumberList.get(location)) > stationNumberList.indexOf(start)){
                // 버스가 종점을 출발하지 않았을때    
                if(location - 1 < 0) return null;
                
                return busUrlList.get(location - 1);
            }             
        }
        return busUrlList.get(location - 1);
    }
    
    
    public int getMyBusLocation() {
    	
        if (TrackData.getInstance().getMyBusUrl() == null) return -100;
        
        try {
        	return getStationIndex(start) - stationNumberList.indexOf(busStationNumberList.get(busUrlList.indexOf(TrackData.getInstance().getMyBusUrl()))) ;
        } catch(IndexOutOfBoundsException e) {
        	return -100;
        }
        
    }
    
    private int getStationIndex(String stationNumber) {
        return stationNumberList.indexOf(stationNumber); 
    }
    
    public String getNextStation() {
        return stationNameList.get(stationNumberList.indexOf(busStationNumberList.get(busUrlList.indexOf(TrackData.getInstance().getMyBusUrl()))) + 1);
    }
    
    public String getPreviousStation() {
        return stationNameList.get(stationNumberList.indexOf(busStationNumberList.get(busUrlList.indexOf(TrackData.getInstance().getMyBusUrl()))));
    }
    
    private int fromMin, toMin, currentMin;
    
    public void updateBusPositionData() {
        
        ArrayList<String> busData = (ArrayList<String>) seoulBusDataRetriever.updateData(targetSiteUrl + TrackData.getInstance().getMyBusUrl());
        
        Calendar calendar = Calendar.getInstance( );  // 현재 날짜/시간 등의 각종 정보 얻기
        currentMin = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE);
        
        // BMS에 데이터가 아직 안들어 갔을때 기본 처리
        if(busData.size() != 2) {
            fromMin = currentMin - 1;
            toMin = currentMin + 1;
            return;
        } else {
        
            fromMin = Integer.parseInt(busData.get(0).substring(0, 2)) * 60 + Integer.parseInt(busData.get(0).substring(4, 6));
            toMin = Integer.parseInt(busData.get(1).substring(0, 2)) * 60 + Integer.parseInt(busData.get(1).substring(4, 6));
        }
    }
    
    public float getBusPositionBetweenStation() {
        updateBusPositionData();
        return 1 - (((float)toMin - (float)currentMin) / ((float)toMin - (float)fromMin)); 
    }
    
    public int getBusPositionMinLeft(){
        return toMin - currentMin;
    }
    
    private void initRoute()
    {
        int startArrayIndex = stationNumberList.indexOf(start);
        int endArrayIndex = stationNumberList.indexOf(end);

        
        if (startArrayIndex < endArrayIndex) {
            // 한방향으로만
            for(; startArrayIndex <= endArrayIndex; startArrayIndex++) {
                routeStationName.add(stationNameList.get(startArrayIndex));
                routeStationNumber.add(stationNumberList.get(startArrayIndex));
            }
        } else {
            // 한바퀴 돌음
            for(; startArrayIndex < stationNumberList.size(); startArrayIndex++) {
                routeStationName.add(stationNameList.get(startArrayIndex));
                routeStationNumber.add(stationNumberList.get(startArrayIndex));
            }
            for(int i = 0; i <= endArrayIndex; i++) {
                routeStationName.add(stationNameList.get(i));
                routeStationNumber.add(stationNumberList.get(i));
            }
        }
        
    }


    public ArrayList<?> getBusLine()
    {
        return stationNameList;
    }
    
    public ArrayList<String> getRouteStationNumber(){
        return routeStationNumber;
    }


    public String[] getCurrentPositionBetweenStations() {
        String[] positionData = new String[3];
        
        if(busUrlList.indexOf(TrackData.getInstance().getMyBusUrl()) == -1) {
        	// 리스트가 버스가 없으면 null을 리턴
            return null;
        }
        positionData[0] = busStationNumberList.get(busUrlList.indexOf(TrackData.getInstance().getMyBusUrl()));
        if(stationNumberList.indexOf(positionData[0]) + 1 < stationNumberList.size()) {
            positionData[1] = stationNumberList.get(stationNumberList.indexOf(positionData[0]) + 1);
        } else {
            positionData[1] = stationNumberList.get(0);
        }
        positionData[2] = String.valueOf(getBusPositionBetweenStation());
        
        
        return positionData;
    }

}
