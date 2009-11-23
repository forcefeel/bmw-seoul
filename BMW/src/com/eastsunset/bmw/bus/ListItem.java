package com.eastsunset.bmw.bus;

import android.view.View;
import android.view.View.OnTouchListener;

abstract class ListItem
{
    public abstract View getView(OnTouchListener onTouchListener);
    public abstract void onClick();
    
}
