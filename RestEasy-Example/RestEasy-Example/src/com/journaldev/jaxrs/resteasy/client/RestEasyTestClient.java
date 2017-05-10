package com.journaldev.jaxrs.resteasy.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.journaldev.jaxrs.model.WeatherInput;
import com.journaldev.jaxrs.model.WeatherIntermediateOutput;

public class RestEasyTestClient {

	private static final String FILENAME = "E:\\city_country.txt";
	private static final String OUTPUTFILENAME = "E:\\weather_report.txt";
	private static final String hdr = "Location|Position|Local Time|Conditions|Temperature|Pressure|Humidity";
	static List<WeatherInput> weatherInputList = new ArrayList<WeatherInput>();

	public static void main(String[] args) throws JSONException, ParseException {

		try {
			BufferedReader br = new BufferedReader(new FileReader(FILENAME));

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {

				String[] sLine = sCurrentLine.split(",");
				WeatherInput weatherInput = new WeatherInput();

				weatherInput.setCity(sLine[0]);
				weatherInput.setCountry(sLine[1]);

				

				weatherInputList.add(weatherInput);

			}

			generateWeatherInput(weatherInputList);
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void generateWeatherInput(List<WeatherInput> weatherInputList2) throws JSONException, IOException, ParseException {
		// TODO Auto-generated method stub
		BufferedWriter bw = null;
		FileWriter fw = null;

		fw = new FileWriter(OUTPUTFILENAME);
		bw = new BufferedWriter(fw);
		bw.write(hdr);
		bw.newLine();
		bw.flush();

		for (WeatherInput weatherInput : weatherInputList2) {
			ResteasyClient client = new ResteasyClientBuilder().build();

			ResteasyWebTarget getDummy = client.target("https://maps.googleapis.com/maps/api/geocode/json?address="
					+ weatherInput.getCity().trim().replace(" ", "+") + weatherInput.getCountry().trim());

			Response getDummyResponse = getDummy.request().get();

			String value = getDummyResponse.readEntity(String.class);
			
			getDummyResponse.close();

			JSONObject jsonObject = new JSONObject(value);
			JSONArray myResponse = jsonObject.getJSONArray("results");

			List<WeatherIntermediateOutput> weatherInterOutputList = new ArrayList<WeatherIntermediateOutput>();

			

			String geo = myResponse.getJSONObject(0).getString("geometry");
			
			JSONObject jsonObject2 = new JSONObject(geo);
			
			System.out.println("Location : " + jsonObject2.get("location"));
			JSONObject latg = (JSONObject) jsonObject2.get("location");
			System.out.println("lat: " + latg.get("lat"));
			System.out.println("lng: " + latg.get("lng"));

			String latLng = latg.get("lat").toString().concat(",").concat(latg.get("lng").toString());
			System.out.println("Lat+Lng : " + latLng);

			String appKey = myResponse.getJSONObject(0).getString("place_id");
			System.out.println("appkey : " + appKey);

			String appid = "71d44774805b03b9e735df2d624d4bbf";

			Elevation ele = new Elevation();

			String elevation1 = ele.getElevation(latLng);

			ResteasyClient client_temp = new ResteasyClientBuilder().build();

			ResteasyWebTarget getDummy_temp = client_temp.target(
					"http://samples.openweathermap.org/data/2.5/weather?lat=" + latg.get("lat").toString().trim() + "&"
							+ "lon=" + latg.get("lng").toString().trim() + "&" + "appid=" + appid);

			Response getDummyResponse_temp = getDummy_temp.request().get();

			String value_temp = getDummyResponse_temp.readEntity(String.class);
			
			getDummyResponse_temp.close();

			JSONObject jsonObject_temp = new JSONObject(value_temp);
			JSONArray myResponse_temp = jsonObject_temp.getJSONArray("weather");
			
			String condition = (String) myResponse_temp.getJSONObject(0).get("main");
			System.out.println("condition: " + myResponse_temp.getJSONObject(0).get("main"));
			

			JSONObject jsonObject_mn = new JSONObject(value_temp);
			Integer humidity = (Integer) jsonObject_mn.getJSONObject("main").get("humidity");
			Double pressure = (Double) jsonObject_mn.getJSONObject("main").get("pressure");
			Double temperature = (Double) jsonObject_mn.getJSONObject("main").get("temp");
			
			String time = jsonObject_temp.getString("dt");
			
			long lTime = Long.parseLong(time) ;
			Date d = new Date(lTime);		
			String dts = d.toString();
			DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
			Date date = (Date)formatter.parse(dts);		
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			String formatedDate = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" +         cal.get(Calendar.DATE)+" "+cal.get(Calendar.HOUR)+":"+cal.get(Calendar.MINUTE)+":"+cal.get(Calendar.SECOND);
			
			

			

			WeatherIntermediateOutput wo = new WeatherIntermediateOutput();
			String[] loc_split = myResponse.getJSONObject(0).getString("formatted_address").split(",");
			
			System.out.println("City : " + loc_split[0].trim());
			System.out.println("Country : " + loc_split[1].trim());
			System.out.println("Elevation : " + elevation1);
			wo.setCity(loc_split[0].trim());
			wo.setCountry(loc_split[1].trim());
			wo.setLat((Double) latg.get("lat"));
			wo.setLng((Double) latg.get("lng"));
			wo.setElevation(elevation1);
			wo.setCondition(condition);
			wo.setHumidity(humidity);			
			wo.setPressure(pressure);
			wo.setTemperature(temperature);
			wo.setTime(formatedDate);

			

			weatherInterOutputList.add(wo);

			

			String content = "";

			for (int i = 0; i < weatherInterOutputList.size(); i++) {
								
				
				content = weatherInterOutputList.get(i).city + "," + weatherInterOutputList.get(i).country + "|"
						+ weatherInterOutputList.get(i).lat + "," + weatherInterOutputList.get(i).lng + ","
						+ weatherInterOutputList.get(i).elevation + "|"+ weatherInterOutputList.get(i).time+"|" + weatherInterOutputList.get(i).condition + "|"+ weatherInterOutputList.get(i).temperature
						 + "|"+ weatherInterOutputList.get(i).pressure  + "|"
						 + weatherInterOutputList.get(i).humidity;
				
				

				try {

					bw.write(content);
					bw.newLine();

					System.out.println("Done");

				} catch (Exception e) {

				}
			}

		}
		fw.flush();
		bw.flush();
		bw.close();

	}

}
