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
 * File Name:  AddServiceActivity.java
 *
 * Description: this class is used to show the alerts or notifications.
 *
 * Routines in this file:
 *     headerControls()
 *     getNoticationService()
 *
 * Configuration Identifier: <Config ID:>
 *
 * Modification History:
 *    Created by:      anil        1.0         01/12/2018
 *    Description:    design and fuctionality completed
 ****************************************************************************/
package com.affluencesystems.insurancetelematics.common.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.affluencesystems.insurancetelematics.common.Models.NotificationModel;
import com.affluencesystems.insurancetelematics.common.Models.NotificationType;
import com.affluencesystems.insurancetelematics.common.Models.Notifications;
import com.affluencesystems.insurancetelematics.R;
import com.affluencesystems.insurancetelematics.common.Utils.LiveConstants;
import com.affluencesystems.insurancetelematics.common.Utils.PreferenceUtils;
import com.affluencesystems.insurancetelematics.common.adapters.NotificationsAdapter;
import com.affluencesystems.insurancetelematics.common.ApiUtils.Constants;
import com.affluencesystems.insurancetelematics.common.views.ShimmerRecyclerView;
import com.google.gson.JsonElement;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.affluencesystems.insurancetelematics.common.Utils.ConnectivityReceiver.isConnected;

public class NotificationsActivity extends Base_activity {
    private String TAG = "NotificationsActivity";
    private ShimmerRecyclerView recyclerView;
    private NotificationsAdapter mAdapter;
    private PreferenceUtils preferenceUtils;
    private TextView no_records;
    private SwipeRefreshLayout swipeToRefresh;
    private ArrayList<NotificationType> notificationTypes = new ArrayList<>();
    private int totalPages, pageNumber = 0;
    private LinearLayoutManager layoutManager;
    private ProgressBar progress;

    private int applicationKey = -1;

    Boolean isScrolling = false;
    int currentItems, totalItems, scrollOutItems;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        headerControls();
        preferenceUtils = new PreferenceUtils(this);

        Intent intent = getIntent();
        applicationKey = intent.getIntExtra(LiveConstants.APPLICATION_KEY, -1);

        if (isConnected()) {
            getNotificationTypes();
        }

        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isConnected()) {
                    getNotificationService();
                }
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = layoutManager.getChildCount();
                totalItems = layoutManager.getItemCount();
                scrollOutItems = layoutManager.findFirstVisibleItemPosition();
                if (dy > 0 && isScrolling && (currentItems + scrollOutItems == totalItems)) {
                    progress.setVisibility(View.VISIBLE);
                    pageNumber++;
                    isScrolling = false;
                    hitForNextPage();
                }
            }
        });

    }


    /*
     *       getting all the types of notifications from the server.
     *       With this type of notifications we can filter the notifications.
     * */
    private void getNotificationTypes() {
        Call<ArrayList<NotificationType>> callRetrofit = null;
        callRetrofit = Constants.service.getNotificationType();
        callRetrofit.enqueue(new Callback<ArrayList<NotificationType>>() {
            @Override
            public void onResponse(Call<ArrayList<NotificationType>> call, Response<ArrayList<NotificationType>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        no_records.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        notificationTypes = response.body();
                        getNotificationService();
                    } else {
                        no_records.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<NotificationType>> call, Throwable t) {
                no_records.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                swipeToRefresh.setRefreshing(false);
                Log.d(TAG, "Error Call" + call.toString());
                Log.d(TAG, "Error" + t.toString());
            }
        });
    }


    /*
     *       getting all the notifications based on the device's imei number.
     * */
    private void getNotificationService() {
        Call<NotificationModel> callRetrofit = null;

        applicationKey = LiveConstants.PERSONAL_APP;                                // for testing purpose.

        if (LiveConstants.PERSONAL_APP == applicationKey) {
            callRetrofit = Constants.service.getNotificationForPersonal("869696049497758"/*preferenceUtils.getStringFromPreference(PreferenceUtils.IMEI_NUMBER, "")*/, pageNumber, 10);
        } else if (LiveConstants.DRIVER_APP == applicationKey) {
            callRetrofit = Constants.service.getNotificationForDriver(preferenceUtils.getStringFromPreference(PreferenceUtils.DRIVERSER_NO, ""));
        } else {
            Toast.makeText(NotificationsActivity.this, R.string.something_wrong, Toast.LENGTH_LONG).show();
            return;
        }

        callRetrofit.enqueue(new Callback<NotificationModel>() {
            @Override
            public void onResponse(Call<NotificationModel> call, Response<NotificationModel> response) {
                swipeToRefresh.setRefreshing(false);
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        NotificationModel notificationModel = response.body();
                        ArrayList<Notifications> allNotifications = notificationModel.getContent();
                        pageNumber = notificationModel.getPageable().getPageNumber();
                        totalPages = notificationModel.getTotalPages();
                        if (allNotifications.size() > 0) {
                            no_records.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            mAdapter = new NotificationsAdapter(NotificationsActivity.this, allNotifications, notificationTypes);
                            recyclerView.setAdapter(mAdapter);
                        } else {
                            no_records.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
                    } else {
                        no_records.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<NotificationModel> call, Throwable t) {
                no_records.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                swipeToRefresh.setRefreshing(false);
                Log.d(TAG, "Error Call" + call.toString());
                Log.d(TAG, "Error" + t.toString());
            }
        });
    }


    private void hitForNextPage() {

        Call<NotificationModel> callRetrofit = null;

        applicationKey = LiveConstants.PERSONAL_APP;                                // for testing purpose.

        Log.d(TAG, "pageNumber ===> " + pageNumber);

        if (LiveConstants.PERSONAL_APP == applicationKey) {
            callRetrofit = Constants.service.getNotificationForPersonal("869696049497758"/*preferenceUtils.getStringFromPreference(PreferenceUtils.IMEI_NUMBER, "")*/, pageNumber, 10);
        } else if (LiveConstants.DRIVER_APP == applicationKey) {
            callRetrofit = Constants.service.getNotificationForDriver(preferenceUtils.getStringFromPreference(PreferenceUtils.DRIVERSER_NO, ""));
        } else {
            Toast.makeText(NotificationsActivity.this, R.string.something_wrong, Toast.LENGTH_LONG).show();
            return;
        }

        callRetrofit.enqueue(new Callback<NotificationModel>() {
            @Override
            public void onResponse(Call<NotificationModel> call, Response<NotificationModel> response) {
                swipeToRefresh.setRefreshing(false);
                if (response.isSuccessful()) {
                    progress.setVisibility(View.GONE);
                    if (response.body() != null) {
                        NotificationModel notificationModel = response.body();
                        ArrayList<Notifications> allNotifications = notificationModel.getContent();
                        if (allNotifications.size() > 0) {
                            mAdapter.updateAdapter(allNotifications);
                        } else {
                            pageNumber--;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<NotificationModel> call, Throwable t) {
                progress.setVisibility(View.GONE);
                no_records.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                swipeToRefresh.setRefreshing(false);
                Log.d(TAG, "Error Call" + call.toString());
                Log.d(TAG, "Error" + t.toString());
            }
        });

    }


    /*
     *       To initiate all the view from xml file
     * */
    public void headerControls() {
        TextView header_text;
        ImageView back_button;
        header_text = (TextView) findViewById(R.id.header_text);
        no_records = (TextView) findViewById(R.id.no_records);
        back_button = (ImageView) findViewById(R.id.back_button);
        swipeToRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
        recyclerView = (ShimmerRecyclerView) findViewById(R.id.recycler_view);
        progress = (ProgressBar) findViewById(R.id.progress);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        header_text.setText(getResources().getString(R.string.notifications));
        header_text.setVisibility(View.VISIBLE);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
