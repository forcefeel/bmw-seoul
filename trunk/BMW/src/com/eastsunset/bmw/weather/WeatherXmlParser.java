package com.eastsunset.bmw.weather;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.util.Log;

public class WeatherXmlParser {

	private static final String SOURCE_SITE = "http://www.google.com/ig/api?ie=utf-8&oe=utf-8&hl=ko&weather=";
	ArrayList<Element> weekWeatherData = new ArrayList<Element>();
	private Element informationElement;
	private Element currentWeatherElement;
	private NodeList forecastWeatherNodeList;
	private Element root;
	
	public WeatherXmlParser(String city){
		getWeatherData(city);
	}
	
	public Element getInformation() {
		return informationElement;
	}
	
	public String getForecastData(int group, String tagName) {
		String value = ((Element)root.getElementsByTagName("forecast_conditions").item(group)).getChildNodes().item(getWeekTagId(tagName)).getAttributes().getNamedItem("data").getNodeValue();
		if (value.length() == 0) value = "No Data";
		return value;
	}
	
	public String getCurrentData(String tagName){
		String value = "No Data";
		if(tagName.equals("forecast_date")) {
			value = ((Element)root.getElementsByTagName("forecast_information").item(0)).getChildNodes().item(getCurrentTagId(tagName)).getAttributes().getNamedItem("data").getNodeValue();
		} else {
			value = ((Element)root.getElementsByTagName("current_conditions").item(0)).getChildNodes().item(getCurrentTagId(tagName)).getAttributes().getNamedItem("data").getNodeValue();
		}
		return value; 
	}
	
	private int getCurrentTagId(String tagName) {
		if(tagName.equals("forecast_date")) return 4;
		else if(tagName.equals("condition")) return 0;
		else if(tagName.equals("temp_c")) return 2;
		else if(tagName.equals("humidity")) return 3;
		else if(tagName.equals("icon")) return 4;
		else if(tagName.equals("wind_condition")) return 5;
		Log.v("myra", "getCurrentTagId  -1");
		return -1;
	}
	
	private int getWeekTagId(String tagName) {
		if(tagName.equals("day_of_week")) return 0;
		else if(tagName.equals("low")) return 1;
		else if(tagName.equals("high")) return 2;
		else if(tagName.equals("icon")) return 3;
		else if(tagName.equals("condition")) return 4;
		
		return -1;
	}
	

	private void getWeatherData(String city){

		URL url;
		try{
			String booksFeed = SOURCE_SITE + city;
			url = new  URL(booksFeed);
			URLConnection connection;
			connection = url.openConnection();
			HttpURLConnection httpConnection = (HttpURLConnection)connection;
			int responseCode = httpConnection.getResponseCode();
			if(responseCode == HttpURLConnection.HTTP_OK){
				
				Log.v("myra", "encordign " + httpConnection.getContentEncoding());
				InputStream in = httpConnection.getInputStream();
				
				DocumentBuilderFactory dbf;
				dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				
				Document dom = db.parse(in);
					
				root = dom.getDocumentElement();

				informationElement = (Element) root.getElementsByTagName("forecast_information").item(0);
				currentWeatherElement = (Element) root.getElementsByTagName("current_conditions").item(0);
				forecastWeatherNodeList = root.getElementsByTagName("forecast_conditions");

			}
	        } catch(MalformedURLException e){
	        	e.printStackTrace();
	        } catch(IOException e){
	        	e.printStackTrace();
	        } catch(ParserConfigurationException e){
	        	e.printStackTrace();
	        } catch(SAXException e){
	        	e.printStackTrace();
	        }
	 }
}
