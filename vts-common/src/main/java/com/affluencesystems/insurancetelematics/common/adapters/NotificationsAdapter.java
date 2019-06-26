/*****************************************************************************
 * Copyright (C) 2018, Affluence Infosystems Pvt Ltd.
 *  All Rights Reserved.
 *
 * This is UNPUBLISHED PROPRIETARY SOURCE CODE of Affluence Infosystems,
 * the contents of this file may not be disclosed to third parties, copied
 * or duplicated in any form, in whole or in part, without the prior
 * written permission of Affluence Infosystems.
 *
 NotificationsActivity
 * File Name:  NotificationsAdapter.java
 *
 * Description: this adapter class is used to show notification details.
 *
 * Routines in this file:
 * Configuration Identifier: <Config ID:>
 *
 * Modification History:
 *    Created by:      anil        1.0         01/12/2018
 *    Description:    design and fuctionality completed
 ****************************************************************************/
package com.affluencesystems.insurancetelematics.common.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.affluencesystems.insurancetelematics.common.Models.NotificationType;
import com.affluencesystems.insurancetelematics.common.Models.Notifications;
import com.affluencesystems.insurancetelematics.R;

import java.util.ArrayList;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.MyViewHolder> {

    private String TAG = "NotificationsAdapter";
    private ArrayList<Notifications> notificationsList;
    private ArrayList<NotificationType> notificationTypes;
    private Context context;


    /*
     *       constructor for adapter.
     * */
    public NotificationsAdapter(Context context, ArrayList<Notifications> notificationsList, ArrayList<NotificationType> notificationTypes) {
        this.notificationsList = notificationsList;
        this.notificationTypes = notificationTypes;
        this.context = context;
    }

    @Override
    public NotificationsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_doc_list_row, parent, false);

        return new NotificationsAdapter.MyViewHolder(itemView);
    }


    /*
     *       initialize views and setting the values
     * */
    @Override
    public void onBindViewHolder(NotificationsAdapter.MyViewHolder holder, final int position) {

        String type = notificationsList.get(position).getTypeOfAlert();

        holder.title.setText(getTitle(type));
        holder.notification_msg.setText(getMessage(type));
//        holder.notification_date.setText(notificationsList.get(position).getDateTime().substring(0, 10));
        holder.tv_time.setText(notificationsList.get(position).getDateTime().substring(11,19));

        if (position == 0) {
            holder.notification_date.setVisibility(View.VISIBLE);
            holder.notification_date.setText(notificationsList.get(position).getDateTime().substring(0, 10));

        } else {
            if (notificationsList.get(position).getDateTime().substring(0,10).equalsIgnoreCase(notificationsList.get(position - 1).getDateTime().substring(0, 10))) {
                holder.notification_date.setVisibility(View.GONE);
            } else {
                holder.notification_date.setVisibility(View.VISIBLE);
                holder.notification_date.setText(notificationsList.get(position).getDateTime().substring(0, 10));
            }
        }
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
        return notificationsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, notification_msg, notification_date, tv_time;

        MyViewHolder(View view) {
            super(view);
            notification_date = (TextView) view.findViewById(R.id.notification_date);
            title = (TextView) view.findViewById(R.id.title);
            notification_msg = (TextView) view.findViewById(R.id.notification_msg);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
        }
    }


    /*
    *       based on the alert type change the alert.
    * */
    private String getTitle(String key) {
        String title = key;
        for (NotificationType n : notificationTypes) {
            if (n.getShotCodeOfAlertType().equals(key)) {
                title = n.getTypeOfAlert();
                break;
            }
        }
        return title;
    }


    /*
    *       All the types of alerts and titles.
    * */
    private String getMessage(String key) {
        String message = "";
        switch (key) {
            case "IN":
                message = context.getString(R.string.msg_ignition_on);
                break;
            case "EMR":
                message = context.getString(R.string.msg_emergency);
                break;
            case "TA":
                message = context.getString(R.string.msg_damage);
                break;
            case "RT":
                message = context.getString(R.string.msg_rash
                );
                break;
            case "HA":
                message = context.getString(R.string.msg_harsh);
                break;
            case "HB":
                message = context.getString(R.string.msg_unsafe);
                break;
            case "IF":
                message = context.getString(R.string.msg_ignition_off);
                break;
            case "BL":
                message = context.getString(R.string.msg_battery_low);
                break;

                default:
                    message = key;
        }
        return message;
    }

    public void updateAdapter(ArrayList<Notifications> notifications){
        notificationsList .addAll(notifications);
        notifyDataSetChanged();
        Log.d(TAG, "notification size ===> " + notificationsList.size());
    }

}