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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.affluencesystems.insurancetelematics.R;
import com.affluencesystems.insurancetelematics.common.Models.GeoResponse;
import com.affluencesystems.insurancetelematics.common.Utils.LiveConstants;
import com.affluencesystems.insurancetelematics.common.adapters.AllFencesAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import static com.affluencesystems.insurancetelematics.common.Activitys.GeoFencingActivity.geoFenceHash;

public class AllGeoFences extends DialogFragment {

    private String TAG = "AllGeoFences";
    private RecyclerView rv_fences;
    private Context context;
    private AllFencesAdapter adapter;
    private TextView title;
    private String updateKey = "";
//    private HashMap<String, GeoResponse> geoResponseHash;


    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
        updateKey = getArguments().getString(LiveConstants.FENCE_UPDATE_KEY);
//        geoResponseHash = (HashMap<String, GeoResponse>) getArguments().getSerializable(LiveConstants.GEO_FENCE_HASH);
    }


    /*
     *       inflate layout and initialize views.
     * */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.all_geo_fences, container, false);
        if (getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        rv_fences = (RecyclerView) v.findViewById(R.id.rv_fences);
        title = (TextView) v.findViewById(R.id.title);

        if (LiveConstants.FENCE_DELETE.equals(updateKey)) {
            title.setText("Delete Geo Fence");
        } else {
            title.setText("Update Geo Fence");
        }

        rv_fences.setHasFixedSize(true);
        rv_fences.setLayoutManager(new LinearLayoutManager(context));

        adapter = new AllFencesAdapter(context, new ArrayList<String>(geoFenceHash.keySet()), updateKey);
        rv_fences.setAdapter(adapter);
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
                dismiss();
            }
        };
    }
}
