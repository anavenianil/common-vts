package com.affluencesystems.insurancetelematics.common.Utils;

public class StrideConstants {
	public static final String IMAGE_BASE_PATH = "http://intellitrack.in:1234/";

	public static final String CERTIFICATE_PATH = "sslcertificates/ssl.p12";
	public static final String CERTIFICATE_PASSWORD = "123456";
	public static final boolean SSL = false;
	public static final boolean EMERGENCY_SERVER = false;
	public static final int EMERGENCY_TCP_PORT = 4589;

	public static final String CERTIFICATE_PATH_CRT = "sslcertificates/ssl.crt";
	public static final String CERTIFICATE_PASSWORD_CRT = "123456";
	public static final String CERTIFICATE_TYPE = "PKCS12";

	public static final String CERTIFICATE_PATH_JAVA = "/usr/lib/jvm/jdk1.8.0_181/jre/lib/security/cacerts";
	public static final String CERTIFICATE_PATH_JAVA_PASSWORD = "changeit";

	public static final int TCP_PORT = 4589;
	public static final String PROTOCOL = "TCP";
	public static final String PROTOCOL_TCP = "TCP";
	public static final String PROTOCOL_UDP = "UDP";
	public static final boolean Normal_response = false;

	public static final byte[] Network_issue = { 1 };
	public static final byte[] DB_lookup_error = { 1 };
	public static final int SUCCESS = 1;

	public static final String LOGIN_REQUEST = "LRQ";
	public static final String LOGIN_RESPONSE = "LRP";
	public static final String LOGIN_RESPONSE_SUCESS="LRS";

	public static final String NORMAL_PACKET = "NOR";
	public static final String NORMAL_PACKET_RESPONSE = "NRP";

	public static final String HISTORT_File = "HISTORY.txt";

	public static final String Alert_packet = "ALR";
	public static final String Alert_packet_History = "ALH";

	// public static final String EMERGENCY_ALERT="TA";
	public static final String HEALTH_PACKET = "HP";
	public static final String GEOFENCE_PACKET = "GRQ";

	public static final String GEOFENCE_PACKET_RESPONSE = "GRP";
	public static final String DEBUG_LOG = "DBL";

	public static final String PACKET = "HP";

	public static final String EMERGENCY_PACKET = "EMR";
	public static final String CONFIG_REQUEST = "CRQ";
	public static final String CONFIG_RESPONSE = "CRP";
	public static final String HEADER_ALL = "EPB";
	public static final String GEOFENCE_RESPONSE = "GRP";

	public static final String STRIDE_STATUS_PROGRESS = "Progress";
	public static final String STRIDE_STATUS_END = "END";

	public static final String START_CHARATER = "$";

	public static final String End_CHARATER = "*";
	public static final String End_CHARATER_siginificance = "_";

	public static final byte[] Sucess = { 0 };
	public static final byte[] Fails = { 1 };

	public static final String FILED_SEPERATOR = "#";

	// Alerts Notification
	public final static String AUTH_KEY_FCM = "AAAAQTAueE0:APA91bFutYOeot-BrblP0UKnscYmdvim8Lbfqa6ZliLLqrfxWXTB-VqgU0R2b3dVW6V0dapRxVrQnc4rPhIF_ssjh9aHV4DHfup472hGLXa0SpfdVcLvQJgDQ8cj5XGrdZ-SO_BCF_0m";
	public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";

	public static final String ALERT_CHECK = "NA";
	public static final int LOGOUt = 30;

	/*
	 * LOW_BATTERY (BL) LOW_BATTERY_REMOVED(BH) CONNECT_BACK_TO_MAIN_BATTERY (BR)
	 * IGNITION_ON (IN) IGNITION_OFF (IF) GPS_BOX_OPENED (OPTIONAL) (TA)
	 * OVER_THE_AIR_PARAMETER_CHANGE (OTA)
	 *
	 * HARSH_BRAKING (HB) HARSH_ACCELERATION (HA) RASH_TURNING (RT) DEVICE_TAMPERED
	 * (TA)
	 */
	public static final byte DISCONNECT_FROM_MAIN_BATTERY =1;
	public static final byte LOW_BATTERY = 0;

	public static final byte LOW_BATTERY_REMOVED = 2;
	public static final byte CONNECT_BACK_TO_MAIN_BATTERY = 3;
	public static final byte IGNITION_ON = 4;
	public static final byte IGNITION_OFF = 5;
	public static final byte GPS_BOX_OPENED = 6;
	public static final byte OVER_THE_AIR_PARAMETER_CHANGE = 7;
	public static final byte HARSH_BRAKING = 8;
	public static final byte HARSH_ACCELERATION = 9;
	public static final byte RASH_TURNING = 10;
	/*	public static final String DEVICE_TAMPERED = "TA";
	 */	public static final byte GEO_FENCING = 11;
	public static final byte GEO_FENCING_INSIDE = 12;
	public static final byte OVER_SPEED = 13;
	public static final String MAGIC_BYTE = "AFF$GEOFENCE";
	public static final String TEMPER_Alerts = "TA";

	public static final String FIELD_REPLACER = "#";
	public static final String GROUP_ADMIN_SESSION = "group_admin_session";
	public static final String DRIVER_SESSION = "driver_session";
	public static final String Singe_USER_SESSION = "single_user_session";

	//types of devices
	public static final String LOW_COST_DEVICES="LOW COST";
	public static final String AUTO_GRAGE_DEVICE="autograde";
	public static final String ARAI_DEVICE="arai";

	public static final char LATITUDE_DIRECTION_NORTH='N';
	public static final char LATITUDE_DIRECTION_SOUTH='S';
	public static final char LONGITUDE_DIRECTION_EAST='E';
	public static final char LONGITUDE_DIRECTION_WEST='W';






	//config parameters
	public static final String REGULATORY_PRIMARY_URL="regulatoryPrimaryUrl";
	public static final String REGULATORY_SECONDARY_URL="regulatorySecondaryUrl";
	public static final String REGULATORY_PRIMARY_TCP_PORT="regulatoryPrimaryTcpPort";
	public static final String REGULATORY_PRIMARY_UDP_PORT="regulatoryPrimaryUdpPort";
	public static final String REGULATORY_SECONDARY_TCP_PORT="regulatorySecondaryTcpPort";
	public static final String REGULATORY_SECONDARY_UDP_PORT="regulatorySecondaryUdpPort";
	public static final String CHANGE_NEW_APN_PARAM="changeNewApn";

	public static final String SLEEP_TIME="sleepTime";
	public static final String DATA_TRANSMISSION_FREQUENCY_NORMAL="dataTransmissionFrequencyNormal";
	public static final String DATA_TRANSMISSION_FREQUENCY_IGNATION_ON="dataTransmissionFrequencyIgnationOn";
	public static final String DATA_TRANSMISSION_FREQUENCY_IGNATION_OFF="dataTransmissionFrequencyIgnationOff";
	public static final String DATA_TRANSMISSION_FREQUENCY_EMERGENCY_STATE="dataTransmissionFrequencyEmergencyState";
	public static final String TIME_DURATION_FOR_EMERGENCY_STATE="timeDurationForEmergencyState";
	public static final String CERTIFICATE_URL="certificateUrl";
	public static final String FIRMWARE_URL="firmwareUrl";
	public static final String CONFIG_DATA_TRANSMISSION_FREQUENCY="configDataTransmissionFrequency";
	public static final String GPS_GLOBAL_GPS_CONFIG_FLP_STAND_BY="GPSGlobalGPSConfigFLPStandBy";
	public static final String GPS_FIELDS_COLUMN_SIZE="GPSFieldsColumnSize";
	public static final String GPS_FIELDS_ROW_SIZE="GPSFieldsRowSize";
	public static final String GPS_CO_ORDINATE_NUMBER_OF_DIGITS_AFTER_DECIMAL="GPSCoOrdinateNumberOfDigitsAfterDecimal";
	public static final String GPS_CONFIG_MAXIMUM_EACH_FIELD_DATA_SIZE="GPSConfigMaximumEachFieldDataSize";
	public static final String GEOFENCE_EACH_FENCE_MAX_SIZE="geofenceEachFenceMaxSize";
	public static final String MAX_NO_RETRY_FOR_TCP_CONNCET="maxNoRetryForTcpConncet";
	public static final String MAX_NO_RETRY_FOR_STATUS="maxNoRetryForStatus";
	public static final String GSM_FTP_USER_NAME="GSMFTPUserName";
	public static final String GSM_FTP_PASSWORD="GSMFTPPassword";
	public static final String GSM_FILE_NAME="GSMFileName";
	public static final String MAX_NO_RETRY_FOR_DATA_CMD="maxNoRetryForDataCmd";
	public static final String MAX_NO_RETRY_FOR_TCP_CONFIG="maxNoRetryForTcpConfig";
	public static final String MAX_NO_RETRY_FOR_ARROW="maxNoRetryForArrow";
	public static final String MAX_NO_RETRY_FOR_SHUT="maxNoRetryForShut";
	public static final String MAX_NO_RETRY_FOR_GET_RESPONSE="maxNoRetryForGetResponse";
	public static final String MAX_NO_RETRY_FOR_FTP_CONFIG="maxNoRetryForFtpConfig";
	public static final String MAX_NO_RETRY_FOR_FTP_GET="maxNoRetryForFtpGet";
	public static final String CAN_PID_MAX_RETRY="CANPIDMaxRetry";
	public static final String CAN_PID_RESPONSE_THRESHOLD_TIME="CANPIDResponseThresholdTime";
	public static final String CAN_Fuel_Type="CANFuelType";
	public static final String CAN_ENGINE_DISPLACEMENT="CANEngineDisplacement";
	public static final String CAN_VOLUMETRIC_EFFICIENCY="CANVolumetricEfficiency";
	public static final String CAN_HOW_FREQUENTLY_TO_CALL_NON_FREQ_PID="CANHowFrequentlyToCallNonFreqPID";
	public static final String GPS_DEVICE_BATTERY_TIME_SLICE_VALUE="GPSDeviceBatteryTimeSliceValue";
	public static final String GEOFENCE="geoFence";


	//userdefined config parameters
	public static final String OVER_SPEEDLIMIT_THRESHOLD="overSpeedLimitThreshold";
	public static final String HARSH_BRAKING_THRESHOLD="harshBrakingThreshold";
	public static final String HARSH_ACCLERATION_THRESHOLD="harshAcclerationThreshold";
	public static final String RASH_TURNING_THRESHOLD="rashTurningThreshold";

}
