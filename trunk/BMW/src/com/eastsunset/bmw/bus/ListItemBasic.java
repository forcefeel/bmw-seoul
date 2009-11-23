package com.eastsunset.bmw.bus;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

import com.eastsunset.bmw.BMW;

public class ListItemBasic extends ListItem {

    private String title;
    private String url;

    public ListItemBasic( String title, String url ) {
        this.title = title;
        this.url = url;
        
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }
  
    public void setTitle(String title){
        this.url = title;
    }
  
    public void setUrl(String url){
        this.url = url;
    }

    public View getView(OnTouchListener onTouchListener){
        
        TextView tv = new TextView(BMW.bmw);
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        tv.setTextColor(Color.BLACK);
        tv.setPadding(0, 10, 0, 10);
        tv.setWidth(320);
        tv.setText(getTitle());
        tv.setTag(this);
        return tv;
    }
    
    public void onClick(){
        SeoulBusDataRetriever dr = new SeoulBusDataRetriever();
        dr.generateNextViewFrom(getUrl(), title);
    }
}
