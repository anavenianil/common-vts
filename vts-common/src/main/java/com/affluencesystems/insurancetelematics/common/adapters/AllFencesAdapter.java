package com.affluencesystems.insurancetelematics.common.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.affluencesystems.insurancetelematics.R;
import com.affluencesystems.insurancetelematics.common.Utils.GeoFencingCommunicator;
import com.affluencesystems.insurancetelematics.common.Utils.LiveConstants;

import java.util.ArrayList;

public class AllFencesAdapter extends RecyclerView.Adapter<AllFencesAdapter.MyViewHolder> {
    private String TAG = "AllFencesAdapter";
    private ArrayList<String> allFences;
    private Context context;
    private String updateKey;

    private GeoFencingCommunicator callback;


    /*
     *       constructor for adapter.
     * */
    public AllFencesAdapter(Context context, ArrayList<String> allFences, String updateKey) {
        this.allFences = allFences;
        this.context = context;
        this.updateKey = updateKey;
        callback = (GeoFencingCommunicator) context;
    }

    @Override
    public AllFencesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fence_list_single, parent, false);

        return new AllFencesAdapter.MyViewHolder(itemView);
    }


    /*
     *       initialize views and setting the values
     * */
    @Override
    public void onBindViewHolder(AllFencesAdapter.MyViewHolder holder, final int position) {
        holder.name.setText(allFences.get(position));

        holder.rv_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(LiveConstants.FENCE_DELETE.equals(updateKey)) {
                    callback.modifyFence(allFences.get(position), updateKey);
//                } else {
//                    callback.modifyFence(allFences.get(position), updateKey);
//                }
            }
        });

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
        return allFences.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        RelativeLayout rv_container;

        MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.tv_fence_name);
            rv_container = (RelativeLayout) view.findViewById(R.id.rv_container);
        }
    }
}
