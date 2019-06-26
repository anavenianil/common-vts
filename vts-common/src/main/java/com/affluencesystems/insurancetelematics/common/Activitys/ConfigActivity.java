package com.affluencesystems.insurancetelematics.common.Activitys;

import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.affluencesystems.insurancetelematics.R;
import com.affluencesystems.insurancetelematics.common.ApiUtils.Constants;
import com.affluencesystems.insurancetelematics.common.Models.ConfigPacket;
import com.affluencesystems.insurancetelematics.common.Models.ConfigSinglePacket;
import com.affluencesystems.insurancetelematics.common.Models.DeviceConfig;
import com.affluencesystems.insurancetelematics.common.Models.GeoResponse;
import com.affluencesystems.insurancetelematics.common.Utils.LiveConstants;
import com.affluencesystems.insurancetelematics.common.Utils.PreferenceUtils;
import com.affluencesystems.insurancetelematics.common.adapters.GeoFencesAdapter;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.affluencesystems.insurancetelematics.common.Utils.ConnectivityReceiver.isConnected;

public class ConfigActivity extends Base_activity implements View.OnClickListener, GeoFencesAdapter.AllPersonFences {

    private String TAG = "ConfigActivity";
    SeekBar et_speed_listener, et_harsh_breaking_listener, et_raah_acceleration_listener, et_rash_turning_listener;
    private EditText et_speed, et_harsh_breaking, et_raah_acceleration, et_rash_turning, et_geo_fence;
    private TextView text_update;
    private DeviceConfig deviceConfig;
    private ProgressDialog progressDoalog;
    private ImageView search_bar;
    private PreferenceUtils preferenceUtils;
    private int speedLimit, harshBreak, rashAcc, rashTurn;
    private ConfigSinglePacket speedLimitPacket, harshBreakPacket, rashAccelerationPacket, rashTurnPacket, fencesPacket;
    private RecyclerView rv_fences;

    private ArrayList<GeoResponse> allFences;
    private ArrayList<String> personFences;
    private ArrayList<String> packetKeys;
    private ArrayList<ConfigSinglePacket> packets;
    private HashMap<String, ConfigSinglePacket> packetsHash;
    private GeoFencesAdapter geoFencesAdapter;
    private boolean isFenceModified = false;
    private String strPersonFence = "";
    private String imei_number;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        preferenceUtils=new PreferenceUtils(this);
        imei_number=getIntent().getStringExtra("IMEI");

        headerControls();
        et_speed = (EditText) findViewById(R.id.et_speed);
        et_harsh_breaking = (EditText) findViewById(R.id.et_harsh_breaking);
        et_raah_acceleration = (EditText) findViewById(R.id.et_raah_acceleration);
        et_rash_turning = (EditText) findViewById(R.id.et_rash_turning);
        et_geo_fence = (EditText) findViewById(R.id.et_geo_fence);
        text_update = (TextView) findViewById(R.id.text_update);
        rv_fences = (RecyclerView) findViewById(R.id.rv_fences);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv_fences.setLayoutManager(mLayoutManager);
        rv_fences.setHasFixedSize(true);


        text_update.setOnClickListener(this);
        search_bar.setOnClickListener(this);

        progressDoalog = new ProgressDialog(context);
        progressDoalog.setCancelable(false);
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        packetKeys = new ArrayList<>();
        packetKeys.add(LiveConstants.OVER_SPEED_LIMIT_KEY);
        packetKeys.add(LiveConstants.HARSH_ACCELERATION_KEY);
        packetKeys.add(LiveConstants.HARSH_BREAKING_KEY);
        packetKeys.add(LiveConstants.RASH_TURNING_KEY);

        if (isConnected()) {
//            callForConfig();
            getAllFences();
        }
        et_speed_listener.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                                                         @Override
                                                         public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                                                             Log.d(TAG, "speed-->" + i);
                                                             et_speed.setText(String.valueOf(i));
                                                         }

                                                         @Override
                                                         public void onStartTrackingTouch(SeekBar seekBar) {

                                                         }

                                                         @Override
                                                         public void onStopTrackingTouch(SeekBar seekBar) {

                                                         }
                                                     }
        );
        et_harsh_breaking_listener.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.d(TAG, "harsh_breaking-->" + i);
                et_harsh_breaking.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setSeekBarColor(seekBar);
            }
        });
        et_raah_acceleration_listener.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.d(TAG, "rash_acceleration-->" + i);
                et_raah_acceleration.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setSeekBarColor(seekBar);

            }
        });
        et_rash_turning_listener.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.d(TAG, "harsh_turning-->" + i);
                et_rash_turning.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setSeekBarColor(seekBar);

            }
        });
    }


    //    To initiate all the view from xml file
    public void headerControls() {
        TextView header_text;
        ImageView back_button;
        header_text = (TextView) findViewById(R.id.header_text);
        back_button = (ImageView) findViewById(R.id.back_button);
        search_bar = (ImageView) findViewById(R.id.search_bar);
        header_text.setText(R.string.config);
        search_bar.setVisibility(View.GONE);
        search_bar.setImageResource(R.drawable.ic_refresh_black_24dp);
        header_text.setVisibility(View.VISIBLE);
        back_button.setOnClickListener(this);
        et_speed_listener = findViewById(R.id.et_speed_listener);
        et_harsh_breaking_listener = findViewById(R.id.et_harsh_breaking_listener);
        et_raah_acceleration_listener = findViewById(R.id.et_raah_acceleration_listener);
        et_rash_turning_listener = findViewById(R.id.et_rash_turning_listener);
    }


    private void getAllFences() {
        allFences = new ArrayList<>();
        progressDoalog = new ProgressDialog(this);
        progressDoalog.setCancelable(false);
        progressDoalog.setMessage(getResources().getString(R.string.loading));
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.show();
        Call<ArrayList<GeoResponse>> callRetrofit = null;
        callRetrofit = Constants.service.checkIsInGeoFencing(preferenceUtils.getStringFromPreference(PreferenceUtils.PERSON_ID, ""));
        callRetrofit.enqueue(new Callback<ArrayList<GeoResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<GeoResponse>> call, Response<ArrayList<GeoResponse>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        allFences = response.body();
                        if(allFences.size() > 0)
                            rv_fences.setVisibility(View.VISIBLE);
                        else
                            rv_fences.setVisibility(View.GONE);

                        callForConfig();
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<GeoResponse>> call, Throwable t) {
                progressDoalog.dismiss();
                message(ConfigActivity.this, getString(R.string.failed_to_connect_server));
                Log.d(TAG, "GeoFence Failure : " + t.getMessage());
            }
        });

    }


    /*
     *       Call for the vehicle configuration and set the values to progressbar
     * */
    private void callForConfig() {
        Call<DeviceConfig> callRetrofit = null;
        callRetrofit = Constants.service_config.getConfig("869696049430759" /*preferenceUtils.getStringFromPreference(PreferenceUtils.IMEI_NUMBER, "")*/);
        callRetrofit.enqueue(new Callback<DeviceConfig>() {
            @Override
            public void onResponse(Call<DeviceConfig> call, Response<DeviceConfig> response) {
                progressDoalog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        deviceConfig = response.body();

                        speedLimit = (int) Double.parseDouble(deviceConfig.getOverSpeedLimitThreshold());
                        harshBreak = (int) Double.parseDouble(deviceConfig.getHarshBrakingThreshold());
                        rashAcc = (int) Double.parseDouble(deviceConfig.getHarshAcclerationThreshold());
                        rashTurn = (int) Double.parseDouble(deviceConfig.getRashTurningThreshold());
                        personFences = deviceConfig.getFenceId();

                        strPersonFence = personFences.toString();


                        et_speed.setText(speedLimit + "");
                        et_speed_listener.setProgress(speedLimit);
                        setSeekBarColor(et_speed_listener);

                        et_harsh_breaking.setText(harshBreak + "");
                        et_harsh_breaking_listener.setProgress(harshBreak);
                        setSeekBarColor(et_harsh_breaking_listener);

                        et_raah_acceleration.setText(rashAcc + "");
                        et_raah_acceleration_listener.setProgress(rashAcc);
                        setSeekBarColor(et_raah_acceleration_listener);

                        et_rash_turning.setText(rashTurn + "");
                        et_rash_turning_listener.setProgress(rashTurn);
                        setSeekBarColor(et_rash_turning_listener);

                        geoFencesAdapter = new GeoFencesAdapter(ConfigActivity.this, allFences, personFences);
                        rv_fences.setAdapter(geoFencesAdapter);
                    }
                } else {
                    message(ConfigActivity.this, getString(R.string.config_not_found));
                }
            }

            @Override
            public void onFailure(Call<DeviceConfig> call, Throwable t) {
                progressDoalog.dismiss();
                message(ConfigActivity.this, getString(R.string.failed_to_connect_server));
            }
        });
    }


    private void getCacheData() {
        packets = new ArrayList<>();
        packetsHash = new HashMap<>();

        if (!checkModification(speedLimit, et_speed.getText().toString()) && !checkModification(harshBreak, et_harsh_breaking.getText().toString()) && !checkModification(rashAcc, et_raah_acceleration.getText().toString()) &&
                !checkModification(rashTurn, et_rash_turning.getText().toString()) && !isFenceModified) {
            Toast.makeText(ConfigActivity.this, "Config Not Modified...", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        progressDoalog.setMessage(getString(R.string.updating));
        progressDoalog.show();

        Call<ConfigPacket> callRetrofit = null;
        callRetrofit = Constants.service_config.getCacheConfig("869696049430759" /*preferenceUtils.getStringFromPreference(PreferenceUtils.IMEI_NUMBER, "")*/);
        callRetrofit.enqueue(new Callback<ConfigPacket>() {
            @Override
            public void onResponse(Call<ConfigPacket> call, Response<ConfigPacket> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        ConfigPacket packet = response.body();

                        if (packet.getGetSetParameters() != null) {
                            packets = packet.getGetSetParameters();
                            for (ConfigSinglePacket c : packets) {
                                packetsHash.put(c.getParameterName(), c);
                            }
                        }
                        updateConfig(LiveConstants.CONFIG_SET);
                    }
                } else {
                    progressDoalog.dismiss();
                    message(ConfigActivity.this, getString(R.string.config_not_found));
                }
            }

            @Override
            public void onFailure(Call<ConfigPacket> call, Throwable t) {
                progressDoalog.dismiss();
                message(ConfigActivity.this, getString(R.string.failed_to_connect_server));
            }
        });
    }

    /*
     *       we can change the vehicle configuration using progressbar, then we can update the values in server.
     *       updateConfig()  used to update config in server
     * */
    private void updateConfig(String key) {

        ArrayList<String> fences = new ArrayList<>();

        if (LiveConstants.CONFIG_SET.equals(key)) {

            if (checkModification(speedLimit, et_speed.getText().toString())) {
                speedLimitPacket = createModifiedPacket(fences, LiveConstants.OVER_SPEED_LIMIT_KEY, et_speed.getText().toString());
                packetsHash.put(LiveConstants.OVER_SPEED_LIMIT_KEY, speedLimitPacket);
            }

            if (checkModification(harshBreak, et_harsh_breaking.getText().toString())) {
                harshBreakPacket = createModifiedPacket(fences, LiveConstants.HARSH_BREAKING_KEY, et_harsh_breaking.getText().toString());
                packetsHash.put(LiveConstants.HARSH_BREAKING_KEY, harshBreakPacket);
            }

            if (checkModification(rashAcc, et_raah_acceleration.getText().toString())) {
                rashAccelerationPacket = createModifiedPacket(fences, LiveConstants.HARSH_ACCELERATION_KEY, et_raah_acceleration.getText().toString());
                packetsHash.put(LiveConstants.HARSH_ACCELERATION_KEY, rashAccelerationPacket);
            }

            if (checkModification(rashTurn, et_rash_turning.getText().toString())) {
                rashTurnPacket = createModifiedPacket(fences, LiveConstants.RASH_TURNING_KEY, et_rash_turning.getText().toString());
                packetsHash.put(LiveConstants.RASH_TURNING_KEY, rashTurnPacket);
            }

            if (isFenceModified) {
                packetsHash.put(LiveConstants.FENCES_KEY, fencesPacket);
            }
        } else {

            packetsHash = new HashMap<>();

            speedLimitPacket = createClearPacket(fences, LiveConstants.OVER_SPEED_LIMIT_KEY);
            packetsHash.put(LiveConstants.OVER_SPEED_LIMIT_KEY, speedLimitPacket);

            harshBreakPacket = createClearPacket(fences, LiveConstants.HARSH_BREAKING_KEY);
            packetsHash.put(LiveConstants.HARSH_BREAKING_KEY, harshBreakPacket);

            rashAccelerationPacket = createClearPacket(fences, LiveConstants.HARSH_ACCELERATION_KEY);
            packetsHash.put(LiveConstants.HARSH_ACCELERATION_KEY, rashAccelerationPacket);

            rashTurnPacket = createClearPacket(fences, LiveConstants.RASH_TURNING_KEY);
            packetsHash.put(LiveConstants.RASH_TURNING_KEY, rashTurnPacket);
        }

        ArrayList<ConfigSinglePacket> packets1 = new ArrayList<>();
        for (String s : packetsHash.keySet()) {
            packets1.add(packetsHash.get(s));
        }

        ConfigPacket configPacket = new ConfigPacket(packets1, "869696049430759");
        Call<DeviceConfig> callRetrofit = null;
        callRetrofit = Constants.service_config.updateConfig(configPacket);
        callRetrofit.enqueue(new Callback<DeviceConfig>() {
            @Override
            public void onResponse(Call<DeviceConfig> call, Response<DeviceConfig> response) {
                progressDoalog.dismiss();
                if (response.isSuccessful()) {
                    Toast.makeText(ConfigActivity.this, R.string.update_success, Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    progressDoalog.dismiss();
                    message(ConfigActivity.this, getString(R.string.failed_to_update));
                }
            }

            @Override
            public void onFailure(Call<DeviceConfig> call, Throwable t) {
                progressDoalog.dismiss();
                message(ConfigActivity.this, getString(R.string.failed_to_connect_server));
            }
        });
    }


    private boolean checkModification(int actualValue, String compareValue) {
        boolean isModified = false;
        try {
            if (actualValue != Integer.parseInt(compareValue)) {
                isModified = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isModified;
    }

    private ConfigSinglePacket getSinglePacketKeyBased(String key) {
        ConfigSinglePacket packet = null;
        if (LiveConstants.OVER_SPEED_LIMIT_KEY.equals(key)) {
            packet = speedLimitPacket;
        } else if (LiveConstants.HARSH_ACCELERATION_KEY.equals(key)) {
            packet = rashAccelerationPacket;
        } else if (LiveConstants.HARSH_BREAKING_KEY.equals(key)) {
            packet = harshBreakPacket;
        } else if (LiveConstants.RASH_TURNING_KEY.equals(key)) {
            packet = rashTurnPacket;
        }
        return packet;
    }


    private ConfigSinglePacket createModifiedPacket(ArrayList<String> fences, String parameterName, String parameterValue) {
        return new ConfigSinglePacket(fences, parameterName, "SET", parameterValue);
    }

    private ConfigSinglePacket createClearPacket(ArrayList<String> fences, String parameterName) {
        return new ConfigSinglePacket(fences, parameterName, "CLEAR", "");
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.back_button) {
            finish();

        } else if (i == R.id.text_update) {
            if (et_speed.getText().toString().equals("")) {
                et_speed.setError(getString(R.string.enter_valid_number));
                et_speed.requestFocus();
            } else if (et_harsh_breaking.getText().toString().equals("")) {
                et_harsh_breaking.setError(getString(R.string.enter_valid_number));
                et_harsh_breaking.requestFocus();
            } else if (et_raah_acceleration.getText().toString().equals("")) {
                et_raah_acceleration.setError(getString(R.string.enter_valid_number));
                et_raah_acceleration.requestFocus();
            } else if (et_rash_turning.getText().toString().equals("")) {
                et_rash_turning.setError(getString(R.string.enter_valid_number));
                et_rash_turning.requestFocus();
            } else if (deviceConfig != null && isConnected()) {
                getCacheData();
            }

        } else if (i == R.id.search_bar) {
            updateConfig(LiveConstants.CONFIG_CLEAR);
        }
    }


    /*
     *       based the progress value change the color of the progressbar color
     * */
    public void setSeekBarColor(SeekBar seekBar) {
        if (seekBar.getProgress() >= 0 && seekBar.getProgress() <= 2) {
            seekBar.getProgressDrawable().setColorFilter(getColor(R.color.color1), PorterDuff.Mode.SRC_IN);
            seekBar.getThumb().setColorFilter(getColor(R.color.color1), PorterDuff.Mode.SRC_ATOP);
        } else if (seekBar.getProgress() > 2 && seekBar.getProgress() <= 4) {
            seekBar.getProgressDrawable().setColorFilter(getColor(R.color.color2), PorterDuff.Mode.SRC_IN);
            seekBar.getThumb().setColorFilter(getColor(R.color.color2), PorterDuff.Mode.SRC_ATOP);
        } else if (seekBar.getProgress() > 4 && seekBar.getProgress() <= 6) {
            seekBar.getProgressDrawable().setColorFilter(getColor(R.color.color3), PorterDuff.Mode.SRC_IN);
            seekBar.getThumb().setColorFilter(getColor(R.color.color3), PorterDuff.Mode.SRC_ATOP);
        } else if (seekBar.getProgress() > 6 && seekBar.getProgress() <= 8) {
            seekBar.getProgressDrawable().setColorFilter(getColor(R.color.color4), PorterDuff.Mode.SRC_IN);
            seekBar.getThumb().setColorFilter(getColor(R.color.color4), PorterDuff.Mode.SRC_ATOP);
        } else if (seekBar.getProgress() > 8 && seekBar.getProgress() <= 10) {
            seekBar.getProgressDrawable().setColorFilter(getColor(R.color.color5), PorterDuff.Mode.SRC_IN);
            seekBar.getThumb().setColorFilter(getColor(R.color.color5), PorterDuff.Mode.SRC_ATOP);
        }
    }

    @Override
    public void getPersonFences(ArrayList<String> personFences) {
        isFenceModified = isFencesModified(personFences.toString());
        fencesPacket = createModifiedPacket(personFences, LiveConstants.FENCES_KEY, "");
    }

    private boolean isFencesModified(String fences) {
        boolean modification = false;
        if (!strPersonFence.equals(fences)) {
            modification = true;
        }
        return modification;
    }

}
