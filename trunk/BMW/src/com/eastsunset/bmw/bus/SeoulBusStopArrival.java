package com.eastsunset.bmw.bus;

import java.util.ArrayList;

import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

import com.eastsunset.bmw.BMW;
import com.eastsunset.bmw.R;

public class SeoulBusStopArrival implements OnTouchListener, ViewFactory {
    
    private RelativeLayout busStopArrivalView;
    private ArrayList<BusStopArrivalItem> data;
    private String url;
    private TextSwitcher mSwitcher;
    
    public SeoulBusStopArrival(String url, ArrayList<BusStopArrivalItem> data) {
        this.data = data;
        this.url = url;
        busStopArrivalView = (RelativeLayout) BMW.bmw.getLayoutInflater().inflate(R.layout.busstoparrival, null);
        busStopArrivalView.setOnTouchListener(this);
        addArrivalView();
    }
    
    public void addArrivalView(){

        LinearLayout ll = (LinearLayout) busStopArrivalView.findViewById(R.id.listitemarrival);
        ll.removeAllViews();
        
        if(data.size() > 0) {
        	
            for(int i = 0; i < data.size(); i++ ) {
                ll.addView(data.get(i).getView());
                
                RelativeLayout line = new RelativeLayout(BMW.bmw);
                line.setLayoutParams(new LayoutParams(1,2));
                ll.addView(line);
                
            }
            setTimer(data.get(0).getTimeLeft());
        } else {
            setReloadTimer(5000);
        }
    }
    
    public void setTimer(int timeLeft){
        
        new CountDownTimer(timeLeft, 1000) {
            public void onTick(long millisUntilFinished) {
                
                
                if(data.size() > 0) {
                    for(int i = 0; i < data.size(); i++ ) {
                        data.get(i).onTick();
                    }
                    mSwitcher.setText(data.get(0).getTimeText() + " 남았습니다.");
                }
            }

            public void onFinish() {
                loadData();
            }

         }.start();
    }
    
    private void loadData()
    {
        SeoulBusDataRetriever seoulBusDataRetriever = new SeoulBusDataRetriever();
        data = (ArrayList<BusStopArrivalItem>) seoulBusDataRetriever.updateData(url);
        // 정보가 없습니다.
        if(data != null) addArrivalView();
    }
    
    public void setReloadTimer(final int newTimeLeft){
        

        new CountDownTimer(newTimeLeft, 1000) {
            int timeLeft = 4;
            
            public void onFinish() 
            {
                loadData();
            }

            @Override
            public void onTick(long arg0)
            {
                mSwitcher.setText("도착 버스 정보가 없습니다. " + timeLeft-- + "초 후 다시 로딩합니다");
            }
         }.start();
    }
    
    
    public RelativeLayout getView(){

        mSwitcher = (TextSwitcher) busStopArrivalView.findViewById(R.id.switcher);
        mSwitcher.setFactory(this);

        Animation in = AnimationUtils.loadAnimation(BMW.bmw,
                android.R.anim.fade_in);
        Animation out = AnimationUtils.loadAnimation(BMW.bmw,
                android.R.anim.fade_out);
        mSwitcher.setInAnimation(in);
        mSwitcher.setOutAnimation(out);
        
        return busStopArrivalView;
        
    }

    public boolean onTouch(View v, MotionEvent event) {
        if(SeoulBus.gd.onTouchEvent(event)) return false;
        return false;
    }

    public View makeView(){
        TextView t = new TextView(BMW.bmw);
        t.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        t.setTextSize(36);
        return t;
    }

       
}
