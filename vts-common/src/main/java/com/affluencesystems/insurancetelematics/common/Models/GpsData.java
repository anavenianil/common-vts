package com.affluencesystems.insurancetelematics.common.Models;

import android.support.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

public class GpsData implements Comparable<GpsData>{
	private Date dateTime;
	private float latitude;
	private char latitudeDir;
	private float longitude;
	private char longitudeDir;
	private int speed;
//	private float heading;
	private int satelitesCount;
	private int altitude;
/*	private float PDOP;
	private float HDOP;*/



	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public void setDateTime(String dateAndTime) {
		try {
			this.dateTime = new SimpleDateFormat("ddMMyyhhmmss").parse(dateAndTime);
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateTime);
		calendar.add(Calendar.HOUR_OF_DAY, 5);
		calendar.add(Calendar.MINUTE, 30);
		this.dateTime = calendar.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public char getLatitudeDir() {
		return latitudeDir;
	}

	public void setLatitudeDir(char latitudeDir) {
		this.latitudeDir = latitudeDir;
	}

	public char getLongitudeDir() {
		return longitudeDir;
	}

	public void setLongitudeDir(char longitudeDir) {
		this.longitudeDir = longitudeDir;
	}



	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}



	public int getSatelitesCount() {
		return satelitesCount;
	}

	public void setSatelitesCount(int satelitesCount) {
		this.satelitesCount = satelitesCount;
	}

	public int getAltitude() {
		return altitude;
	}

	public void setAltitude(int altitude) {
		this.altitude = altitude;
	}

	/*Comparator for sorting the list by roll no*//*
	public static Comparator<GpsData> compareDate = new Comparator<GpsData>() {
		public int compare(GpsData s1, GpsData s2) {
			long rollno1 = s1.getDateTime().getTime();
			long rollno2 = s2.getDateTime().getTime();
			return rollno1-rollno2 > 0 ? 1 : 0;
		}
	};


*//*	@Override
	public int compareTo(@NonNull Object o) {
		long time = ((GpsData)o).getDateTime().getTime();
		return this.dateTime.getTime() - time > 0 ? 1 : 0;
	}*/


	@Override
	public String toString() {
		return "GpsData{" +
				"dateTime=" + dateTime +
				", latitude=" + latitude +
				", latitudeDir=" + latitudeDir +
				", longitude=" + longitude +
				", longitudeDir=" + longitudeDir +
				", speed=" + speed +
				", satelitesCount=" + satelitesCount +
				", altitude=" + altitude +
				'}';
	}

    @Override
    public int compareTo(@NonNull GpsData gpsData) {
        return (int)(this.dateTime.getTime() - gpsData.getDateTime().getTime());
    }
}
