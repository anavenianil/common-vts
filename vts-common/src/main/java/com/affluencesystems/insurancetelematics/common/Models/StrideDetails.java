package com.affluencesystems.insurancetelematics.common.Models;

import java.util.ArrayList;

public class StrideDetails {

	private int canSpeed;
	private int millage;
	private float fuelPerLiter;
//	private int engineCoolentTemparature;
	private float engineLoad;
	private float engineRpm;
	private int speedCan;
	private byte FuelType;
	private float canFuelConsumed;

	private ArrayList<GpsData> gpsData;

	public int getCanSpeed() {
		return canSpeed;
	}

	public void setCanSpeed(int canSpeed) {
		this.canSpeed = canSpeed;
	}

	public int getMillage() {
		return millage;
	}

	public void setMillage(int millage) {
		this.millage = millage;
	}

	public float getFuelPerLiter() {
		return fuelPerLiter;
	}

	public void setFuelPerLiter(float fuelPerLiter) {
		this.fuelPerLiter = fuelPerLiter;
	}

	public ArrayList<GpsData> getGpsData() {
		return gpsData;
	}

	public void setGpsData(ArrayList<GpsData> gpsData) {
		this.gpsData = gpsData;
	}



	public float getEngineLoad() {
		return engineLoad;
	}

	public void setEngineLoad(float engineLoad) {
		this.engineLoad = engineLoad;
	}

	public float getEngineRpm() {
		return engineRpm;
	}

	public void setEngineRpm(float engineRpm) {
		this.engineRpm = engineRpm;
	}

	public int getSpeedCan() {
		return speedCan;
	}

	public void setSpeedCan(int speedCan) {
		this.speedCan = speedCan;
	}

	public byte getFuelType() {
		return FuelType;
	}

	public void setFuelType(byte fuelType) {
		FuelType = fuelType;
	}

	public float getCanFuelConsumed() {
		return canFuelConsumed;
	}

	public void setCanFuelConsumed(float canFuelConsumed) {
		this.canFuelConsumed = canFuelConsumed;
	}

}
