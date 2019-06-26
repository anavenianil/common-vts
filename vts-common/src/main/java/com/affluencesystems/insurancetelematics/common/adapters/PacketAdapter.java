package com.affluencesystems.insurancetelematics.common.adapters;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.affluencesystems.insurancetelematics.common.Models.TripPacket;
import com.affluencesystems.insurancetelematics.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PacketAdapter extends RecyclerView.Adapter<PacketAdapter.MyViewHolder> {

    private ArrayList<TripPacket> packets;
    private Context context;


    /*
     *       constructor for adapter.
     * */
    public PacketAdapter(Context context, ArrayList<TripPacket> packets) {
        this.packets = packets;
        this.context = context;
    }

    @Override
    public PacketAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.packet_single, parent, false);

        return new PacketAdapter.MyViewHolder(itemView);
    }


    /*
     *       initialize views and setting the values
     * */
    @Override
    public void onBindViewHolder(PacketAdapter.MyViewHolder holder, final int position) {
        TripPacket packet = packets.get(position);
        holder.stride_name.setText(getAddress(packet.getLatitude(), packet.getLongitude()));
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
        return packets.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView stride_name;

        MyViewHolder(View view) {
            super(view);
            stride_name = (TextView) view.findViewById(R.id.stride_name);
        }
    }


    /*
    *       To get address from taking latlng
    * */
    private String getAddress(String lat, String lng) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lng), 1);

//            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
//            String country = addresses.get(0).getCountryName();
//            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            return knownName + ", " + city + ", " + state;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
