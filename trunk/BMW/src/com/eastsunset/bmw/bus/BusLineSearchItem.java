package com.eastsunset.bmw.bus;

import android.app.Activity;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eastsunset.bmw.BMW;
import com.eastsunset.bmw.R;


public class BusLineSearchItem {
    
    private final String srchLine;
    private final String busLineNumber;
    private String busType;
    private RelativeLayout rl;
    
    public BusLineSearchItem(String srchLine, String busLineNumber) {
        this.srchLine = srchLine;
        this.busLineNumber = busLineNumber;
    }

    public View getView(OnTouchListener onTouchListener){
        rl = (RelativeLayout)((Activity) BMW.bmw).getLayoutInflater().inflate(R.layout.listbasicitem, null);
        rl.setOnTouchListener(onTouchListener);
        rl.setTag(srchLine);
        setTitle();
        return rl;
    }
    
    public void setTitle(){
        ((TextView)rl.findViewById(R.id.title)).setText(busLineNumber + "   " + busType);
    }

    
    public void setBusType(String busType){
        this.busType = busType;
    }
}