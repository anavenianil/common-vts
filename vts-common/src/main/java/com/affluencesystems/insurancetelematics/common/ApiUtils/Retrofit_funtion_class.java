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
 * File Name:  Retrofit_funtion_class.java
 *
 * Description: this class is very important  this is  retrofit library we are used for API service
 *
 * Routines in this file:
 *
 * Configuration Identifier: <Config ID:>
 *
 * Modification History:
 *    Created by:      anil        1.0         31/10/2018
 * Description: this class is very important  this is  retrofit library we are used for API service


 ****************************************************************************/


package com.affluencesystems.insurancetelematics.common.ApiUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Retrofit_funtion_class {

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {


        if (retrofit==null) {

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.MINUTES)
                    .readTimeout(5, TimeUnit.MINUTES)
                    .writeTimeout(5, TimeUnit.MINUTES)
                    .build();
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(DZ_URL.BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}
