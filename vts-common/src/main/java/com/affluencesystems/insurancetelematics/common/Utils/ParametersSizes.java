package com.affluencesystems.insurancetelematics.common.Utils;

public class ParametersSizes {

	/* packet Size */
	//header and tail
	public static final byte startCharatersize = 1;
	public static final byte HEADER = 3;
	public static final byte Vendor_id = 0;
	public static final byte Packet_type = 3;
	public static final byte checkSum = 8;
	public static final byte EndChar = 1;

	/* login parfameters */
	public static final byte IMEI = 15;
	public static final byte Device_name = 16;
	public static final byte FirmWare = 5;
	public static final byte protocol = 3;
//	public static final byte Latitude = 4;
	
	public static final byte Latitude_BEFORE_DECIMAL = 1;
	public static final byte Latitude_AFTER_DECIMAL = 3;

	public static final byte LONGITUDE_BEFORE_DECIMAL = 1;
	public static final byte LONGITUDE_AFTER_DECIMAL = 3;
	
	public static final byte Longitude = 4;
	public static final byte Error_type_DB = 2;
	public static final byte Error_type_NE = 1;
	public static final byte Error_type = 0;

	/* Aert messages */
	public static final byte Disconnect_from_main_battery = 1;
	public static final byte Low_battery = 4;
	public static final byte Low_battery_removed = 1;
	public static final byte Connect_back_to_main_battery = 1;
	public static final byte Ignition_ON = 1;
	public static final byte Ignition_OFF = 1;
	public static final byte GPS_box_opened = 1;
	public static final byte Over_the_air_parameter_change = 1;
	public static final byte config_param_validity = 16;
	public static final byte Source_ip = 15;
	public static final byte Source_mobile_number = 10;
	public static final byte Harsh_Braking = 4;
	public static final byte Harsh_Acceleration = 4;
	public static final byte Rash_Turning = 4;
	public static final byte Device_Tampered = 2;
	public static final byte Session_id = 8;
	public static final byte ALERT_TYPE = 1;
	public static final byte ALERT_SCALE = 1;

	public static final byte VehicleRegNo = 16;
	public static final byte GpsFix = 1;
	public static final byte DateAndTime = 4;
	public static final byte DateAndTimeForAlerts = 4;

	public static final byte SPEED = 2;

	public static final byte FRAME_FORMAT = 1;
	public static final byte HARDWARE_FORMAT = 1;
	public static final byte Mobile_country_code = 2;
	public static final byte Cell_id = 2;
	public static final byte Ignation = 1;
	public static final byte byteernal_battery_voltage = 4;
	public static final byte Emergency_status = 1;
	public static final byte Temper_alert = 1;
	public static final byte DIGITL_INPUT_AND_DIGITAL_OUTPUT = 1;
	public static final byte Gyro_sensor = 8;
	public static final byte Accelero = 8;
	public static final byte ALTITUDE = 4;
/*	public static final byte HEADING = 6;
	public static final byte Pdop = 4;
	public static final byte hdop = 4;*/
	public static final byte NetworkOperator = 16;
	public static final byte GSM_SIGNAL_STRENTGTH = 5;
	public static final byte M_N_C = 2;
	public static final byte M_C_C = 2;
	//public static final byte L_A_C = 2;


	public static final byte LOCATION_CODE = 2;
	public static final byte NMR = 30;
	public static final byte MainPowerStatus = 1;
	public static final byte MainInputVoltage = 2;
	public static final byte InternalBatteryVoltage = 2;
	public static final byte EMERGENCY_STATUS = 1;
	public static final byte Temper_Alert = 1;

	public static final byte MEMORY_PERCENTAGE = 1;
	public static final byte DATA_UPDATE_RATE_WHEN_IGNITION_ON = 2;
	public static final byte DATA_UPDATE_RATE_WHEN_IGNITION_OFF = 2;
	public static final byte ANALOGUE_IO_STATUS = 1;

	/* Emergency Packet */
	public static final byte GpsValid = 1;
	public static final byte Provider = 1;
	public static final byte REPLY_NUMBER = 8;
	public static final byte DISTANCE = 6;
	public static final byte PROVIDER = 1;
	/* Config */
	
	public static final byte CONFIG_PARPRIMARY_OR_SECONDARY_URLAM_VALIDITY = 16;
	public static final short URL = 128;
	public static final byte PRIMARY_OR_SECONDARY_URL = 30;

	public static final byte REGULATORY__PORT = 2;
	public static final byte CHANGE_NEW_APN = 16;
	public static final byte FTP_USER_NAME = 20;
	public static final byte FTP_PASSWORD = 10;
	public static final byte FTP_FileName = 20;
	public static final byte CAN_FUEL_TYPE = 1;



	/*public static final byte SLEEP_TIME = 1;
	public static final byte OVER_SPEED_LIMIT_THRESHOLD = 6;
	public static final byte HARSH_BRAKING_THRESHOLD = 4;
	public static final byte HARSH_ACCLERATION_THRESHOLD = 4;
	public static final byte RASH_TURNING_THRESHOLD = 4;
	public static final byte EMERGENCY_CONTROLE_NUMBER = 10;
	public static final byte DATA_TRANSMISSION = 1;
	public static final byte TIME_DURATION_FOR_EMERGENCY_STATE = 4;
	public static final byte DEVICE_RESET_CODE = 1;
	public static final byte REGULATORY_CONTROLE_NUMBER = 10;
	public static final byte CONFIG_DATA_TRANSMISSION_FREQUENCY = 2;
	public static final short CONFIG_IOBUFFER_ALLOCATION = 833;
	public static final short FirmWareUrl = 128;*/
	
	
	public static final byte FRAMENUMBER = 5;
	public static final byte MILLAGE=2;
	public static final byte LITER_PER_HOUR=2;
	public static final byte CAN_FUEL_CONSUMED=4;
	public static final byte NO_OF_GPS=1;

	
	//bits
	//public static final byte NO_OF_GPS=1;
	public static final byte NO_OF_HEADERS=1;



	/* CAN parameters */
	public static final byte Pid_4 = 2;
	public static final byte Pid_5 = 2;
	public static final byte Pid_11 = 3;
	public static final byte Pid_12 = 4;
	public static final byte Pid_13 = 1;
	public static final byte Pid_15 = 3;
	public static final byte Pid_16 = 6;
	public static final byte Pid_17 = 2;
	public static final byte Pid_28 = 2;
	public static final byte Pid_31 = 2;
	public static final byte Pid_33 = 2;
	public static final byte CAN_FLAG=1;
	//public static final byte Pid_47 = 6;
	public static final byte Pid_49 = 2;
	public static final byte Pid_51 = 1;
//	public static final byte Pid_66 = 6;
	public static final byte Pid_77 = 2;
	public static final byte Pid_78 = 2;
	public static final byte Pid_81 = 1;
	public static final byte Pid_94 = 4;
	public static final byte Pid_0 = 1;
	public static final byte VIN=18;

	
	//bits
	public static final byte pid_1_1 = 7;
	public static final byte pid_1_2 = 1;


	//public static final byte Request_trouble_code = 8;
	//public static final byte Error_code = 8;

	public static byte count = 0;

	//geoFencing data
	public static final byte GEOFENCING_ID=2;
	public static final byte GEOFENCE_RADIUS=4;
	public static final byte PAGE_NUMBER=1;
	public static final byte NUMBER_FENCES=2;
	public static final short GEOFENCE_LIMIT=512;
	public static final byte FileSize=2;
	public static final byte Fence_TYPE=1;
	public static final byte DATA_SIZE=2;
	public static final byte MAGIC_BYTE_SIZE=12;
	public static final byte RADIUS=4;

	public static final byte RESERVED_BYTE=12;

public static final byte GEOFENCE_Latitude = 5;
	
	public static final byte GEOFENCE_Longitude = 6;
	
	
	
	
	
	public static final byte VEHICLE_STATUS_DATA=2;
	public static final byte CAN_DETAILS=1;
	public static final byte GPS_HEADER_DETAILS=4;
	public static final byte DateAndTime_with_sec = 4;
	
	public static final byte GPS_DETAILS=4;
	public static final byte sec_And_Directions=1;


	
	
	//date and Time bits
	public static final byte DATE=5;
	
	public static final byte MONTH=4;
	
	public static final byte YEAR=7;
	
	public static final byte HOURS=5;
	
	public static final byte MINUTES=6;
	
	public static final byte SEC=6;
	
	//bits
	public static final byte NO_OF_SATILITES = 5;

	public static final byte Latitude_dir = 1;
	
	public static final byte Longitude_dir = 1;
	
	public static final byte Packet_Status = 1;

public static final byte NUMBER_OF_ALERTS=1;


public static final byte HEADER_TAIL=16;


//packet sizes
public static final byte LOGINRESPONSE_SUCESS_SIZES=36;
public static final byte LOGINRESPONSE_SUCESS_Message=20;

public static final short CONFIG_SIZE=446;
public static final short CONFIG_LIMIT=512;


	
	}
