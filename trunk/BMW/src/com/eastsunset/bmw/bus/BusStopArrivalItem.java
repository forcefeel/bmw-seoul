package com.eastsunset.bmw.bus;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.eastsunset.bmw.BMW;

public class BusStopArrivalItem {

    private RelativeLayout rl;
    private String licenseNumber;
    private int timeLeft;
    private String busNumber;
    private String beforeNstation;
    
    TextView textView1;
    TextView textView2;
    
    
    public BusStopArrivalItem(String busNumber, String licenseNumber, String timeLeft, String beforeNstation) {
        
        this.busNumber = busNumber;
        this.licenseNumber = licenseNumber;
        this.timeLeft = Integer.parseInt(timeLeft) * 60 * 1000;
        this.beforeNstation = beforeNstation;
    }
    
    public View getView(){
        rl = new RelativeLayout(BMW.bmw);
        rl.setGravity(Gravity.CENTER_HORIZONTAL);
        
        LinearLayout ll = new LinearLayout(BMW.bmw);
        ll.setLayoutParams(new LayoutParams(245, 63));
        ll.setBackgroundDrawable( new ColorDrawable(Color.parseColor(BMW.BACKGROUND_DRAWABLE_BUS)));
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setGravity(Gravity.TOP);
        ll.setPadding(1, 0, 1, 1);
        
        textView1 = new TextView(BMW.bmw);
        textView1.setTextColor(0xFFFFFFFF);
        textView1.setGravity(Gravity.CENTER_HORIZONTAL);
        textView1.setBackgroundDrawable( new ColorDrawable(Color.parseColor(BMW.BACKGROUND_DRAWABLE_BUS)));
        textView1.setHeight(60);
        textView1.setWidth(30);
        textView1.setTextSize(22);
        ll.addView(textView1);
        

        
        textView2 = new TextView(BMW.bmw);
        textView2.setTextColor(0xFF000000);
        textView2.setBackgroundColor(Color.WHITE);
        textView2.setWidth(210);
        textView2.setHeight(58);
        textView2.setGravity(Gravity.CENTER_HORIZONTAL);
        textView2.setTextSize(20);
        ll.addView(textView2);
              
        rl.addView(ll);
        
        return rl;
    }
    
    public String getLocation(){
        return beforeNstation;
    }
    
    public void setTitle(){
        textView1.setText(convertWordToNumber(busNumber));
        textView2.setText(licenseNumber + "\n 도착까지 " + getTimeText());
    }
    
    public int getTimeLeft(){
        return timeLeft;
    }
    
    public void onTick(){
        timeLeft -= 1000;
        setTitle();
    }
    
    public String getTimeText(){
        int minute = (int) ((timeLeft/ 1000) / 60);
        int second = (int) ((timeLeft/ 1000) % 60);
        return minute + " 분 " + second + " 초";
    }
    
    public String convertWordToNumber(String numberString){
        if(numberString.equals("첫")) return "1";
        if(numberString.equals("두")) return "2";
        if(numberString.equals("세")) return "3";
        
        Log.e("myra", "BusStopArrivalItem, getNumber");
        return " ";
    }
}
