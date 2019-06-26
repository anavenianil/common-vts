package com.affluencesystems.insurancetelematics.common.adapters;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.affluencesystems.insurancetelematics.common.Activitys.Base_activity;
import com.affluencesystems.insurancetelematics.common.Activitys.StridePathActivity;
import com.affluencesystems.insurancetelematics.common.Activitys.TripListActivity;
import com.affluencesystems.insurancetelematics.common.ApiUtils.Constants;
import com.affluencesystems.insurancetelematics.common.Models.GpsData;
import com.affluencesystems.insurancetelematics.common.Models.Map_history;
import com.affluencesystems.insurancetelematics.common.Models.Stride;
import com.affluencesystems.insurancetelematics.common.Models.StrideDetails;
import com.affluencesystems.insurancetelematics.common.Models.StrideGpsData;
import com.affluencesystems.insurancetelematics.common.Models.TripForList;
import com.affluencesystems.insurancetelematics.common.Models.TripPacket;
import com.affluencesystems.insurancetelematics.R;
import com.affluencesystems.insurancetelematics.common.Utils.DeviceCapturingDateExtraction;
import com.affluencesystems.insurancetelematics.common.Utils.DeviceCapturingDateExtractionNew;
import com.affluencesystems.insurancetelematics.common.Utils.LiveConstants;
import com.affluencesystems.insurancetelematics.common.Utils.PreferenceUtils;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.DOWNLOAD_SERVICE;
import static com.affluencesystems.insurancetelematics.common.Activitys.Base_activity.message;

public class TripListAdapter extends RecyclerView.Adapter<TripListAdapter.MyViewHolder> {

    private String TAG = "TripListAdapter";
    private ArrayList<TripForList> trips;
    private Context context;
    private PacketAdapter pagerAdapter;
    //    public static ArrayList<Map_history> map_history_current;
    private ArrayList<TripPacket> tripPackets;
    private ProgressDialog progressDoalog;
    private PreferenceUtils preferenceUtils;
    private long downloadID;
    private String fileName = "";
    private ArrayList<Stride> strides;
    private boolean isBroadcastReceiverRunning = false;
    private ArrayList<GpsData> allGpsData;
    public static ArrayList<LatLng> latLngs;
    private LatLng zeroLatLng = new LatLng(0,0);
    private ExtractFile task;
    private boolean isClicked = false;


    /*
     *       Constructor for get all the trips
     * */
    public TripListAdapter(Context context, ArrayList<TripForList> trips) {
        this.trips = trips;
        this.context = context;
        preferenceUtils = new PreferenceUtils(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trip_details_row, parent, false);

        return new MyViewHolder(itemView);
    }

    /*
     *       set the values for the views.
     *       set the adapter for all ignition off values.
     * */
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final TripForList tripForList = trips.get(position);
        holder.title.setText(tripForList.getTripName());
        holder.start_date.setText(timeAndDateFormat(tripForList.getTripStartDate()));
        holder.end_date.setText(timeAndDateFormat(tripForList.getTripEndDate()));
        holder.mileage.setText(context.getString(R.string.mileage) + " " + tripForList.getFuelConsumed() + "km/l");
        holder.distance.setText(context.getString(R.string.distance) + " " + tripForList.getDistanceTravel().substring(0, tripForList.getDistanceTravel().indexOf(".") + 2) + "km");
        holder.time.setText(context.getString(R.string.time) + " " + tripForList.getTimeDuration() + "min");

//        if (tripForList.getLatlong() != null) {
//            ArrayList<TripPacket> packets = tripForList.getLatlong();
//            tripPackets = new ArrayList<>();
//            if (packets != null && packets.size() > 0) {
//                for (int i = 0; i < packets.size(); i++) {
//                    Log.d(TAG, "Packet index ==> " + i);
//                    if ("0".equals(packets.get(i).getIgnition())) {
//                        tripPackets.add(packets.get(i));
//                    }
//                }
//                pagerAdapter = new PacketAdapter(context, tripPackets);
//                holder.trip_details_recycler.setAdapter(pagerAdapter);
//            }

        /*
         *       onClick event for set the latlngs for stride path activity for animate vehicle.
         * */
          /*  holder.title_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    message((TripListActivity) context, context.getString(R.string.no_data));

                  *//*  map_history_current = new ArrayList<>();
                    if (trips.get(position).getLatlong() != null) {
                        for (TripPacket t : trips.get(position).getLatlong()) {
                            if(!t.getLatitude().equals("0.0") && !t.getLongitude().equals("0.0")) {
                                Map_history m = new Map_history();
                                m.setLatitude(t.getLatitude());
                                m.setLongitude(t.getLongitude());
                                m.setSpeed(t.getSpeed());
                                map_history_current.add(m);
                            }
                        }
                    }
                    if (map_history_current.size() > 0) {
                        context.startActivity(new Intent(context, StridePathActivity.class));
                    } else {
                        Base_activity.message((TripListActivity) context, context.getString(R.string.no_data));
                    }*//*
                }
            });*/
//        }
    }

    private int convertTimeMinuteToHour(String mins) {
        return Integer.parseInt(mins) / 60;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, start_date, end_date, mileage, distance, time;
        RecyclerView trip_details_recycler;
        ImageView im_path;
        CardView card1;
        LinearLayout title_layout;

        MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            card1 = (CardView) view.findViewById(R.id.card1);
            start_date = (TextView) view.findViewById(R.id.start_date);
            end_date = (TextView) view.findViewById(R.id.end_date);
            mileage = (TextView) view.findViewById(R.id.mileage);
            distance = (TextView) view.findViewById(R.id.distance);
            time = (TextView) view.findViewById(R.id.time);
            im_path = (ImageView) view.findViewById(R.id.im_path);
            title_layout = (LinearLayout) view.findViewById(R.id.title_layout);
            trip_details_recycler = (RecyclerView) view.findViewById(R.id.trip_details_recycler);
            LinearLayoutManager llm = new LinearLayoutManager(context);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            trip_details_recycler.setLayoutManager(llm);
            trip_details_recycler.setHasFixedSize(true);

            card1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(context, "Adapter position : " + getAdapterPosition()/*context.getString(R.string.no_data)*/, Toast.LENGTH_LONG).show();
//                    String startDate = trips.get(getAdapterPosition()).getTripStartDate();
//                    startDate = startDate.substring(0,startDate.indexOf("."))+"Z";
//                    String endDate = trips.get(getAdapterPosition()).getTripEndDate();
//                    endDate = endDate.substring(0, endDate.indexOf(".")) + "Z";
                    if(!isClicked) {
                        getStrideDataForTrip(convertDate(trips.get(getAdapterPosition()).getTripStartDate()), convertDate(trips.get(getAdapterPosition()).getTripEndDate()));
                        isClicked = true;
                    }
//                    message((TripListActivity) context, context.getString(R.string.no_data));
                }
            });

        }
    }

    public void changeClickEvent(){
        isClicked = false;
    }

    /*
     *       time and date format for display.
     * */
    private String timeAndDateFormat(String dateString){
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);
        Date date = null;
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat("dd-MM-yyyy HH:mm a", Locale.US).format(date);
    }

    /*private String timeAndDateFormat(String dateandtime) {
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

    private void getStrideDataForTrip(String startDate, String endDate) {
        progressDoalog = new ProgressDialog(context);
        progressDoalog.setCancelable(false);
        progressDoalog.setMessage(context.getResources().getString(R.string.loading));
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.show();
        Call<ArrayList<Stride>> callRetrofit = null;
        callRetrofit = Constants.service.getStrideForTrip(preferenceUtils.getStringFromPreference(PreferenceUtils.IMEI_NUMBER, ""), startDate, endDate);
        callRetrofit.enqueue(new Callback<ArrayList<Stride>>() {
            @Override
            public void onResponse(Call<ArrayList<Stride>> call, Response<ArrayList<Stride>> response) {
                progressDoalog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        strides = response.body();
                        if (strides.size() > 0) {
                            for (int i = 0; i < strides.size(); i++) {
                                checkFileExistence(strides.get(i).getPrimaryKey(), i);
                            }
                        } else {
                            Toast.makeText(context, context.getString(R.string.no_data), Toast.LENGTH_LONG).show();
                        }

//                        DeviceCapturingDateExtraction deviceCapturingDateExtraction = new DeviceCapturingDateExtraction();
//                        String strideData = response.body().getStride();
//                        String historyData = response.body().getHistory();
//                        ArrayList<StrideDetails> strides = deviceCapturingDateExtraction.constructNormalPacket(strideData);
//                        ArrayList<StrideDetails> history = deviceCapturingDateExtraction.constructNormalPacket(historyData);
//
//                        ArrayList<GpsData> allGpsData = new ArrayList<>();
//                        for (StrideDetails s : strides){
//                            allGpsData.addAll(s.getGpsData());
//                        }
//                        for (StrideDetails s : history){
//                            allGpsData.addAll(s.getGpsData());
//                        }
//
//                        Log.d(TAG, "stride size ==> " + strides.size());
//                        Log.d(TAG, "history size ==> " + history.size());
//
//                        Collections.sort(allGpsData);
//
//                        Log.d(TAG, "all size ==> " + allGpsData.size());
//
//                        ArrayList<LatLng> latLngs = new ArrayList<>();
//                        for (GpsData g : allGpsData) {
//                            LatLng latLng = new LatLng(g.getLatitude(), g.getLongitude());
//                            Log.d(TAG, "lat lngs ==> " + latLng);
//                            latLngs.add(latLng);
//                        }
//
//                        progressDoalog.dismiss();
//
//                        Intent intent = new Intent(context, StridePathActivity.class);
//                        intent.putExtra(LiveConstants.STRIDE_PATH_LATLNGS , latLngs);
//                        context.startActivity(intent);
//                    }
//                } else {
//                    progressDoalog.dismiss();
//                    Toast.makeText(context, context.getString(R.string.no_data), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, context.getString(R.string.no_data), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Stride>> call, Throwable t) {
                Toast.makeText(context, context.getString(R.string.no_data), Toast.LENGTH_LONG).show();
                progressDoalog.dismiss();
                Toast.makeText(context, context.getString(R.string.failed_to_connect_server), Toast.LENGTH_LONG).show();
            }
        });
    }

    private String convertDate(String inDate) {
        Log.d(TAG, "Given date is " + inDate);
        Date date = null;
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);
        try {
            date = sdf.parse(inDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date != null)
            return new SimpleDateFormat("E MMM dd yyyy HH:mm:ss Z", Locale.US).format(date);
        else
            return "";
    }


    private void checkFileExistence(String fileName, int strideNumber) {
        File file = new File(Environment.getExternalStorageDirectory() + "/PersonalApp/Strides/" + fileName + ".txt");
        progressDoalog = new ProgressDialog(context);
        progressDoalog.setCancelable(false);
        progressDoalog.setMessage(context.getResources().getString(R.string.loading));
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        if (!progressDoalog.isShowing()) {
            progressDoalog.show();
        }
        if (file.exists() && strides.size() - 1 == strideNumber && !isBroadcastReceiverRunning) {
            ArrayList<String> strideName = new ArrayList<>();
            for (Stride s : strides){
                strideName.add(s.getPrimaryKey());
            }
            String[] arr = new String[strideName.size()];
            arr = strideName.toArray(arr);
//            String arr[] = (String[]) strideName.toArray();
            task = new ExtractFile();
            task.execute(arr);
//            for (Stride stride : strides){
//                convertNormalPacket(stride.getPrimaryKey());
//            }
//            openStridePathActivity();
            Log.d(TAG, "File Already Exist...");
        } else {
//            progressDoalog = new ProgressDialog(context);
//            progressDoalog.setCancelable(false);
//            progressDoalog.setMessage(context.getResources().getString(R.string.loading));
//            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            if (!progressDoalog.isShowing()) {
//                progressDoalog.show();
//            }
            downloadStrideFile("http://intellitrack.in:3344/stridedata/getstridedocumentsfile/" + fileName, fileName);
            this.fileName = fileName;
            isBroadcastReceiverRunning  = true;
            context.registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        }
    }

    private void unRegisterReceiver() {
        isBroadcastReceiverRunning = false;
        context.unregisterReceiver(onDownloadComplete);
    }


    private void downloadStrideFile(String DownloadUrl, String fileName) {
//        String DownloadUrl = "http://intellitrack.in:3344/stridedata/getDocumentsFile";
        DownloadManager.Request request1 = new DownloadManager.Request(Uri.parse(DownloadUrl));
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
//            Toast.makeText(context, "download success ", Toast.LENGTH_LONG).show();
        }
    }

    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            Log.d(TAG, "downloadId ==> " + downloadID);
            if (downloadID == id) {
//                progressDoalog.dismiss();

                ArrayList<String> strideName = new ArrayList<>();
                for (Stride s : strides){
                    strideName.add(s.getPrimaryKey());
                }
                String arr[] = (String[]) strideName.toArray();
                task = new ExtractFile();
                task.execute(arr);

//                for (Stride stride : strides){
//                    convertNormalPacket(stride.getPrimaryKey());
//                }
//                openStridePathActivity();
                Log.d(TAG, "File Downloaded...");
                unRegisterReceiver();
            }
        }
    };

    private void convertNormalPacket(String fileName){
        if(allGpsData == null)
            allGpsData = new ArrayList<>();
        DeviceCapturingDateExtractionNew deviceCapturingDateExtraction = new DeviceCapturingDateExtractionNew();
        ArrayList<StrideDetails> strides = deviceCapturingDateExtraction.constructNormalPacket(Environment.getExternalStorageDirectory() + "/PersonalApp/Strides/"+fileName+".txt");
        for (StrideDetails s : strides) {
            allGpsData.addAll(s.getGpsData());
        }
    }

    private void openStridePathActivity(){
        if(latLngs == null)
            latLngs = new ArrayList<>();

        for (GpsData g : allGpsData) {
            LatLng latLng = new LatLng(g.getLatitude(), g.getLongitude());
            Log.d(TAG, "lat lngs ==> " + latLng);
            if (!zeroLatLng.equals(latLng))
                latLngs.add(latLng);
        }
        if(progressDoalog != null)
            progressDoalog.dismiss();
        Intent intent = new Intent(context, StridePathActivity.class);
//        intent.putExtra(LiveConstants.STRIDE_PATH_LATLNGS, latLngs);
        intent.putExtra(LiveConstants.IS_FROM_STRIDE, false);
        context.startActivity(intent);
    }

    class ExtractFile extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            for (int i = 0; i<strings.length; i++) {
                convertNormalPacket(strings[i]);
            }
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
