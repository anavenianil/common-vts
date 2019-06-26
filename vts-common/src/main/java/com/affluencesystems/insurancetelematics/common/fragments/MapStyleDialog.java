package com.affluencesystems.insurancetelematics.common.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.affluencesystems.insurancetelematics.R;
import com.affluencesystems.insurancetelematics.common.Utils.PreferenceUtils;

public class MapStyleDialog extends DialogFragment implements View.OnClickListener {

    String TAG = "MapStyleDialog";
    private LinearLayout lv_night, lv_dark, lv_retro, lv_silver, lv_standard, lv_satellite, lv_hybrid, lv_terrain;

    /*
     *       interface for change map style.
     * */
    public interface ChangeMapStyle {
        void onChangeStyleClicked(String key);
    }

    ChangeMapStyle callback;

    /*
     *       initialize map style call back interface.
     * */
    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        callback = (ChangeMapStyle) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
    }


    /*
     *       inflate layout and initialize views.
     * */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.map_style_dialog, container, false);
        if (getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        lv_standard = (LinearLayout) v.findViewById(R.id.lv_standard);
        lv_silver = (LinearLayout) v.findViewById(R.id.lv_silver);
        lv_retro = (LinearLayout) v.findViewById(R.id.lv_retro);
        lv_dark = (LinearLayout) v.findViewById(R.id.lv_dark);
        lv_night = (LinearLayout) v.findViewById(R.id.lv_night);
        lv_satellite = (LinearLayout) v.findViewById(R.id.lv_satellite);
        lv_hybrid = (LinearLayout) v.findViewById(R.id.lv_hybrid);
        lv_terrain = (LinearLayout) v.findViewById(R.id.lv_terrain);

        lv_night.setOnClickListener(this);
        lv_silver.setOnClickListener(this);
        lv_standard.setOnClickListener(this);
        lv_retro.setOnClickListener(this);
        lv_dark.setOnClickListener(this);
        lv_terrain.setOnClickListener(this);
        lv_hybrid.setOnClickListener(this);
        lv_satellite.setOnClickListener(this);

        return v;
    }


    /*
     *       Click events,
     *       once click done, call the callback method.
     * */
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.lv_silver) {
            callback.onChangeStyleClicked(PreferenceUtils.SILVER);
            dismiss();

        } else if (i == R.id.lv_standard) {
            callback.onChangeStyleClicked(PreferenceUtils.STANDARD);
            dismiss();

        } else if (i == R.id.lv_retro) {
            callback.onChangeStyleClicked(PreferenceUtils.RETRO);
            dismiss();

        } else if (i == R.id.lv_dark) {
            callback.onChangeStyleClicked(PreferenceUtils.DARK);
            dismiss();

        } else if (i == R.id.lv_night) {
            callback.onChangeStyleClicked(PreferenceUtils.NIGHT);
            dismiss();

        } else if (i == R.id.lv_terrain) {
            callback.onChangeStyleClicked(PreferenceUtils.TERRAIN);
            dismiss();

        } else if (i == R.id.lv_hybrid) {
            callback.onChangeStyleClicked(PreferenceUtils.HYBRID);
            dismiss();

        } else if (i == R.id.lv_satellite) {
            callback.onChangeStyleClicked(PreferenceUtils.SATELLITE);
            dismiss();

        }
    }

    /*
     *       override onBack press for dismiss.
     * */
    @Override
    public Dialog onCreateDialog(@NonNull Bundle savedInstanceState) {
        return new Dialog(getActivity(), getTheme()) {
            @Override
            public void onBackPressed() {
                dismiss();
            }
        };
    }

}
