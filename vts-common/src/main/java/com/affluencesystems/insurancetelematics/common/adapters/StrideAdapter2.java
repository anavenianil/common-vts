package com.affluencesystems.insurancetelematics.common.adapters;

import android.app.Dialog;
import android.app.DownloadManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.affluencesystems.insurancetelematics.R;
import com.affluencesystems.insurancetelematics.common.Activitys.StridePathActivity;
import com.affluencesystems.insurancetelematics.common.Activitys.TripCreationActivity;
import com.affluencesystems.insurancetelematics.common.ApiUtils.Constants;
import com.affluencesystems.insurancetelematics.common.Models.GpsData;
import com.affluencesystems.insurancetelematics.common.Models.Map_history;
import com.affluencesystems.insurancetelematics.common.Models.Stride;
import com.affluencesystems.insurancetelematics.common.Models.StrideDetails;
import com.affluencesystems.insurancetelematics.common.Models.StrideGpsData;
import com.affluencesystems.insurancetelematics.common.Models.Trip;
import com.affluencesystems.insurancetelematics.common.Utils.DeviceCapturingDateExtractionNew;
import com.affluencesystems.insurancetelematics.common.Utils.LiveConstants;
import com.affluencesystems.insurancetelematics.common.Utils.PreferenceUtils;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.DOWNLOAD_SERVICE;
import static com.affluencesystems.insurancetelematics.common.Activitys.TripCreationActivity.start_from;
import static com.affluencesystems.insurancetelematics.common.Utils.ConnectivityReceiver.isConnected;
import static com.affluencesystems.insurancetelematics.common.Utils.MapUtils.LatLngFilter;
import static com.affluencesystems.insurancetelematics.common.Utils.MapUtils.getDistanceInMeters;

public class StrideAdapter2 extends RecyclerView.Adapter<StrideAdapter2.ViewHolder> {

    private String TAG = "StridesAdapter";
    private ArrayList<Stride> strides;
    private Context context;
    public static ArrayList<Map_history> map_history_current;
    private boolean isForTrip = false;
    private Dialog dialog;

    private TextView text_adress, text_start, text_yes, text_no, text_yes_title, text_no_title;
    private EditText et_tilte;
    private PreferenceUtils preferenceUtils;
    private Trip responseTrip;
    private ProgressDialog progressDialog;
    private DecimalFormat df2 = new DecimalFormat("#.##");
    private int applicationKey = -1;
    private ProgressDialog progressDoalog;
    private long downloadID;
    private String fileName = "";
    private ArrayList<GpsData> allGpsData;
    public static ArrayList<LatLng> latLngs;
    public static ArrayList<Float> fuel_consumption_array_list;
    private LatLng zeroLatLng = new LatLng(0, 0);
    private ExtractFile task;
    private boolean isClicked = false;


    /*
     *       constructor of adapter for getting strides.
     * */
    public StrideAdapter2(Context context, ArrayList<Stride> strides, boolean isForTrip, int applicationKey) {
        this.strides = strides;
        this.context = context;
        this.isForTrip = isForTrip;
        this.applicationKey = applicationKey;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.stride_list_single, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        /*
         *       get all the strides latlngs and based on the first and last position set time, from address, to address.
         * */
        try {
            viewHolder.time.setText(timeAndDateFormat(strides.get(position).getStartDate()) + " - " + timeAndDateFormat(strides.get(position).getEndDate()));
            viewHolder.from_address.setText(getAddress(strides.get(position).getStartLatitude(), strides.get(position).getStartlongitude()));
            viewHolder.to_aadress.setText(getAddress(strides.get(position).getLatitude(), strides.get(position).getLongitude()));
            viewHolder.tv_mileage.setText(context.getString(R.string.mileage) + /*" " +*/ strides.get(position).getMillage() + "km/l");
            viewHolder.tv_distance.setText(context.getString(R.string.distance) + /*" " + */df2.format(Double.parseDouble(strides.get(position).getDistanceSinceStrideON())) + "km");
            viewHolder.tv_time_hour.setText(context.getString(R.string.time) + /*" " +*/ df2.format(Double.parseDouble(strides.get(position).getTravelledTime())) + "min");
        } catch (Exception e) {
            e.printStackTrace();
        }


        viewHolder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                /*
                 *       Two screens using only this adapter
                 *       1. In StridesActivity for showing vehicle animation.
                 *           we can start and end the trip here.
                 *       2. In Stride_path_activity for creating trip.
                 *           getting all the latlngs and animate vehicle.
                 * */
                if (!isForTrip) {
//                    getStrideData(position);
                    if (!isClicked) {
                        progressDoalog = new ProgressDialog(context);
                        progressDoalog.setCancelable(false);
                        progressDoalog.setMessage(context.getResources().getString(R.string.loading));
                        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDoalog.show();
                        checkFileExistence(strides.get(position).getPrimaryKey());
                        isClicked = true;
                    }
                } else {
                    if (!preferenceUtils.getbooleanFromPreference(PreferenceUtils.IS_START)) {
                        text_adress.setText(getAddress(strides.get(position).getStartLatitude(), strides.get(position).getStartlongitude()));
                    } else {
                        text_adress.setText(getAddress(strides.get(position).getLatitude(), strides.get(position).getLongitude()));
                    }

                    dialog.show();
                    if (preferenceUtils.getbooleanFromPreference(PreferenceUtils.IS_START)) {
                        text_start.setText(context.getString(R.string.trip_end_text));
                        et_tilte.setVisibility(View.GONE);
                    } else {
                        text_start.setText(context.getString(R.string.trip_start_text));
                        et_tilte.setVisibility(View.VISIBLE);
                    }
                    text_yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (isConnected()) {
                                if (!preferenceUtils.getbooleanFromPreference(PreferenceUtils.IS_START)) {

                                    if (!et_tilte.getText().toString().equals("")) {

                                        progressDialog = new ProgressDialog(context);
                                        progressDialog.setCancelable(false);
                                        progressDialog.setMessage(context.getString(R.string.please_wait));
                                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                        progressDialog.show();

                                        saveTrip(position);
                                    } else {
                                        Toast.makeText(context, context.getString(R.string.enter_title), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    progressDialog = new ProgressDialog(context);
                                    progressDialog.setCancelable(false);
                                    progressDialog.setMessage(context.getString(R.string.please_wait));
                                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                    progressDialog.show();

                                    updateTrip(position);
                                }
                            }
                        }
                    });


                    text_no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (et_tilte.getText().toString().equals("")) {
                                text_adress.setVisibility(View.VISIBLE);
                                et_tilte.setVisibility(View.GONE);
                            }
                            dialog.dismiss();
                        }
                    });
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return strides.size();
    }

    public void changeClickEvent() {
        isClicked = false;
    }

    /*
     *       To get address from taking latlng.
     * */
    private String getAddress(String lat, String lng) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());
        String address = "Address loading here";

        try {
            addresses = geocoder.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lng), 1);

            if (addresses.size() > 0) {
//            String address = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
//            String country = addresses.get(0).getCountryName();
//            String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
                return knownName + ", " + city + ", " + state;
            } else {
                return address;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }


    private String timeAndDateFormat(String dateString) {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);
        Date date = null;
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat("dd-MM-yyyy HH:mm a", Locale.US).format(date);
    }


    /*
     *       Change the date and time format for display.
     * */
   /* public String timeAndDateFormat(String dateandtime) {
        SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        Date date = null;
        try {
            date = form.parse(dateandtime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat postFormater = new SimpleDateFormat("MM-dd-yyyy hh:mm a");
        String newDateStr = postFormater.format(date);
        return newDateStr;
    }*/


    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout card;
        private TextView time, /*date,*/
                from_address, to_aadress, tv_mileage, tv_distance, tv_time_hour;

        public ViewHolder(View view) {
            super(view);
            time = (TextView) view.findViewById(R.id.time);
            tv_mileage = (TextView) view.findViewById(R.id.tv_mileage);
            tv_distance = (TextView) view.findViewById(R.id.tv_distance);
            tv_time_hour = (TextView) view.findViewById(R.id.tv_time_hour);
//            date = (TextView) view.findViewById(R.id.date);
            from_address = (TextView) view.findViewById(R.id.from_address);
            to_aadress = (TextView) view.findViewById(R.id.to_aadress);
            card = view.findViewById(R.id.card);

            init();
        }
    }


    /*
     *       initialize all the views.
     * */
    private void init() {
        preferenceUtils = new PreferenceUtils(context);
        dialog = new Dialog(context, R.style.CustomDialog);
        dialog.setContentView(R.layout.activity_stridedialog);
        text_adress = (TextView) dialog.findViewById(R.id.address);
        text_start = (TextView) dialog.findViewById(R.id.Stridestart_Dialog);
        text_yes = (TextView) dialog.findViewById(R.id.yes);
        text_no = (TextView) dialog.findViewById(R.id.no);
        et_tilte = (EditText) dialog.findViewById(R.id.edit_title);
    }


    /*
     *       Api hit for save trip based on imei number.
     *           trip status always "open" when trip start.
     * */
    private void saveTrip(final int position) {
//        Log.d(TAG,"start_trip date ==> " + date);
        Trip trip = null;
        if (applicationKey == LiveConstants.PERSONAL_APP) {
            trip = new Trip(et_tilte.getText().toString(), strides.get(position).getStartDate(), "", "open", preferenceUtils.getStringFromPreference(PreferenceUtils.IMEI_NUMBER, ""));
        } else if (applicationKey == LiveConstants.DRIVER_APP) {
            trip = new Trip(et_tilte.getText().toString(), "", "", "open", preferenceUtils.getStringFromPreference(PreferenceUtils.IMEI_NUMBER, ""));       //should modify for driver app
        } else {
            Toast.makeText(context, R.string.something_wrong, Toast.LENGTH_LONG).show();
            return;
        }

        Call<JsonElement> callRetrofit = null;
        callRetrofit = Constants.service.saveTripDetails(trip);

        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressDialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {

                    start_from.setText(context.getString(R.string.end_here));                                             //     commented for common code.
                    text_start.setText(context.getString(R.string.trip_end_text));

                    preferenceUtils.saveString(PreferenceUtils.RESPONSE_TRIP, response.body().toString());
                    Toast.makeText(context, context.getString(R.string.trip_added), Toast.LENGTH_LONG).show();
                    preferenceUtils.saveBoolean(PreferenceUtils.IS_START, true);
                    showNotification();
                    preferenceUtils.saveString(PreferenceUtils.TRIP_START_DATE, strides.get(position).getStartDate());
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, "Trip add failed", Toast.LENGTH_LONG).show();
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
            }
        });
    }

    /*
     *       Api hit for end/update trip based on imei number.
     *           trip status always "close" when trip ends.
     * */
    private void updateTrip(int position) {
        JSONObject j = null;
        try {

            if (applicationKey == LiveConstants.PERSONAL_APP) {
                j = new JSONObject(preferenceUtils.getStringFromPreference(PreferenceUtils.RESPONSE_TRIP, ""));
                responseTrip = new Trip(j.getString("tripName"), j.getString("tripStartDate"), strides.get(position).getEndDate(), "close", j.getString("imeiNumber"));
            } else if (applicationKey == LiveConstants.DRIVER_APP) {                                                                                                                     //      should modify for driver app
                j = new JSONObject(preferenceUtils.getStringFromPreference(PreferenceUtils.RESPONSE_TRIP, ""));
                responseTrip = new Trip(j.getString("tripName"), j.getString("tripStartDate"), "", "close", j.getString("imeiNumber"));
            } else {
                Toast.makeText(context, R.string.something_wrong, Toast.LENGTH_LONG).show();
                return;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

//        Log.d(TAG,"end date ==> " + date);

        Call<JsonElement> callRetrofit = null;
        callRetrofit = Constants.service.updateTrip(responseTrip);

        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    Toast.makeText(context, context.getString(R.string.trip_end_msg), Toast.LENGTH_LONG).show();
                    preferenceUtils.saveBoolean(PreferenceUtils.IS_START, false);
                    start_from.setText(context.getString(R.string.start_here));                                    //     commented for common code.
                    text_start.setText(context.getString(R.string.trip_start_text));
                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    if (notificationManager != null)
                        notificationManager.cancel(LiveConstants.TRIP_NOTIFICATION_ID);
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, context.getString(R.string.failed_to_connect_server), Toast.LENGTH_LONG).show();
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
            }
        });
    }


    /*
     *       showing the notification when the trip is started.
     *       Here we can directly end the trip from the notification.
     * */
    public void showNotification() {
        String channelId = "channel-01";
        String channelName = "Channel Name";

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.customnotification);
        Intent intent = new Intent(context, TripCreationActivity.class);                                        // changed from homeactivitynew to stridestartactivity for common code.
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent endTrip = new Intent(context, TripCreationActivity.EndTripNotification.class);
        endTrip.setAction("Download_Cancelled");
        endTrip.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_message_black_24dp)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContent(remoteViews)
                .setOngoing(true)
                .setAutoCancel(false);

        PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(context, 0, endTrip, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.end, pendingSwitchIntent);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);

        assert notificationManager != null;
        notificationManager.notify(LiveConstants.TRIP_NOTIFICATION_ID, mBuilder.build());
    }

    private void getStrideData(int position) {
        progressDoalog = new ProgressDialog(context);
        progressDoalog.setCancelable(false);
        progressDoalog.setMessage(context.getResources().getString(R.string.loading));
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.show();
        Call<StrideGpsData> callRetrofit = null;
        callRetrofit = Constants.service.getStrideGpsData(strides.get(position).getPrimaryKey());
        callRetrofit.enqueue(new Callback<StrideGpsData>() {
            @Override
            public void onResponse(Call<StrideGpsData> call, Response<StrideGpsData> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        DeviceCapturingDateExtractionNew deviceCapturingDateExtraction = new DeviceCapturingDateExtractionNew();
                        String strideData = response.body().getStride();
                        String historyData = response.body().getHistory();
                        ArrayList<StrideDetails> strides = deviceCapturingDateExtraction.constructNormalPacket(strideData);
                        ArrayList<StrideDetails> history = deviceCapturingDateExtraction.constructNormalPacket(historyData);

                        ArrayList<GpsData> allGpsData = new ArrayList<>();
                        for (StrideDetails s : strides) {
                            allGpsData.addAll(s.getGpsData());
                        }
                        for (StrideDetails s : history) {
                            allGpsData.addAll(s.getGpsData());
                        }

                        Log.d(TAG, "stride size ==> " + strides.size());
                        Log.d(TAG, "history size ==> " + history.size());

                        Collections.sort(allGpsData);

                        Log.d(TAG, "all size ==> " + allGpsData.size());

                        ArrayList<LatLng> latLngs = new ArrayList<>();
                        for (GpsData g : allGpsData) {
                            LatLng latLng = new LatLng(g.getLatitude(), g.getLongitude());
                            Log.d(TAG, "lat lngs ==> " + latLng);
                            latLngs.add(latLng);
                        }

                        progressDoalog.dismiss();

                        Intent intent = new Intent(context, StridePathActivity.class);
                        intent.putExtra(LiveConstants.STRIDE_PATH_LATLNGS, latLngs);
                        context.startActivity(intent);
                    }
                } else {
                    progressDoalog.dismiss();
                    Toast.makeText(context, context.getString(R.string.no_data), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<StrideGpsData> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(context, context.getString(R.string.failed_to_connect_server), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void checkFileExistence(String fileName) {
        File file = new File(Environment.getExternalStorageDirectory() + "/PersonalApp/Strides/" + fileName + ".txt");
        if (file.exists()) {
            String arr[] = {fileName};
            task = new ExtractFile();
            task.execute(arr);

            Log.d(TAG, "File Already Exist...");
        } else {
//            progressDoalog = new ProgressDialog(context);
//            progressDoalog.setCancelable(false);
//            progressDoalog.setMessage(context.getResources().getString(R.string.loading));
//            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progressDoalog.show();
            boolean isDownloaded = downloadStrideFile("http://intellitrack.in:3344/stridedata/getstridedocumentsfile/" + fileName, fileName);
            this.fileName = fileName;
            context.registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        }
    }

    private void unRegisterReceiver() {
        context.unregisterReceiver(onDownloadComplete);
    }


  /*  private boolean downloadStrideFile(String DownloadUrl, String fileName) {
        boolean isDownloaded = false;

//        String DownloadUrl = "http://intellitrack.in:3344/stridedata/getDocumentsFile";

//        Download_Uri = Uri.parse("http://www.gadgetsaint.com/wp-content/uploads/2016/11/cropped-web_hi_res_512.png");

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(DownloadUrl));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);
        request.setTitle("GadgetSaint Downloading " + "Sample" + ".png");
        request.setDescription("Downloading " + "Sample" + ".png");
        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/GadgetSaint/"  + "/" + "Sample" + ".png");


        downloadID = downloadManager.enqueue(request);
        return isDownloaded;
    }*/


    private boolean downloadStrideFile(String DownloadUrl, String fileName) {
        boolean isDownloaded = false;
//        String DownloadUrl = "http://intellitrack.in:3344/stridedata/getDocumentsFile";
        DownloadManager.Request request1 = new DownloadManager.Request(Uri.parse(DownloadUrl));
        request1.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request1.setAllowedOverRoaming(false);
        request1.setVisibleInDownloadsUi(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request1.allowScanningByMediaScanner();
            request1.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        }
        request1.setDestinationInExternalPublicDir("PersonalApp/Strides", fileName + ".txt");
        DownloadManager manager1 = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
//        Objects.requireNonNull(manager1).enqueue(request1);
        downloadID = manager1.enqueue(request1);
        if (DownloadManager.STATUS_SUCCESSFUL == 8) {
            isDownloaded = true;
//            Toast.makeText(context, "download success ", Toast.LENGTH_LONG).show();
        }
        return isDownloaded;
    }

    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            Log.d(TAG, "downloadId ==> " + downloadID);
            if (downloadID == id) {
//                progressDoalog.dismiss();
                String arr[] = {fileName};
                task = new ExtractFile();
                task.execute(arr);
                Log.d(TAG, "File Downloaded...");
                unRegisterReceiver();
            }
        }
    };

    private void convertNormalPacket(String fileName) {
//        if (allGpsData == null)
        allGpsData = new ArrayList<>();
        DeviceCapturingDateExtractionNew deviceCapturingDateExtraction = new DeviceCapturingDateExtractionNew();
        ArrayList<StrideDetails> strides = deviceCapturingDateExtraction.constructNormalPacket(Environment.getExternalStorageDirectory() + "/PersonalApp/Strides/" + fileName + ".txt");
        for (StrideDetails s : strides) {
            allGpsData.addAll(s.getGpsData());
        }
    }

    private void openStridePathActivity() {
        latLngs = new ArrayList<>();
        fuel_consumption_array_list = new ArrayList<>();

        ArrayList<LatLng> baseLatLngs = new ArrayList<>();
        ArrayList<GpsData> newGpsData = filterLatLngs();
        Log.d(TAG, "newGpsData --> " + newGpsData.size());
        for (GpsData g : newGpsData) {
            LatLng latLng = new LatLng(g.getLatitude(), g.getLongitude());
            Log.d(TAG, " After lat lngs ==> " + latLng);
            baseLatLngs.add(latLng);
        }
        latLngs = LatLngFilter(baseLatLngs, 2);
//        latLngs = baseLatLngs;
//        for (int i = 0; i < latLngs.size(); i++){
//            final int min = 0;
//            final int max = 40;
//            float random = new Random().nextInt((max - min) + 1) + min;
//            Log.d(TAG,"Random Value ==> " + random);
//            fuel_consumption_array_list.add(random);
//        }

        if (progressDoalog != null)
            progressDoalog.dismiss();
        Intent intent = new Intent(context, StridePathActivity.class);
        intent.putExtra(LiveConstants.IS_FROM_STRIDE, true);
        context.startActivity(intent);
    }

    private ArrayList<GpsData> filterLatLngs() {
        ArrayList<GpsData> newGpsData = new ArrayList<>();
        Log.d(TAG, "Array  size is before " + allGpsData.size());
        Log.d(TAG, "Array  size is total before " + newGpsData.size());
        for (int i = 1; i < allGpsData.size(); i++) {
            for (int j = i; j < allGpsData.size(); j++) {
                if (allGpsData.get(i - 1).getLatitude() - allGpsData.get(j).getLatitude() < 1 && allGpsData.get(i - 1).getLongitude() - allGpsData.get(j).getLongitude() < 1) {
//                    double distance = getDistanceInMeters(allGpsData.get(i - 1).getLatitude(), allGpsData.get(i - 1).getLongitude(), allGpsData.get(j).getLatitude(), allGpsData.get(j).getLatitude());
//                    Log.d(TAG,"i - 1 latlng ==> " + allGpsData.get(i - 1));
//                    Log.d(TAG,"j latlng ==> " + allGpsData.get(j));
//                    Log.d(TAG,"Distance ==> " + distance);
//                    if(distance >= 5) {
                    newGpsData.add(allGpsData.get(i - 1));
                    if (allGpsData.size() - 1 == i)
                        newGpsData.add(allGpsData.get(allGpsData.size() - 1));
                    i = j;
                    break;
//                    }
                }
            }
        }
        Log.d(TAG, "Array  size is after " + newGpsData.size());
        return newGpsData;

    }


    class ExtractFile extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            convertNormalPacket(strings[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            if(task != null && task.getStatus() != AsyncTask.Status.FINISHED) {
//                task.cancel(true);
//            }
            openStridePathActivity();
        }
    }


}
