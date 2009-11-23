package com.eastsunset.bmw.weather;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eastsunset.bmw.BMW;
import com.eastsunset.bmw.R;

public class WeatherCity {

	private String city;
	private RelativeLayout mainView;
	WeatherXmlParser weatherXmlParser;

	public WeatherCity(String city){
		
		
		this.city = city;
        mainView = (RelativeLayout) BMW.bmw.getLayoutInflater().inflate(R.layout.weathercity, null);
        setTitle(city);
        weatherXmlParser = new WeatherXmlParser(city);
        displayWeatherData();
        displayForecastData();
        
        ((TextView)mainView.findViewById(R.id.current_weather_title)).setBackgroundDrawable(new ColorDrawable(Color.parseColor(BMW.BACKGROUND_DRAWABLE_WEATHER)));
	}
	
	private void displayForecastData() {
		
		int MAX_WIDTH = 64;
		
		LinearLayout ll = (LinearLayout) mainView.findViewById(R.id.weather_forecast);

		for(int i = 0; i < 4; i++) {
			LinearLayout.LayoutParams a = new LinearLayout.LayoutParams(MAX_WIDTH, LinearLayout.LayoutParams.WRAP_CONTENT);
			LinearLayout weekForecastLayout = new LinearLayout(BMW.bmw);
			

			LayoutParams textParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT); 
			weekForecastLayout.setLayoutParams(a);
			weekForecastLayout.setGravity(Gravity.CENTER_HORIZONTAL);
			weekForecastLayout.setOrientation(LinearLayout.VERTICAL);
			
			TextView day = new TextView(BMW.bmw);
			day.setText(weatherXmlParser.getForecastData(i, "day_of_week"));
			day.setTextColor(Color.WHITE);
			day.setLayoutParams(textParams);
			day.setTextSize(16);
			day.setBackgroundDrawable(new ColorDrawable(Color.parseColor(BMW.BACKGROUND_DRAWABLE_WEATHER)));
			day.setGravity(Gravity.CENTER_HORIZONTAL);
			weekForecastLayout.addView(day);
			
			ImageView icon = new ImageView(BMW.bmw);
			icon.setImageResource(getWeatherIcon(weatherXmlParser.getForecastData(i, "icon")));
			icon.setPadding(0, 3, 0, 0);
			weekForecastLayout.addView(icon);
			
			TextView condition = new TextView(BMW.bmw);
			condition.setText(weatherXmlParser.getForecastData(i, "condition").replace(" ", "\n"));
			condition.setTextColor(Color.BLACK);
			condition.setLayoutParams(textParams);
			condition.setHeight(35);
			condition.setGravity(Gravity.CENTER_HORIZONTAL);
			weekForecastLayout.addView(condition);
			
			TextView temp = new TextView(BMW.bmw);
			temp.setText(weatherXmlParser.getForecastData(i, "low") + " / " + weatherXmlParser.getForecastData(0, "high"));
			temp.setTextColor(Color.BLACK);
			temp.setLayoutParams(textParams);
			temp.setGravity(Gravity.CENTER_HORIZONTAL);
			weekForecastLayout.addView(temp);
			
			ll.addView(weekForecastLayout);
			
		}
	}

	private void displayWeatherData() {
		((TextView)mainView.findViewById(R.id.current_weather_title)).setText(weatherXmlParser.getCurrentData("forecast_date"));
		((TextView)mainView.findViewById(R.id.current_weather_data)).setText("온도: " + weatherXmlParser.getCurrentData("temp_c") + 
																				"℃ \n" + weatherXmlParser.getCurrentData("humidity") +
																				"\n" + weatherXmlParser.getCurrentData("wind_condition").replace(", ", "\n        "));
		((ImageView)mainView.findViewById(R.id.current_weather_image)).setImageResource(getWeatherIcon(weatherXmlParser.getCurrentData("icon")));
		((TextView)mainView.findViewById(R.id.current_weather_condition)).setText(weatherXmlParser.getCurrentData("condition"));
		
	}

	public View getWeatherView() {
		return mainView;
	}
	
	public void setTitle(String city){
		TextView titleView = (TextView) mainView.findViewById(R.id.main_title);
		
		if(city.equals("seoul")) titleView.setText("서울의 날씨");
		else if(city.equals("daejeon")) titleView.setText("대전의 날씨");
		else if(city.equals("taegu")) titleView.setText("대구의 날씨");
		else if(city.equals("pusan")) titleView.setText("부산의 날씨");
		
	}
	
	private int getWeatherIcon(String imgUrl){
		
		if (imgUrl.contains("fog")) return R.drawable.fog;
		if (imgUrl.contains("mist")) return R.drawable.mist;
		if (imgUrl.contains("snow")) return R.drawable.snow;
		if (imgUrl.contains("haze")) return R.drawable.haze;
		if (imgUrl.contains("rain")) return R.drawable.rain;
		if (imgUrl.contains("sunny")) return R.drawable.sunny;
		if (imgUrl.contains("storm")) return R.drawable.storm;
		if (imgUrl.contains("cloudy")) return R.drawable.cloudy;
		if (imgUrl.contains("mostly_sunny")) return R.drawable.mostly_sunny;
		if (imgUrl.contains("thunderstorm")) return R.drawable.thunderstorm;
		if (imgUrl.contains("mostly_cloudy")) return R.drawable.mostly_cloudy;
		if (imgUrl.contains("chance_of_snow")) return R.drawable.chance_of_snow;
		if (imgUrl.contains("chance_of_rain")) return R.drawable.chance_of_rain;
		if (imgUrl.contains("chance_of_storm")) return R.drawable.chance_of_storm;
		
		return -1;
	}
	
	
}
