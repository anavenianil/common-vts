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
 * File Name:  ImageTags.java
 *
 * Description: This file used for select image from the device .
 *
 * Routines in this file:
 *
 * Configuration Identifier: <Config ID:>
 *
 * Modification History:
 *    Created by:      Shiva        1.0         31/10/2018
 *    Description:     This file used for select image from the device .


 ****************************************************************************/
package com.affluencesystems.insurancetelematics.common.Media_picker;

public class ImageTags {
    public static final class Tags {
        public static final String TAG = "ImagePicker";
        public static final String CAMERA_IMAGE_URI = "cameraImageUri";
        public static final String IMAGE_PATH = "IMAGE_PATH";
        public static final String IMAGE_PICKER_DIR = "/mediapicker/images/";
        public static final String IMG_CONFIG = "IMG_CONFIG";
        public static final String PICK_ERROR = "PICK_ERROR";
        public static final String IS_ALERT_SHOWING = "IS_ALERT_SHOWING";
    }

    public static final class Action {
        public static final String SERVICE_ACTION = "net.alhazmy13.mediapicker.rxjava.image.service";
    }

    public final class IntentCode {
        public static final int REQUEST_CODE_SELECT_MULTI_PHOTO = 5341;
        public static final int CAMERA_REQUEST = 1888;
        public static final int REQUEST_CODE_ASK_PERMISSIONS = 123;
        public static final int REQUEST_CODE_SELECT_PHOTO = 43;


    }


}
