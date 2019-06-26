package com.affluencesystems.insurancetelematics.common.fragments;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.affluencesystems.insurancetelematics.common.Activitys.Base_activity;
import com.affluencesystems.insurancetelematics.common.Models.GeoFenceResponse;
import com.affluencesystems.insurancetelematics.common.Models.GeoResponse;
import com.affluencesystems.insurancetelematics.common.Models.RadiusLatLng;
import com.affluencesystems.insurancetelematics.R;
import com.affluencesystems.insurancetelematics.common.Utils.CheckActivityStatus;
import com.affluencesystems.insurancetelematics.common.Utils.GeoFencingCommunicator;
import com.affluencesystems.insurancetelematics.common.Utils.LiveConstants;
import com.affluencesystems.insurancetelematics.common.Utils.PreferenceUtils;
import com.affluencesystems.insurancetelematics.common.ApiUtils.Constants;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.Color.rgb;
import static com.affluencesystems.insurancetelematics.common.Activitys.GeoFencingActivity.customLatLngs;
import static com.affluencesystems.insurancetelematics.common.Activitys.GeoFencingActivity.geoFenceHash;
import static com.affluencesystems.insurancetelematics.common.Activitys.GeoFencingActivity.radiusLatLngs;
import static com.affluencesystems.insurancetelematics.common.Utils.ConnectivityReceiver.isConnected;
import static com.affluencesystems.insurancetelematics.common.Utils.MapUtils.carMarker;
import static com.affluencesystems.insurancetelematics.common.Utils.MapUtils.getBearing;
import static com.affluencesystems.insurancetelematics.common.Utils.MapUtils.isContainsId;
import static com.affluencesystems.insurancetelematics.common.Utils.MapUtils.marker;
import static com.affluencesystems.insurancetelematics.common.Utils.MapUtils.move_Camera;

public class GeofencingRadiusFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener, View.OnClickListener, Base_activity.RadiusItemListener {
    private static final String TAG = "GeoRadiusFragment";
    private static final int REQUEST_LOCATION_PERMISSION_CODE = 101;
    private static final long ANIMATION_TIME_PER_ROUTE = 3000;
    public static LatLng latLng = null;
    public static Dialog dialog;
    static ValueAnimator valueAnimator;
    boolean isFirst_curent_latlong = true;
    static LatLng updated_curent_latlong, latlong_from, latlong_to;
    private static Marker marker;
    private Marker fenceMarker;
    private static double lat;
    private static double lng;
    private static float v;
    private static GoogleMap mMap;
    ImageView navigation, ok_, cancel_, delete_, add, cancel_image_btn, update;
    TextView ok;
    LinearLayout car_location, pick_location;
    EditText et_radius, et_fence_name;
    // SupportMapFragment mapFragment;
    MapView mMapView;
    float radius;
    View v1;
    private static PreferenceUtils preferenceUtils;
    private static Context context;
    boolean is_radiul_geofence_on = false;
    Base_activity base_activity;
    private boolean isAddClicked = false;
    private String visibleKey;
    private String fenceName;


//    private HashMap<String, GeoResponse> geoResponseHash;

    /*
     *       interface for communicate geo fence fragments.
     * */
    GeoFencingCommunicator callback;


    /*
     *       It takes the current latlng and place the marker(vehicle) for the first time,
     *       then from the second time navigate the vehicle using startCarAnimation() method.
     * */
    public void location(LatLng latLng) {
        Log.d("latlong", " " + latLng);
        updated_curent_latlong = latLng;

        if (isFirst_curent_latlong) {
            latlong_from = latLng;
//            marker = marker(mMap, updated_curent_latlong, R.mipmap.car_white_three);
            marker = carMarker(mMap, updated_curent_latlong, context, preferenceUtils.getIntFromPreference(PreferenceUtils.VEHICLE_CODE, R.drawable.white_car_with_radius));
//            move_Camera(updated_curent_latlong, mMap, context);
            isFirst_curent_latlong = false;

        } else {
            latlong_to = latLng;
            if ((latlong_from.latitude != latlong_to.latitude) || (latlong_from.longitude != latlong_to.longitude)) {
                Log.e(TAG, "NOT SAME");
                startCarAnimation(latlong_from, latlong_to);
            }
        }
    }


    /*
     *       initialize interface.
     * */
    @Override
    public void onAttach(Context context) {
        this.context = context;
        callback = (GeoFencingCommunicator) context;
        super.onAttach(context);
    }


    /*
     *       To use animate vehicle in google map
     *       It takes previous latlng and current latlng,
     *       then navigate the vehicle.
     * */
    public static void startCarAnimation(final LatLng start, final LatLng end) {
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
                LatLng newPos = new LatLng(lat, lng);
                marker.setPosition(newPos);
                marker.setAnchor(0.5f, 0.5f);
                marker.setRotation(getBearing(start, end));
                latlong_from = marker.getPosition();
                marker.showInfoWindow();

            }

        });
        valueAnimator.start();

    }


    /*
     *       inflate layout file and initialize views
     * */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_geofencing_radius, container, false);
        base_activity = new Base_activity(this, this);

        preferenceUtils = new PreferenceUtils(getContext());
        v1 = new View(getContext());
        mMapView = v.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION_CODE);
        }
        navigation = (ImageView) v.findViewById(R.id.navigation);
        ok_ = (ImageView) v.findViewById(R.id.ok);
        cancel_ = (ImageView) v.findViewById(R.id.cancel);
        add = (ImageView) v.findViewById(R.id.add);
        add.setOnClickListener(this);
        delete_ = v.findViewById(R.id.delete);
        update = v.findViewById(R.id.update);

        if(isContainsId(LiveConstants.PRIVILEGE_LIVE_TRACKING_KEY, context)){
            navigation.setVisibility(View.VISIBLE);
        } else {
            navigation.setVisibility(View.GONE);
        }

        navigation.setOnClickListener(this);
        ok_.setOnClickListener(this);
        cancel_.setOnClickListener(this);
        delete_.setOnClickListener(this);
        update.setOnClickListener(this);
//        geoResponseHash = (HashMap<String, GeoResponse>) getArguments().getSerializable(LiveConstants.GEO_FENCE_HASH);
        ok_and_cancle_gone();
        delete_.setVisibility(View.GONE);
        dialog = new Dialog(getContext());
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });

        return v;
    }


    public void addGeoFences(LatLng latLngForUpdate) {
        if (geoFenceHash.values().size() > 0) {
            mMap.clear();
            for (GeoResponse g : geoFenceHash.values()) {
                if (g.getRadiusLatAndLong().size() > 0) {
                    RadiusLatLng radiusLatAndLong = g.getRadiusLatAndLong().get(0);
                    radius = (float) radiusLatAndLong.getRadiusDistance();
                    latLng = new LatLng(radiusLatAndLong.getLatitude(), radiusLatAndLong.getLongitude());
                    marker(mMap, latLng, R.drawable.marker_blue);
                    if(latLngForUpdate.equals(latLng)) {
                        mMap.addCircle(new CircleOptions()
                                .center(latLng)
                                .radius(radius * 1000)
                                .strokeColor(rgb(255, 0, 0)).strokeWidth(3).fillColor(Color.argb(100, 255, 0, 0)));
                    } else {
                        mMap.addCircle(new CircleOptions()
                                .center(latLng)
                                .radius(radius * 1000)
                                .strokeColor(rgb(91, 184, 234)).strokeWidth(3).fillColor(Color.argb(100, 142, 178, 237)));
                    }
                    delete_.setVisibility(View.VISIBLE);
                    update.setVisibility(View.VISIBLE);
                    CameraUpdate center = CameraUpdateFactory.newLatLng(latLng);
                    mMap.animateCamera(center);
                }
            }
        } else {
            mMap.clear();
            addDummyLatLng();
        }
    }

    @Override
    public void onClick(View v) {
        v1 = v;
        int i = v.getId();
        if (i == R.id.navigation) {/*
         *       move camera for current vehicle location
         * */
            if (updated_curent_latlong != null) {
                marker.remove();
                marker = carMarker(mMap, updated_curent_latlong, context, preferenceUtils.getIntFromPreference(PreferenceUtils.VEHICLE_CODE, R.drawable.white_car_with_radius));
                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                        .target(updated_curent_latlong)
                        .zoom(preferenceUtils.getFloatFromPreference(PreferenceUtils.ZOOM_LEVEL, 17))
                        .build()));
            }

        } else if (i == R.id.ok) {/*
         *       create geo fence and save/update in server.
         * */
            radiusDialog().show();

        } else if (i == R.id.cancel) {/*
         *       remove all the fence markers.
         * */
            clearMap();
            option_dialog().dismiss();
            is_radiul_geofence_on = false;
            ok_.setVisibility(View.GONE);
            cancel_.setVisibility(View.GONE);
            radiusLatLngs.clear();
            if (!LiveConstants.FENCE_FOR_UPDATE.equals(visibleKey)) {
                isAddClicked = false;
                add.setVisibility(View.VISIBLE);
            }
        }  else if (i == R.id.add) {/*
         *       make user to put markers.
         * */
            if (!is_radiul_geofence_on) {
                isAddClicked = true;
                option_dialog().show();
            } else {
                option_dialog().dismiss();
                Toast.makeText(getContext(), R.string.already_in_fence, Toast.LENGTH_SHORT).show();
            }

        } else if (i == R.id.cancel_image_btn) {/*
         *       dismiss the km dialog.
         * */
            isAddClicked = false;
            option_dialog().dismiss();

        }
    }

    private void clearMap() {
        addGeoFences(new LatLng(0,0));
        if (updated_curent_latlong != null) {
            marker.remove();
            marker = carMarker(mMap, updated_curent_latlong, context, preferenceUtils.getIntFromPreference(PreferenceUtils.VEHICLE_CODE, R.drawable.white_car_with_radius));
        }
    }

    @Override
    public void onMapClick(LatLng point) {
        /*
         *       click event on google map
         *       if fence not added and add button not clicked,
         *       then we can't put the marker here.
         * */
        if (isAddClicked) {
            latLng = point;

            if (fenceMarker != null)
                fenceMarker.remove();

            fenceMarker = marker(mMap, point, R.drawable.marker_blue);
            ok_and_cancle_visibsle();
            Log.d(TAG, "points ===> " + point.latitude + " ==> " + point.longitude);
        }
    }


    /*
     *       map preferences added.
     * */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        radiusDialog().dismiss();
        mMap.setOnMapClickListener((GoogleMap.OnMapClickListener) this);
//        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
        addDummyLatLng();
        addGeoFences(new LatLng(0,0));
        mMap.setMinZoomPreference(11.0f);
        mMap.setMaxZoomPreference(18.0f);
    }

    /*
     *       returns a dialog, which gives a chance to enter kms.
     * */
    public Dialog radiusDialog() {
        dialog.setContentView(R.layout.geo_fence_radius_layout);
        et_fence_name = (EditText) dialog.findViewById(R.id.et_fence_name);
        ok = (TextView) dialog.findViewById(R.id.ok);
        et_radius = dialog.findViewById(R.id.radius);

        if (LiveConstants.FENCE_FOR_UPDATE.equals(visibleKey)) {
            et_fence_name.setText(fenceName);
        }

        if (customLatLngs.size() > 0 /*|| LiveConstants.FENCE_FOR_UPDATE.equals(visibleKey)*/) {
            et_fence_name.setVisibility(View.GONE);
        }

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_fence_name.getVisibility() == View.VISIBLE && et_fence_name.getText().toString().trim().equals("")) {
                    Toast.makeText(context, R.string.enter_fence_name, Toast.LENGTH_LONG).show();
                } else if (!et_radius.getText().toString().equals("") && !et_radius.getText().toString().equals(" ") && !et_radius.getText().toString().equals("null")) {
                    radius = Float.parseFloat(et_radius.getText().toString());
                    dialog.dismiss();
                    ok_.setVisibility(View.GONE);
                    cancel_.setVisibility(View.GONE);
                    RadiusLatLng radiusLatLng = new RadiusLatLng(radius, latLng.latitude, latLng.longitude);
                    ArrayList<RadiusLatLng> radiusLatLngs = new ArrayList<>();
                    radiusLatLngs.add(radiusLatLng);
                    boolean isFoUpdate = false;
                    if (LiveConstants.FENCE_FOR_UPDATE.equals(visibleKey)) {
                        isFoUpdate = true;
                    }
                    if (et_fence_name.getVisibility() == View.GONE) {
                        callback.getGeoFence(null, radiusLatLngs, null, isFoUpdate);
                    } else {
                        callback.openAlert(et_fence_name.getText().toString().trim(), radiusLatLngs, null, isFoUpdate);
                    }
                    fenceMarker = null;
//                    }
                } else {
                    Toast.makeText(context, R.string.enter_radius, Toast.LENGTH_LONG).show();
                }
                et_radius.setText("");
            }

        });
        return dialog;
    }

    public void handleAddVisible() {
        add.setVisibility(View.VISIBLE);
        navigation.setVisibility(View.VISIBLE);
        isAddClicked = false;
        visibleKey = null;
    }

    public void handleOkVisible() {
        ok_.setVisibility(View.VISIBLE);
        cancel_.setVisibility(View.VISIBLE);
    }

    public void addRadiusFenceUi() {
        mMap.addCircle(new CircleOptions()
                .center(new LatLng(latLng.latitude, latLng.longitude))
                .radius(radius * 1000)
                .strokeColor(rgb(91, 184, 234)).strokeWidth(3).fillColor(Color.argb(100, 142, 178, 237)));
        CameraUpdate center = CameraUpdateFactory.newLatLngZoom(latLng, 15);
        mMap.animateCamera(center);
    }

    public void focusFence(ArrayList<RadiusLatLng> radiusLatLngs, String visibleKey, String fenceName) {
        if (radiusLatLngs != null && radiusLatLngs.size() > 0) {
            LatLng latLng = new LatLng(radiusLatLngs.get(0).getLatitude(), radiusLatLngs.get(0).getLongitude());
            addGeoFences(latLng);
            CameraUpdate center = CameraUpdateFactory.newLatLngZoom(latLng, 15);
            mMap.animateCamera(center);
        }
        this.visibleKey = visibleKey;
        this.fenceName = fenceName;
        isAddClicked = true;
        add.setVisibility(View.GONE);
        navigation.setVisibility(View.GONE);
    }


    /*
     *       if latlngs are (0,0),
     *       Then add the dummy latlngs as office location.
     * */
    private void addDummyLatLng() {
        MarkerOptions markerOptions = new MarkerOptions();
        LatLng dummyLatLng = new LatLng(17.409995, 78.543898);
        mMap.clear();
        mMap.setMinZoomPreference(11.0f);
        mMap.setMaxZoomPreference(18.0f);
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                .target(dummyLatLng)
                .zoom(preferenceUtils.getFloatFromPreference(PreferenceUtils.ZOOM_LEVEL, 17)).build()));
    }



    /*
     *       After tap on add button, give two option dialog
     *       1. user can take particular location.
     *       2. take vehicle as centre point.
     * */
    public Dialog option_dialog() {
        dialog.setContentView(R.layout.geofence_radius_option_dialog);
        car_location = dialog.findViewById(R.id.car_location);
        pick_location = dialog.findViewById(R.id.pick_location);
        cancel_image_btn = dialog.findViewById(R.id.cancel_image_btn);
        cancel_image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAddClicked = false;
                add.setVisibility(View.VISIBLE);
                dialog.dismiss();
            }
        });
        car_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "updated lat lng " + updated_curent_latlong);

                if (updated_curent_latlong != null) {
                    marker(mMap, updated_curent_latlong, R.drawable.marker_blue);
                    ok_and_cancle_visibsle();
                    latLng = updated_curent_latlong;
                    move_Camera(updated_curent_latlong, mMap, context);
                    add.setVisibility(View.GONE);
                    dialog.dismiss();
                }
            }
        });
        pick_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!is_radiul_geofence_on) {
                    add.setVisibility(View.GONE);
                    option_dialog().dismiss();
                } else {
                    dialog.dismiss();
                    Toast.makeText(getContext(), R.string.already_in_fence, Toast.LENGTH_LONG).show();
                }
            }
        });
        return dialog;
    }

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
        CheckActivityStatus.geofencingResumed();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        CheckActivityStatus.geofencingPaused();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


    /*
     *       Live tracking will show when we tap on radius geo fence tab clicked.
     * */
    @Override
    public void onRadiusTabClicked(LatLng latLng) {
        location(latLng);
        updated_curent_latlong = latLng;
    }


    /*
     *       handle the view visibilities.
     * */
    private void ok_and_cancle_gone() {
        ok_.setVisibility(View.GONE);
        cancel_.setVisibility(View.GONE);
    }


    /*
     *       handle the view visibilities.
     * */
    private void ok_and_cancle_visibsle() {
        ok_.setVisibility(View.VISIBLE);
        cancel_.setVisibility(View.VISIBLE);
    }
}