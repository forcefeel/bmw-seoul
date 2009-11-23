package com.eastsunset.bmw.bus;

import java.util.ArrayList;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.eastsunset.bmw.BMW;
import com.eastsunset.bmw.R;
import com.eastsunset.bmw.chosungkeyboard.ChosungKeyboard;

public class SeoulBusLineSearch implements OnTouchListener, OnClickListener {

    private SeoulBusDataRetriever seoulBusDataRetriever;
    private ChosungKeyboard chosungKeyboard;    
    private RelativeLayout mainView;
    private LinearLayout busLineItemListView;
    private Button confirmButton; 
    private EditText textBox;    

    public SeoulBusLineSearch(){
        mainView = (RelativeLayout) BMW.bmw.getLayoutInflater().inflate(R.layout.bussearchbasic, null);
        mainView.setOnTouchListener(SeoulBus.otl);
        
        textBox = (EditText)mainView.findViewById(R.id.searchEditText);
        textBox.setFocusable(false);
        textBox.setWidth(120);
        
        confirmButton = (Button)mainView.findViewById(R.id.confirm);
        confirmButton.setOnClickListener(this);
        confirmButton.setEnabled(false);
        
        chosungKeyboard = new ChosungKeyboard(BMW.bmw, textBox, this);
        chosungKeyboard.addConfirmButton(confirmButton);
        chosungKeyboard.setCharacterLimit(4);
        ((RelativeLayout)mainView.findViewById(R.id.busSearchBasic)).addView(chosungKeyboard.getKeyboard(ChosungKeyboard.KeyboardType.num));
        busLineItemListView = (LinearLayout)mainView.findViewById(R.id.listItemBusLine);
        
        seoulBusDataRetriever = new SeoulBusDataRetriever();
    }

    public RelativeLayout getView() {
        return mainView;
    }
    
    public void addBusLineView(String keyword){
        ArrayList<?> data = seoulBusDataRetriever.updateData("http://mobile.bus.go.kr/pda/bus_information/bus_number_result.jsp?routeName=" + keyword);
        for(int i = 0; i < data.size(); i ++){
            busLineItemListView.addView(((BusLineSearchItem)data.get(i)).getView(this));
        }
    }
    

    public void onClick(View v){
        
        if(v instanceof Button){
            
            if (((Button)v).getText().equals("지우기")) 
            {
                clearResult();
            } else if (((Button)v).getText().equals("검색하기")) {
                if(displaySearchResult()) 
                {
                    chosungKeyboard.keyBoardHide(true);
                    mainView.findViewById(R.id.searchKeyword).setVisibility(View.GONE);
                }
            } else if (((Button)v).getText().equals("다시검색")) {  
                clearResult();
                mainView.findViewById(R.id.searchKeyword).setVisibility(View.VISIBLE);
            }else {
            }
            return;
        }
        
        if(v instanceof TextView){         
            String keyword = ((TextView)v).getText().toString();
            if(keyword.length() < 1) return;
            SeoulBusDataRetriever dr = new SeoulBusDataRetriever();
            dr.generateNextViewFrom("http://mobile.bus.go.kr/pda/" + keyword, ((TextView)v).getText() + " 에서 출발하는  버스 방향");
        }
    }

    private boolean displaySearchResult(){
        clearResult();
        
        ArrayList<String> searchResult = (ArrayList<String>) seoulBusDataRetriever.updateData("http://mobile.bus.go.kr/pda/bus_information/bus_number_result.jsp?routeName=" + textBox.getText().toString());
        
        if( searchResult.size() == 0) 
        {
            TextView textView = new TextView(BMW.bmw);
            textView.setText("검색된 데이터가 없습니다.");
            textView.setTextColor(0xFF000000);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 30);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setHeight(500);
            textView.setPadding(0, 70, 0, 0);
            
            ((LinearLayout)mainView.findViewById(R.id.listdata)).addView(textView);
            return true;
        }
        else if( searchResult.size() == 2) 
        {
            SeoulBusDataRetriever dr = new SeoulBusDataRetriever();
            dr.generateNextViewFrom("http://mobile.bus.go.kr/pda/bus_information/" + searchResult.get(1), searchResult.get(0));
            
            return false;
        }
        
        for(int i = 0; i < searchResult.size(); i+=4) {
            
            LinearLayout display = new LinearLayout(BMW.bmw);
            display.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                                                    LayoutParams.WRAP_CONTENT,
                                                    1));
            display.setOrientation(LinearLayout.HORIZONTAL);
            display.setGravity(Gravity.CENTER);
            display.setPadding(0, 0, 0, 5);
            
            TextView textView = new TextView(BMW.bmw);
            textView.setText(searchResult.get(i));
            textView.setTag(searchResult.get(i+1));
            textView.setTextColor(0xFF000000);
            textView.setWidth(160);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setTextSize(22);
            textView.setOnTouchListener(this);            
            display.addView(textView);
            
            textView = new TextView(BMW.bmw);
            textView.setWidth(160);
            if(searchResult.size() > i + 2){
                textView.setText(searchResult.get(i+2));
                textView.setTag(searchResult.get(i+3));
                textView.setTextColor(0xFF000000);
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                textView.setTextSize(22);
                textView.setOnTouchListener(this);            
            }
            display.addView(textView);
            ((LinearLayout)mainView.findViewById(R.id.listdata)).addView(display);
        }
        return true;
    }
    
    private void clearResult(){
        ((LinearLayout)mainView.findViewById(R.id.listdata)).removeAllViews();
    }       
    
    
    private View selectedView;
    public boolean onTouch(View v, MotionEvent event) {
    	
        // 제스처라면 false;
        if(SeoulBus.gd.onTouchEvent(event)) return false; 
        
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            selectedView = v;
            return true;
        } 
        if(event.getAction() == MotionEvent.ACTION_UP){
            if (selectedView == v){
                String keyword = v.getTag().toString();
                SeoulBusDataRetriever dr = new SeoulBusDataRetriever();
                dr.generateNextViewFrom("http://mobile.bus.go.kr/pda/bus_information/" + keyword, (String) ((TextView)v).getText());
            } else {
                selectedView = null;
            }
            return true;
        }
    return false;    
    }
}
