package com.journaldev.jaxrs.resteasy.client;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


import org.json.JSONException;

public class GetCurrentDateTime {

	public static void main(String[] args) throws JSONException, ParseException {

		long timestamp = 1437010200;
		String as = "1437010568";		
		long a = Long.parseLong(as) ;
		System.out.println(a);
		Date d = new Date(a);		
		String dts = d.toString();
		DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
		Date date = (Date)formatter.parse(dts);		
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		String formatedDate = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" +         cal.get(Calendar.DATE)+" "+cal.get(Calendar.HOUR)+":"+cal.get(Calendar.MINUTE)+":"+cal.get(Calendar.SECOND);
		System.out.println("formatedDate : " + formatedDate);
		

		

	}

}
