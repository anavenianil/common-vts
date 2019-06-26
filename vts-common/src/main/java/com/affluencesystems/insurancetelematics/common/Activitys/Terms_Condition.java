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
 * File Name:  Terms_Condition.java
 *
 * Description: This file is used in sign up screen to accept the terms and conditions.
 *
 * Routines in this file:
 *     startWebView()

 * Configuration Identifier: <Config ID:>
 *
 * Modification History:
 *    Created by:      Shiva        1.0         20/10/2018
 *    Description:    This file is used in sign up screen to accept the terms and conditions.

 ****************************************************************************/
package com.affluencesystems.insurancetelematics.common.Activitys;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.affluencesystems.insurancetelematics.R;

public class Terms_Condition extends Activity {

    //private Button button;
    private WebView webView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_conditions);
        //Get webview
        webView = (WebView) findViewById(R.id.webView1);
        /*
        *       call for load term&conditions page.
        * */
        startWebView("http://affluencesystems.com/terms/affluenceterms.html");
    }

    private void startWebView(String url) {
        //Create new webview Client to show progress dialog
        //When opening a url or click on link
        webView.setWebViewClient(new WebViewClient() {
            ProgressDialog progressDialog;
            //If you will not use this method url links are opeen in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            //Show loader on url load
            public void onLoadResource(WebView view, String url) {
                if (progressDialog == null) {
                    // in standard case YourActivity.this
                    progressDialog = new ProgressDialog(Terms_Condition.this);
                    progressDialog.setMessage(getString(R.string.loading));
                    progressDialog.show();
                }
            }

            public void onPageFinished(WebView view, String url) {
                try {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }

        });

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }

    // Open previous opened link from history on webview when back button pressed

    @Override
    // Detect when the back button is pressed
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            // Let the system handle the back button
            super.onBackPressed();
        }
    }

}

