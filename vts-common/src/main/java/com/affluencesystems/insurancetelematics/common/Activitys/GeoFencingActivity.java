package com.affluencesystems.insurancetelematics.common.Activitys;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.affluencesystems.insurancetelematics.common.ApiUtils.Constants;
import com.affluencesystems.insurancetelematics.common.Models.GeoFenceResponse;
import com.affluencesystems.insurancetelematics.R;
import com.affluencesystems.insurancetelematics.common.Models.GeoResponse;
import com.affluencesystems.insurancetelematics.common.Models.PersonGeoFenceMap;
import com.affluencesystems.insurancetelematics.common.Models.RadiusLatLng;
import com.affluencesystems.insurancetelematics.common.Utils.GeoFencingCommunicator;
import com.affluencesystems.insurancetelematics.common.Utils.LiveConstants;
import com.affluencesystems.insurancetelematics.common.Utils.PreferenceUtils;
import com.affluencesystems.insurancetelematics.common.adapters.GeoFencingViewPagerAdapter;
import com.affluencesystems.insurancetelematics.common.fragments.AlertForSecondFence;
import com.affluencesystems.insurancetelematics.common.fragments.AllGeoFences;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.Color.rgb;

public class GeoFencingActivity extends Base_activity implements GeoFencingCommunicator, View.OnClickListener {

    private String TAG = "GeoFencingActivity";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private GeoFencingViewPagerAdapter geoFencingViewPagerAdapter;
    private int applicationKey = LiveConstants.PERSONAL_APP;
    private ProgressDialog progressDoalog;
    public static LinkedHashMap<String, GeoResponse> geoFenceHash;
    private GeoResponse geoResponse;
    public static ArrayList<RadiusLatLng> radiusLatLngs = new ArrayList<>();
    public static ArrayList<LatLng> customLatLngs = new ArrayList<>();
    private String fenceName;
    private ImageView delete, update, back_btn;
    private DialogFragment allFencesDialog;
    private String visibleKey;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_fencing2);
        Intent intent = getIntent();
        applicationKey = intent.getIntExtra(LiveConstants.APPLICATION_KEY, LiveConstants.PERSONAL_APP);
//        init();
        headerControls();
        getAllGeoFences();
    }


    /*
     * To initiate tabs and view pager and assign adapter.
     * */
    private void init(ArrayList<GeoResponse> geoResponses) {
        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewPager);
        separateGeoFences(geoResponses);
        geoFencingViewPagerAdapter = new GeoFencingViewPagerAdapter(getSupportFragmentManager(), applicationKey, geoFenceHash);
        if (applicationKey == LiveConstants.PERSONAL_APP) {
            tabLayout.setVisibility(View.VISIBLE);
        } else {
            tabLayout.setVisibility(View.GONE);
        }
        viewPager.setAdapter(geoFencingViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        progressDoalog.dismiss();
    }


    private void separateGeoFences(ArrayList<GeoResponse> geoResponses) {
        geoFenceHash = new LinkedHashMap<>();

        if(applicationKey == LiveConstants.PERSONAL_APP)
            handleItemVisibles(geoResponses.size());

        for (GeoResponse geoResponse : geoResponses) {
            if (geoResponse != null)
                geoFenceHash.put(geoResponse.getFenceName(), geoResponse);
        }
    }


    /*
     *       To initiate all the view from xml file
     * */
    public void headerControls() {
        TextView header_text;
        ImageView back_button;
        header_text = findViewById(R.id.header_text);
        back_button = findViewById(R.id.back_button);
        delete = findViewById(R.id.delete);
        update = findViewById(R.id.update);
        back_btn = findViewById(R.id.back_btn);
        header_text.setText(R.string.geo_fencing);
        header_text.setVisibility(View.VISIBLE);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        delete.setOnClickListener(this);
        update.setOnClickListener(this);
        back_btn.setOnClickListener(this);

    }

    @Override
    public void modifyFence(String fenceName, String updateKey) {
        Log.d(TAG, "Fence Name ===> " + fenceName);
        geoResponse = geoFenceHash.get(fenceName);
        if (LiveConstants.FENCE_DELETE.equals(updateKey)) {
            deleteGeoFence(geoResponse.getFenceId(), fenceName);
        } else {
            this.visibleKey = LiveConstants.FENCE_FOR_UPDATE;
            geofencingRadiusFragment.focusFence(geoResponse.getRadiusLatAndLong()/*.get(0).getLatitude(), geoResponse.getRadiusLatAndLong().get(0).getLongitude()*/, LiveConstants.FENCE_FOR_UPDATE, fenceName);
            geofencingCustomFragment.focusFence(geoResponse.getCustomLatAndLong(), LiveConstants.FENCE_FOR_UPDATE, fenceName, geoResponse.getFenceId());
            delete.setVisibility(View.GONE);
            update.setVisibility(View.GONE);
            back_btn.setVisibility(View.VISIBLE);
            allFencesDialog.dismiss();
        }
    }

    @Override
    public void moveNextFragment(int fragmentNumber) {
        viewPager.setCurrentItem(fragmentNumber);
    }

    @Override
    public void openAlert(String fenceName, ArrayList<RadiusLatLng> radiusLatLngs, ArrayList<LatLng> customLatlngs, boolean isForUpdate) {
        if (fenceName != null && !fenceName.equals(""))
            this.fenceName = fenceName;
        if (radiusLatLngs != null && customLatlngs != null) {
            setGeoFence();
        } else if (radiusLatLngs != null) {
            this.radiusLatLngs = radiusLatLngs;
            openAlertDialog(LiveConstants.FROM_RADIUS_FENCE);
        } else if (customLatlngs != null) {
            this.customLatLngs = customLatlngs;
            openAlertDialog(LiveConstants.FROM_CUSTOM_FENCE);
        }
    }

    @Override
    public void postGeoFence(boolean isForUpdate) {
        if (isForUpdate) {
            updateGeoFence();
        } else {
            setGeoFence();
        }
    }

    @Override
    public void getGeoFence(String fenceName, ArrayList<RadiusLatLng> radiusLatLngs, ArrayList<LatLng> customLatlngs, boolean isForUpdate) {
        if (fenceName != null && !fenceName.equals(""))
            this.fenceName = fenceName;
        if (radiusLatLngs != null)
            this.radiusLatLngs = radiusLatLngs;
        if (customLatlngs != null)
            this.customLatLngs = customLatlngs;
        if (isForUpdate) {
            updateGeoFence();
        } else {
            setGeoFence();
        }
    }

    @Override
    public void onBackPressed() {
        customLatLngs.clear();
        radiusLatLngs.clear();
        super.onBackPressed();
    }

    private void openAlertDialog(String key) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        DialogFragment dialogFragment = new AlertForSecondFence();
        Bundle bundle = new Bundle();
        bundle.putString(LiveConstants.FENCE_FROM_KEY, key);
        bundle.putString(LiveConstants._FENCE_UPDATE_KEY_, visibleKey);
        dialogFragment.setArguments(bundle);
        dialogFragment.show(ft, "dialog");
        dialogFragment.setCancelable(false);
    }


    private void openFencesDialog(String updateKey) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        allFencesDialog = new AllGeoFences();
        Bundle bundle = new Bundle();
        bundle.putSerializable(LiveConstants.GEO_FENCE_HASH, geoFenceHash);
        bundle.putString(LiveConstants.FENCE_UPDATE_KEY, updateKey);
        allFencesDialog.setArguments(bundle);
        allFencesDialog.show(ft, "dialog");
        allFencesDialog.setCancelable(false);
    }


    private void getAllGeoFences() {
        progressDoalog = new ProgressDialog(this);
        progressDoalog.setCancelable(false);
        progressDoalog.setMessage(getString(R.string.fence_loading));
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.show();
        Call<ArrayList<GeoResponse>> callRetrofit = null;
        callRetrofit = Constants.service.checkIsInGeoFencing(preferenceUtils.getStringFromPreference(PreferenceUtils.PERSON_ID, ""));
        callRetrofit.enqueue(new Callback<ArrayList<GeoResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<GeoResponse>> call, Response<ArrayList<GeoResponse>> response) {
//                progressDoalog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        ArrayList<GeoResponse> geoFenceResponses = response.body();
                        init(geoFenceResponses);
                    }
                } else {
                    ArrayList<GeoResponse> geoFenceResponses = new ArrayList<>();
                    init(geoFenceResponses);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<GeoResponse>> call, Throwable t) {
                progressDoalog.dismiss();
                ArrayList<GeoResponse> geoFenceResponses = new ArrayList<>();
                init(geoFenceResponses);
//                addDummyLatLng();
                message(GeoFencingActivity.this, getString(R.string.failed_to_connect_server));
                Log.d(TAG, "GeoFence Failure : " + t.getMessage());
            }
        });
    }


    private void setGeoFence() {
        geoResponse = new GeoResponse(fenceName, radiusLatLngs, customLatLngs);
        progressDoalog = new ProgressDialog(this);
        progressDoalog.setCancelable(false);
        progressDoalog.setMessage(getString(R.string.please_wait));
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.show();
        Call<GeoResponse> callRetrofit = null;
        callRetrofit = Constants.service.postGeoFencing(geoResponse);
        callRetrofit.enqueue(new Callback<GeoResponse>() {
            @Override
            public void onResponse(Call<GeoResponse> call, Response<GeoResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        geoResponse = response.body();
                        personGeoFenceMapping();
                    }
                } else {
                    message(GeoFencingActivity.this, getString(R.string.failed_to_connect_server));
                }
            }

            @Override
            public void onFailure(Call<GeoResponse> call, Throwable t) {
                progressDoalog.dismiss();
                message(GeoFencingActivity.this, getString(R.string.failed_to_connect_server));
                Log.d(TAG, "GeoFence Failure : " + t.getMessage());
            }
        });
    }

    private void personGeoFenceMapping() {
        PersonGeoFenceMap personGeoFenceMap = new PersonGeoFenceMap(preferenceUtils.getStringFromPreference(PreferenceUtils.PERSON_ID, ""), geoResponse.getFenceId());
        Call<PersonGeoFenceMap> callRetrofit = null;
        callRetrofit = Constants.service.personGeoMap(personGeoFenceMap);
        callRetrofit.enqueue(new Callback<PersonGeoFenceMap>() {
            @Override
            public void onResponse(Call<PersonGeoFenceMap> call, Response<PersonGeoFenceMap> response) {
                progressDoalog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        geofencingRadiusFragment.addRadiusFenceUi();
                        geofencingRadiusFragment.handleAddVisible();

                        geofencingCustomFragment.creatPolygone(geoResponse.getFenceId(), geoResponse.getCustomLatAndLong(), "");
                        geofencingCustomFragment.handleAddVisible();

                        fenceName = null;
                        radiusLatLngs.clear();
                        customLatLngs.clear();

                        geoFenceHash.put(geoResponse.getFenceName(), geoResponse);

                        handleItemVisibles(geoFenceHash.size());
                    }
                } else {
                    message(GeoFencingActivity.this, getString(R.string.failed_to_connect_server));
                }
            }

            @Override
            public void onFailure(Call<PersonGeoFenceMap> call, Throwable t) {
                progressDoalog.dismiss();
                message(GeoFencingActivity.this, getString(R.string.failed_to_connect_server));
                Log.d(TAG, "GeoFence Failure : " + t.getMessage());
            }
        });
    }


    private void deleteGeoFence(String fenceId, final String fenceName) {
        progressDoalog = new ProgressDialog(this);
        progressDoalog.setCancelable(false);
        progressDoalog.setMessage(getString(R.string.please_wait));
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.show();
        Call<JsonElement> callRetrofit = null;
        callRetrofit = Constants.service.deleteGeoFence(fenceId);
        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressDoalog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Toast.makeText(GeoFencingActivity.this, R.string.delete_success, Toast.LENGTH_LONG).show();
                        allFencesDialog.dismiss();
                        geoFenceHash.remove(fenceName);
                        geofencingCustomFragment.addGeoFences("");
                        geofencingRadiusFragment.addGeoFences(new LatLng(0, 0));
                        handleItemVisibles(geoFenceHash.size());
                    }
                } else {
                    message(GeoFencingActivity.this, getString(R.string.failed_to_connect_server));
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDoalog.dismiss();
                message(GeoFencingActivity.this, getString(R.string.failed_to_connect_server));
                Log.d(TAG, "GeoFence Failure : " + t.getMessage());
            }
        });
    }


    private void updateGeoFence() {

        if (customLatLngs.size() == 0) {
            customLatLngs.addAll(geoResponse.getCustomLatAndLong());
        }

        if (radiusLatLngs.size() == 0) {
            radiusLatLngs.addAll(geoResponse.getRadiusLatAndLong());
        }

        GeoResponse geoResponseNew = new GeoResponse(fenceName, geoResponse.getFenceId(), "", radiusLatLngs, customLatLngs);

        progressDoalog = new ProgressDialog(this);
        progressDoalog.setCancelable(false);
        progressDoalog.setMessage(getString(R.string.please_wait));
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.show();

        Call<GeoResponse> callRetrofit = null;
        callRetrofit = Constants.service.updateGeoFencing(geoResponseNew);
        callRetrofit.enqueue(new Callback<GeoResponse>() {
            @Override
            public void onResponse(Call<GeoResponse> call, Response<GeoResponse> response) {
                progressDoalog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Toast.makeText(GeoFencingActivity.this, R.string.update_success, Toast.LENGTH_LONG).show();
                        geoFenceHash.remove(geoResponse.getFenceName());
                        geoFenceHash.put(response.body().getFenceName(), response.body());
                        geofencingCustomFragment.addGeoFences("");
                        geofencingRadiusFragment.addGeoFences(new LatLng(0, 0));
                        handleItemVisibles(geoFenceHash.size());

                        geofencingCustomFragment.handleAddVisible();
                        geofencingRadiusFragment.handleAddVisible();

                        radiusLatLngs.clear();
                        customLatLngs.clear();

                        back_btn.setVisibility(View.GONE);
                    }
                } else {
                    message(GeoFencingActivity.this, getString(R.string.failed_to_connect_server));
                }
            }

            @Override
            public void onFailure(Call<GeoResponse> call, Throwable t) {
                progressDoalog.dismiss();
                message(GeoFencingActivity.this, getString(R.string.failed_to_connect_server));
                Log.d(TAG, "GeoFence Failure : " + t.getMessage());
            }
        });
    }

    private void handleItemVisibles(int size) {
        if (size > 0) {
            delete.setVisibility(View.VISIBLE);
            update.setVisibility(View.VISIBLE);
        } else {
            delete.setVisibility(View.GONE);
            update.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.delete) {
            openFencesDialog(LiveConstants.FENCE_DELETE);
        } else if (view.getId() == R.id.update) {
            openFencesDialog(LiveConstants.FENCE_UPDATE);
        } else if(view.getId() == R.id.back_btn) {
            geofencingCustomFragment.addGeoFences("");
            geofencingCustomFragment.handleAddVisible();
            geofencingRadiusFragment.addGeoFences(new LatLng(0,0));
            geofencingRadiusFragment.handleAddVisible();
            handleItemVisibles(geoFenceHash.size());
            back_btn.setVisibility(View.GONE);
        }
    }
}
