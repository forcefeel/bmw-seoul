package com.eastsunset.bmw.bus;

import java.util.ArrayList;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eastsunset.bmw.BMW;

class ListItemBusLine extends ListItem
{

    public static final int BUS_INFO = 1;
    public static final int BUS_LINE = 2;
    public static final int TABLE_TITLE1 = 3;
    public static final int TABLE_TITLE2 = 4;
    public static final int TABLE_LINE = 5;
    
    private int height = 1;
    private int mode = -1;
    private int textColor1 = Color.BLACK; // black
    private int textColor2 = Color.BLACK; // black
    private int defaultLineColor = Color.BLACK;
    private int textBackgroundColor1 = Color.WHITE;
    private int textBackgroundColor2 = Color.WHITE;
    
   
    
    private ArrayList<String> itemTitleList = new ArrayList<String>();
    private ArrayList<String> itemInfoList = new ArrayList<String>();

    public ListItemBusLine(int mode)
    {
        this.mode = mode;
    }

    @Override
    public View getView(OnTouchListener onTouchListener)
    {
        
        LinearLayout ll = new LinearLayout(BMW.bmw);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setGravity(Gravity.CENTER_HORIZONTAL);
        ll.setBackgroundColor(Color.WHITE);
        
        defaultLineColor = Color.parseColor(BMW.BACKGROUND_DRAWABLE_BUS);
        switch (mode){
            case BUS_INFO : 
            {
                for(int i = 0; i < itemTitleList.size(); i ++) {
                    if(i == 0)ll.addView(generateRow2(itemTitleList.get(i), itemInfoList.get(i), 1,0));
                    else if(i == itemTitleList.size() - 1) ll.addView(generateRow2(itemTitleList.get(i), itemInfoList.get(i), 0,1));
                    else ll.addView(generateRow2(itemTitleList.get(i), itemInfoList.get(i), 0,0));
                }
                break;
            }
            case BUS_LINE :
            {
                ll.addView(generateRow2(itemTitleList.get(0), itemInfoList.get(0), 0, 0));
                break;
            }
            case TABLE_TITLE1 :
            {
                setHeight(5);
                ll.addView(generateLine(Color.WHITE));
                
                textColor1 = Color.WHITE; // black
                textColor2 = Color.WHITE;
                textBackgroundColor1 = Color.parseColor(BMW.BACKGROUND_DRAWABLE_BUS);
                textBackgroundColor2 = Color.parseColor(BMW.BACKGROUND_DRAWABLE_BUS);
                
                ll.addView(generateRow1(itemTitleList.get(0), 0, 0));
                break;
            }
            case TABLE_TITLE2 :
            {
                textColor1 = Color.WHITE; // black
                textColor2 = Color.WHITE;
                textBackgroundColor1 = Color.parseColor(BMW.BACKGROUND_DRAWABLE_BUS);
                textBackgroundColor2 = Color.parseColor(BMW.BACKGROUND_DRAWABLE_BUS);
                
                ll.addView(generateRow2(itemTitleList.get(0), itemInfoList.get(0), 0, 0));
                break;
            }
            case TABLE_LINE :
            {
                ll.addView(generateLine(textBackgroundColor1));
                break;
            }
        }
        
        return ll;
        
    }
    public View generateLine(int color )
    {
        LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(270, height);
        LinearLayout lineLayout = new LinearLayout(BMW.bmw);
        lineLayout.setBackgroundDrawable(new ColorDrawable(color));
        lineLayout.setLayoutParams(lineParams);
        return lineLayout;
    }
    public View generateRow1(String title, int paddingTop, int paddingBottom)
    {

        LinearLayout.LayoutParams a = new LinearLayout.LayoutParams(270, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout table = new LinearLayout(BMW.bmw);
        table.setBackgroundDrawable(new ColorDrawable(defaultLineColor));
        table.setLayoutParams(a);
        table.setOrientation(LinearLayout.HORIZONTAL);
        table.setPadding(1, paddingTop, 1, paddingBottom);
        
        TextView tv = new TextView(BMW.bmw);
        tv.setText(title);
        tv.setId(1);
        tv.setBackgroundDrawable(new ColorDrawable(textBackgroundColor1));
        tv.setTextColor(textColor1);
        tv.setWidth(270);
        tv.setPadding(1, 1, 1, 1);
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        table.addView(tv);

        return table;
    }
    
    public View generateRow2(String title, String content, int paddingTop, int paddingBottom){
        if(title.length() > 12) {
            title = title.substring(0, 12);
        }
        if(content.length() > 8) {
        	content = content.substring(0, 8);
        }
        LinearLayout.LayoutParams a = new LinearLayout.LayoutParams(270, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout table = new LinearLayout(BMW.bmw);
        table.setBackgroundDrawable(new ColorDrawable(defaultLineColor));
        table.setLayoutParams(a);
        table.setOrientation(LinearLayout.HORIZONTAL);
        table.setPadding(1, paddingTop, 1, paddingBottom);
        
        TextView tv = new TextView(BMW.bmw);
        tv.setText(title);
        tv.setId(1);
        tv.setBackgroundDrawable(new ColorDrawable(textBackgroundColor1));
        tv.setTextColor(textColor1);
        tv.setWidth(170);
        tv.setPadding(1, 1, 1, 1);
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        table.addView(tv);
        
        tv = new TextView(BMW.bmw);
        tv.setText(content);
        tv.setId(1);
        tv.setBackgroundDrawable(new ColorDrawable(textBackgroundColor2));
        tv.setTextColor(textColor2);
        tv.setWidth(100);
        tv.setPadding(1, 1, 1, 1);
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        table.addView(tv);

        return table;
    }
    
     
    public void setBackgroundColor(int color )
    {
        textBackgroundColor1 = color;
    }
    
    public void setHeight(int height)
    {
        this.height = height;
    }

    @Override
    public void onClick()
    {
        // TODO Auto-generated method stub
        
    }

    public void addItemTitle(String itemTitle)
    {
        itemTitleList.add(itemTitle);
    }
    
    public void addItemInfo(String itemInfo)
    {
        itemInfoList.add(itemInfo);
    }
    
}
