package com.eastsunset.bmw.bus;

import java.util.ArrayList;

import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.eastsunset.bmw.BMW;
import com.eastsunset.bmw.DBAdapter;
import com.eastsunset.bmw.R;
import com.eastsunset.bmw.chosungkeyboard.ChosungDBManager;
import com.eastsunset.bmw.chosungkeyboard.ChosungKeyboard;

public class SeoulBusRouteSearch implements OnClickListener {
    
    private RelativeLayout mainView;
    private ChosungDBManager chosungDBManager;
    private ChosungKeyboard ck;
    private EditText editText1;
    private EditText editText2;
    private String[] startPoint = null;
   
    
    public SeoulBusRouteSearch(){
        chosungDBManager= new ChosungDBManager(DBAdapter.getInstance().getDB());
        mainView = (RelativeLayout) BMW.bmw.getLayoutInflater().inflate(R.layout.busroutesearch, null);
        mainView.setOnTouchListener(SeoulBus.otl);
        
        editText1 = (EditText)mainView.findViewById(R.id.editText1);
        editText2 = (EditText) mainView.findViewById(R.id.editText2);
        
        ck = new ChosungKeyboard(BMW.bmw, editText1, this);
        ((RelativeLayout)mainView.findViewById(R.id.busStopSearch)).addView(ck.getKeyboard(ChosungKeyboard.KeyboardType.han));
    }

    public RelativeLayout getView() {
        return mainView;
    }

    public void onClick(View v){
        
        if(v instanceof Button){
            
            if (((Button)v).getText().equals("삭제")) {
                clearResult();
                startPoint = null;
                editText1.setText("");
                editText2.setText("");
                editText2.setVisibility(View.INVISIBLE);
                ck.setTextBox(editText1);

            } else if (((Button)v).getText().equals("검색하기")) {
                if(startPoint == null){
                    displayChosungWords(editText1, 30);
                } else {
                    displayChosungWords(editText2, 30);
                }
            } else if (((Button)v).getText().equals("다시검색")) {
                clearResult();
                startPoint = null;
                editText1.setText("");
                editText2.setText("");
                editText2.setVisibility(View.INVISIBLE);
                ck.setTextBox(editText1);
            }else {
            }
            return;
        }
        
        if(v instanceof TextView){

            clearResult();   
            String keyword = ((TextView)v).getText().toString();
            
            if(startPoint == null){
                
                startPoint = chosungDBManager.getCoordinate(keyword);
                editText1.setText(keyword);
                editText2.setVisibility(View.VISIBLE);
                ck.setTextBox(editText2);
                ck.keyBoardHide(false);
                
            } else {
                editText2.setText(keyword);
                String[] endPoint = chosungDBManager.getCoordinate(keyword);
                SeoulBusDataRetriever dr = new SeoulBusDataRetriever();
                dr.generateNextViewFrom("http://210.96.13.87/Server/MTISServer2.dll?FindPathBmsPDA&" + 
                        startPoint[0] + "&" + startPoint[1] + "&" + endPoint[0] + "&" + endPoint[1] + "&2", "");
            }
        }
    }
    
    
    private void displayChosungWords(EditText editText, int maxNumberOfWord){
        clearResult();
        
        ArrayList<String> chosungSearchResult = chosungDBManager.getWords(editText.getText().toString(), maxNumberOfWord, 8);
        
        for(int i = 0; i < chosungSearchResult.size(); i+=2) {
            
            LinearLayout display = new LinearLayout(BMW.bmw);
            display.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                                                    LayoutParams.WRAP_CONTENT,
                                                    1));
            display.setOrientation(LinearLayout.HORIZONTAL);
            display.setGravity(Gravity.CENTER);
            display.setPadding(0, 0, 0, 5);
            
            TextView textView = new TextView(BMW.bmw);
            textView.setText(chosungSearchResult.get(i));
            textView.setTextColor(0xFF000000);
            textView.setWidth(160);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setTextSize(15);
            textView.setOnClickListener(this);            
            display.addView(textView);
            
            textView = new TextView(BMW.bmw);
            if(chosungSearchResult.size() > i + 1){
                textView = new TextView(BMW.bmw);
                textView.setText(chosungSearchResult.get(i+1));
                textView.setTextColor(0xFF000000);
                textView.setWidth(160);
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                textView.setTextSize(15);
                textView.setOnClickListener(this);            
            }
            display.addView(textView);
            ((LinearLayout)mainView.findViewById(R.id.listdata)).addView(display);
        }
    }

    private void clearResult(){
        ((LinearLayout)mainView.findViewById(R.id.listdata)).removeAllViews();
    }        
}
