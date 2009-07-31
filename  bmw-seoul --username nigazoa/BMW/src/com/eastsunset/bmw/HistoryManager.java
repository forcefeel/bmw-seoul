package com.eastsunset.bmw;

import java.util.ArrayList;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class HistoryManager {

	private ArrayList<RelativeLayout> history = new ArrayList<RelativeLayout>();
	private int currentPosition = 0;
	private LinearLayout contents;
	
	
	public HistoryManager(LinearLayout contents) {
		this.contents = contents;
	}

	public void add(RelativeLayout newView) {
		while(currentPosition < history.size() - 1){
			history.remove(history.size()-1);
		}
		
		history.add(newView);
		contents.addView(newView);
	}
	
	public RelativeLayout getCurrentView(){
		return history.get(currentPosition);
	}

	public void nextView() {
		if (currentPosition == history.size()-1 | history.size() == 0){
			return;
		}
		
		if (history.size() == 10){
			history.remove(0);
			currentPosition --;
		}
		
		
		RelativeLayout view = history.get(currentPosition);
		throwLayoutForward(view);
		currentPosition++;
		view = history.get(currentPosition);	
		bringLayoutForward(view);
	}
	
	public void previousView() {
		if (currentPosition < 1){
			return;
		}
		
		RelativeLayout view = history.get(currentPosition);
		throwLayoutBack(view);
		currentPosition--;
		view = history.get(currentPosition);	
		bringLayoutBack(view);
	}
	
	private void bringLayoutForward(RelativeLayout panel) {
		  panel.setVisibility(View.VISIBLE);
		  Animation animation = new TranslateAnimation(320, 0, 0, 0);
		  animation.setDuration(500);
		  panel.setAnimation(animation);
	}
	
	private void throwLayoutForward(RelativeLayout panel) {
		  Animation animation = new TranslateAnimation(0, -320, 0, 0);
		  animation.setDuration(500);
		  panel.setAnimation(animation);
		  panel.setVisibility(View.GONE);
	}
	
	private void bringLayoutBack(RelativeLayout panel) {
		  panel.setVisibility(View.VISIBLE);
		  Animation animation = new TranslateAnimation(-320, 0, 0, 0);
		  animation.setDuration(500);
		  panel.setAnimation(animation);
	}
	
	private void throwLayoutBack(RelativeLayout panel) {
		  Animation animation = new TranslateAnimation(0, 320, 0, 0);
		  animation.setDuration(500);
		  panel.setAnimation(animation);
		  panel.setVisibility(View.GONE);
	}
}
