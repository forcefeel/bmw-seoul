package com.eastsunset.bmw.map;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.ZoomControls;

import com.eastsunset.bmw.BMW;
import com.eastsunset.bmw.R;
import com.eastsunset.bmw.TrackData;
import com.eastsunset.bmw.bus.SeoulBusTrack;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MapMain extends MapActivity implements OnClickListener {
	
	private static final int MENU_SATELLITE = 0;
	private static final int MENU_STREETVIEW = 1;
	
	LinearLayout linearLayout;
	MapView mapView;
	ZoomControls mZoom;
    List<Overlay> mapOverlays;
//    MapController mapController;
    
    
    MapOverlayLoadingThread progressThread;
    MapControlHelper mapControlHelper;
    
	private LocationManager mgr;
	private String best;
    private boolean isEmulator = true;
    private String betweenStart;
    private String betweenEnd;
    private float betweenPosition;

    static final int PROGRESS_DIALOG = 0;
	private static final int SEARCH_STATION_DIALOG = 1;

	public static Context context;
   
    /** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
   
        setContentView(R.layout.mapmain);
        
        context = this;
        mapView = (MapView)findViewById(R.id.mapview);
        mapView.setSatellite(false);
        mapView.setBuiltInZoomControls(true);
        mapView.setOnClickListener(this);
        mapView.getController().setZoom(14);
        setupButton();
        
        // log.v("myra", "onCreate");
        if(!isEmulator ) mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mView = new MagnometerView(this);
        // log.v("myra", "onCreate1");

    }
	
	@Override
	public void onResume(){
		super.onResume();
        // log.v("myra", "onResume");
        if(!isEmulator) mSensorManager.registerListener(mListener, 
        		SensorManager.SENSOR_ORIENTATION,
        		SensorManager.SENSOR_DELAY_GAME);
        
        // 자동 새로고침 시작
//        if(countDownTimer != null & ((ToggleButton)findViewById(R.id.second_button)).isChecked()) {
//            // log.v("myra", "Countdown resume");
//            countDownTimer.start();
//        }
	}
	
	@Override
	public void onPause() {

		// locationManager 초기화
		if(locationManager != null) {
			locationManager.removeUpdates(locationListener);
			locationManager = null;
		}
		
		// 자동 새로고침 취소
//		if(countDownTimer != null) countDownTimer.cancel();
		
	    // log.v("myra", "onPause");
	    super.onPause();

	}
	
    @Override
    protected void onStop(){
	    // log.v("myra", "onStop");
        if(!isEmulator)  mSensorManager.unregisterListener(mListener);
        super.onStop();
    }

   @Override
    protected boolean isRouteDisplayed() {
        return false;
   }
   
   
   private void attachNewOverlay() {

       if(mapView.getOverlays().size() != 0 ) mapView.getOverlays().remove(0);
       mapControlHelper = new MapControlHelper(mapView);
   }

	private void setupButton() {
		((ToggleButton)findViewById(R.id.first_button)).setOnClickListener(this);
		findViewById(R.id.second_button).setOnClickListener(this);
		findViewById(R.id.third_button).setOnClickListener(this);
		findViewById(R.id.fourth_button).setOnClickListener(this);
	}
   

	/* Creates the menu items */
	public boolean onCreateOptionsMenu(Menu menu) {
	    menu.add(0, MENU_SATELLITE, 0, "위성 사진").setIcon(android.R.drawable.ic_menu_view);
	    menu.add(0, MENU_STREETVIEW, 0, "지도").setIcon(android.R.drawable.ic_menu_mapmode);
	    return true;
	}

	/* Handles item selections */
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case MENU_SATELLITE:
	    	mapView.setSatellite(true);
	        return true;
	    case MENU_STREETVIEW:
	        mapView.setSatellite(false);
	        return true;
	    case 9:
//	    	mapView.setVisibility(View.GONE);
	    	RelativeLayout rl = ((RelativeLayout)this.findViewById(R.id.mainlayout));
	    	rl.addView(mView);
	    	
	    	return true;
	    case 8:
	    	RelativeLayout ll = ((RelativeLayout)this.findViewById(R.id.mainlayout));
	    	ll.removeAllViews();
	    	ll.addView(mapView);
	    	mapView.setVisibility(View.VISIBLE);
//	    	attachNewOverlay();
//	    	mapControlHelper.currentPosition("08258", "08259", 0.2f);
        return true;
    }
	    return false;
	}

	private void setRouteData(ArrayList<String> data) {
		attachNewOverlay();

		ProgressDialog progressDialog = new ProgressDialog(this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setMessage("정류장 위치 정보를 가지고 오고 있습니다.");
		
		if(progressThread == null) {
			progressThread = new MapOverlayLoadingThread(mapControlHelper);
		}
		progressThread.setProgressDialog(progressDialog);
		
		if(!progressThread.setStationList(data)){
			// log.v("myra", "not set");
			progressDialog.setProgress(data.size());
		} else {
			// log.v("myra", "setsetsetsetsetset");
			progressDialog.setMax(data.size());
			progressDialog.setProgress(0);
		    progressDialog.show();
		}
		// 버스가 출발하여 정보가 있을때만 현재 버스 위치 표시
		if(betweenStart != null ) progressThread.setCurrentPosition(betweenStart, betweenEnd, betweenPosition);
//        progressThread.setCurrentPosition("08260", "08262", 0.5f);
		progressThread.process();
	}
	
	private void setCurrentPosition(String[] positionData){
	    //버스가 출발하지 않았을때
	    if(positionData == null) {
	        betweenStart = null;
	        return;
	    }
        this.betweenStart = positionData[0];
        this.betweenEnd = positionData[1];
        // log.v("myra", "post Parser Float " + positionData[2]);
        this.betweenPosition = Float.parseFloat(positionData[2]);
        // log.v("myra", "fafter Parser Float " + betweenPosition);
	    
	}

    LocationManager locationManager;
	private LocationListener locationListener;
    private CountDownTimer countDownTimer;
    
    private void init_gps(){
    	locationListener = new MyLocationListener();
    	
    	locationManager = (LocationManager)this.getSystemService(LOCATION_SERVICE);      
    	locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, locationListener);
    	
//    	lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


//		mapControlHelper.moveToCenter();
    	// log.v("myra", " asdf sfd  "  + lastLocation);
    }

    private ArrayList<String> getDummyData() {
		ArrayList<String> stationlist = new ArrayList<String>();
		stationlist.add("08258");
		stationlist.add("08259");
		stationlist.add("08260");
		stationlist.add("08262");
		stationlist.add("08324");
		stationlist.add("08322");
		stationlist.add("08320");
		stationlist.add("08115");
		stationlist.add("08117");
		stationlist.add("08119");
		return stationlist;
	}

	public void onClick(final View v) {
		if (v == findViewById(R.id.first_button)) {

			if(((ToggleButton)v).isChecked()) {
				((ToggleButton)v).setChecked(true);
				
				uncheckToggleButton((ToggleButton)findViewById(R.id.second_button));
				
				if(locationManager == null ) {
					// log.v("myra", "init location");
					init_gps();
				} 
				lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				if(lastLocation != null) {
					if(mapControlHelper == null) attachNewOverlay();
					if(lastStation != null & ((ToggleButton)findViewById(R.id.third_button)).isChecked()) {
						mapControlHelper.addStation(lastStation);
					}
					mapControlHelper.updateGpsPosition(lastLocation);
				}
			} else {
				((ToggleButton)v).setChecked(false);
				
				// locationManager 초기화
				if(locationManager != null) {
					locationManager.removeUpdates(locationListener);
					locationManager = null;
				}
			    
			    releaseMapOverlay();

				if(lastStation != null & ((ToggleButton)findViewById(R.id.third_button)).isChecked()) {
	            	attachNewOverlay();
	            	String[] point = mapControlHelper.addStation(lastStation);
					mapControlHelper.moveTo(Double.parseDouble(point[1]), Double.parseDouble(point[0]), -1);
				}
			}
		} else if (v == findViewById(R.id.second_button)) {
			
			if(((ToggleButton)v).isChecked()) {
			    
			    if(TrackData.getInstance().getTrack() == null) {
			        ((ToggleButton)v).setChecked(false);
			        return;
			    }
				((ToggleButton)v).setChecked(true);
				// 첫번째 버튼 취소

				uncheckToggleButton((ToggleButton)findViewById(R.id.first_button));
				uncheckToggleButton((ToggleButton)findViewById(R.id.third_button));
				
//				setCurrentPosition(TrackData.getInstaz1nce().getInstance().getTrack().getCurrentPositionBetweenStations());
//				setRouteData(TrackData.getInstance().getTrack().getRouteStationNumber());
				
				countDownTimer = new CountDownTimerExtension(12000000, 30000).start();
				
			} else {
				((ToggleButton)v).setChecked(false);
				countDownTimer.cancel();
//				countDownTimer.cancel();
			    releaseMapOverlay();
			}

		} else if(v == findViewById(R.id.third_button)) {
			if(((ToggleButton)v).isChecked()) {
				((ToggleButton)v).setChecked(true);
				uncheckToggleButton((ToggleButton)findViewById(R.id.second_button));
				
				final EditText inputText = new EditText(this);
				inputText.setSingleLine();
				new AlertDialog.Builder(BMW.bmw).setTitle("정류장 검색").setMessage("정류장 번호를 입력 하세요.").setView(
						inputText).setPositiveButton("확인", new DialogInterface.OnClickListener() {
							 public void onClick(DialogInterface dialog, int whichButton) {
								attachNewOverlay();
								if(lastLocation != null & ((ToggleButton)findViewById(R.id.first_button)).isChecked()) {
									mapControlHelper.updateGpsPosition(lastLocation);
								}
								//	else mapControlHelper.moveTo(lastLocation.getLatitude(), lastLocation.getLongitude(), -1);
								lastStation = inputText.getText().toString();
								
								// 올바른 입력일때만 검색, 아니면 에러메세지
								String[] position = mapControlHelper.addStation(lastStation);
								
								// 정류장을 찾을 수 없을때
								if(position == null ) {
                                    Toast.makeText(MapMain.context, lastStation + " 정류장을 찾을 수 없습니다.", 3000).show();    
									((ToggleButton)v).setChecked(false);
									return;
								}
								mapControlHelper.moveTo(Double.parseDouble(position[1]), Double.parseDouble(position[0]), -1);
							}
						 }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
							 public void onClick(DialogInterface dialog, int whichButton) {
									((ToggleButton)v).setChecked(false);
							 }
							}).show();
			} else {
				((ToggleButton)v).setChecked(false);
			    releaseMapOverlay();
			    
				if(lastLocation != null & ((ToggleButton)findViewById(R.id.first_button)).isChecked()) {
	            	attachNewOverlay();
					mapControlHelper.updateGpsPosition(lastLocation);
				}
				
			}
		} else if (v == findViewById(R.id.fourth_button)) {
			
			if(((ToggleButton)v).isChecked()) {
				((ToggleButton)v).setChecked(true);
		    	((RelativeLayout)this.findViewById(R.id.mainlayout)).addView(mView);
			} else {
				((ToggleButton)v).setChecked(false);
		    	((RelativeLayout)this.findViewById(R.id.mainlayout)).removeView(mView);
			}

		}  
	}

	private void uncheckToggleButton(ToggleButton toggleButton) {
		if(toggleButton.isChecked()) {
			toggleButton.setChecked(false);
			onClick(toggleButton);
		}
	}
	
	private void releaseMapOverlay(){
		attachNewOverlay();
		preventOverlayUnderFlow();
		mapView.invalidate();
	}
	
	private void preventOverlayUnderFlow() {
		if(mapView.getOverlays().size() != 1) return;
		
		if(((MapItemizedOverlay)mapView.getOverlays().get(0)).size() < 2) {
			mapView.getOverlays().remove(0);
			mapView.invalidate();
			mapControlHelper = null;
		}
	}
	

    
	Location lastLocation;
	protected String lastStation;
    
	private final class CountDownTimerExtension extends CountDownTimer
    {
        private CountDownTimerExtension(long millisInFuture,
                long countDownInterval)
        {
            super(millisInFuture, countDownInterval);
        }

        public void onTick(long millisUntilFinished) {
            // log.v("myra", "MapMain on tick");
            SeoulBusTrack busTrack = TrackData.getInstance().getTrack(); 
            
            setCurrentPosition(busTrack.getCurrentPositionBetweenStations());
            setRouteData(busTrack.getRouteStationNumber());
            if((busTrack.getMyBusLocation() - busTrack.getRouteLenght() - 1) > 5) {
            	this.cancel();
            }
        }

        public void onFinish() {
        }
    }


    private class MyLocationListener implements LocationListener {

            public void onLocationChanged(Location location) {
                 // TODO Auto-generated method stub
            	attachNewOverlay();
            	if(lastStation != null) mapControlHelper.addStation(lastStation);
				mapControlHelper.updateGpsPosition(location);
            	lastLocation = location;
            	
                 // log.v("myra", "onLocationChanged: lat="+location.getLatitude());
                 // log.v("myra", "onLocationChanged: lat="+location.getLongitude());                 
            }

            public void onProviderDisabled(String provider) {
                 // log.v("myra", "onProviderDisabled: " +provider);
                 	
            }
            public void onProviderEnabled(String provider) {
         	   // log.v("myra", "onProviderEnabled: " +provider);
                 
            }
            public void onStatusChanged(String provider, int status,
                      Bundle extras) {
                 // TODO Auto-generated method stub
            	if(lastLocation != null) onLocationChanged(lastLocation);
            	// log.v("myra", "onStatusChanged: " +provider + status);
                 
            }
      
     };
	
	private SensorManager mSensorManager;
    private MagnometerView mView;
    private float[] mValues;

    private final SensorListener mListener = new SensorListener() {

        public void onSensorChanged(int sensor, float[] values) {
            mValues = values;
            if (mView != null) {
                mView.invalidate();
            }
        }

        public void onAccuracyChanged(int sensor, int accuracy) {
            // TODO Auto-generated method stub

        }
    };

	
    private class MagnometerView extends View {

        private Paint paintNorth = new Paint();
        private Paint paintSouth = new Paint();
		private Paint paintText  = new Paint();
        
        private Path northPath = new Path();
        private Path southPath = new Path();

        private int w, h, cx, cy;
        
        public MagnometerView(Context context) {
            super(context);

            // Construct a wedge-shaped path
            northPath.moveTo(0, -50);
            northPath.lineTo(-20, 0);
            northPath.lineTo(20, 0);
            northPath.close();

            southPath.moveTo(0, 50);
            southPath.lineTo(-20, 0);
            southPath.lineTo(20, 0);
            southPath.close();
            
            paintNorth.setAntiAlias(true);
            paintNorth.setColor(Color.RED);
            paintNorth.setStyle(Paint.Style.FILL);

            paintSouth.setAntiAlias(true);
            paintSouth.setColor(Color.BLUE);
            paintSouth.setStyle(Paint.Style.FILL);
            
            paintText.setAntiAlias(true);
            paintText.setColor(Color.WHITE);
            paintText.setStyle(Paint.Style.FILL);

        }

        @Override 
        protected void onDraw(Canvas canvas) {
            
            w = canvas.getWidth();
            h = canvas.getHeight();
            cx = w - 50;
            cy = h / 8;

            canvas.translate(cx, cy);
            if (mValues != null) {            
                canvas.rotate(-mValues[0]);
            }
            canvas.drawPath(northPath, paintNorth);
            canvas.drawPath(southPath, paintSouth);
            canvas.drawText("남", -7, 22, paintText);
            canvas.drawText("북", -7, -15, paintText);
        }

    }
}