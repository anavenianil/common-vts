package com.affluencesystems.insurancetelematics.common.Utils;

import android.app.Application;
import android.content.IntentFilter;

public class MyApplication extends Application {

    private static MyApplication mInstance;
    ConnectivityReceiver connectivityReceiver;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        connectivityReceiver = new ConnectivityReceiver();
        //  this.registerReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(connectivityReceiver, filter);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        unregisterReceiver(connectivityReceiver);
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }


    /*
    *           Add connectivity listener to base activity.
    *           For check the network connection.
    * */
    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
