package com.eastsunset.bmw.bus;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eastsunset.bmw.BMW;
import com.eastsunset.bmw.R;
import com.eastsunset.bmw.TrackData;
import com.eastsunset.bmw.VibratorControl;

public class SeoulBusTrackResult {

    private RelativeLayout mainView;
    private SeoulBusTrack seoulBusTrack;
    private String myBusUrl;
    private CountDownTimer countDownTimer;
    
    
    public SeoulBusTrackResult(SeoulBusTrack seoulBusTrack) {
        
        this.myBusUrl = seoulBusTrack.getMyBus();
        this.seoulBusTrack = seoulBusTrack;
        TrackData.getInstance().setMyBus(myBusUrl);
        TrackData.getInstance().setTrack(seoulBusTrack);
        
        mainView = (RelativeLayout) BMW.bmw.getLayoutInflater().inflate(R.layout.bustrackresult, null);
        mainView.setOnTouchListener(SeoulBus.otl);
        
        countDownTimer = new CountDownTimerExtension(12000000, 30000).start();
    }
    
    BusLocationView busLocationView;
    TextView statusText = new TextView(BMW.bmw);
    TextView progressText = new TextView(BMW.bmw);
    
    public RelativeLayout getView(){
        LinearLayout ll = (LinearLayout) mainView.findViewById(R.id.main_content);
        
        TextView startStationText = new TextView(BMW.bmw);
        startStationText.setText("출발지: " + seoulBusTrack.getStartStationName());
        startStationText.setTextSize(17);
        startStationText.setTextColor(Color.BLACK);
        
        TextView endStationText = new TextView(BMW.bmw);
        endStationText.setText("도착지: " + seoulBusTrack.getEndStationName());
        endStationText.setTextSize(17);
        endStationText.setTextColor(Color.BLACK);
        
        progressText = new TextView(BMW.bmw);
        progressText.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 50));
        progressText.setGravity(Gravity.CENTER);
        
        setProgressInfo(progressText);
        
        progressText.setTextSize(28);
        progressText.setTextColor(Color.BLACK);
        
        
        statusText.setTextColor(Color.BLACK);
        setStatusInfo(statusText, seoulBusTrack.getMyBusLocation());
        
        ll.addView(startStationText);
        ll.addView(endStationText);
        ll.addView(progressText);
        ll.addView(statusText);
        
        String startStationName = seoulBusTrack.getStartStationName();
        String endStationName = seoulBusTrack.getEndStationName();
        startStationName = startStationName.substring(0, startStationName.length() - 7);
        endStationName = endStationName.substring(0, endStationName.length() - 7);
        
        busLocationView = new BusLocationView(BMW.bmw, startStationName, endStationName, seoulBusTrack.getRouteLenght());
        mainView.addView(busLocationView);

        return mainView;
        
    }


    private void setProgressInfo(TextView progressText)
    {
        int percent = (int)((((float) -seoulBusTrack.getMyBusLocation() + 1) / (float)seoulBusTrack.getRouteLenght()) * 100);
        int currentPosition = (-seoulBusTrack.getMyBusLocation() + 1);
        if(currentPosition < 0 || (1 - currentPosition) == TrackData.NO_BUS_DEPARTED) {
            percent = 0;
            currentPosition = 0;
        }
        
        progressText.setText(percent + "% (" + currentPosition + " / " + seoulBusTrack.getRouteLenght() + ")");
    }
    

    private void setStatusInfo(TextView textView, int myBusLocation) {
        if(myBusLocation == TrackData.NO_BUS_DEPARTED){
              textView.setText("아직 종점을 출발하지 않았습니다."  + myBusLocation);
          } else if (myBusLocation == 1){
              textView.setText("출발지인 "  + seoulBusTrack.getNextStation() + "에  도착합니다." + myBusLocation);
          } else if (myBusLocation == 0){
              textView.setText(seoulBusTrack.getPreviousStation() + " 를 출발하여 "  + seoulBusTrack.getNextStation() + "정류장을 향하고 있습니다 \n");
          } else if (-(seoulBusTrack.getRouteLenght() + myBusLocation - 1) == 0 ) {
        	  textView.setText("도착지를 방금 지나첬습니다. " + seoulBusTrack.getPreviousStation() + " 를 출발하여 "  + seoulBusTrack.getNextStation() + "정류장을 향하고 있습니다 \n");//+ seoulBusTrack.getRouteStationName(-myBusLocation) + " " + seoulBusTrack.getRouteStationNumber(-myBusLocation) + " buspostion " + seoulBusTrack.getBusPositionBetweenStation() + "  time left " +seoulBusTrack.getBusPositionMinLeft());        	  
          } else if ((seoulBusTrack.getRouteLenght() + myBusLocation - 1) <= 0){
              textView.setText("도착지에서 " + (-(seoulBusTrack.getRouteLenght() + myBusLocation - 1))+ " 정거장 지났습니다.\n" + seoulBusTrack.getPreviousStation() + " 를 출발하여 "  + seoulBusTrack.getNextStation() + "정류장을 향하고 있습니다 \n");//+ seoulBusTrack.getRouteStationName(-myBusLocation) + " " + seoulBusTrack.getRouteStationNumber(-myBusLocation) + " buspostion " + seoulBusTrack.getBusPositionBetweenStation() + "  time left " +seoulBusTrack.getBusPositionMinLeft());
              if(-(seoulBusTrack.getRouteLenght() + myBusLocation - 1) > 5) {
            	  countDownTimer.cancel();
              }
          } else if (myBusLocation > 0){
        	  textView.setText((myBusLocation)+ " 전 정거장을 버스가 출발 하였습니다.");
          } else if (seoulBusTrack.getRouteLenght() + myBusLocation == 2){
        	  textView.setText("도착지인 "  + seoulBusTrack.getNextStation() + "에  곧 도착합니다." + myBusLocation);
        	  VibratorControl vib = new VibratorControl(BMW.bmw);
        	  vib.start();
          } else {
              textView.setText("도착지까지 " + (seoulBusTrack.getRouteLenght() + myBusLocation - 1)+ " 정거장 남았습니다.\n" + seoulBusTrack.getPreviousStation() + " 를 출발하여 "  + seoulBusTrack.getNextStation() + " 정류장을 향하고 있습니다. \n");//+ seoulBusTrack.getRouteStationName(-myBusLocation) + " " + seoulBusTrack.getRouteStationNumber(-myBusLocation) + " buspostion " + seoulBusTrack.getBusPositionBetweenStation() + "  time left " +seoulBusTrack.getBusPositionMinLeft());
          }
        
    }
    
    public boolean onTouch(View v, MotionEvent event) {
        if(SeoulBus.gd.onTouchEvent(event)) return false;
        return false;
    }
    
    private final class CountDownTimerExtension extends CountDownTimer
    {
        private CountDownTimerExtension(long millisInFuture,
                long countDownInterval)
        {
            super(millisInFuture, countDownInterval);
        }

        public void onTick(long millisUntilFinished) {

            if(myBusUrl == null) myBusUrl = TrackData.getInstance().getTrack().getMyBus();
            TrackData.getInstance().setMyBus(myBusUrl);
            seoulBusTrack = TrackData.getInstance().getTrack();
            
            if(seoulBusTrack.getMyBusLocation() != TrackData.NO_BUS_DEPARTED) {
                busLocationView.setCurrentBusPosition(seoulBusTrack.getNextStation(), -seoulBusTrack.getMyBusLocation(), Float.parseFloat(seoulBusTrack.getCurrentPositionBetweenStations()[2]));
            } else {
                busLocationView.setCurrentBusPosition("종점 출발 대기", -seoulBusTrack.getMyBusLocation(), 0);
            }
            updateInfo();
            
            if(-seoulBusTrack.getMyBusLocation() == seoulBusTrack.getRouteLenght() - 1 ) this.cancel();
        }

        private void updateInfo() {
            setStatusInfo(statusText, seoulBusTrack.getMyBusLocation());    
            setProgressInfo(progressText);        
        }

        public void onFinish() {
        }
    }

    private class BusLocationView extends View {
        private Paint paintBlack = new Paint();
        private Paint paintBlue = new Paint();
        
        private Path pathBusLine = new Path();
        private Path pathArrow = new Path();
        
        private int stationNumber;
        private int lineStart;
        private int lineEnd;
        private int width;

        private float position = 0;
        private int busDestination = 0;
        private Bitmap bitmap;
        private String busDestinationName = "        ";
        private String busStartName;
        private String busEndName;
        
        
        public BusLocationView(Context context, String busStartName, String busEndName, int stationNumber) {
            super(context);
            
            this.busStartName = busStartName;
            this.busEndName = busEndName;
            this.stationNumber = stationNumber;
            
            lineStart = 20;
            lineEnd = 280;
            
            width = (lineEnd - lineStart) / ( stationNumber - 1 );

            pathBusLine.moveTo(0, -1);
            pathBusLine.lineTo(300, -1);
            pathBusLine.lineTo(300, 1);
            pathBusLine.lineTo(0, 1);
            pathBusLine.close();
            
            pathArrow.moveTo(5, -20);
            pathArrow.lineTo(25, -20);
            pathArrow.lineTo(15, -5);
            pathArrow.close();

            paintBlack.setAntiAlias(true);
            paintBlack.setColor(Color.BLACK);
            paintBlack.setStyle(Paint.Style.FILL);
            
            paintBlue.setAntiAlias(true);
            paintBlue.setColor(Color.BLUE);
            paintBlue.setStyle(Paint.Style.FILL);

            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bus_small, null); 
        }

        
        
        @Override 
        protected void onDraw(Canvas canvas) {

            canvas.translate(10, 270);
            drawLine(canvas);
            drawStations(canvas);
            drawBus(canvas);
        }
        
        public void setCurrentBusPosition(String busDestinationName, int busDestination, float position) {
            busDestination++;
            this.busDestinationName = busDestinationName;
            if(-busDestination == TrackData.NO_BUS_DEPARTED) {
                this.busDestination = -1;
            } else {
                this.busDestination = busDestination;    
            }
            
            this.position = position;
            
    		if(position > 1) {
    			this.position = 1f;
    		} else if (position < 0) {
    			this.position = 0f;
    		} else if (position == 0) {
    			this.position = 0.3f;
    		} else if (position == 1) {
    			this.position = 0.7f;
    		}
            
            this.invalidate();
        }
        

        private void drawLine(Canvas canvas) {
            canvas.drawPath(pathBusLine, paintBlack);
            canvas.drawText("출발" ,0, 18, paintBlue);
            canvas.drawText(busStartName, 0, 30, paintBlack);
            canvas.drawText("도착", lineEnd - 20, 18, paintBlue);
            canvas.drawText(busEndName, lineEnd - 20 - busEndName.length() * 7, 30, paintBlack);
        }

        private void drawBus(Canvas canvas){
            
            float offset;
            
            if(busDestination <= 0 || busDestination > 100) {
                offset = -5;
            } else if (busDestination > stationNumber) {
                offset = (lineEnd - lineStart - 6);
            } else if(busDestination >= stationNumber) {
                offset = 275;
            } else {
                offset = 5 + width * (busDestination - 1) + width * position;            
            }
            
            canvas.drawBitmap(bitmap, offset + 5, -36f, null);
            float textOffset = offset + 60 - (busDestinationName.length() * 6);
            if (textOffset < 0) {
                textOffset = 0;
            } else if (textOffset > 250) {
                textOffset = 250;
            }
            
            canvas.drawText(busDestinationName.substring(0, busDestinationName.length() - 8), textOffset, -45f, paintBlack);
            
        }
        
        private void drawStations(Canvas canvas){

            for(int i = 0; i < stationNumber; i ++) {
                canvas.drawCircle(lineStart + width * i, 0, 3, paintBlack);
            }

            int highlight = busDestination;
            if(busDestination < 0 || busDestination >= 100) highlight = 0;
            else if(busDestination > stationNumber) highlight = stationNumber;
            canvas.drawCircle(lineStart + width * highlight, 0, 4, paintBlue);
            
        }
    }

    
}
