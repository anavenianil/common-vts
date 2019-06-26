package com.affluencesystems.insurancetelematics.common.fragments;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
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
import com.affluencesystems.insurancetelematics.common.Utils.MapUtils;
import com.affluencesystems.insurancetelematics.common.Utils.PreferenceUtils;
import com.affluencesystems.insurancetelematics.common.ApiUtils.Constants;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

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

public class GeofencingCustomFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener, View.OnClickListener, Base_activity.CustomItemListener {
    private static final String TAG = "GeoCustomFragment";
    private static final int REQUEST_LOCATION_PERMISSION_CODE = 101;
    private static final long ANIMATION_TIME_PER_ROUTE = 3000;
    public ArrayList<LatLng> custome_poinrts_lat_longs;
    static ValueAnimator valueAnimator;
    private boolean isFirst_curent_latlong = true;
    static LatLng updated_curent_latlong, latlong_from, latlong_to;
    private static Marker marker;
    private static double lat;
    private static double lng;
    private static float v;
    private static LatLng startPosition;
    private static GoogleMap mMap;
    ImageView navigation, ok_, cancel_, delete_, add;
    private GeoFenceResponse geoFenceResponse;
    GeoFencingCommunicator callback;
    private boolean isAddClicked = false;
    MapView mMapView;
    PopupWindow popupWindow;
    View v1;
    private EditText et_fence_name;
    private ProgressDialog progressDoalog;
    private static PreferenceUtils preferenceUtils;
    private static Context context;
    public static boolean is_picking_custome = true, is_poligon_on = false;
    Base_activity base_activity;
    private PolygonOptions polygonOptions;
    private int applicationKey = LiveConstants.PERSONAL_APP;
    private Dialog dialog;
    private String visibleKey;
    private String fenceName;

//    private HashMap<String, GeoResponse> geoResponseHash;


    /*
     *       It takes the current latlng and place the marker(vehicle) for the first time,
     *       then from the second time navigate the vehicle using startCarAnimation() method.
     * */
    public void location(LatLng latLng) {
        Log.d("latlong", " " + latLng);
        updated_curent_latlong = latLng;

        if (custome_poinrts_lat_longs != null && MapUtils.isPointInPolygon(latLng, custome_poinrts_lat_longs)) {
            Log.d(TAG, "It is in side");
        } else {
            Log.d(TAG, "it is out side");
        }

        if (isFirst_curent_latlong) {
            //  mMap.clear();
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

                //  move_Camera(newPos, mMap);
               /* mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                        .target(newPos)
                        .zoom(zoom_level)
                        //.bearing(30)
                        //.tilt(45)
                        .build()));*/

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
        View v = inflater.inflate(R.layout.fragment_geofencing_custom, container, false);
        base_activity = new Base_activity(this, this);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            applicationKey = bundle.getInt(LiveConstants.APPLICATION_KEY, LiveConstants.PERSONAL_APP);
        }

        preferenceUtils = new PreferenceUtils(getContext());
        v1 = new View(getContext());
        mMapView = v.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION_CODE);
        }
        navigation = (ImageView) v.findViewById(R.id.navigation);

        if(isContainsId(LiveConstants.PRIVILEGE_LIVE_TRACKING_KEY, context)){
            navigation.setVisibility(View.VISIBLE);
        } else {
            navigation.setVisibility(View.GONE);
        }
        navigation.setVisibility(View.VISIBLE);

        ok_ = (ImageView) v.findViewById(R.id.ok);
        cancel_ = (ImageView) v.findViewById(R.id.cancel);
        delete_ = v.findViewById(R.id.delete);
        add = (ImageView) v.findViewById(R.id.add);
        add.setOnClickListener(this);
//        option_buttons = v.findViewById(R.id.option_buttons);
        navigation.setOnClickListener(this);
        ok_.setOnClickListener(this);
        cancel_.setOnClickListener(this);
        delete_.setOnClickListener(this);

        if (applicationKey == LiveConstants.DRIVER_APP) {
            add.setVisibility(View.GONE);
        } else if (applicationKey == LiveConstants.PERSONAL_APP) {
            add.setVisibility(View.VISIBLE);
        }

//        geoResponseHash = (HashMap<String, GeoResponse>) getArguments().getSerializable(LiveConstants.GEO_FENCE_HASH);

        popupWindow = new PopupWindow(getContext());
       /* if (isConnected()) {
//            progressDoalog = new ProgressDialog(getActivity());
//            progressDoalog.setCancelable(false);
//            progressDoalog.setMessage(getString(R.string.fence_loading));
//            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progressDoalog.show();
//            checkIsInGeoFencingService();
        } else {
            addDummyLatLng();
        }*/

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
                        //.bearing(30)
                        //.tilt(45)
                        .build()));

            }

        } else if (i == R.id.ok) {/*
         *       create geo fence and save/update in server.
         * */
            if (custome_poinrts_lat_longs != null && custome_poinrts_lat_longs.size() < 3) {
                Toast.makeText(getContext(), R.string.select_3points, Toast.LENGTH_SHORT).show();
                return;
            }
            if (radiusLatLngs.size() > 0) {
                if (custome_poinrts_lat_longs != null && custome_poinrts_lat_longs.size() >= 3) {
                    if (LiveConstants.FENCE_FOR_UPDATE.equals(visibleKey)) {
                        callback.getGeoFence(null, null, custome_poinrts_lat_longs, true);
                    } else {
                        callback.getGeoFence(null, null, custome_poinrts_lat_longs, false);
                    }
                    custome_poinrts_lat_longs = null;
                    ok_.setVisibility(View.GONE);
                    cancel_.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getContext(), R.string.select_3points, Toast.LENGTH_SHORT).show();
                }
            } else {
                radiusDialog().show();
            }

        } else if (i == R.id.cancel) {/*
         *       remove all the fence markers.
         * */
            clearMap();
            custome_poinrts_lat_longs.clear();
            ok_.setVisibility(View.GONE);
            cancel_.setVisibility(View.GONE);
            customLatLngs.clear();
            if (!LiveConstants.FENCE_FOR_UPDATE.equals(visibleKey)) {
                isAddClicked = false;
                add.setVisibility(View.VISIBLE);
            }
        } else if (i == R.id.add) {/*
         *       make user to put markers.
         * */
            add.setVisibility(View.GONE);
            isAddClicked = true;
        }
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

    public void addCustomLatLngs(ArrayList<LatLng> latLngs) {
        custome_poinrts_lat_longs = new ArrayList<>();
        custome_poinrts_lat_longs.addAll(latLngs);
    }


    public Dialog radiusDialog() {
        dialog.setContentView(R.layout.geo_fence_radius_layout);
        et_fence_name = (EditText) dialog.findViewById(R.id.et_fence_name);
        TextView ok = (TextView) dialog.findViewById(R.id.ok);
        EditText et_radius = dialog.findViewById(R.id.radius);
        et_radius.setVisibility(View.GONE);

        if (LiveConstants.FENCE_FOR_UPDATE.equals(visibleKey)) {
            et_fence_name.setText(fenceName);
        }

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_fence_name.getText().toString().trim().equals("")) {
                    Toast.makeText(context, R.string.enter_fence_name, Toast.LENGTH_LONG).show();
                } else {
                    if (LiveConstants.FENCE_FOR_UPDATE.equals(visibleKey)) {
                        callback.openAlert(et_fence_name.getText().toString().trim(), null, custome_poinrts_lat_longs, true);
                    } else {
                        callback.openAlert(et_fence_name.getText().toString().trim(), null, custome_poinrts_lat_longs, false);
                    }
                    ok_.setVisibility(View.GONE);
                    cancel_.setVisibility(View.GONE);
                    dialog.dismiss();
                }
            }

        });
        return dialog;
    }

    private void clearMap() {
        addGeoFences("");
        if (updated_curent_latlong != null) {
            marker.remove();
            marker = carMarker(mMap, updated_curent_latlong, context, preferenceUtils.getIntFromPreference(PreferenceUtils.VEHICLE_CODE, R.drawable.white_car_with_radius));
        }
    }

    @Override
    public void onMapClick(LatLng point) {
        if (isAddClicked) {
            if (custome_poinrts_lat_longs == null)
                custome_poinrts_lat_longs = new ArrayList<>();

            custome_poinrts_lat_longs.add(point);
            marker(mMap, point, R.drawable.marker_blue);
            Log.d(TAG, "points ===> " + point.latitude + " ==> " + point.longitude);
            ok_.setVisibility(View.VISIBLE);
            cancel_.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setUpMap();
//        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
        addDummyLatLng();
        addGeoFences("");
        mMap.setMinZoomPreference(11.0f);
        mMap.setMaxZoomPreference(18.0f);
        delete_.setVisibility(View.GONE);
    }


    public void addGeoFences(String fenceIdForUpdate) {
        if (geoFenceHash.values().size() > 0) {
            mMap.clear();
            for (GeoResponse geoFenceResponse : geoFenceHash.values()) {
                ArrayList<LatLng> placeLatLngs = geoFenceResponse.getCustomLatAndLong();
                if (placeLatLngs.size() != 0) {
                    creatPolygone(geoFenceResponse.getFenceId(), placeLatLngs, fenceIdForUpdate);
                }
            }
        } else {
            mMap.clear();
            addDummyLatLng();
        }
    }


    /*
     *       click event on google map
     * */
    private void setUpMap() {
        mMap.setOnMapClickListener((GoogleMap.OnMapClickListener) this);
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
//        marker = marker(mMap, dummyLatLng, R.mipmap.car_white_three);
    }


    public void creatPolygone(String fenceId, ArrayList<LatLng> latLngs, String fenceIdForUpdate) {
        if (latLngs.size() > 0) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            polygonOptions = new PolygonOptions();
            for (int i = 0; i < latLngs.size(); i++) {
                marker(mMap, latLngs.get(i), R.drawable.marker_blue);
                builder.include(latLngs.get(i));
                polygonOptions.add(latLngs.get(i));
            }
            LatLngBounds bounds = builder.build();

            if (latLngs.size() >= 3) {
                if (fenceId.equals(fenceIdForUpdate)) {
                    polygonOptions.strokeColor(rgb(255, 0, 0));
                    polygonOptions.strokeWidth((float) 3);
                    polygonOptions.fillColor(Color.argb(100, 255, 0, 0));
                } else {
                    polygonOptions.strokeColor(rgb(91, 184, 234));
                    polygonOptions.strokeWidth((float) 3);
                    polygonOptions.fillColor(Color.argb(100, 142, 178, 237));
                }
                mMap.addPolygon(polygonOptions);
                if (!is_poligon_on) {
                    is_poligon_on = true;
                }
                int padding = 100;
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                mMap.animateCamera(cu);

            } else {
                Toast.makeText(getContext(), R.string.select_3points, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void focusFence(ArrayList<LatLng> latLngs, String visibleKey, String fenceName, String fenceId) {
        if (latLngs.size() > 0) {
            addGeoFences(fenceId);
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            polygonOptions = new PolygonOptions();
            for (int i = 0; i < latLngs.size(); i++) {
                marker(mMap, latLngs.get(i), R.drawable.marker_blue);
                builder.include(latLngs.get(i));
            }
            LatLngBounds bounds = builder.build();
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 100);
            mMap.animateCamera(cu);
        }
        this.visibleKey = visibleKey;
        this.fenceName = fenceName;
        isAddClicked = true;
        add.setVisibility(View.GONE);
        navigation.setVisibility(View.GONE);
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
     *       Live tracking will show when we tap on custom geo fence tab clicked.
     * */
    @Override
    public void onCustomTabClicked(LatLng latLng) {
        location(latLng);
        Log.d(TAG, "custom fence : lat " + latLng.latitude + " long" + latLng.longitude);
        updated_curent_latlong = latLng;
    }
}
