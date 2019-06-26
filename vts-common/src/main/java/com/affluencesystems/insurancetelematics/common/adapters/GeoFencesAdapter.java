package com.affluencesystems.insurancetelematics.common.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.affluencesystems.insurancetelematics.R;
import com.affluencesystems.insurancetelematics.common.Models.FenceForCheck;
import com.affluencesystems.insurancetelematics.common.Models.GeoResponse;

import java.util.ArrayList;

public class GeoFencesAdapter extends RecyclerView.Adapter<GeoFencesAdapter.ViewHolder> {

    private ArrayList<GeoResponse> allFences;
    private ArrayList<String> personFences;
    private Context context;
    private ArrayList<FenceForCheck> checkedFences;

    public interface AllPersonFences {
        void getPersonFences(ArrayList<String> personFences);
    }

    private AllPersonFences callback;

    public GeoFencesAdapter(Context context, ArrayList<GeoResponse> allFences, ArrayList<String> personFences) {
        this.allFences = allFences;
        this.personFences = personFences;
        this.context = context;
        callback = (AllPersonFences) context;
        checkPersonRoles();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fence_single, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        viewHolder.role.setText(checkedFences.get(i).getName());
        viewHolder.role.setChecked(checkedFences.get(i).isSelected());

        viewHolder.role.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    personFences.add(checkedFences.get(i).getId());
                else
                    personFences.remove(checkedFences.get(i).getId());

                callback.getPersonFences(personFences);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allFences.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public CheckBox role;

        public ViewHolder(View view) {
            super(view);
            role = (CheckBox) view.findViewById(R.id.fence);
        }
    }

    private void checkPersonRoles() {
        checkedFences = new ArrayList<>();
        FenceForCheck fenceForCheck;

        if (allFences.size() > 0) {
            for (GeoResponse fence : allFences) {
                fenceForCheck = new FenceForCheck(fence.getFenceId(), fence.getFenceName(), false);
                checkedFences.add(fenceForCheck);
            }
        }

        if (personFences.size() > 0) {
            for (FenceForCheck role : checkedFences) {
                for (String personRole : personFences) {
                    if (personRole.equals(role.getId())) {
                        role.setSelected(true);
                    }
                }
            }
        }
    }
}
