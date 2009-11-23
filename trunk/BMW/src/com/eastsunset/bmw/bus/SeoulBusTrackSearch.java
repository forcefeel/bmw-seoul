package com.eastsunset.bmw.bus;

import java.util.ArrayList;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.eastsunset.bmw.BMW;
import com.eastsunset.bmw.DBAdapter;
import com.eastsunset.bmw.R;
import com.eastsunset.bmw.chosungkeyboard.ChosungDBManager;
import com.eastsunset.bmw.chosungkeyboard.ChosungKeyboard;

public class SeoulBusTrackSearch implements OnTouchListener, OnClickListener, OnItemSelectedListener {
    
    private static final String SPINNER_HINT_1 = "도착 정류장을 선택하세요.";
    private static final String SPINNER_HINT_2 = "버스 방향을 선택하세요.";
    private static final String SPINNER_HINT_3 = "버스 번호를 선택하세요.";
    private RelativeLayout mainView;
    private View keyboardView;
    private LinearLayout listDataView;

    private ChosungDBManager chosungDBManager;
    private ChosungKeyboard chosungKeyboard;
    private SeoulBusDataRetriever seoulBusDataRetriever;
   
    ArrayList<?> dataList;
    
    private Button confirmButton; 
    private EditText textBox;
    private EditText currentEditText;
    
    private EditText inputDepartureBusStop;
    private Spinner inputDirection;
    private Spinner inputBusLine;
    private Spinner inputArrivalBusStop;
    private View selectedView;
    
    String[] urlArray;
    String[] titleArray;
    String routeUrl;
    
    public SeoulBusTrackSearch(){
        
        chosungDBManager= new ChosungDBManager(DBAdapter.getInstance().getDB());
        seoulBusDataRetriever = new SeoulBusDataRetriever();
        
        mainView = (RelativeLayout) BMW.bmw.getLayoutInflater().inflate(R.layout.bustracksearch, null);
        mainView.setOnTouchListener(SeoulBus.otl);

        textBox = (EditText)mainView.findViewById(R.id.searchEditText);
        textBox.setFocusable(false);    
        textBox.setWidth(200);
        textBox.setOnClickListener(this);
        
        confirmButton = (Button)mainView.findViewById(R.id.confirm);
        confirmButton.setOnClickListener(this);
        confirmButton.setEnabled(false);
        
        chosungKeyboard = new ChosungKeyboard(BMW.bmw, textBox, this);
        chosungKeyboard.addConfirmButton(confirmButton);
        chosungKeyboard.setCharacterLimit(5);
        
        keyboardView = chosungKeyboard.getKeyboard(ChosungKeyboard.KeyboardType.num); 
        ((RelativeLayout)mainView.findViewById(R.id.busSearchBasic)).addView(keyboardView);
        
        inputDepartureBusStop = new EditText(BMW.bmw);
        inputDepartureBusStop.setFocusable(false);
        inputDepartureBusStop.setOnClickListener(this);
        inputDepartureBusStop.setHint("출발 정류장 번호를 입력하세요."); 
        
        listDataView = ((LinearLayout)mainView.findViewById(R.id.listdata));
        
        listDataView.addView(inputDepartureBusStop);
        
        toggleKeyboard(false);
        
    }

    
    public void toggleKeyboard(boolean toggle)
    {
        if(!toggle)
        {
            keyboardView.setVisibility(View.GONE);
            ((RelativeLayout)mainView.findViewById(R.id.search_data)).setVisibility(View.GONE);
        }
        else
        {
            keyboardView.setVisibility(View.VISIBLE);
            ((RelativeLayout)mainView.findViewById(R.id.search_data)).setVisibility(View.VISIBLE);
        }
        
    }
    public RelativeLayout getView() {
        return mainView;
    }
    
    public void onClick(View v){
        if(v == inputDepartureBusStop) {
        	
            for(int i = 1; i < listDataView.getChildCount(); i++){
                listDataView.removeViewAt(i);
            }
            toggleKeyboard(true);
            currentEditText = inputDepartureBusStop;
            
        }
        
        if(v instanceof Button){
            
            if (((Button)v).getText().equals("지우기")) {
                clearResult();

            } else if (((Button)v).getText().equals("검색하기")) {
                currentEditText.setText(textBox.getText());
                textBox.setText("");
                String url = "http://mobile.bus.go.kr/pda/bus_information/real_station_result.jsp?station=" + currentEditText.getText().toString();
                if(inputDirection != null) listDataView.removeView(inputDirection);
                inputDirection = getSpinner(url);
                
            } else if (((Button)v).getText().equals("다시검색") |
                        ((Button)v).getText().equals("한글") |
                        ((Button)v).getText().equals("숫자")) {
                clearResult();
                mainView.findViewById(R.id.searchKeyword).setVisibility(View.VISIBLE);
            }else {
            }
            return;
        }
    }


    private Spinner getSpinner(String url) {
        
        if(url == null) {
            Log.e("myra", "getSpinner NO URL");
            return null;
        }
        Spinner spinner = new Spinner(BMW.bmw);
        
        dataList = (ArrayList<?>) seoulBusDataRetriever.updateData(url);
        if(dataList == null | dataList.size() == 0) return null;
        
        if(dataList.get(0) instanceof ListItemBasic){

            titleArray = new String[dataList.size() + 1];
            urlArray = new String[dataList.size() + 1];
            
            if(((ListItemBasic)dataList.get(0)).getTitle().charAt(0) <= '9') titleArray[0] = SPINNER_HINT_3;
            else titleArray[0] = SPINNER_HINT_2; 
            for(int i = 1; i < titleArray.length; i++ ){
                titleArray[i] = ((ListItemBasic)dataList.get(i-1)).getTitle();
                urlArray[i] = ((ListItemBasic)dataList.get(i-1)).getUrl(); 
            }
            
        } else {
            ((ArrayList<String>)dataList).add(0, SPINNER_HINT_1);
            titleArray = (String[])dataList.toArray(new String[0]);
        }
        
        
        ArrayAdapter adapter = new ArrayAdapter(BMW.bmw, android.R.layout.simple_spinner_item, titleArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        
        listDataView.addView(spinner);
        
        toggleKeyboard(false);
        
        return spinner;
    }
    
    private void clearResult(){
        ((LinearLayout)mainView.findViewById(R.id.listdata)).removeAllViews();
    }        
    
    
    
    public boolean onTouch(View v, MotionEvent event) {
        // 제스처라면 false;
        if(SeoulBus.gd.onTouchEvent(event)) return false; 
        
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            selectedView = v;
            return true;
        } 
        if(event.getAction() == MotionEvent.ACTION_UP){
            if (selectedView == v){
                if(v instanceof TextView){         
                    String keyword = ((TextView)v).getText().toString();
                    seoulBusDataRetriever.generateNextViewFrom("http://mobile.bus.go.kr/pda/stationlist.jsp?dong=&SrchStation=" + keyword, ((TextView)v).getText() + " 에서 출발하는  버스 방향");
                }
            } else {
                selectedView = null;
            }
            return true;
        }
    return false;    
    }


    public void onItemSelected(AdapterView<?> arg0, View v, int arg2, long arg3) {
        
        if(((TextView)v).getText().toString().equals(SPINNER_HINT_1) | 
                ((TextView)v).getText().toString().equals(SPINNER_HINT_2) | 
                ((TextView)v).getText().toString().equals(SPINNER_HINT_3)) return;
        
        if(v.getParent() == inputDirection) {

            inputBusLine = getSpinner(urlArray[arg2]);
            inputDirection.setOnItemSelectedListener(null);
        }
        else if(v.getParent() == inputBusLine) {
            int routeIdIndexStart = urlArray[arg2].indexOf("routeId=");
            int routeIdIndexEnd = urlArray[arg2].indexOf("&amp;routeName=");
            String routeId = urlArray[arg2].substring(routeIdIndexStart + 8, routeIdIndexEnd);
            String start = currentEditText.getText().toString(); 
            routeUrl = "http://210.96.13.82/bms/web/realtime_bus/bus_stations.jsp?routeId=" + routeId + "&routeType=1&flashHeight=700&routeName=" + start;

            inputArrivalBusStop = getSpinner(routeUrl);
            inputBusLine.setOnItemSelectedListener(null);
            
        } else if(v.getParent() == inputArrivalBusStop) {
            String end = titleArray[arg2].substring(titleArray[arg2].indexOf("(") + 1, titleArray[arg2].length() - 1);
            
            seoulBusDataRetriever.generateNextViewFrom(routeUrl + end, null);
            inputArrivalBusStop.setOnItemSelectedListener(null);
        }
        
    }
    
    
    public void onNothingSelected(AdapterView<?> arg0) {
        
    }
}
