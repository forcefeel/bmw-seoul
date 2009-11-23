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
import com.eastsunset.bmw.DBAdapter;
import com.eastsunset.bmw.R;
import com.eastsunset.bmw.chosungkeyboard.ChosungDBManager;
import com.eastsunset.bmw.chosungkeyboard.ChosungKeyboard;

public class SeoulBusStopSearch implements OnTouchListener, OnClickListener {
    
    private RelativeLayout mainView;
    private ChosungDBManager chosungDBManager;
    private Button confirmButton; 
    private EditText textBox;
    ChosungKeyboard chosungKeyboard;
    
    public SeoulBusStopSearch(){
        chosungDBManager= new ChosungDBManager(DBAdapter.getInstance().getDB());
        mainView = (RelativeLayout) BMW.bmw.getLayoutInflater().inflate(R.layout.bussearchbasic, null);
        mainView.setOnTouchListener(SeoulBus.otl);

        textBox = (EditText)mainView.findViewById(R.id.searchEditText);
        textBox.setFocusable(false);
        textBox.setWidth(200);

        confirmButton = (Button)mainView.findViewById(R.id.confirm);
        confirmButton.setOnClickListener(this);
        confirmButton.setEnabled(false);
        
        chosungKeyboard = new ChosungKeyboard(BMW.bmw, textBox, this);
        chosungKeyboard.addConfirmButton(confirmButton);
        chosungKeyboard.setCharacterLimit(8);
        ((RelativeLayout)mainView.findViewById(R.id.busSearchBasic)).addView(chosungKeyboard.getKeyboard(ChosungKeyboard.KeyboardType.hannum));
    }

    public RelativeLayout getView() {
        return mainView;
    }

    public void onClick(View v){
        
        if(v instanceof Button){
            
            if (((Button)v).getText().equals("�����")) {
                clearResult();

            } else if (((Button)v).getText().equals("�˻��ϱ�")) {
                if(displaySearchResult(30)) 
                {
                    chosungKeyboard.keyBoardHide(true);
                    mainView.findViewById(R.id.searchKeyword).setVisibility(View.GONE);
                }
            } else if (((Button)v).getText().equals("�ٽð˻�") |
                        ((Button)v).getText().equals("�ѱ�") |
                        ((Button)v).getText().equals("����")) {
                clearResult();
                mainView.findViewById(R.id.searchKeyword).setVisibility(View.VISIBLE);
            }else {
            }
            return;
        }
        

    }
    
    
    private boolean displaySearchResult(int maxNumberOfWord){
        clearResult();

        String textBoxString = textBox.getText().toString();
        
        if(textBoxString.charAt(0) >= '0' & textBoxString.charAt(0) <= '9')
        {
            if(textBoxString.length() != 5)
            {
                TextView textView = new TextView(BMW.bmw);
                textView.setText("������ ��ȣ �˻��� 5�ڸ� ���� ��� �Է� �ϼž� �մϴ�.");
                textView.setTextColor(0xFF000000);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 30);
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                textView.setHeight(500);
                textView.setPadding(0, 70, 0, 0);
                
                ((LinearLayout)mainView.findViewById(R.id.listdata)).addView(textView);
                return true;
            }
            
            String keyword = textBox.getText().toString();
            SeoulBusDataRetriever dr = new SeoulBusDataRetriever();
            dr.generateNextViewFrom("http://mobile.bus.go.kr/pda/bus_information/real_station_result.jsp?station=" + keyword, keyword + " ���� ����ϴ�  ���� ����");
            return false;
        }

        ArrayList<String> searchResult = chosungDBManager.getWords(textBoxString, maxNumberOfWord, 7);
        
        if( searchResult.size() == 0) 
        {
            TextView textView = new TextView(BMW.bmw);
            textView.setText("�˻��� �����Ͱ� �����ϴ�.");
            textView.setTextColor(0xFF000000);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 30);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setHeight(500);
            textView.setPadding(0, 70, 0, 0);
            
            ((LinearLayout)mainView.findViewById(R.id.listdata)).addView(textView);
            return true;
        } else if( searchResult.size() == 1) 
        {
            String keyword = searchResult.get(0);
            SeoulBusDataRetriever dr = new SeoulBusDataRetriever();
            dr.generateNextViewFrom("http://mobile.bus.go.kr/pda/bus_information/real_station_result.jsp?station=" + keyword, keyword + " ���� ����ϴ�  ���� ����");
            
            return false;
        }
        
        for(int i = 0; i < searchResult.size(); i+=2) {
            // ���� ����
            if(i > 13) break;
            
            LinearLayout display = new LinearLayout(BMW.bmw);
            display.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                                                    LayoutParams.WRAP_CONTENT,
                                                    1));
            display.setOrientation(LinearLayout.HORIZONTAL);
            display.setGravity(Gravity.CENTER);
            display.setPadding(5, 0, 0, 5);
            
            TextView textView = new TextView(BMW.bmw);
            textView.setText(searchResult.get(i));
            textView.setTextColor(0xFF000000);
            textView.setWidth(160);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setTextSize(20);
            textView.setOnTouchListener(this);            
            display.addView(textView);
            
            textView = new TextView(BMW.bmw);
            textView.setWidth(160);
            if(searchResult.size() > i + 1){
                textView.setText(searchResult.get(i+1));
                textView.setTextColor(0xFF000000);
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                textView.setTextSize(20);
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
        // ����ó��� false;
        if(SeoulBus.gd.onTouchEvent(event)) return false; 
        
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            selectedView = v;
            return true;
        } 
        if(event.getAction() == MotionEvent.ACTION_UP){
            if (selectedView == v){
                if(v instanceof TextView){         
                    String keyword = ((TextView)v).getText().toString();
                    SeoulBusDataRetriever dr = new SeoulBusDataRetriever();
                    dr.generateNextViewFrom("http://mobile.bus.go.kr/pda/bus_information/real_station_result.jsp?station=" + keyword, keyword + " ���� ����ϴ�  ���� ����");
                }
            } else {
                selectedView = null;
            }
            return true;
        }
    return false;    
    }
}
