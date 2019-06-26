package com.affluencesystems.insurancetelematics.common.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.affluencesystems.insurancetelematics.R;

public class NoNetworkDialog extends Dialog {

    static ImageView wifi_img;
    static Animation animation;
    public Activity c;
    public Dialog d;

    public NoNetworkDialog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }


    /*
    *       showing the wifi symbol, if mobile not connected to network.
    * */
    public static void startAnimation_wifi_img() {
        if (wifi_img != null && animation != null) {
            wifi_img.startAnimation(animation);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.no_wifi_dialog);
        wifi_img = findViewById(R.id.wifi_img);
        animation = new AlphaAnimation(1, 0); //to change visibility from visible to invisible
        animation.setDuration(1000); //1 second duration for each animation cycle
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE); //repeating indefinitely
        animation.setRepeatMode(Animation.REVERSE); //animation will start_trip from end point once ended.
        //to start_trip animation
    }

}
