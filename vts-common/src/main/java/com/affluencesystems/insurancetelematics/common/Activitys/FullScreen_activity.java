package com.affluencesystems.insurancetelematics.common.Activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.affluencesystems.insurancetelematics.R;
import com.affluencesystems.insurancetelematics.common.Utils.LiveConstants;
import com.affluencesystems.insurancetelematics.common.Utils.TouchToZoomImageView;
import com.squareup.picasso.Picasso;

/**
 * Created by WingHinChan on 2016/09/28.
 */

public class FullScreen_activity extends Base_activity {

    ImageView back_button;
    TouchToZoomImageView backdrop;
    String image_link;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        image_link = getIntent().getStringExtra(LiveConstants.FULL_IMAGE_KEY);
        backdrop = findViewById(R.id.picture);
        back_button = findViewById(R.id.back_button);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


/*
*       Take the image url and check if it's not null and empty then it will show image as full screen.
* */
        if (image_link != null && !image_link.equals("")) {
            Picasso.with(getApplicationContext()).load(image_link)/*.fit().centerCrop()*/
                    .placeholder(R.drawable.profile_sample)
                    .error(R.drawable.profile_sample)
                    .into(backdrop);

        } else {
            message(FullScreen_activity.this, getString(R.string.image_uri_not_found));
        }
    }

    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
        super.onBackPressed();

    }
}
