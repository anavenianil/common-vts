package com.affluencesystems.insurancetelematics.common.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.affluencesystems.insurancetelematics.R;
import com.affluencesystems.insurancetelematics.common.Utils.GeoFencingCommunicator;
import com.affluencesystems.insurancetelematics.common.Utils.LiveConstants;

import static com.affluencesystems.insurancetelematics.common.Activitys.Base_activity.geofencingCustomFragment;
import static com.affluencesystems.insurancetelematics.common.Activitys.Base_activity.geofencingRadiusFragment;
import static com.affluencesystems.insurancetelematics.common.Activitys.GeoFencingActivity.customLatLngs;
import static com.affluencesystems.insurancetelematics.common.Activitys.GeoFencingActivity.radiusLatLngs;

public class AlertForSecondFence extends DialogFragment implements View.OnClickListener {

    private String TAG = "AllGeoFences";
    private Context context;
    private TextView submit, next, tv_text;
    private String key;
    private String updateKey;

    private GeoFencingCommunicator callback;

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        this.context = context;
        callback = (GeoFencingCommunicator) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
        key = getArguments().getString(LiveConstants.FENCE_FROM_KEY);
        updateKey = getArguments().getString(LiveConstants._FENCE_UPDATE_KEY_);
    }


    /*
     *       inflate layout and initialize views.
     * */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.alert_for_second_fence, container, false);
        if (getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        submit = (TextView) v.findViewById(R.id.submit);
        next = (TextView) v.findViewById(R.id.next);
        tv_text = (TextView) v.findViewById(R.id.tv_text);

        if(LiveConstants.FROM_CUSTOM_FENCE.equals(key)) {
            tv_text.setText(R.string.geo_fence_alert_radius_text);
        } else {
            tv_text.setText(R.string.geo_fence_alert_custom_text);
        }

        if(LiveConstants.FENCE_FOR_UPDATE.equals(updateKey)){
            tv_text.setText(R.string.geo_fence_alert_update_next_text);
        }

        submit.setOnClickListener(this);
        next.setOnClickListener(this);

        return v;
    }

    /*
     *       override onBack press for dismiss.
     * */
    @Override
    public Dialog onCreateDialog(@NonNull Bundle savedInstanceState) {
        return new Dialog(getActivity(), getTheme()) {
            @Override
            public void onBackPressed() {
                if(LiveConstants.FROM_CUSTOM_FENCE.equals(key)) {
                    geofencingCustomFragment.addCustomLatLngs(customLatLngs);
                    customLatLngs.clear();
                    geofencingCustomFragment.handleOkVisible();
                } else {
                    radiusLatLngs.clear();
                    geofencingRadiusFragment.handleOkVisible();
                }
                dismiss();
            }
        };
    }

    @Override
    public void onClick(View view) {
       if(view.getId() == R.id.submit){
           boolean isForUpdate = false;
           if(LiveConstants.FENCE_FOR_UPDATE.equals(updateKey)){
               isForUpdate = true;
           }
               callback.postGeoFence(isForUpdate);
           dismiss();
       } else if(view.getId() == R.id.next){
           if(LiveConstants.FROM_CUSTOM_FENCE.equals(key)) {
               callback.moveNextFragment(0);
           } else {
               callback.moveNextFragment(1);

           }
           dismiss();
       }

    }
}
