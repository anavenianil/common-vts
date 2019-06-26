package com.affluencesystems.insurancetelematics.common.Activitys;

import android.animation.ValueAnimator;
import android.app.DialogFragment;
import android.app.DownloadManager;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.affluencesystems.insurancetelematics.R;
import com.affluencesystems.insurancetelematics.common.ApiUtils.Constants;
import com.affluencesystems.insurancetelematics.common.Models.GpsData;
import com.affluencesystems.insurancetelematics.common.Models.StrideDetails;
import com.affluencesystems.insurancetelematics.common.Utils.DeviceCapturingDateExtraction;
import com.affluencesystems.insurancetelematics.common.Utils.LiveConstants;
import com.affluencesystems.insurancetelematics.common.Utils.MapUtils;
import com.affluencesystems.insurancetelematics.common.Utils.PreferenceUtils;
import com.affluencesystems.insurancetelematics.common.adapters.StrideAdapter2;
import com.affluencesystems.insurancetelematics.common.adapters.TripListAdapter;
import com.affluencesystems.insurancetelematics.common.fragments.MapStyleDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.Color.rgb;
import static com.affluencesystems.insurancetelematics.common.Utils.MapUtils.animate_camera;
import static com.affluencesystems.insurancetelematics.common.Utils.MapUtils.getBearing;
import static com.affluencesystems.insurancetelematics.common.Utils.MapUtils.getDistanceInMeters;
import static com.affluencesystems.insurancetelematics.common.Utils.MapUtils.latlong;
import static com.affluencesystems.insurancetelematics.common.Utils.MapUtils.marker;
import static com.affluencesystems.insurancetelematics.common.Utils.MapUtils.move_3D_Camera;
import static com.affluencesystems.insurancetelematics.common.Utils.MapUtils.move_Camera;
import static com.affluencesystems.insurancetelematics.common.Utils.MapUtils.randum_angle;
import static com.affluencesystems.insurancetelematics.common.Utils.MapUtils.rotateMarker;
import static com.affluencesystems.insurancetelematics.common.Utils.MapUtils.zoom_level;

public class StridePathActivity extends Base_activity implements OnMapReadyCallback, View.OnClickListener, MapStyleDialog.ChangeMapStyle, GoogleMap.OnCameraIdleListener, GoogleMap.OnMapClickListener {

    private static final String TAG = "Stride1 Path Activity";
    private long ANIMATION_TIME_PER_ROUTE = 2000;
    private long DELAY = 2000;
    public LatLng latLng;
    static LatLng latlong_from;
    static ValueAnimator valueAnimator;
    private LatLng startPosition;
    private Marker marker = null, start_pos_marker = null;
    private double lat;
    private double lng;
    private float v;
    private TextView tv_view;
    private static GoogleMap mMap;
    private static boolean isFirstPosition = true;
    SupportMapFragment mapFragment;
    private CardView view_card, style_card, zoom_enable, zoom_disable;
    public boolean is2D = true;
    private ImageView play, search_bar, im_select_marker;
    ArrayList<LatLng> latlongs/*, rotateLatLngs*/;
    ArrayList<Float> fuel_consumption_array_list;
    int i = -1, position = -1;
    private boolean play_and_pause;
    private Double startLatitude, startLongitude;
    private LatLng endPosition;
    private Handler handler;
    private ImageView im_fast, im_slow;
    private CardView zoom_minus, zoom_plus, select_latlng;
    private LatLng newPos;
    private LinearLayout lv_zooms;
    private ProgressDialog progressDoalog;
    //    private String fileName;
    private boolean isFromStride;
    private Marker middleMarker;
    private boolean isSelectMiddleMarker = false;
    private Circle circle;
    private boolean isNotificationShowed = false;

    /*
     *       Runnable for change the lat lngs for every DELAY(2 sec).
     * */
    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                line();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
            Log.d(TAG, "i value ==> " + i);
            if (i == latlongs.size() - 1) {
                play_and_pause = false;
                play.setImageResource(R.drawable.play);
                stopRepeatingTask();
                i = -1;
            } else {
                handler.postDelayed(mStatusChecker, DELAY);
            }
        }
    };


    /*
     *       To use animate vehicle in google map
     *       It takes previous latlng and current latlng,
     *       then navigate the vehicle.
     * */
    public void startCarAnimation(final LatLng start, final LatLng end) {

        Log.i(TAG, "startBikeAnimation called...");
        valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(ANIMATION_TIME_PER_ROUTE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                //LogMe.i(TAG, "Car Animation Started...");
                v = valueAnimator.getAnimatedFraction();
                lng = v * end.longitude + (1 - v)
                        * start.longitude;
                lat = v * end.latitude + (1 - v)
                        * start.latitude;

                newPos = new LatLng(lat, lng);
                marker.setPosition(newPos);
                marker.setAnchor(0.5f, 0.5f);

                float rotateAngle = getBearing(start, end);
                if (rotateAngle > 270) {
                    rotateMarker(marker, rotateAngle);
                    Log.d(TAG, "Anim type ====> " + " Animation");
                } else {
                    marker.setRotation(getBearing(start, end));
                    Log.d(TAG, "Anim Type ====> " + "Normal");
                }
                Log.d(TAG, "Rotate Angle ====> " + getBearing(start, end));

//                marker.setRotation(getBearing(start, end));

                if (is2D && zoom_enable.getVisibility() == View.VISIBLE) {
                    move_Camera(newPos, mMap, context);
                }

//                if (zoom_enable.getVisibility() == View.VISIBLE) {
//                    if (is2D) {
//                        move_Camera(newPos, mMap, context);
//                    } else {
//                        move_3D_Camera(newPos, mMap, getBearing(start, end), context);
//                    }
//                }
                startPosition = marker.getPosition();
                latlong_from = marker.getPosition();

                marker.showInfoWindow();
            }

        });
        valueAnimator.start();
        if (!is2D && zoom_enable.getVisibility() == View.VISIBLE) {
            move_3D_Camera(newPos, mMap, getBearing(start, end), context);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stride_path);
        headerControls();
        checkVisibility();
        Intent intent = getIntent();
        isFromStride = intent.getBooleanExtra(LiveConstants.IS_FROM_STRIDE, false);
        getRecords();
    }

    private void getRecords() {
        latlongs = new ArrayList<>();
        fuel_consumption_array_list = new ArrayList<>();
        if (isFromStride) {
            latlongs = StrideAdapter2.latLngs;
            fuel_consumption_array_list = StrideAdapter2.fuel_consumption_array_list;
        } else {
            latlongs = TripListAdapter.latLngs;
        }

//        rotateLatLngs = latlongs;
    }


    /*
     *       To initiate all the view from xml file
     * */
    public void headerControls() {
        isFirstPosition = true;
//        map_services = new MAP_SERVICES(StridePathActivity.this);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.stride_path);
        mapFragment.getMapAsync(this);// Obtain the SupportMapFragment and get notified when the map is ready to be used.
        handler = new Handler();
        TextView header_text;
        ImageView back_button;
        header_text = (TextView) findViewById(R.id.header_text);
        back_button = (ImageView) findViewById(R.id.back_button);
        view_card = (CardView) findViewById(R.id.view_card);
        style_card = (CardView) findViewById(R.id.style_card);
        zoom_minus = (CardView) findViewById(R.id.zoom_minus);
        zoom_plus = (CardView) findViewById(R.id.zoom_plus);
        zoom_enable = (CardView) findViewById(R.id.zoom_enable);
        zoom_disable = (CardView) findViewById(R.id.zoom_disable);
        select_latlng = (CardView) findViewById(R.id.select_latlng);
        tv_view = (TextView) findViewById(R.id.tv_view);
        im_slow = (ImageView) findViewById(R.id.im_slow);
        im_fast = (ImageView) findViewById(R.id.im_fast);
        search_bar = (ImageView) findViewById(R.id.search_bar);
        lv_zooms = (LinearLayout) findViewById(R.id.lv_zooms);
        header_text.setText(getResources().getString(R.string.stride_path));
        search_bar.setImageResource(R.drawable.ic_settings_black_24dp);
        search_bar.setVisibility(View.VISIBLE);
        header_text.setVisibility(View.VISIBLE);
        play = (ImageView) findViewById(R.id.play);
        im_select_marker = (ImageView) findViewById(R.id.im_select_marker);
        play.setOnClickListener(this);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacks(mStatusChecker);
                handler.removeCallbacksAndMessages(mStatusChecker);
                finish();
            }
        });
        view_card.setOnClickListener(this);
        style_card.setOnClickListener(this);
        zoom_plus.setOnClickListener(this);
        zoom_minus.setOnClickListener(this);
        im_slow.setOnClickListener(this);
        im_fast.setOnClickListener(this);
        zoom_enable.setOnClickListener(this);
        zoom_disable.setOnClickListener(this);
        search_bar.setOnClickListener(this);
        select_latlng.setOnClickListener(this);

        manageZoomVisibility();
    }

    private void manageZoomVisibility() {
        if (preferenceUtils.getBooleanFromPreferenceForSideBar(PreferenceUtils.ZOOM_CONTROLS)) {
            lv_zooms.setVisibility(View.VISIBLE);
        } else {
            lv_zooms.setVisibility(View.GONE);
        }

        if (preferenceUtils.getBooleanFromPreferenceForSideBar(PreferenceUtils.IS_Map_Styles_Enable))
            style_card.setVisibility(View.VISIBLE);
        else
            style_card.setVisibility(View.GONE);

        if (preferenceUtils.getBooleanFromPreferenceForSideBar(PreferenceUtils.Is_2D_3D_Enable))
            view_card.setVisibility(View.VISIBLE);
        else
            view_card.setVisibility(View.GONE);
    }

    /*
     *       Click events.
     * */
    @Override
    public void onClick(View view) {
        int i1 = view.getId();
        if (i1 == R.id.play) {/*
         *       if vehicle is moving, then stop the vehicle
         *       else move the vehicle
         * */
            if (i == -1) {
                if (marker != null) {
                    marker.remove();
                    isFirstPosition = true;
                }
            }


            isSelectMiddleMarker = false;
            im_select_marker.setImageResource(R.drawable.ic_pin_drop_black_24dp);
            if (play_and_pause) {
                play_and_pause = false;
                play.setImageResource(R.drawable.play);
                stopRepeatingTask();
            } else {
                play_and_pause = true;
                play.setImageResource(R.drawable.pause);
                handler.post(mStatusChecker);
            }

        } else if (i1 == R.id.view_card) {/*
         *       change the map view 2d to 3d vice verse.
         * */
            if (marker != null) {
                marker.remove();
            }
            if (is2D) {
                is2D = false;
                tv_view.setText("2D");
                if (startPosition != null) {
                    marker = marker(mMap, startPosition, R.drawable.blue_dot);
                    move_3D_Camera(startPosition, mMap, randum_angle(), context);
                } else {
//                    doubt here for middle latlangs
                    move_3D_Camera(latlongs.get(0), mMap, randum_angle(), context);
                }
            } else {
                is2D = true;
                tv_view.setText("3D");
                if (startPosition != null) {
                    marker = MapUtils.carMarker(mMap, startPosition, context, preferenceUtils.getIntFromPreference(PreferenceUtils.VEHICLE_CODE, R.drawable.white_car_with_radius));
                    move_Camera(marker.getPosition(), mMap, context);
                } else {
                    move_Camera(start_pos_marker.getPosition(), mMap, context);
                }
            }

        } else if (i1 == R.id.style_card) {/*
         *       opens maps style dialog.
         * */
            openStylesDialog();

        } else if (i1 == R.id.zoom_minus) {
            float zoom = preferenceUtils.getFloatFromPreference(PreferenceUtils.ZOOM_LEVEL, 17);
            if (zoom > 16) {
                zoom_level--;
//                float zoom = preferenceUtils.getFloatFromPreference(PreferenceUtils.ZOOM_LEVEL, 17) - 1;
                preferenceUtils.saveFloat(PreferenceUtils.ZOOM_LEVEL, zoom - 1);
                if (!isFirstPosition) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), preferenceUtils.getFloatFromPreference(PreferenceUtils.ZOOM_LEVEL, 17)));
                } else {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start_pos_marker.getPosition(), preferenceUtils.getFloatFromPreference(PreferenceUtils.ZOOM_LEVEL, 17)));
                }
                Log.d(TAG, "Zoom level ===> " + zoom_level);
            } else {
//                Base_activity.message(StridePathActivity.this, "Map in minimum zoom level");
            }

        } else if (i1 == R.id.zoom_plus) {
            float zoom1 = preferenceUtils.getFloatFromPreference(PreferenceUtils.ZOOM_LEVEL, 17);
            if (zoom1 < 20) {
                zoom_level++;
//                    float zoom1 = preferenceUtils.getFloatFromPreference(PreferenceUtils.ZOOM_LEVEL, 17) + 1;
                preferenceUtils.saveFloat(PreferenceUtils.ZOOM_LEVEL, zoom1 + 1);
                if (!isFirstPosition) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), preferenceUtils.getFloatFromPreference(PreferenceUtils.ZOOM_LEVEL, 17)));
                } else {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start_pos_marker.getPosition(), preferenceUtils.getFloatFromPreference(PreferenceUtils.ZOOM_LEVEL, 17)));
                }
                Log.d(TAG, "Zoom level ===> " + zoom_level);
            } else {
//                Base_activity.message(StridePathActivity.this, "Map in maximum zoom level");
            }

        } else if (i1 == R.id.im_slow) {/*
         *       vehicle animation speed decrease
         * */
            if (ANIMATION_TIME_PER_ROUTE < 3000 && DELAY < 3500) {
                ANIMATION_TIME_PER_ROUTE = ANIMATION_TIME_PER_ROUTE + 500;
                DELAY = DELAY + 500;
            }
            if (DELAY > 1000 && DELAY <= 1500)
                Toast.makeText(StridePathActivity.this, getString(R.string.speed_change) + " 3X", Toast.LENGTH_SHORT).show();
            else if (DELAY > 1500 && DELAY <= 2500)
                Toast.makeText(StridePathActivity.this, getString(R.string.speed_change) + " 2X", Toast.LENGTH_SHORT).show();
            else if (DELAY > 2500 && DELAY <= 3500)
                Toast.makeText(StridePathActivity.this, getString(R.string.speed_change) + " 1X", Toast.LENGTH_SHORT).show();
            Log.d(TAG, " Anim time ==> " + ANIMATION_TIME_PER_ROUTE + " delay ==> " + DELAY);


//                if (ANIMATION_TIME_PER_ROUTE >= 500 && ANIMATION_TIME_PER_ROUTE < 3000) {
//                    ANIMATION_TIME_PER_ROUTE = ANIMATION_TIME_PER_ROUTE + 500;
//                    DELAY = DELAY + 500;
//                    CHANGE_3D_DELAY = DELAY;
//                }
//                Toast.makeText(StridePathActivity.this, "Vehicle Speed " + setSpeedTimings(), Toast.LENGTH_SHORT).show();
//                Log.d(TAG, " Anim time ==> " + ANIMATION_TIME_PER_ROUTE + " delay ==> " + DELAY);


        } else if (i1 == R.id.im_fast) {/*
         *       vehicle animation speed increase
         * */
            if (ANIMATION_TIME_PER_ROUTE > 500 && DELAY > 1000) {
                ANIMATION_TIME_PER_ROUTE = ANIMATION_TIME_PER_ROUTE - 500;
                DELAY = DELAY - 500;
            }

            if (DELAY > 500 && DELAY <= 1000)
                Toast.makeText(StridePathActivity.this, getString(R.string.speed_change) + " 3X", Toast.LENGTH_SHORT).show();
            else if (DELAY > 1000 && DELAY <= 2000)
                Toast.makeText(StridePathActivity.this, getString(R.string.speed_change) + " 2X", Toast.LENGTH_SHORT).show();
            else if (DELAY > 2000 && DELAY <= 3000)
                Toast.makeText(StridePathActivity.this, getString(R.string.speed_change) + " 1X", Toast.LENGTH_SHORT).show();


            Log.d(TAG, " Anim time ==> " + ANIMATION_TIME_PER_ROUTE + " delay ==> " + DELAY);


//                if (ANIMATION_TIME_PER_ROUTE > 500 && ANIMATION_TIME_PER_ROUTE <= 3000) {
//                    ANIMATION_TIME_PER_ROUTE = ANIMATION_TIME_PER_ROUTE - 500;
//                    DELAY = DELAY - 500;
//                    CHANGE_3D_DELAY = DELAY;
//                }
//                Toast.makeText(StridePathActivity.this, "Vehicle Speed " + setSpeedTimings(), Toast.LENGTH_SHORT).show();
//                Log.d(TAG, " Anim time ==> " + ANIMATION_TIME_PER_ROUTE + " delay ==> " + DELAY);

        } else if (i1 == R.id.zoom_enable) {/*
         *       enable zoom controls and stops the camera movement of vehicle.
         * */
            if (mMap != null) {
                mMap.getUiSettings().setZoomGesturesEnabled(true);
                mMap.getUiSettings().setScrollGesturesEnabled(true);
                mMap.getUiSettings().setRotateGesturesEnabled(true);
                zoom_disable.setVisibility(View.VISIBLE);
                zoom_enable.setVisibility(View.GONE);
                Toast.makeText(StridePathActivity.this, getString(R.string.zoom_enable), Toast.LENGTH_LONG).show();
            }

        } else if (i1 == R.id.zoom_disable) {
            /*
             *       disable zoom controls and starts the camera movement of vehicle.
             * */
            if (mMap != null) {
                mMap.getUiSettings().setZoomGesturesEnabled(false);
                mMap.getUiSettings().setScrollGesturesEnabled(false);
                mMap.getUiSettings().setRotateGesturesEnabled(false);
                zoom_disable.setVisibility(View.GONE);
                zoom_enable.setVisibility(View.VISIBLE);
                Toast.makeText(StridePathActivity.this, getString(R.string.zoom_disable), Toast.LENGTH_LONG).show();
            }

        } else if (i1 == R.id.search_bar) {
            Intent intent = new Intent(StridePathActivity.this, SettingsActivity.class);
            intent.putExtra(LiveConstants.APPLICATION_KEY, LiveConstants.PERSONAL_APP);
            startActivityForResult(intent, LiveConstants.NAV_SETTINGS_CHANGED);

        } else if (i1 == R.id.select_latlng) {
            if (marker != null)
                marker.remove();
            if (middleMarker != null)
                middleMarker.remove();

            play_and_pause = false;
            play.setImageResource(R.drawable.play);
            stopRepeatingTask();

            position = -1;
            i = -1;
            isFirstPosition = true;

            if (isSelectMiddleMarker) {
                isSelectMiddleMarker = false;
                im_select_marker.setImageResource(R.drawable.ic_pin_drop_black_24dp);
            } else {
                isSelectMiddleMarker = true;
                im_select_marker.setImageResource(R.drawable.cancle_icon);
            }
        }
    }


    /*
     *       open map styles dialog.
     * */
    private void openStylesDialog() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        DialogFragment dialogFragment = new MapStyleDialog();
        dialogFragment.show(ft, "dialog");
        dialogFragment.setCancelable(true);
    }


    /*
     *       putting start and end markers on map and create poly line based on latlngs
     * */
    void CreatePolyLineOnly() {
        mMap.clear();

//        testAlert();


        start_pos_marker = marker(mMap, latlongs.get(0), R.drawable.marker_red);
        mMap.addPolyline(MapUtils.polylineOptions(latlongs, fuel_consumption_array_list, this));
        marker(mMap, latlongs.get(latlongs.size() - 1), R.drawable.marker_blue);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        zoom_level = 18f;
//        mMap.setMaxZoomPreference(21);
//        mMap.setMinZoomPreference(10);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setZoomGesturesEnabled(false);
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);

        mMap.setOnCameraIdleListener(this);
        mMap.setOnMapClickListener(this);

        try {
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.map_style));

            if (!success) {
                Log.e("MapsActivityRaw", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivityRaw", "Can't find style.", e);
        }


       /* DeviceCapturingDateExtraction deviceCapturingDateExtraction = new DeviceCapturingDateExtraction();
        ArrayList<StrideDetails> strides = deviceCapturingDateExtraction.constructNormalPacket(Environment.getExternalStorageDirectory() + "/PersonalApp/Strides/"+fileName+".txt");

        allGpsData = new ArrayList<>();
        for (StrideDetails s : strides) {
            allGpsData.addAll(s.getGpsData());
        }

        for (GpsData g : allGpsData) {
            LatLng latLng = new LatLng(g.getLatitude(), g.getLongitude());
            Log.d(TAG, "lat lngs ==> " + latLng);
            latlongs.add(latLng);
        }*/

        if (latlongs != null && latlongs.size() > 0) {
            CreatePolyLineOnly();
            animate_camera(latlongs.get(0), mMap, context);
        }
    }

    public void zoomRoute(ArrayList<LatLng> lstLatLngRoute) {
        if (mMap == null || lstLatLngRoute == null || lstLatLngRoute.isEmpty()) return;
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (LatLng latLngPoint : lstLatLngRoute)
            boundsBuilder.include(latLngPoint);

        int routePadding = 100;
        LatLngBounds latLngBounds = boundsBuilder.build();
        Log.d(TAG, "size ==> " + lstLatLngRoute.size());
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding));
    }


    /*
     *       Animate vehicle movement based on latlngs.
     * */
    public void line() {
        if (i == -1)
            i = position;

        if (i < latlongs.size()) {
            i = ++i;
            Log.d(TAG, "rotate i value ==> " + i);
        }

//        testCreateAlert(latlongs.get(i));

        startLatitude = latlongs.get(i).latitude;
        startLongitude = latlongs.get(i).longitude;
        if (isFirstPosition) {
            startPosition = latlong(startLatitude, startLongitude);
            if (is2D)
                marker = MapUtils.carMarker(mMap, startPosition, context, preferenceUtils.getIntFromPreference(PreferenceUtils.VEHICLE_CODE, R.drawable.white_car_with_radius));
            else
                marker = marker(mMap, startPosition, R.drawable.blue_dot);
            marker.setAnchor(0.5f, 0.5f);
            animate_camera(startPosition, mMap, context);
            marker.showInfoWindow();
            isFirstPosition = false;
        } else {
            endPosition = latlong(startLatitude, startLongitude);
            Log.d(TAG, startPosition.latitude + "--" + endPosition.latitude + "--Check --" + startPosition.longitude + "--" + endPosition.longitude);
            if ((startPosition.latitude != endPosition.latitude) || (startPosition.longitude != endPosition.longitude)) {

                Log.e(TAG, "NOT SAME");
                startCarAnimation(startPosition, endPosition);
            } else {
                Log.e(TAG, "SAME");
            }
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (isSelectMiddleMarker) {
            Log.d(TAG, "Map click ==> " + latLng);
            i = -1;
            if (marker != null)
                marker.remove();
            isFirstPosition = true;
//            im_select_marker.setImageResource(R.drawable.ic_pin_drop_black_24dp);
            isContainLatLng(latLng);
        }
    }


    private void isContainLatLng(LatLng clickLatLng) {
        Log.d(TAG, "click latlng ==> " + clickLatLng);
        double difLat1 = /*5*/ getDistanceInMeters(latlongs.get(0).latitude, latlongs.get(0).longitude, clickLatLng.latitude, clickLatLng.longitude);
//        int position = -1;
        for (int k = 0; k < latlongs.size(); k++) {
            double difLat = getDistanceInMeters(latlongs.get(k).latitude, latlongs.get(k).longitude, clickLatLng.latitude, clickLatLng.longitude);
            Log.d(TAG, "Latitude Difference ==> " + difLat);

            if (difLat < difLat1) {
                difLat1 = difLat;
                position = k;
            }
        }
        Log.d(TAG, "Latitude Difference1111 ==> " + difLat1);
        Log.d(TAG, "Latitude position  ==> " + position);
        if (position != -1) {
            if (middleMarker != null)
                middleMarker.remove();

            middleMarker = marker(mMap, latlongs.get(position), R.drawable.marker_path);
//            rotateLatLngs = new ArrayList<>();
//            for (int l = position; l < latlongs.size(); l++){
//                rotateLatLngs.add(latlongs.get(l));
//            }
        } else {
            Toast.makeText(this, "Click on the line...", Toast.LENGTH_LONG).show();
        }
    }


    /*
     *       remove runnable calls, when activity destroys.
     * */
    void stopRepeatingTask() {
        if (mStatusChecker != null) {
            handler.removeCallbacks(mStatusChecker);
            handler.removeCallbacksAndMessages(mStatusChecker);
        } else {
            Log.d("handler", "not stoped");
        }
    }

    /*
     *       remove runnable calls
     * */
    @Override
    protected void onPause() {
        super.onPause();
        stopRepeatingTask();
        checkVisibility();
    }

    @Override
    protected void onResume() {
        super.onResume();
        play.setImageResource(R.drawable.play);
        play_and_pause = false;
    }

    /*
     *       remove runnable calls
     * */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopRepeatingTask();
        isFirstPosition = true;
        if (marker != null) {
            marker.remove();
            is2D = true;
            ANIMATION_TIME_PER_ROUTE = 3000;
            DELAY = 3500;
        }
    }


    /*
     *       override method for change map style.
     * */
    @Override
    public void onChangeStyleClicked(String key) {
        try {
            if (key.equals(PreferenceUtils.DARK)) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.dark));
            } else if (key.equals(PreferenceUtils.NIGHT)) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.night));
            } else if (key.equals(PreferenceUtils.RETRO)) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.retro));
            } else if (key.equals(PreferenceUtils.SILVER)) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.silver));
            } else if (key.equals(PreferenceUtils.TERRAIN)) {
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            } else if (key.equals(PreferenceUtils.SATELLITE)) {
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            } else if (key.equals(PreferenceUtils.HYBRID)) {
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            } else {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivityRaw", "Can't find style.", e);
        }
    }


    /*
     *       handle 2d/3d visibility based on enable or disable in the settings screen.
     * */
    public void checkVisibility() {
        if (preferenceUtils.getBooleanFromPreferenceForSideBar(PreferenceUtils.IS_Map_Styles_Enable))
            style_card.setVisibility(View.VISIBLE);
        else
            style_card.setVisibility(View.GONE);
        if (preferenceUtils.getBooleanFromPreferenceForSideBar(PreferenceUtils.Is_2D_3D_Enable))
            view_card.setVisibility(View.VISIBLE);
        else
            view_card.setVisibility(View.GONE);
    }

    /*
     *       save the zoom levels in shared preference data.
     * */
    @Override
    public void onCameraIdle() {
        preferenceUtils.saveFloat(PreferenceUtils.ZOOM_LEVEL, mMap.getCameraPosition().zoom);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == LiveConstants.NAV_SETTINGS_CHANGED) {
                manageZoomVisibility();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getStrideData() {
        progressDoalog = new ProgressDialog(this);
        progressDoalog.setCancelable(false);
        progressDoalog.setMessage(getResources().getString(R.string.loading));
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.show();
        Call<JsonElement> callRetrofit = null;
        callRetrofit = Constants.service.downloadFile();
        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressDoalog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        String s = response.body().toString().substring(1, response.body().toString().length() - 1);
                        DeviceCapturingDateExtraction deviceCapturingDateExtraction = new DeviceCapturingDateExtraction();
                        List<StrideDetails> strides = deviceCapturingDateExtraction.constructNormalPacket(s);
                        Log.d(TAG, "stride size ==> " + strides.size());
                        ArrayList<GpsData> gpsData = strides.get(0).getGpsData();
                        ArrayList<LatLng> latLngs1 = new ArrayList<>();
                        for (GpsData g : gpsData) {
                            latLng = new LatLng(g.getLatitude(), g.getLongitude());
                            Log.d(TAG, "lat lngs ==> " + latLng);
                            latLngs1.add(latLng);
                        }
//        latlongs = LatLngFilter(latLngs1, 1);
                        latlongs = latLngs1;
                        Log.d(TAG, "lat lngs ==> size " + latlongs.size());

                        if (latlongs.size() > 0) {
                            CreatePolyLineOnly();
                            animate_camera(latlongs.get(0), mMap, context);
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDoalog.dismiss();
                message(StridePathActivity.this, getString(R.string.failed_to_connect_server));
                Log.d(TAG, "GeoFence Failure : " + t.getMessage());
            }
        });
    }


    public void downloadFile11() {
        String DownloadUrl = "http://intellitrack.in:3344/stridedata/getDocumentsFile";
        DownloadManager.Request request1 = new DownloadManager.Request(Uri.parse(DownloadUrl));
        request1.setVisibleInDownloadsUi(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request1.allowScanningByMediaScanner();
            request1.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        }
        request1.setDestinationInExternalPublicDir("", "File2.txt");
        DownloadManager manager1 = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Objects.requireNonNull(manager1).enqueue(request1);
        if (DownloadManager.STATUS_SUCCESSFUL == 8) {
            Toast.makeText(StridePathActivity.this, "download success ", Toast.LENGTH_LONG).show();
        }
    }

    private void testAlert() {
        LatLng mark = new LatLng(17.405333832717606, 78.54436945170164);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mark));
        circle = mMap.addCircle(new CircleOptions()
                .center(mark)
                .radius(100)
                .strokeColor(Color.TRANSPARENT));
    }

    private void testCreateAlert(LatLng latLng) {
        double distance = getDistanceInMeters(latLng.latitude, latLng.longitude, 17.405333832717606, 78.54436945170164);
        if (distance <= 100 && !isNotificationShowed) {
            showNotification();
            isNotificationShowed = true;
            Toast.makeText(getBaseContext(), "Inside, distance from center: " + distance + " radius: " + circle.getRadius(), Toast.LENGTH_LONG).show();
        }
    }

    public void showNotification() {
        String channelId = "channel-01";
        String channelName = "Channel Name";

//        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.customnotification);
        Intent intent = new Intent(this, TripCreationActivity.class);                                        // changed from homeactivitynew to stridestartactivity for common code.
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_message_black_24dp)
                .setContentTitle("Vehicle Reached")
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentText("Your vehicle reaching your saved location..")
//                .setContent(remoteViews)
                .setAutoCancel(true);


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
}
