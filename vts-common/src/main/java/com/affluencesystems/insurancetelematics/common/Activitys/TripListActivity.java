package com.affluencesystems.insurancetelematics.common.Activitys;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.affluencesystems.insurancetelematics.common.Models.TripForList;
import com.affluencesystems.insurancetelematics.R;
import com.affluencesystems.insurancetelematics.common.Utils.LiveConstants;
import com.affluencesystems.insurancetelematics.common.Utils.PreferenceUtils;
import com.affluencesystems.insurancetelematics.common.adapters.TripListAdapter;
import com.affluencesystems.insurancetelematics.common.ApiUtils.Constants;
import com.affluencesystems.insurancetelematics.common.views.ShimmerRecyclerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.affluencesystems.insurancetelematics.common.Utils.ConnectivityReceiver.isConnected;

public class TripListActivity extends Base_activity implements View.OnClickListener/*, DatePickerDialog.OnDateSetListener*/ {

    private String TAG = "TripListActivity";
    private ShimmerRecyclerView recyclerView;
    private TextView no_docs, tv_ok;
    private TripListAdapter adapter;
    private SwipeRefreshLayout swipeToRefresh;
    private TextView start_date, end_date;
    PreferenceUtils preferenceUtils;
    private DatePickerDialog.OnDateSetListener start, end;
    /*
     *       The format of dates for sending to server are different from showing in edittext.
     * */
    private String sendStart, sendEnd;
    private Context context;

    private int applicationKey = -1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);
        context = this;
        preferenceUtils = new PreferenceUtils(this);
        preferenceUtils.saveString(PreferenceUtils.IMEI_NUMBER,getIntent().getStringExtra("IMEI"));
        headerControls();
        permissionsForStorage();

        Intent intent = getIntent();
        applicationKey = intent.getIntExtra(LiveConstants.APPLICATION_KEY, -1);

        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isConnected())
                    getTrips();
            }
        });

        recyclerView.hideShimmerAdapter();

        tv_ok.setOnClickListener(this);
    }


    /*
     *       To initiate all the view from xml file
     * */
    public void headerControls() {
        TextView header_text;
        ImageView back_button;
        header_text = (TextView) findViewById(R.id.header_text);
        no_docs = (TextView) findViewById(R.id.no_docs);
        tv_ok = (TextView) findViewById(R.id.tv_ok);
        back_button = (ImageView) findViewById(R.id.back_button);
        start_date = (TextView) findViewById(R.id.start_date);
        end_date = (TextView) findViewById(R.id.end_date);
        recyclerView = (ShimmerRecyclerView) findViewById(R.id.trip_recycler_view);
        swipeToRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);

        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llm);
        header_text.setText(getResources().getString(R.string.trip_list));
        header_text.setVisibility(View.VISIBLE);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        /*
         *       Date picker Dialog listener for start date
         * */
        start = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                view.setMaxDate(System.currentTimeMillis());
                Calendar myCalendar = Calendar.getInstance();
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(myCalendar, start_date, LiveConstants.START_DATE);
            }
        };



        /*
         *       Date picker Dialog listener for end date
         * */
        end = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                view.setMaxDate(System.currentTimeMillis());
                Calendar myCalendar = Calendar.getInstance();
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);;
                updateLabel(myCalendar, end_date, LiveConstants.END_DATE);
            }
        };

        start_date.setOnClickListener(this);
        end_date.setOnClickListener(this);
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
           /* if (isConnected()) {
                no_docs.setVisibility(View.GONE);
                swipeToRefresh.setVisibility(View.VISIBLE);
                getTrips();
            }*/
        }
        Log.d(TAG, "start_trip ===> " + sendStart + "  " + " End ===> " + sendEnd);
    }

    public String dateFormat(String dateString) {
        System.out.println("Given date is " + dateString);
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        Date date = null;
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(date != null)
            return new SimpleDateFormat("E MMM dd yyyy HH:mm:ss Z", Locale.US).format(date);
        else
            return "";
    }


    /*
     *       To get all the trips based on imei number and start date and end date.
     * */
    private void getTrips() {
        recyclerView.showShimmerAdapter();
        Call<ArrayList<TripForList>> callRetrofit = null;

        if (LiveConstants.PERSONAL_APP == applicationKey) {
            callRetrofit = Constants.service.getTrips(preferenceUtils.getStringFromPreference(PreferenceUtils.IMEI_NUMBER, ""),
                    sendStart/*"Fri Feb 01 2019 11:10:42 GMT+0530"*/, sendEnd/*"Tue Mar 19 2019 11:10:42 GMT+0530"*/);
        } else if (LiveConstants.DRIVER_APP == applicationKey) {
            callRetrofit = Constants.service.getTripsForDriver(preferenceUtils.getStringFromPreference(PreferenceUtils.DRIVERSER_NO,
                    ""), sendStart, sendEnd);
        } else {
            Toast.makeText(TripListActivity.this, R.string.something_wrong, Toast.LENGTH_LONG).show();
            return;
        }

        callRetrofit.enqueue(new Callback<ArrayList<TripForList>>() {
            @Override
            public void onResponse(Call<ArrayList<TripForList>> call, Response<ArrayList<TripForList>> response) {
                swipeToRefresh.setRefreshing(false);
                recyclerView.hideShimmerAdapter();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        ArrayList<TripForList> trips = response.body();

                        if (trips.size() > 0) {
                            no_docs.setVisibility(View.GONE);
                        } else {
                            no_docs.setVisibility(View.VISIBLE);
                        }
                        adapter = new TripListAdapter(context, trips);
                        recyclerView.setAdapter(adapter);
                    } else {
                        no_docs.setVisibility(View.VISIBLE);
                    }


                } else {
                    no_docs.setVisibility(View.VISIBLE);
                    message((Activity) context, getString(R.string.no_response));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<TripForList>> call, Throwable t) {
                swipeToRefresh.setRefreshing(false);
                recyclerView.hideShimmerAdapter();
                no_docs.setVisibility(View.VISIBLE);
                message((Activity) context, getString(R.string.failed_to_connect_server));
                Log.d(TAG, "Error Call " + call.toString());
                Log.d(TAG, "Error" + t.toString());
            }
        });
    }


    /*
    *       click events.
    * */
    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.start_date) {/*
         *       blocking future dates
         * */
            Calendar myCalendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(TripListActivity.this, start, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();

        } else if (i == R.id.end_date) {/*
         *       blocking future dates and before start date
         * */
            if (!start_date.getText().toString().equals("")) {
                Calendar myCalendar1 = Calendar.getInstance();
                DatePickerDialog datePickerDialog1 = new DatePickerDialog(TripListActivity.this, end, myCalendar1.get(Calendar.YEAR), myCalendar1.get(Calendar.MONTH), myCalendar1.get(Calendar.DAY_OF_MONTH));
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
                message(TripListActivity.this, getString(R.string.select_start_date));
            }


        } else if (i == R.id.tv_ok) {/*
         *       validating all the fields and send server request for strides.
         * */
            if (start_date.getText().toString().equals("")) {
                message(TripListActivity.this, getString(R.string.select_start_date));
                return;
            } else if (end_date.getText().toString().equals("")) {
                message(TripListActivity.this, getString(R.string.select_end_date));
                return;
            } else if (isConnected()) {
                no_docs.setVisibility(View.GONE);
                swipeToRefresh.setVisibility(View.VISIBLE);
                getTrips();
            }

        }
    }

    private void permissionsForStorage() {
        int permissionCheck = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, LiveConstants.STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case LiveConstants.STORAGE_PERMISSION_CODE:

                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    finish();
                }
        }
    }

}