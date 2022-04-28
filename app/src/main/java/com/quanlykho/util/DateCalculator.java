package com.quanlykho.util;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DateCalculator {
	private static final SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");
	private static final long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;

	private static Date findNextDay(Date date)
	{
		return new Date(date.getTime() + MILLIS_IN_A_DAY);
	}

	private static Date findPrevDay(Date date)
	{
		return new Date(date.getTime() - MILLIS_IN_A_DAY);
	}

	public static long getDifferenceDays(Date d1, Date d2) {
		long diff = d2.getTime() - d1.getTime();
		return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
	}
	public static ArrayList<String> listDate(Date date1, Date date2){
		ArrayList<String> dateList=new ArrayList<String>();
		long diff = getDifferenceDays(date1,date2);
		Date tempDate=date1;
		for(long i=0;i<=diff;i++){
			dateList.add(format.format(tempDate).toString());
			tempDate=findNextDay(tempDate);
		}
		Log.d("Date","test tao list date"+dateList.toString());
		return dateList;
	}
}
