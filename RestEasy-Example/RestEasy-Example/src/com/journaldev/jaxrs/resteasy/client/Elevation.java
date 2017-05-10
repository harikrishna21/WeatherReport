package com.journaldev.jaxrs.resteasy.client;

import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Elevation {

	String getElevation(String ltlg) {

		String elevation = null;
		String latLng = ltlg;
		ResteasyClient client_ele = new ResteasyClientBuilder().build();

		String keyy = "AIzaSyARFL7FR1T3eu69pfnZmbduEfN8I9oMueI";
		ResteasyWebTarget getDummy_ele = client_ele
				.target("https://maps.googleapis.com/maps/api/elevation/json?locations=" + latLng + "&key=" + keyy);

		Response getDummyResponse_ele = getDummy_ele.request().get();

		String value_ele = getDummyResponse_ele.readEntity(String.class);
		System.out.println("Elevation json : " + value_ele);
		getDummyResponse_ele.close();

		JSONObject jsonObject_ele = null;
		try {
			jsonObject_ele = new JSONObject(value_ele);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONArray myResponse_ele = null;
		try {
			myResponse_ele = jsonObject_ele.getJSONArray("results");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			elevation = myResponse_ele.getJSONObject(0).getString("elevation");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Elevation: " + elevation);
		return elevation;

	}

}
