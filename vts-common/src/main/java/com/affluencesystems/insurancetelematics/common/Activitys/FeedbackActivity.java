/*****************************************************************************
 * Copyright (C) 2018, Affluence Infosystems Pvt Ltd.
 *  All Rights Reserved.
 *
 * This is UNPUBLISHED PROPRIETARY SOURCE CODE of Affluence Infosystems,
 * the contents of this file may not be disclosed to third parties, copied
 * or duplicated in any form, in whole or in part, without the prior
 * written permission of Affluence Infosystems.
 *
 *
 * File Name:  FeedbackActivity.java
 *
 * Description: this class is used for to submit feedback.
 *
 * Routines in this file:
 *     headerControls()
 *     submitFeedBack()
 *     updateService()
 *
 * Configuration Identifier: <Config ID:>
 *
 * Modification History:
 *    Created by:      anil        1.0         01/12/2018
 *    Description:    design and fuctionality completed
 ****************************************************************************/
package com.affluencesystems.insurancetelematics.common.Activitys;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.affluencesystems.insurancetelematics.common.Models.Feedback;
import com.affluencesystems.insurancetelematics.R;
import com.affluencesystems.insurancetelematics.common.ApiUtils.Constants;
import com.affluencesystems.insurancetelematics.common.Utils.PreferenceUtils;
import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.affluencesystems.insurancetelematics.common.Utils.ConnectivityReceiver.isConnected;

public class FeedbackActivity extends Base_activity implements View.OnClickListener {
    public static String feedbacktype;
    public Float rating_ = 0f;
    private Spinner feedback_type;
    private EditText tv_description;
    private RatingBar rating_bar;
    private TextView txt_rating_value, submit;
    private LinearLayout rating_bar_layout;
    private PreferenceUtils preferenceUtils;
    private PreferenceUtils getPreferenceUtilsadmin;
    private ProgressDialog progressDialog;
    private String orgId="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        headerControls();
        orgId=getIntent().getStringExtra("organizationId");


        ArrayAdapter<CharSequence> doc_adapter = ArrayAdapter.createFromResource(this,
                R.array.feedback, R.layout.spinner_text_size);
        doc_adapter.setDropDownViewResource(R.layout.spinner_text_size);
        feedback_type.setAdapter(doc_adapter);
        feedback_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                feedbacktype = feedback_type.getSelectedItem().toString();
                if (feedbacktype.equals("Bug/Crash/Problem")) {
                    rating_bar_layout.setVisibility(View.GONE);
                } else rating_bar_layout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        rating_bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                txt_rating_value.setText(String.valueOf(rating));
                rating_ = rating;

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
        back_button = (ImageView) findViewById(R.id.back_button);
        feedback_type = (Spinner) findViewById(R.id.feedback_type);
        tv_description = (EditText) findViewById(R.id.tv_description);
        rating_bar = (RatingBar) findViewById(R.id.rating_bar);
        txt_rating_value = (TextView) findViewById(R.id.txt_rating_value);
        submit = (TextView) findViewById(R.id.submit);
        rating_bar_layout = (LinearLayout) findViewById(R.id.rating_bar_layout);
        header_text.setText(getResources().getString(R.string.feedback));
        header_text.setVisibility(View.VISIBLE);
        back_button.setOnClickListener(this);
        submit.setOnClickListener(this);
    }


    /*
     *       To validate all the fields in the screen and submit feedback to the server.
     * */
    public void submitFeedBack() {
        if (rating_ == 0 && rating_bar_layout.getVisibility() == View.VISIBLE) {
            message(FeedbackActivity.this, getString(R.string.give_rating));
        } else if (tv_description.getText().toString().equals("")) {
            tv_description.setError(getResources().getString(R.string.description_error));
            tv_description.requestFocus();
        } else {
            progressDialog = new ProgressDialog(FeedbackActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getString(R.string.please_wait));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            preferenceUtils = new PreferenceUtils(this);
         if(orgId.equals(""))
             orgId="0";
            Feedback feedback = new Feedback(orgId,
                    preferenceUtils.getStringFromPreference(PreferenceUtils.PERSON_ID, ""),
                    tv_description.getText().toString(), rating_, feedbacktype);
            Call<JsonElement> callRetrofit = null;
            callRetrofit = Constants.service2.saveFeedbackDetails(feedback);

            callRetrofit.enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                    progressDialog.dismiss();
                    if (response.isSuccessful()) {
                        message(FeedbackActivity.this, getString(R.string.feedback_sent));
                        rating_ = 0f;
                        rating_bar.setRating(0);
                        tv_description.setText("");
                    } else {
                        message(FeedbackActivity.this, getString(R.string.failed));
                    }
                }

                @Override
                public void onFailure(Call<JsonElement> call, Throwable t) {
                    progressDialog.dismiss();
                    message(FeedbackActivity.this, getString(R.string.failed_to_connect_server));
                    Toast.makeText(FeedbackActivity.this, "feedback sending failed", Toast.LENGTH_LONG).show();
                    Log.d("Error Call", ">>>>" + call.toString());
                    Log.d("Error", ">>>>" + t.toString());
                }
            });

        }
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.back_button) {
            finish();

        } else if (i == R.id.submit) {
            if (isConnected()) {
                submitFeedBack();
            }
        }
    }
}
