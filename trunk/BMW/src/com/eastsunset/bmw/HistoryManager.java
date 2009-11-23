package com.eastsunset.bmw;

import java.util.ArrayList;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

public class HistoryManager {

	private ArrayList<View> history = new ArrayList<View>();
	private int currentPosition = 0;
	private LinearLayout contents;
	
	
	public HistoryManager(LinearLayout contents) {
		this.contents = contents;
	}

	public void add(View newView) {
		while(currentPosition < history.size() - 1){
			history.remove(history.size()-1);
		}
		
		history.add(newView);
		contents.addView(newView);
	}
	
	public void setNewParent(LinearLayout ll){
		contents.removeAllViews();
		contents = ll;
		
		for(int i = 0; i < history.size(); i++){
			contents.addView(history.get(i));
		}
	}
	
	public View getCurrentView(){
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
		
		
		View view = history.get(currentPosition);
		throwLayoutForward(view);
		currentPosition++;
		view = history.get(currentPosition);	
		bringLayoutForward(view);
	}
	
	public void previousView() {
		if (currentPosition < 1){
			return;
		}
		
		View view = history.get(currentPosition);
		throwLayoutBack(view);
		currentPosition--;
		view = history.get(currentPosition);	
		bringLayoutBack(view);
	}
	
	private void bringLayoutForward(View panel) {
		  panel.setVisibility(View.VISIBLE);
		  Animation animation = new TranslateAnimation(320, 0, 0, 0);
		  animation.setDuration(500);
		  panel.setAnimation(animation);
	}
	
	private void throwLayoutForward(View panel) {
		  Animation animation = new TranslateAnimation(0, -320, 0, 0);
		  animation.setDuration(500);
		  panel.setAnimation(animation);
		  panel.setVisibility(View.GONE);
	}
	
	private void bringLayoutBack(View panel) {
		  panel.setVisibility(View.VISIBLE);
		  Animation animation = new TranslateAnimation(-320, 0, 0, 0);
		  animation.setDuration(500);
		  panel.setAnimation(animation);
	}
	
	private void throwLayoutBack(View panel) {
		  Animation animation = new TranslateAnimation(0, 320, 0, 0);
		  animation.setDuration(500);
		  panel.setAnimation(animation);
		  panel.setVisibility(View.GONE);
	}
}
