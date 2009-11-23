package com.eastsunset.bmw;

import com.eastsunset.bmw.bus.SeoulBusDataRetriever;
import com.eastsunset.bmw.bus.SeoulBusTrack;


public class TrackData
{
    public static final TrackData only = new TrackData();
    public static final int NO_BUS_DEPARTED = -100;
    private SeoulBusDataRetriever seoulBusRetriver = new SeoulBusDataRetriever();
    private SeoulBusTrack seoulBusTrack;
    private String url;
    private String myBusUrl;
    private long lastUpdate;
    
    public static TrackData getInstance(){
        return only;
    }
    
    public void setTrack(SeoulBusTrack seoulBusTrack) {
        this.seoulBusTrack = seoulBusTrack;
        if(seoulBusTrack.getUrl() == null) {
            this.seoulBusTrack.setUrl(url);
        } else {
            url = seoulBusTrack.getUrl();
        }
        lastUpdate = System.currentTimeMillis();
    }
    
    public void setUrl(String url){
        this.url = url;
    }
    
    public String getMyBusUrl() {
        return myBusUrl;
    }
    
    public SeoulBusTrack getTrack() {
        if(url == null) return null;
        if( (System.currentTimeMillis() - lastUpdate ) > 9000) {
            setTrack(seoulBusRetriver.getTrackResultData(url));
        }
        return seoulBusTrack;
    }

    public void setMyBus(String myBusUrl) {
        this.myBusUrl = myBusUrl;
    }
    
    
    
}
