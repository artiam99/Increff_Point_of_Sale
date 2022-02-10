package com.increff.pos.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StringUtil {

	public static boolean isEmpty(String s) {
		return s == null || s.trim().length() == 0;
	}

	public static String toLowerCase(String s) {
		return s == null ? null : s.trim().toLowerCase();
	}

	public static String getDateTime() {
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		Date dateobj = new Date();
		String datetime = df.format(dateobj);
		return datetime;
	}

	public static String trimDate(String date) {
		String day = date.substring(0,2);
		String month = date.substring(3,5);
		String year = date.substring(6,10);
		date = year + "-" + month + "-" + day;
		return date;
	}

	public static boolean isAfter(String date1, String date2) {
		int year1 = Integer.parseInt(date1.substring(0, 4));
		int year2 = Integer.parseInt(date2.substring(0, 4));
		int month1 = Integer.parseInt(date1.substring(5, 7));
		int month2 = Integer.parseInt(date2.substring(5, 7));
		int day1 = Integer.parseInt(date1.substring(8, 10));
		int day2 = Integer.parseInt(date2.substring(8, 10));
		Calendar c1 = Calendar.getInstance();
		c1.set(Calendar.MONTH, month1-1);
		c1.set(Calendar.DATE, day1);
		c1.set(Calendar.YEAR, year1);
		Date dateOne = c1.getTime();
		Calendar c2 = Calendar.getInstance();
		c2.set(Calendar.MONTH, month2-1);
		c2.set(Calendar.DATE, day2);
		c2.set(Calendar.YEAR, year2);
		Date dateTwo = c2.getTime();
		return dateTwo.after(dateOne);
	}
}