package com.eastsunset.bmw.bus;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.AdapterView.OnItemClickListener;


public class ListAdapter extends BaseAdapter implements OnItemClickListener {

    private List<?> dataList;
    private float yCoord;
    private View selectedView;


    public ListAdapter(ArrayList<?> weatherList ) { 
        this.dataList = weatherList;
    }

    public int getCount() {                        
        return dataList.size();
    }

    public Object getItem(int position) {     
        return dataList.get(position);
    }

    public long getItemId(int position) {  
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) { 
        return ((ListItem)dataList.get(position)).getView(null);
    }

    public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3)
    {
        ((ListItem)v.getTag()).onClick();
        
    }
}
