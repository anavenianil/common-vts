package com.affluencesystems.insurancetelematics.common.Activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.affluencesystems.insurancetelematics.R;
import com.affluencesystems.insurancetelematics.common.Utils.LiveConstants;
import com.affluencesystems.insurancetelematics.common.Utils.PreferenceUtils;

import org.json.JSONArray;
import org.json.JSONException;

public class SettingsActivity extends Base_activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private String TAG = "SettingsActivity";
    private LinearLayout lv_live, lv_geo, lv_service, lv_documents, lv_notifications, lv_feedback, lv_trip, lv_stride, lv_nav_my_car, lv_add_vehicle, lv_update_vehicle, lv_change_vehicle;
    private CardView card_config, pattern_lock_card, finger_print_card;
    private Switch s_live, s_geo, s_service, s_documents, s_notifications, s_feedback, s_trip, s_stride, switch000, switch00, switch_finger, switch_pattern, s_navigation, s_zoom, s_nav_my_car, s_add_vehicle, s_update_vehicle, s_change_vehicle;
    private PreferenceUtils preferenceUtils;
    private int applicationKey = -1;
    private boolean modificationDone = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        headerControls();

        Intent intent = getIntent();
        applicationKey = intent.getIntExtra(LiveConstants.APPLICATION_KEY, -1);

        preferenceUtils = new PreferenceUtils(this);

        lv_live = (LinearLayout) findViewById(R.id.lv_live);
        lv_geo = (LinearLayout) findViewById(R.id.lv_geo);
        lv_service = (LinearLayout) findViewById(R.id.lv_service);
        lv_documents = (LinearLayout) findViewById(R.id.lv_documents);
        lv_notifications = (LinearLayout) findViewById(R.id.lv_notifications);
        lv_feedback = (LinearLayout) findViewById(R.id.lv_feedback);
        lv_trip = (LinearLayout) findViewById(R.id.lv_trip);
        lv_stride = (LinearLayout) findViewById(R.id.lv_stride);

        lv_nav_my_car = (LinearLayout) findViewById(R.id.lv_nav_my_car);
        lv_add_vehicle = (LinearLayout) findViewById(R.id.lv_add_vehicle);
        lv_update_vehicle = (LinearLayout) findViewById(R.id.lv_update_vehicle);
        lv_change_vehicle = (LinearLayout) findViewById(R.id.lv_change_vehicle);

        card_config = (CardView) findViewById(R.id.card_config);
        finger_print_card = (CardView) findViewById(R.id.finger_print_card);
        pattern_lock_card = (CardView) findViewById(R.id.pattern_lock_card);

        if(isContainsId("12")){
            card_config.setVisibility(View.VISIBLE);
        } else {
            card_config.setVisibility(View.GONE);
        }

        if(applicationKey == LiveConstants.DRIVER_APP){
            card_config.setVisibility(View.GONE);
            lv_live.setVisibility(View.GONE);
            lv_geo.setVisibility(View.GONE);
            lv_service.setVisibility(View.GONE);
            lv_documents.setVisibility(View.GONE);
            lv_feedback.setVisibility(View.GONE);
            lv_trip.setVisibility(View.GONE);
            lv_stride.setVisibility(View.GONE);
            lv_notifications.setVisibility(View.GONE);
            lv_nav_my_car.setVisibility(View.GONE);
            lv_add_vehicle.setVisibility(View.GONE);
            lv_update_vehicle.setVisibility(View.GONE);
            lv_change_vehicle.setVisibility(View.GONE);
        }

        s_live = (Switch) findViewById(R.id.s_live);
        s_live.setOnCheckedChangeListener(this);
        s_geo = (Switch) findViewById(R.id.s_geo);
        s_geo.setOnCheckedChangeListener(this);
        s_service = (Switch) findViewById(R.id.s_service);
        s_service.setOnCheckedChangeListener(this);
        s_documents = (Switch) findViewById(R.id.s_documents);
        s_documents.setOnCheckedChangeListener(this);
        s_notifications = (Switch) findViewById(R.id.s_notifications);
        s_notifications.setOnCheckedChangeListener(this);
        s_feedback = (Switch) findViewById(R.id.s_feedback);
        s_feedback.setOnCheckedChangeListener(this);
        s_trip = (Switch) findViewById(R.id.s_trip);
        s_trip.setOnCheckedChangeListener(this);
        s_stride = (Switch) findViewById(R.id.s_stride);
        s_stride.setOnCheckedChangeListener(this);
        switch000 = (Switch) findViewById(R.id.switch000);
        switch000.setOnCheckedChangeListener(this);
        switch00 = (Switch) findViewById(R.id.switch00);
        switch00.setOnCheckedChangeListener(this);
        switch_finger = (Switch) findViewById(R.id.switch_finger);
        switch_finger.setOnCheckedChangeListener(this);
        switch_pattern = (Switch) findViewById(R.id.switch_pattern);
        switch_pattern.setOnCheckedChangeListener(this);
        s_navigation = (Switch) findViewById(R.id.s_navigation);
        s_navigation.setOnCheckedChangeListener(this);
        s_zoom = (Switch) findViewById(R.id.s_zoom);
        s_zoom.setOnCheckedChangeListener(this);

        s_nav_my_car = (Switch) findViewById(R.id.s_nav_my_car);
        s_nav_my_car.setOnCheckedChangeListener(this);
        s_add_vehicle = (Switch) findViewById(R.id.s_add_vehicle);
        s_add_vehicle.setOnCheckedChangeListener(this);
        s_update_vehicle = (Switch) findViewById(R.id.s_update_vehicle);
        s_update_vehicle.setOnCheckedChangeListener(this);
        s_change_vehicle = (Switch) findViewById(R.id.s_change_vehicle);
        s_change_vehicle.setOnCheckedChangeListener(this);


        updateSwitches();
        card_config.setOnClickListener(this);
    }


    /*
    *       boolean value modificationDone will be true, if any changes done in this screen.
    *       If boolean value modificationDone is true we can update the home screen drawer adapter will be updated.
     * */
    @Override
    public void onBackPressed() {
        if (modificationDone)
            setResult(RESULT_OK);
        super.onBackPressed();
    }

    /*
     *       Used to get the role ids exists or not
     *       If exists it will sho other wise hide the role for this user.
     * */
    public boolean isContainsId(String value) {
        boolean found = false;
        if (preferenceUtils == null)
            preferenceUtils = new PreferenceUtils(this);
        try {
            JSONArray privilegeArray = new JSONArray(preferenceUtils.getStringFromPreference(PreferenceUtils.PERSON_PRIVILEGES, ""));
            for (int i = 0; i < privilegeArray.length(); i++) {
                try {
                    if (privilegeArray.getString(i).equals(value))
                        found = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return found;
    }


    /*
     *       To initiate all the view from xml file
     * */
    public void headerControls() {
        TextView header_text;
        ImageView back_button;
        header_text = (TextView) findViewById(R.id.header_text);
        back_button = (ImageView) findViewById(R.id.back_button);
        header_text.setText(getResources().getString(R.string.settings));
        header_text.setVisibility(View.VISIBLE);
        back_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.back_button) {
            onBackPressed();
        } else if (i == R.id.card_config) {
            startActivity(new Intent(SettingsActivity.this, ConfigActivity.class));                           // commented for common code.
        }
    }


    /*
    *       Based on the shared preference values we can handle the switchs here.
    * */
    private void updateSwitches() {
        if (preferenceUtils.getBooleanFromPreferenceForSideBar(PreferenceUtils.ZOOM_CONTROLS))
            s_zoom.setChecked(true);
        else
            s_zoom.setChecked(false);

        if (preferenceUtils.getBooleanFromPreferenceForSideBar(PreferenceUtils.NAVIGATION_CONTROL))
            s_navigation.setChecked(true);
        else
            s_navigation.setChecked(false);

        if (preferenceUtils.getBooleanFromPreferenceForSideBar(PreferenceUtils.Is_2D_3D_Enable))
            switch000.setChecked(true);
        else
            switch000.setChecked(false);

        if (preferenceUtils.getBooleanFromPreferenceForSideBar(PreferenceUtils.IS_Map_Styles_Enable))
            switch00.setChecked(true);
        else
            switch00.setChecked(false);

        if (preferenceUtils.getBooleanFromPreferenceForSideBar(PreferenceUtils.LIVE_TRACKING)) {
            s_live.setChecked(true);
        } else {
            s_live.setChecked(false);
        }

        if (preferenceUtils.getBooleanFromPreferenceForSideBar(PreferenceUtils.TRIP_LIST)) {
            s_trip.setChecked(true);
        } else {
            s_trip.setChecked(false);
        }

        if (preferenceUtils.getBooleanFromPreferenceForSideBar(PreferenceUtils.STRIDE_LIST)) {
            s_stride.setChecked(true);
        } else {
            s_stride.setChecked(false);
        }

        if (preferenceUtils.getBooleanFromPreferenceForSideBar(PreferenceUtils.GEO_FENCE)) {
            s_geo.setChecked(true);
        } else {
            s_geo.setChecked(false);
        }

        if (preferenceUtils.getBooleanFromPreferenceForSideBar(PreferenceUtils.SERVICES)) {
            s_service.setChecked(true);
        } else {
            s_service.setChecked(false);
        }

        if (preferenceUtils.getBooleanFromPreferenceForSideBar(PreferenceUtils.DOCUMENTS)) {
            s_documents.setChecked(true);
        } else {
            s_documents.setChecked(false);
        }

        if (preferenceUtils.getBooleanFromPreferenceForSideBar(PreferenceUtils.NOTIFICATIONS)) {
            s_notifications.setChecked(true);
        } else {
            s_notifications.setChecked(false);
        }

        if (preferenceUtils.getBooleanFromPreferenceForSideBar(PreferenceUtils.FEEDBACK)) {
            s_feedback.setChecked(true);
        } else {
            s_feedback.setChecked(false);
        }

        if (preferenceUtils.getbooleanFromPreference(PreferenceUtils.Enable_FingerPrint)) {
            switch_finger.setChecked(true);
        } else {
            switch_finger.setChecked(false);
        }

        if (preferenceUtils.getbooleanFromPreference(PreferenceUtils.Enable_Pattern_Visibity))
            pattern_lock_card.setVisibility(View.VISIBLE);
        else
            pattern_lock_card.setVisibility(View.GONE);

        if (preferenceUtils.getbooleanFromPreference(PreferenceUtils.Enable_FingerPrint_Visibity))
            finger_print_card.setVisibility(View.VISIBLE);
        else
            finger_print_card.setVisibility(View.GONE);

        if (preferenceUtils.getbooleanFromPreference(PreferenceUtils.Enable_Pattern)) {
            switch_pattern.setChecked(true);
        } else {
            switch_pattern.setChecked(false);
        }

        if (preferenceUtils.getBooleanFromPreferenceForSideBar(PreferenceUtils.NAV_MY_CAR)) {
            s_nav_my_car.setChecked(true);
        } else {
            s_nav_my_car.setChecked(false);
        }

        if (preferenceUtils.getBooleanFromPreferenceForSideBar(PreferenceUtils.ADD_VEHICLE)) {
            s_add_vehicle.setChecked(true);
        } else {
            s_add_vehicle.setChecked(false);
        }

        if (preferenceUtils.getBooleanFromPreferenceForSideBar(PreferenceUtils.UPDATE_VEHICLE)) {
            s_update_vehicle.setChecked(true);
        } else {
            s_update_vehicle.setChecked(false);
        }

        if (preferenceUtils.getBooleanFromPreferenceForSideBar(PreferenceUtils.CHANGE_VEHICLE)) {
            s_change_vehicle.setChecked(true);
        } else {
            s_change_vehicle.setChecked(false);
        }
    }


    /*
    *       handle switchs and shared preference values.
    * */
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        modificationDone = true;

        int i = compoundButton.getId();
        if (i == R.id.switch000) {
            if (compoundButton == switch000 && b) {
                preferenceUtils.saveBoolean(PreferenceUtils.Is_2D_3D_Enable, true);
                switch000.setChecked(true);
            } else {
                preferenceUtils.saveBoolean(PreferenceUtils.Is_2D_3D_Enable, false);
                switch000.setChecked(false);
            }

        } else if (i == R.id.switch00) {
            if (compoundButton == switch00 && b) {
                preferenceUtils.saveBoolean(PreferenceUtils.IS_Map_Styles_Enable, true);
                switch00.setChecked(true);
            } else {
                preferenceUtils.saveBoolean(PreferenceUtils.IS_Map_Styles_Enable, false);
                switch00.setChecked(false);
            }

        } else if (i == R.id.s_live) {
            if (compoundButton == s_live && b) {
                preferenceUtils.saveBoolean(PreferenceUtils.LIVE_TRACKING, true);
                s_live.setChecked(true);
            } else {
                preferenceUtils.saveBoolean(PreferenceUtils.LIVE_TRACKING, false);
                s_live.setChecked(false);
            }

        } else if (i == R.id.s_geo) {
            if (compoundButton == s_geo && b) {
                preferenceUtils.saveBoolean(PreferenceUtils.GEO_FENCE, true);
                s_geo.setChecked(true);
            } else {
                preferenceUtils.saveBoolean(PreferenceUtils.GEO_FENCE, false);
                s_geo.setChecked(false);
            }


        } else if (i == R.id.s_service) {
            if (compoundButton == s_service && b) {
                preferenceUtils.saveBoolean(PreferenceUtils.SERVICES, true);
                s_service.setChecked(true);
            } else {
                preferenceUtils.saveBoolean(PreferenceUtils.SERVICES, false);
                s_service.setChecked(false);
            }


        } else if (i == R.id.s_documents) {
            if (compoundButton == s_documents && b) {
                preferenceUtils.saveBoolean(PreferenceUtils.DOCUMENTS, true);
                s_documents.setChecked(true);
            } else {
                preferenceUtils.saveBoolean(PreferenceUtils.DOCUMENTS, false);
                s_documents.setChecked(false);
            }


        } else if (i == R.id.s_notifications) {
            if (compoundButton == s_notifications && b) {
                preferenceUtils.saveBoolean(PreferenceUtils.NOTIFICATIONS, true);
                s_notifications.setChecked(true);
            } else {
                preferenceUtils.saveBoolean(PreferenceUtils.NOTIFICATIONS, false);
                s_notifications.setChecked(false);
            }


        } else if (i == R.id.s_trip) {
            if (compoundButton == s_trip && b) {
                preferenceUtils.saveBoolean(PreferenceUtils.TRIP_LIST, true);
                s_trip.setChecked(true);
            } else {
                preferenceUtils.saveBoolean(PreferenceUtils.TRIP_LIST, false);
                s_trip.setChecked(false);
            }


        } else if (i == R.id.s_stride) {
            if (compoundButton == s_stride && b) {
                preferenceUtils.saveBoolean(PreferenceUtils.STRIDE_LIST, true);
                s_stride.setChecked(true);
            } else {
                preferenceUtils.saveBoolean(PreferenceUtils.STRIDE_LIST, false);
                s_stride.setChecked(false);
            }


        } else if (i == R.id.s_feedback) {
            if (compoundButton == s_feedback && b) {
                preferenceUtils.saveBoolean(PreferenceUtils.FEEDBACK, true);
                s_feedback.setChecked(true);
            } else {
                preferenceUtils.saveBoolean(PreferenceUtils.FEEDBACK, false);
                s_feedback.setChecked(false);
            }

        } else if (i == R.id.switch_finger) {
            if (compoundButton == switch_finger && b) {
                preferenceUtils.saveBoolean(PreferenceUtils.Enable_FingerPrint, true);
                switch_finger.setChecked(true);
                if (preferenceUtils.getbooleanFromPreference(PreferenceUtils.Enable_Pattern_Visibity) && preferenceUtils.getbooleanFromPreference(PreferenceUtils.Enable_FingerPrint_Visibity)) {
                    switch_pattern.setChecked(false);
                }
            } else {
                preferenceUtils.saveBoolean(PreferenceUtils.Enable_FingerPrint, false);
                switch_finger.setChecked(false);
            }


        } else if (i == R.id.switch_pattern) {
            if (compoundButton == switch_pattern && b) {
                preferenceUtils.saveBoolean(PreferenceUtils.Enable_Pattern, true);
                switch_pattern.setChecked(true);
                if (preferenceUtils.getbooleanFromPreference(PreferenceUtils.Enable_Pattern_Visibity) && preferenceUtils.getbooleanFromPreference(PreferenceUtils.Enable_FingerPrint_Visibity)) {
                    switch_finger.setChecked(false);
                }
            } else {
                preferenceUtils.saveBoolean(PreferenceUtils.Enable_Pattern, false);
                switch_pattern.setChecked(false);
            }

        } else if (i == R.id.s_zoom) {
            setResult(RESULT_OK);
            if (compoundButton == s_zoom && b) {
                preferenceUtils.saveBoolean(PreferenceUtils.ZOOM_CONTROLS, true);
                s_zoom.setChecked(true);
            } else {
                preferenceUtils.saveBoolean(PreferenceUtils.ZOOM_CONTROLS, false);
                s_zoom.setChecked(false);
            }

        } else if (i == R.id.s_navigation) {
            setResult(RESULT_OK);
            if (compoundButton == s_navigation && b) {
                preferenceUtils.saveBoolean(PreferenceUtils.NAVIGATION_CONTROL, true);
                s_navigation.setChecked(true);
            } else {
                preferenceUtils.saveBoolean(PreferenceUtils.NAVIGATION_CONTROL, false);
                s_navigation.setChecked(false);
            }
        } else if (i == R.id.s_nav_my_car) {
            setResult(RESULT_OK);
            if (compoundButton == s_nav_my_car && b) {
                preferenceUtils.saveBoolean(PreferenceUtils.NAV_MY_CAR, true);
                s_nav_my_car.setChecked(true);
            } else {
                preferenceUtils.saveBoolean(PreferenceUtils.NAV_MY_CAR, false);
                s_nav_my_car.setChecked(false);
            }
        } else if (i == R.id.s_add_vehicle) {
            setResult(RESULT_OK);
            if (compoundButton == s_add_vehicle&& b) {
                preferenceUtils.saveBoolean(PreferenceUtils.ADD_VEHICLE, true);
                s_add_vehicle.setChecked(true);
            } else {
                preferenceUtils.saveBoolean(PreferenceUtils.ADD_VEHICLE, false);
                s_add_vehicle.setChecked(false);
            }
        } else if (i == R.id.s_update_vehicle) {
            setResult(RESULT_OK);
            if (compoundButton == s_update_vehicle&& b) {
                preferenceUtils.saveBoolean(PreferenceUtils.UPDATE_VEHICLE, true);
                s_update_vehicle.setChecked(true);
            } else {
                preferenceUtils.saveBoolean(PreferenceUtils.UPDATE_VEHICLE, false);
                s_update_vehicle.setChecked(false);
            }
        } else if (i == R.id.s_change_vehicle) {
            setResult(RESULT_OK);
            if (compoundButton == s_change_vehicle&& b) {
                preferenceUtils.saveBoolean(PreferenceUtils.CHANGE_VEHICLE, true);
                s_change_vehicle.setChecked(true);
            } else {
                preferenceUtils.saveBoolean(PreferenceUtils.CHANGE_VEHICLE, false);
                s_change_vehicle.setChecked(false);
            }
        }

    }
}
