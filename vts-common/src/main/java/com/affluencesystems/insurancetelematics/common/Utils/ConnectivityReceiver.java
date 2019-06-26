package com.affluencesystems.insurancetelematics.common.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectivityReceiver extends BroadcastReceiver {

    public static ConnectivityReceiverListener connectivityReceiverListener;

    @Override
    public void onReceive(Context context, Intent arg1) {
        String action = arg1.getAction();
        if (("android.net.conn.CONNECTIVITY_CHANGE").equals(action)) {
//            Toast.makeText(context, "Implicit Broadcast was triggered using registerReceiver", Toast.LENGTH_SHORT).show();
        }
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
        if (connectivityReceiverListener != null) {
            connectivityReceiverListener.onNetworkConnectionChanged(isConnected);
        }
    }


    /*
    *       check if the mobile connected to network or not
    *       if true : mobile connected to network
    *       if false : mobile not connected to network
    * */
    public static boolean isConnected() {
        ConnectivityManager
                cm = (ConnectivityManager) MyApplication.getInstance().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }


    /*
    *       interface for check in every activity connected to network or not
    * */
    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }
}
