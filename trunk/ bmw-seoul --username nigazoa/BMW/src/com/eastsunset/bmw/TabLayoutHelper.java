package com.eastsunset.bmw;

import android.view.View;

public class TabLayoutHelper {

	private int currentTab;
	private View[] currentView = new View[4];
	
	public void showContents(int i, View v){
		i--;
		if(currentView[i] != null) 
			currentView[i].setVisibility(View.GONE);
		v.setVisibility(View.VISIBLE);
		currentView[i] = v;
	}

	public int getCurrentTab() {
		return currentTab;
	}

	public void setCurrentTab(int currentTab) {
		this.currentTab = currentTab;
	}	
}
