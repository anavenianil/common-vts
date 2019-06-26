package com.affluencesystems.insurancetelematics.common.Activitys;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.affluencesystems.insurancetelematics.R;
import com.affluencesystems.insurancetelematics.common.ApiUtils.Constants;
import com.affluencesystems.insurancetelematics.common.Models.Stride;
import com.affluencesystems.insurancetelematics.common.Utils.LiveConstants;
import com.affluencesystems.insurancetelematics.common.Utils.PreferenceUtils;
import com.affluencesystems.insurancetelematics.common.adapters.StrideAdapter2;
import com.affluencesystems.insurancetelematics.common.views.ShimmerRecyclerView;
import com.google.gson.JsonElement;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.affluencesystems.insurancetelematics.common.Utils.ConnectivityReceiver.isConnected;

public class StrideActivity extends Base_activity implements View.OnClickListener {

    private String TAG = "StrideActivity";
    private ShimmerRecyclerView recyclerView;
    private ArrayList<Stride> strideDetails = new ArrayList<>();
    private StrideAdapter2 stridesAdapter;
    private TextView no_docs;
    private SwipeRefreshLayout swipeToRefresh;
    private JSONArray j = null;
    private TextView start_date, end_date;
    private TextView tv_ok;
    private DatePickerDialog.OnDateSetListener start, end;
    /*
     *       The format of dates for sending to server are different from showing in edittext.
     * */
    private String sendStart, sendEnd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stride);
        preferenceUtils = new PreferenceUtils(this);
        preferenceUtils.saveString(PreferenceUtils.IMEI_NUMBER, getIntent().getStringExtra("IMEI"));
        headerControls();
        recyclerView.hideShimmerAdapter();
        permissionsForStorage();

        /*
         *       Date picker Dialog listener for start date
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

//                myCalendar.set(Calendar.HOUR, 0);
//                myCalendar.set(Calendar.MINUTE, 0);
//                myCalendar.set(Calendar.SECOND, 0);
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
                // TODO Auto-generated method stub
                Calendar myCalendar = Calendar.getInstance();
                view.setMaxDate(System.currentTimeMillis());
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

//                myCalendar.set(Calendar.HOUR, 22);
//                myCalendar.set(Calendar.MINUTE, 59);
//                myCalendar.set(Calendar.SECOND, 59);

                updateLabel(myCalendar, end_date, LiveConstants.END_DATE);
            }
        };

        start_date.setOnClickListener(this);
        end_date.setOnClickListener(this);
        tv_ok.setOnClickListener(this);

        /*
         *       Swipe to refresh/get all stride list
         * */
        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isConnected())
                    getStrideList();
            }
        });
    }

    private void permissionsForStorage() {
        int permissionCheck = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, LiveConstants.STORAGE_PERMISSION_CODE);
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
     *       To initiate all the view from xml file
     * */
    public void headerControls() {
        TextView header_text;
        ImageView back_button;
        header_text = (TextView) findViewById(R.id.header_text);
        no_docs = (TextView) findViewById(R.id.no_docs);
        back_button = (ImageView) findViewById(R.id.back_button);
        swipeToRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
        recyclerView = (ShimmerRecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        start_date = (TextView) findViewById(R.id.start_date);
        tv_ok = (TextView) findViewById(R.id.tv_ok);
        end_date = (TextView) findViewById(R.id.end_date);
        header_text.setText(getResources().getString(R.string.stride_list));
        header_text.setVisibility(View.VISIBLE);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    /*
     *       To get all the strides based on imei number and start date and end date.
     * */
    private void getStrideList() {
        recyclerView.showShimmerAdapter();
        Call<ArrayList<Stride>> callRetrofit = null;
        callRetrofit = Constants.service.getStridesNew(preferenceUtils.getStringFromPreference(PreferenceUtils.IMEI_NUMBER, "")/*"869696049430759"*/, sendStart, sendEnd);
        callRetrofit.enqueue(new Callback<ArrayList<Stride>>() {
            @Override
            public void onResponse(Call<ArrayList<Stride>> call, Response<ArrayList<Stride>> response) {
                swipeToRefresh.setRefreshing(false);
                recyclerView.hideShimmerAdapter();
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().size() > 0) {
                        no_docs.setVisibility(View.GONE);
                        stridesAdapter = new StrideAdapter2(StrideActivity.this, response.body(), false, LiveConstants.PERSONAL_APP);
                        recyclerView.setAdapter(stridesAdapter);
                    } else {
                        no_docs.setVisibility(View.VISIBLE);
                        recyclerView.hideShimmerAdapter();
                    }
//                        try {
//
//                            JSONArray j = new JSONArray(response.body().toString());
//                            for (int i = 0; i < j.length(); i++) {
//                                JSONObject j2 = j.getJSONObject(i);
//                                String distance = j2.getString("distanceTraveled");
//                                String time = j2.getString("timeForStride");
//                                JSONArray j3 = j2.getJSONArray("latlong");
//                                for (int k = 0; k < j3.length(); k++) {
//                                    if (!j3.getJSONObject(k).getString("latitude").equals("0.0") && !j3.getJSONObject(k).getString("longitude").equals("0.0")) {
//                                        Log.d(TAG, "LatLngs : " + j3.getJSONObject(k).getString("latitude") + "---" + j3.getJSONObject(k).getString("longitude"));
//                                        Stride s = new Stride(j3.getJSONObject(k).getString("latitude"), j3.getJSONObject(k).getString("longitude"), j3.getJSONObject(k).getString("speed"), j3.getJSONObject(k).getString("heading"), j3.getJSONObject(k).getString("date"));
//                                        strideDetails.add(s);
//                                        s = null;
//                                    }
//                                }
//                                AllStrides all = new AllStrides(distance, time, strideDetails);
//                                objects.add(all);
//                                all = null;
//                            }
//                            if (objects.size() > 0) {
//                                no_docs.setVisibility(View.GONE);
//                                stridesAdapter = new StridesAdapter(StrideActivity.this, objects, false, -1);
//                                recyclerView.setAdapter(stridesAdapter);
//                            } else {
//                                no_docs.setVisibility(View.VISIBLE);
//                                recyclerView.hideShimmerAdapter();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    } else {
//                        no_docs.setVisibility(View.VISIBLE);
//                        recyclerView.hideShimmerAdapter();
//                    }
//                } else {
//                    message(StrideActivity.this, getString(R.string.no_response));
//                }
                } else {
                    message(StrideActivity.this, getString(R.string.no_response));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Stride>> call, Throwable t) {
                swipeToRefresh.setRefreshing(false);
                recyclerView.hideShimmerAdapter();
                no_docs.setVisibility(View.VISIBLE);
                message(StrideActivity.this, getString(R.string.failed_to_connect_server));
                Log.d(TAG, "Error Call" + call.toString());
                Log.d(TAG, "Error" + t.toString());
            }
        });
    }




   /* private void getStrideList() {
        recyclerView.showShimmerAdapter();
        Call<JsonElement> callRetrofit = null;
        callRetrofit = Constants.service.getStrides(preferenceUtils.getStringFromPreference(PreferenceUtils.IMEI_NUMBER, "")*//*"869696049430759"*//*, sendStart, sendEnd);
        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                swipeToRefresh.setRefreshing(false);
                recyclerView.hideShimmerAdapter();
                objects = new ArrayList<>();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {

                            JSONArray j = new JSONArray(response.body().toString());
                            for (int i = 0; i < j.length(); i++) {
                                JSONObject j2 = j.getJSONObject(i);
                                String distance = j2.getString("distanceTraveled");
                                String time = j2.getString("timeForStride");
                                JSONArray j3 = j2.getJSONArray("latlong");
                                for (int k = 0; k < j3.length(); k++) {
                                    if (!j3.getJSONObject(k).getString("latitude").equals("0.0") && !j3.getJSONObject(k).getString("longitude").equals("0.0")) {
                                        Log.d(TAG, "LatLngs : " + j3.getJSONObject(k).getString("latitude") + "---" + j3.getJSONObject(k).getString("longitude"));
                                        Stride s = new Stride(j3.getJSONObject(k).getString("latitude"), j3.getJSONObject(k).getString("longitude"), j3.getJSONObject(k).getString("speed"), j3.getJSONObject(k).getString("heading"), j3.getJSONObject(k).getString("date"));
                                        strideDetails.add(s);
                                        s = null;
                                    }
                                }
                                AllStrides all = new AllStrides(distance, time, strideDetails);
                                objects.add(all);
                                all = null;
                            }
                            if (objects.size() > 0) {
                                no_docs.setVisibility(View.GONE);
                                stridesAdapter = new StridesAdapter(StrideActivity.this, objects, false, -1);
                                recyclerView.setAdapter(stridesAdapter);
                            } else {
                                no_docs.setVisibility(View.VISIBLE);
                                recyclerView.hideShimmerAdapter();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        no_docs.setVisibility(View.VISIBLE);
                        recyclerView.hideShimmerAdapter();
                    }
                } else {
                    message(StrideActivity.this, getString(R.string.no_response));
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                swipeToRefresh.setRefreshing(false);
                recyclerView.hideShimmerAdapter();
                no_docs.setVisibility(View.VISIBLE);
                message(StrideActivity.this, getString(R.string.failed_to_connect_server));
                Log.d(TAG, "Error Call" + call.toString());
                Log.d(TAG, "Error" + t.toString());
            }
        });
    }*/


    private void getStrideListForTest() {
        Call<JsonElement> callRetrofit = null;
//        callRetrofit = Constants.service_live.testLiveService("869696049483576");
        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                swipeToRefresh.setRefreshing(false);
                if (response.isSuccessful()) {
                    if (response.body() != null) {

//                        try {
//                            j = new JSONArray(response.body().toString());
//                            for (int i = 0; i < j.length(); i++) {
//                                if (Double.parseDouble(j.getJSONObject(i).getString("latitude")) != 0.0 && Double.parseDouble(j.getJSONObject(i).getString("longitude")) != 0.0) {
//                                    Stride s = new Stride(j.getJSONObject(i).getString("latitude"), j.getJSONObject(i).getString("longitude"), j.getJSONObject(i).getString("speed"), j.getJSONObject(i).getString("heading"), j.getJSONObject(i).getString("dateTime"));
//                                    strideDetails.add(s);
//                                }
//                            }
//
//                            if (strideDetails.size() > 0) {
//                                AllStrides all = new AllStrides(strideDetails);
//                                objects.add(all);
//                                no_docs.setVisibility(View.GONE);
//                                recyclerView.setVisibility(View.VISIBLE);
//                                stridesAdapter = new StridesAdapter(StrideActivity.this, objects, false);
//                                recyclerView.setAdapter(stridesAdapter);
//                            } else {
//                                no_docs.setVisibility(View.VISIBLE);
//                                recyclerView.setVisibility(View.GONE);
//                            }
//
//                        } catch (JSONException e) {
//                            Log.d(TAG, "JSON Exception -----> ");
//                            e.printStackTrace();
//                        }

//                        try {
//                            JSONArray j = new JSONArray(response.body().toString());
//                            for (int i = 0; i < j.length(); i++) {
////                                JSONArray j2 = j.getJSONArray(i);
////                                for (int k = 0; k < j2.length(); k++) {
//                                    Stride s = new Stride(j.getJSONObject(i).getString("latitude"), j.getJSONObject(i).getString("longitude"), j.getJSONObject(i).getString("speed"), j.getJSONObject(i).getString("heading"), j.getJSONObject(i).getString("dateAndTime"));
//                                    strideDetails.add(s);
////                                }
//                                AllStrides all = new AllStrides(strideDetails);
//                                objects.add(all);
//                            }

//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                    } else {
                        no_docs.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                } else {
                    message(StrideActivity.this, "No Response..");
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                swipeToRefresh.setRefreshing(false);
                no_docs.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                message(StrideActivity.this, "Failed to Load Data..");
                Log.d(TAG, "Error Call" + call.toString());
                Log.d(TAG, "Error" + t.toString());
            }
        });
    }


    /*
     *       Click events
     * */
    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.start_date) {/*
         *       blocking future dates
         * */
            Calendar myCalendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(StrideActivity.this, start, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();

        } else if (i == R.id.end_date) {/*
         *       blocking future dates and before start date
         * */
            if (!start_date.getText().toString().equals("")) {
                Calendar myCalendar1 = Calendar.getInstance();
                DatePickerDialog datePickerDialog1 = new DatePickerDialog(StrideActivity.this, end, myCalendar1.get(Calendar.YEAR), myCalendar1.get(Calendar.MONTH), myCalendar1.get(Calendar.DAY_OF_MONTH));

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
                message(StrideActivity.this, getString(R.string.select_start_date));
            }

        } else if (i == R.id.tv_ok) {/*
         *       validating all the fields and send server request for strides.
         * */
            if (start_date.getText().toString().equals("")) {
                message(StrideActivity.this, getString(R.string.select_start_date));
                return;
            } else if (end_date.getText().toString().equals("")) {
                message(StrideActivity.this, getString(R.string.select_end_date));
                return;
            } else if (isConnected()) {
                no_docs.setVisibility(View.GONE);
                swipeToRefresh.setVisibility(View.VISIBLE);
                getStrideList();
            }

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
