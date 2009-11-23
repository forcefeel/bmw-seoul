package com.eastsunset.bmw.bus;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.graphics.Color;
import android.util.Log;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eastsunset.bmw.BMW;
import com.eastsunset.bmw.FileDownloader;
import com.eastsunset.bmw.R;

public class SeoulBusDataRetriever
{
    private static final int SEARCH_BUSSTOP_DESTINATION = 0;
    private static final int SEARCH_BUSSTOP_LINE = 1;
    private static final int SEARCH_BUSSTOP_ARRIVAL = 2;
    private static final int SEARCH_BUSLINE_NAME = 3;
    private static final int SEARCH_BUSLINE = 4;
    private static final int SEARCH_BUSROUTE = 5;
    private static final int SEARCH_BUSLINE_LINE_INFO = 6;
    private static final int SEARCH_TRACK_REALTIME_LINE_INFO = 7;
    private static final int SEARCH_TRACK_REALTIME_BUS_INFO = 8;
    
    
    
    private FileDownloader fdn = new FileDownloader();
    public static String siteUrl = "http://mobile.bus.go.kr/pda/bus_information/";
    
    public void generateNextViewFrom(String url, String mainTitle){
        String textData = fdn.downloadText(url);
        if(textData == null) return;
        
        RelativeLayout newView = null;
        switch (analizeUrl(url)){
            case SEARCH_BUSSTOP_DESTINATION :  {
                newView = (RelativeLayout) BMW.bmw.getLayoutInflater().inflate(R.layout.buslist, null);
                ((TextView)newView.findViewById(R.id.main_title)).setText(mainTitle);
                ListAdapter ula = new ListAdapter(parseBusStopDestinationData(textData));
                ListView lv = (ListView)newView.findViewById(R.id.list);
                lv.setOnItemClickListener(ula);
                lv.setOnTouchListener(SeoulBus.otl);
                lv.setAdapter(ula);
                break;
            }
                
            case SEARCH_BUSSTOP_LINE : {
                newView = (RelativeLayout) BMW.bmw.getLayoutInflater().inflate(R.layout.buslist, null);
                ((TextView)newView.findViewById(R.id.main_title)).setText(mainTitle);
                ListAdapter ula = new ListAdapter(parseBusStopLineData(textData));
                ListView lv = (ListView)newView.findViewById(R.id.list);
                lv.setOnItemClickListener(ula);
                lv.setOnTouchListener(SeoulBus.otl);
                lv.setAdapter(ula);
                break;
            }
            case SEARCH_BUSSTOP_ARRIVAL : {
                SeoulBusStopArrival sbsa = new SeoulBusStopArrival(url, parseBusStopArrivalData(textData));
                newView = sbsa.getView();
                ((TextView)newView.findViewById(R.id.main_title)).setText(mainTitle + "번 버스 도착 정보");
                break;
            }
            case SEARCH_BUSLINE : {
                newView = (RelativeLayout) BMW.bmw.getLayoutInflater().inflate(R.layout.buslist, null);
                ((TextView)newView.findViewById(R.id.main_title)).setText(mainTitle + "번 버스 노선 정보");
                ListAdapter ula = new ListAdapter(parseBusLineData(textData));
                ListView lv = (ListView)newView.findViewById(R.id.list);
                lv.setOnTouchListener(SeoulBus.otl);
                lv.setDividerHeight(0);
                lv.setAdapter(ula);
                break;
            }
            case SEARCH_BUSROUTE : {
                newView = (RelativeLayout) BMW.bmw.getLayoutInflater().inflate(R.layout.buslist, null);
                parseBusRouteData(textData);
                break;
            }
            case SEARCH_TRACK_REALTIME_LINE_INFO: {
                SeoulBusTrackResult seoulTrackResult = new SeoulBusTrackResult(parseTrackResultData(textData, url));
                newView = seoulTrackResult.getView();
                break;
            }
        }

        SeoulBus.historyManager.add(newView);    
        SeoulBus.historyManager.nextView();
        
    }
    public ArrayList<?> updateData(String url){
        String textData = fdn.downloadText(url);
        if(textData == null) return null;
        
        switch (analizeUrl(url)){
            case SEARCH_BUSSTOP_DESTINATION : 
                return parseBusStopDestinationData(textData);
            case SEARCH_BUSSTOP_LINE :
                return parseBusStopLineData(textData);
            case SEARCH_BUSLINE_NAME :
                return parseBusLineName(textData);
            case SEARCH_BUSLINE : 
                return parseBusLineData(textData);
            case SEARCH_BUSSTOP_ARRIVAL :
                return parseBusStopArrivalData(textData);
            case SEARCH_TRACK_REALTIME_LINE_INFO:
                return parseTrackRealTimeLineData(textData);
            case SEARCH_TRACK_REALTIME_BUS_INFO:
                return parseTrackRealTimeBusData(textData);
                
        }
        return null;
    }

    public SeoulBusTrack getTrackResultData(String url) {
        String textData = fdn.downloadText(url);
        return parseTrackResultData(textData, url);
    }
    
    
    private ArrayList<?> parseTrackRealTimeBusData(String textData) {
        ArrayList<String> busData = new ArrayList<String>();
        
        Pattern busLinePattern = Pattern.compile("<td class=\"content\">(.+)</td>");
        Matcher matches = busLinePattern.matcher(textData);
        
        while (matches.find()) {
            busData.add(matches.group(1).toString());
        }

        return busData;
    }
    private SeoulBusTrack parseTrackResultData(String textData, String url) {
        SeoulBusTrack seoulBusTrack = new SeoulBusTrack();
        
        Pattern busLinePattern = Pattern.compile("<tr>\\x0D\\x0A\\s+<td>(.+)</td>\\x0D\\x0A\\s+</tr>");
        Matcher matches = busLinePattern.matcher(textData);
        while (matches.find()) {
            seoulBusTrack.add(matches.group(1).toString());
        }

        seoulBusTrack.setUrl(url);
        return seoulBusTrack;
    }
    
    private ArrayList<?> parseTrackRealTimeLineData(String textData) {
        SeoulBusTrack seoulBusTrack = new SeoulBusTrack();
        
        Pattern busLinePattern = Pattern.compile("<tr>\\x0D\\x0A\\s+<td>(.+)</td>\\x0D\\x0A\\s+</tr>");
        Matcher matches = busLinePattern.matcher(textData);
        while (matches.find()) {
            seoulBusTrack.add(matches.group(1).toString());
        }
        
        return seoulBusTrack.getBusLine();
    }
    
    public ArrayList<String> parseBusRouteData(String textData) {

        ArrayList<String> list = new ArrayList<String>();
        Pattern routePattern1 = Pattern.compile("<td width='55' bgcolor='#FFFFFF'><strong>\\x0A([0-9]+)분</strong></td>\\x0A\\s+<td colspan='2' bgcolor='#EEEEEE'>\\x0A<a href='.+'>\\x0A\\s+<img src=/image/view.gif width='49' height='18' border='0'></a></td>\\x0A\\s+</tr>\\x0A\\s+<tr align='center'> \\x0A\\s+<td height='20' colspan='2' bgcolor='#EEEEEE'>교통편</td>\\x0A\\s+<td width='55' bgcolor='#EEEEEE'>탑승</td>\\x0A\\s+<td width='55' bgcolor='#EEEEEE'>하차</td>\\x0A\\s+</tr>\\x0A\\s+<tr align='center'> \\x0A\\s+<td colspan='2' bgcolor='#FFFFFF'><strong>\\x0A(.+)번</strong></td>\\x0A\\s+<td bgcolor='#FFFFFF'><strong>\\x0A(.+)</strong></td>\\x0A\\s+<td bgcolor='#FFFFFF'><strong>\\x0A(.+)</strong></td>");
        Pattern routePattern2 = Pattern.compile("<td width='55' bgcolor='#FFFFFF'><strong>\\x0A([0-9]+)분</strong></td>\\x0A\\s+<td colspan='2' bgcolor='#EEEEEE'>\\x0A<a href='.+'>\\x0A\\s+<img src=/image/view.gif width='49' height='18' border='0'></a></td>\\x0A\\s+</tr>\\x0A\\s+<tr align='center'> \\x0A\\s+<td height='20' colspan='2' bgcolor='#EEEEEE'>교통편</td>\\x0A\\s+<td width='55' bgcolor='#EEEEEE'>탑승</td>\\x0A\\s+<td width='55' bgcolor='#EEEEEE'>하차</td>\\x0A\\s+</tr>\\x0A\\s+<tr align='center'> \\x0A\\s+<td colspan='2' bgcolor='#FFFFFF'><strong>\\x0A(.+)번</strong></td>\\x0A\\s+<td bgcolor='#FFFFFF'><strong>\\x0A(.+)</strong></td>\\x0A\\s+<td bgcolor='#FFFFFF'><strong>\\x0A(.+)</strong></td>\\x0A\\s+</tr>\\x0A\\s+<tr align='center'> \\x0A\\s+<td colspan='2' bgcolor='#FFFFFF'><strong>\\x0A(.+)</strong></td>\\x0A\\s+<td bgcolor='#FFFFFF'><strong>\\x0A(.+)</strong></td>\\x0A\\s+<td bgcolor='#FFFFFF'><strong>\\x0A(.+)</strong></td>");
        Matcher matches = routePattern2.matcher(textData);
        
        boolean hard = false; 
        while (matches.find()) {
            String a = "";
            for(int i = 1; i <= matches.groupCount(); i ++){
                a += matches.group(i);
            }
            hard = true;
        }
        
        if (!hard){
        matches = routePattern1.matcher(textData);
        while (matches.find()) {
            String a = "";
            for(int i = 1; i < matches.groupCount(); i ++){
                a += matches.group(i);
            }
        }
        }    
        return list;
    }
    
    
    public ArrayList<String> parseBusLineName(String textData) {

        ArrayList<String> list = new ArrayList<String>();
        Pattern busLinePattern = Pattern.compile("<td class=\"two-cell\"><a href=\"(.+)\">(.+)</a></td>");
        Matcher matches = busLinePattern.matcher(textData);
        while (matches.find()) {
            list.add(matches.group(2).toString());
            list.add(matches.group(1).toString());
        }
        return list;
    }
    
    public ArrayList<ListItemBusLine> parseBusLineData(String textData) {
        ArrayList<ListItemBusLine> list = new ArrayList<ListItemBusLine>();
        Pattern parseBusLinePattern = Pattern.compile("<th class=\"one-head\">(.+)</th>\\x0D\\x0A\\s+<td class=\"one-cell\">(.+)</td>");
        Matcher matches = parseBusLinePattern.matcher(textData);
        
        ListItemBusLine lia = new ListItemBusLine(ListItemBusLine.TABLE_TITLE1);
        lia.addItemTitle("노선 정보");
        list.add(lia);
        lia = new ListItemBusLine(ListItemBusLine.BUS_INFO);
        
        while (matches.find()) {
            lia.addItemTitle(matches.group(1).trim());
            lia.addItemInfo(matches.group(2).trim());
        }
        
        list.add(lia);
        
        lia = new ListItemBusLine(ListItemBusLine.TABLE_TITLE2);
        lia.addItemTitle("정류장 이름");
        lia.addItemInfo("정류장 번호");
        list.add(lia);
        parseBusLinePattern = Pattern.compile("<td class=\"two-cell\">(.+)</td>\\x0D\\x0A\\s+<td class=\"two-cell\">(.+)</td>");
        matches = parseBusLinePattern.matcher(textData);

        
        while (matches.find()) {
            lia = new ListItemBusLine(ListItemBusLine.BUS_LINE);
            lia.addItemTitle(matches.group(1));
            lia.addItemInfo(matches.group(2));
            list.add(lia);
        }
        
        lia = new ListItemBusLine(ListItemBusLine.TABLE_LINE);
        lia.setHeight(2);
        lia.setBackgroundColor(Color.parseColor(BMW.BACKGROUND_DRAWABLE_BUS));
        list.add(lia);
        return list;
    }
    
    public ArrayList<BusStopArrivalItem> parseBusStopArrivalData(String textData)
    {
        ArrayList<BusStopArrivalItem> list = new ArrayList<BusStopArrivalItem>();
        Pattern busStationLinePattern = Pattern.compile("<td class=\"one-cell\"> (.+) 번째 버스 \\((.+)\\)<br />약 ([0-9]+)분 후 도착예정<br />(.+)</td>");
        Matcher matches = busStationLinePattern.matcher(textData);
        while (matches.find()) {
            BusStopArrivalItem lia = new BusStopArrivalItem(matches.group(1), matches.group(2), matches.group(3), matches.group(4));
            list.add(lia);
        }
        return list;
    }
    
    private ArrayList<?> parseBusStopLineData(String textData) {

        ArrayList<ListItemBasic> list = new ArrayList<ListItemBasic>();
        Pattern busStationLinePattern = Pattern.compile("<td class=\"three-cell\"><a href=\"(.+)\">(.+)</a></td>");
                
        Matcher matches = busStationLinePattern.matcher(textData);
        
        while (matches.find()) {
            list.add(new ListItemBasic(matches.group(2), siteUrl + matches.group(1)));
        }
        return list;
    }

    private ArrayList<?> parseBusStopDestinationData(String textData){
        
        
        ArrayList<ListItemBasic> list = new ArrayList<ListItemBasic>();
        
        Pattern busStationPattern = Pattern.compile("<td class=\"one-cell\" style=\"text-align:left;\"><a href=\"(.+)\">(.+)</a></td>");
        Matcher matches = busStationPattern.matcher(textData);

        ArrayList<String> titleListSorted = new ArrayList<String>();
        ArrayList<String> urlList = new ArrayList<String>();
        
        while (matches.find()) 
        {
            titleListSorted.add(matches.group(2));
            urlList.add(matches.group(1));
            
        }
        
        for(int i = 0; i < titleListSorted.size(); i++)
        {
            ListItemBasic lbi = new ListItemBasic(titleListSorted.get(i), siteUrl + urlList.get(i));
            list.add(lbi);
        }
        
        return list;
    }

    private int analizeUrl(String url){
        
        if(url.contains("real_station_result.jsp")) return SEARCH_BUSSTOP_DESTINATION;
        else if(url.contains("real_via_bus.jsp")) return SEARCH_BUSSTOP_LINE;
        else if(url.contains("bus_arrive_information.jsp")) return SEARCH_BUSSTOP_ARRIVAL;
        else if(url.contains("arrive_information.jsp")) return SEARCH_TRACK_REALTIME_BUS_INFO;
        else if(url.contains("bus_number_result.jsp")) return SEARCH_BUSLINE_NAME;
        else if(url.contains("bus_detail_information.jsp")) return SEARCH_BUSLINE;
        else if(url.contains("MTISServer2.dll")) return SEARCH_BUSROUTE;
        else if(url.contains("busstation.jsp")) return SEARCH_BUSLINE_LINE_INFO;
        else if(url.contains("bus_stations.jsp")) return SEARCH_TRACK_REALTIME_LINE_INFO;
        
        Log.e("myra", "URL Analizing fail!");
        return -1;
    }

}
