package com.affluencesystems.insurancetelematics.common.Activitys;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.affluencesystems.insurancetelematics.R;
import com.affluencesystems.insurancetelematics.common.ApiUtils.Constants;
import com.affluencesystems.insurancetelematics.common.Models.AllStrides;
import com.affluencesystems.insurancetelematics.common.Models.Stride;
import com.affluencesystems.insurancetelematics.common.Models.Trip;
import com.affluencesystems.insurancetelematics.common.Utils.ConnectivityReceiver;
import com.affluencesystems.insurancetelematics.common.Utils.LiveConstants;
import com.affluencesystems.insurancetelematics.common.Utils.PreferenceUtils;
import com.affluencesystems.insurancetelematics.common.adapters.StrideAdapter2;
import com.affluencesystems.insurancetelematics.common.views.ShimmerRecyclerView;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.affluencesystems.insurancetelematics.common.Utils.ConnectivityReceiver.isConnected;

public class TripCreationActivity extends Base_activity implements LocationListener, View.OnClickListener {

    private static String TAG = "TripCreationActivity";
    public static ShimmerRecyclerView recyclerView;
    public static Dialog dialog;
    public EditText et_tilte;
    public static TextView start_from, text_adress, text_start, text_yes, text_no, text_yes_title, text_no_title;
    private TextView start_date, end_date;
    private TextView tv_ok;
    private ArrayList<Stride> strideDetails = new ArrayList<>();
    private ArrayList<AllStrides> objects;
    private TextView no_records;
    private SwipeRefreshLayout swipeToRefresh;
    private static Trip responseTrip;
    private DatePickerDialog.OnDateSetListener start, end;

    private StrideAdapter2 adapter;
    private static ProgressDialog progressDialog;
    private String date = "";
    private LocationManager locationManager;
    private String currentLocation = "";
    private String sendStart, sendEnd;

    private int applicationKey = -1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stridebutton);
        init();
        headerControls();

        Intent intent = getIntent();
        applicationKey = intent.getIntExtra(LiveConstants.APPLICATION_KEY, LiveConstants.DRIVER_APP);


        /*
         *       check the location permission enable.
         *       If not then call for location permission here.
         * */
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
        statusGpsCheck();
        getLocation();
        recyclerView.hideShimmerAdapter();

        /*
         *       Date picker listener to set start date
         * */
        start = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                Calendar myCalendar = Calendar.getInstance();
                view.setMaxDate(System.currentTimeMillis());
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                myCalendar.set(Calendar.HOUR, 0);
                myCalendar.set(Calendar.MINUTE, 0);
                myCalendar.set(Calendar.SECOND, 0);
                updateLabel(myCalendar, start_date, LiveConstants.START_DATE);
            }
        };


        /*
         *       Date picker listener to set end date
         * */
        end = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                Calendar myCalendar = Calendar.getInstance();
                view.setMaxDate(System.currentTimeMillis());
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(myCalendar, end_date, LiveConstants.END_DATE);
            }
        };

        start_date.setOnClickListener(this);
        end_date.setOnClickListener(this);


        /*
         *       click event to start and end trips.
         * */
        start_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                 *       Show the dialog for start/end trip.
                 *       In start trip time we can add title for the trip.
                 * */
                et_tilte.setText("");
                dialog.show();
                if (preferenceUtils.getbooleanFromPreference(PreferenceUtils.IS_START)) {
                    text_start.setText(R.string.trip_end_text);
                    et_tilte.setVisibility(View.GONE);
                } else {
                    text_start.setText(R.string.trip_start_text);
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
                                    progressDialog.setMessage(getString(R.string.please_wait));
                                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                    progressDialog.show();

                                    saveTrip();
                                } else {
                                    Toast.makeText(context, R.string.enter_title, Toast.LENGTH_LONG).show();
                                }
                            } else {
                                progressDialog = new ProgressDialog(context);
                                progressDialog.setCancelable(false);
                                progressDialog.setMessage(getString(R.string.please_wait));
                                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                progressDialog.show();

                                updateTrip();
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
        });


        /*
         *       When we swipe Call the service for get the data from server
         * */
        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isConnected()) {
                    getStrides();
                }
            }
        });

        if (!isConnected()) {
            boolean isConnected = ConnectivityReceiver.isConnected();
            noWifiDialog(isConnected);
        }

    }


    /*
     *       taking the date from the date picker and set it into edittext.
     * */
    private void updateLabel(Calendar myCalendar, TextView edittext, String key) {
        String myFormat = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        edittext.setText(sdf.format(myCalendar.getTime()));
        updateDate(myCalendar.getTime(), key);
    }


    /*
     *       taking the date from the date picker and set it into edittext.
     * */
    private void updateDate(Date date, String key) {
        String myFormat = "E MMM dd yyyy HH:mm:ss Z";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        if (LiveConstants.START_DATE.equals(key)) {
            date.setHours(0);
            date.setMinutes(0);
            date.setSeconds(0);
            sendStart = sdf.format(date.getTime());
            sendEnd = "";
            end_date.setText("");
        } else {
            date.setHours(23);
            date.setMinutes(59);
            date.setSeconds(59);
            sendEnd = sdf.format(date.getTime());
        }
        Log.d(TAG, "start_trip ===> " + sendStart + "  " + " End ===> " + sendEnd);
    }


    /*
     *       To initiate header views from xml file
     * */
    public void headerControls() {
        TextView header_text;
        ImageView back_button;
        header_text = (TextView) findViewById(R.id.header_text);
        no_records = (TextView) findViewById(R.id.no_records);
        back_button = (ImageView) findViewById(R.id.back_button);
        swipeToRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
        start_date = (TextView) findViewById(R.id.start_date);
        tv_ok = (TextView) findViewById(R.id.tv_ok);
        end_date = (TextView) findViewById(R.id.end_date);
//        recyclerView = (RecyclerView) findViewById(R.id.stride_view);
        header_text.setText(R.string.trip);
        header_text.setVisibility(View.VISIBLE);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_ok.setOnClickListener(this);

    }


    /*
     *       service call for get all strides from the server.
     *       And initialize adapter set to the recycler view
     * */
    public void getStrides() {
        recyclerView.showShimmerAdapter();
        Call<ArrayList<Stride>> callRetrofit = null;
        if (LiveConstants.PERSONAL_APP == applicationKey) {
            callRetrofit = Constants.service.getStridesNew(preferenceUtils.getStringFromPreference(PreferenceUtils.IMEI_NUMBER, ""), sendStart, sendEnd);
        } else if (LiveConstants.DRIVER_APP == applicationKey) {
            callRetrofit = Constants.service.getStridesForDriver(preferenceUtils.getStringFromPreference(PreferenceUtils.DRIVERSER_NO, ""), sendStart, sendEnd);
        } else {
            Toast.makeText(TripCreationActivity.this, R.string.something_wrong, Toast.LENGTH_LONG).show();
            return;
        }

        callRetrofit.enqueue(new Callback<ArrayList<Stride>>() {
            @Override
            public void onResponse(Call<ArrayList<Stride>> call, Response<ArrayList<Stride>> response) {
                swipeToRefresh.setRefreshing(false);
                recyclerView.hideShimmerAdapter();

                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().size() > 0) {
                        no_records.setVisibility(View.GONE);
                        adapter = new StrideAdapter2(TripCreationActivity.this, response.body(), true, LiveConstants.DRIVER_APP);
                        recyclerView.setAdapter(adapter);
                    } else {
                        no_records.setVisibility(View.VISIBLE);
                        recyclerView.hideShimmerAdapter();
                    }

//                if (response.isSuccessful()) {
//                    if (response.body() != null) {
//                        objects = new ArrayList<>();
//                        try {
//                            JSONArray j = new JSONArray(response.body().toString());
//                            for (int i = 0; i < j.length(); i++) {
//                                JSONObject j2 = j.getJSONObject(i);
//                                String distance = j2.getString("distanceTraveled");
//                                String time = j2.getString("timeForStride");
//                                JSONArray j3 = j2.getJSONArray("latlong");
//                                for (int k = 0; k < j3.length(); k++) {
//                                    if (!j3.getJSONObject(k).getString("latitude").equals("0.0") && !j3.getJSONObject(k).getString("longitude").equals("0.0")) {
////                                        Stride s = new Stride(j3.getJSONObject(k).getString("latitude"), j3.getJSONObject(k).getString("longitude"), j3.getJSONObject(k).getString("speed"), j3.getJSONObject(k).getString("heading"), j3.getJSONObject(k).getString("date"));
////                                        strideDetails.add(s);
////                                        s = null;
//                                    }
//                                }
//
//                                AllStrides all = new AllStrides(distance, time, strideDetails);
//                                objects.add(all);
//                                all = null;
//                            }
//                            if (objects.size() > 0) {
//                                no_records.setVisibility(View.GONE);
//                                adapter = new StridesAdapter(TripCreationActivity.this, objects, true, applicationKey);
//                                recyclerView.setAdapter(adapter);
//                            } else {
//                                no_records.setVisibility(View.VISIBLE);
//                                recyclerView.hideShimmerAdapter();
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    } else {
//                        no_records.setVisibility(View.VISIBLE);
//                        recyclerView.hideShimmerAdapter();
//                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Stride>> call, Throwable t) {
                swipeToRefresh.setRefreshing(false);
                recyclerView.hideShimmerAdapter();
                no_records.setVisibility(View.VISIBLE);
                recyclerView.hideShimmerAdapter();
                message(TripCreationActivity.this, getString(R.string.failed_to_connect_server));
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
            }
        });
    }


    /*
     *       To initiate all the view from xml file
     * */
    private void init() {
        dialog = new Dialog(TripCreationActivity.this, R.style.CustomDialog);

        dialog.setContentView(R.layout.activity_stridedialog);
        dialog.setCancelable(false);
        text_adress = (TextView) dialog.findViewById(R.id.address);
        text_start = (TextView) dialog.findViewById(R.id.Stridestart_Dialog);
        text_yes = (TextView) dialog.findViewById(R.id.yes);
        text_no = (TextView) dialog.findViewById(R.id.no);
        et_tilte = (EditText) dialog.findViewById(R.id.edit_title);
        start_from = findViewById(R.id.stride_start);
        recyclerView = (ShimmerRecyclerView) findViewById(R.id.stride_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (preferenceUtils.getbooleanFromPreference(PreferenceUtils.IS_START)) {
            start_from.setText(getString(R.string.end_here));
            text_start.setText(R.string.trip_end_text);
        } else {
            start_from.setText(R.string.start_here);
            text_start.setText(R.string.trip_start_text);
        }

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        date = df.format(c);
        Log.d(TAG, "Current date => " + date);
    }


    /*
     *       showing the notification when the trip is started.
     *       Here we can directly end the trip from the notification.
     * */
    public void showNotification() {
        String channelId = "channel-01";
        String channelName = "Channel Name";

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.customnotification);
        Intent intent = new Intent(this, TripCreationActivity.class);                                        // changed from homeactivitynew to stridestartactivity for common code.
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent endTrip = new Intent(context, EndTripNotification.class);
        endTrip.setAction("Download_Cancelled");
        endTrip.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_message_black_24dp)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContent(remoteViews)
                .setOngoing(true)
                .setAutoCancel(false);

        PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(this, 0, endTrip, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.end, pendingSwitchIntent);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);

        assert notificationManager != null;
        notificationManager.notify(LiveConstants.TRIP_NOTIFICATION_ID, mBuilder.build());
    }


    /*
     *       Service call for start trip.
     * */
    private void saveTrip() {
//        preferenceUtils.saveString(PreferenceUtils.TRIP_NAME, et_tilte.getText().toString());
        Trip trip = null;
        if (applicationKey == LiveConstants.PERSONAL_APP) {
            trip = new Trip(et_tilte.getText().toString(), "", "", "open", preferenceUtils.getStringFromPreference(PreferenceUtils.IMEI_NUMBER, ""));
        } else if(applicationKey == LiveConstants.DRIVER_APP) {
            trip = new Trip(et_tilte.getText().toString(), "", "", "open", preferenceUtils.getStringFromPreference(PreferenceUtils.IMEI_NUMBER, ""));       //should modify for driver app
        } else {
            progressDialog.dismiss();
            Toast.makeText(TripCreationActivity.this, R.string.something_wrong, Toast.LENGTH_LONG).show();
            return;
        }
        Call<JsonElement> callRetrofit = null;
        callRetrofit = Constants.service.saveTripDetails(trip);

        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressDialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {

                    try {
                        JSONObject j = new JSONObject(response.body().toString());
                        preferenceUtils.saveString(PreferenceUtils.RESPONSE_TRIP, response.body().toString());
                        showNotification();
                        preferenceUtils.saveString(PreferenceUtils.TRIP_START_DATE, j.getString("tripStartDate"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    start_from.setText(R.string.end_here);
                    text_start.setText(R.string.trip_end_text);

                    message(TripCreationActivity.this, getString(R.string.trip_added));
                    preferenceUtils.saveBoolean(PreferenceUtils.IS_START, true);

                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog.dismiss();
                //mapUtils.progressDialog_dismiss().dismiss();
                message(TripCreationActivity.this, getString(R.string.failed_to_connect_server));
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
            }
        });
    }


    /*
     *       check the gps on or off,
     *       if gps is on the get the current lat lngs.
     *       else ask for gps on.
     * */
    public void statusGpsCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else {
            getLocation();
        }
    }


    /*
     *       Dialog for asking location permission.
     * */
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.enable_gps)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 1000);
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                        finish();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    /*
     *       Service call for end trip.
     * */
    private void updateTrip() {
        JSONObject j = null;
        try {
            if (applicationKey == LiveConstants.PERSONAL_APP) {
                j = new JSONObject(preferenceUtils.getStringFromPreference(PreferenceUtils.RESPONSE_TRIP, ""));
                responseTrip = new Trip(j.getString("tripName"), j.getString("tripStartDate"), "", "close", j.getString("imeiNumber"));
            } else if(applicationKey == LiveConstants.DRIVER_APP) {                                             //      should modify for driver app
                j = new JSONObject(preferenceUtils.getStringFromPreference(PreferenceUtils.RESPONSE_TRIP, ""));
                responseTrip = new Trip(j.getString("tripName"), j.getString("tripStartDate"), "", "close", j.getString("imeiNumber"));
            } else {
                Toast.makeText(TripCreationActivity.this, R.string.something_wrong, Toast.LENGTH_LONG).show();
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Call<JsonElement> callRetrofit = null;
        callRetrofit = Constants.service.updateTrip(responseTrip);
        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    message(TripCreationActivity.this, getString(R.string.trip_end_msg));
                    start_from.setText(R.string.start_here);
                    text_start.setText(R.string.trip_start_text);
                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancel(LiveConstants.TRIP_NOTIFICATION_ID);
                    preferenceUtils.saveBoolean(PreferenceUtils.IS_START, false);
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog.dismiss();
                message(TripCreationActivity.this,getString(R.string.failed_to_connect_server));
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
            }
        });
    }

    /*
     *       if phone location enabled, then takes the current lat lngs.
     * */
    private void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }


    /*
     *       getting current lat lngs and convert into address and set on the textview
     * */
    @Override
    public void onLocationChanged(Location location) {
        currentLocation = getAddress(location.getLatitude(), location.getLongitude());
        text_adress.setText(currentLocation);
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }


    /*
     *       takes lat, lng and convert into address
     * */
    private String getAddress(double lat, double lng) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);

//            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
//            String country = addresses.get(0).getCountryName();
//            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            return knownName + ", " + city + ", " + state;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    /*
     *       if location permission granted call for the location and convert into address.
     * */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 101 && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            finish();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1000) {
//            To check location on/off
            statusGpsCheck();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.start_date) {
            Calendar myCalendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(TripCreationActivity.this, start, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();

        } else if (i == R.id.end_date) {
            if (!start_date.getText().toString().equals("")) {
                Calendar myCalendar1 = Calendar.getInstance();
                DatePickerDialog datePickerDialog1 = new DatePickerDialog(TripCreationActivity.this, end, myCalendar1.get(Calendar.YEAR), myCalendar1.get(Calendar.MONTH), myCalendar1.get(Calendar.DAY_OF_MONTH));
                String getfrom[] = start_date.getText().toString().split("-");
                int year, month, day;
                year = Integer.parseInt(getfrom[2]);
                month = Integer.parseInt(getfrom[1]) - 1;
                day = Integer.parseInt(getfrom[0]);
                final Calendar c = Calendar.getInstance();
                c.set(year, month, day);

                datePickerDialog1.getDatePicker().setMinDate(c.getTimeInMillis());
                datePickerDialog1.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog1.show();
            } else {
                message(TripCreationActivity.this, getString(R.string.select_start_date));
            }

        } else if (i == R.id.tv_ok) {
            if (start_date.getText().toString().equals("")) {
                message(TripCreationActivity.this, getString(R.string.select_start_date));
                return;
            } else if (end_date.getText().toString().equals("")) {
                message(TripCreationActivity.this, getString(R.string.select_end_date));
                return;
            } else if (isConnected()) {
                no_records.setVisibility(View.GONE);
                swipeToRefresh.setVisibility(View.VISIBLE);
                getStrides();
            }

        }
    }


    /*
     *       End Trip from the notification.
     * */
    public static class EndTripNotification extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Intent intent1 = new Intent(context, TripCreationActivity.class);
//            intent1.putExtra(LiveConstants.FROM_NOTIFICATION, true);
            Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            context.sendBroadcast(closeDialog);
            context.startActivity(intent1);
            Log.d(TAG, "Notification end called....");
        }
    }
}
