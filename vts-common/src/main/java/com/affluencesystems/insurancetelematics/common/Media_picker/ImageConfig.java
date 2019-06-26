
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
 * File Name:  ImageConfig.java
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

import android.os.Environment;

import java.io.Serializable;


class ImageConfig implements Serializable {

    protected ImagePicker.Extension extension;
    protected ImagePicker.ComperesLevel compressLevel;
    protected ImagePicker.Mode mode;
    protected String directory;
    protected int reqHeight;
    protected int reqWidth;
    protected boolean allowMultiple;
    protected boolean isImgFromCamera;
    protected boolean allowOnlineImages;
    protected boolean debug;


    /*
    *       constructor
    * */
    ImageConfig() {
        this.extension = ImagePicker.Extension.PNG;
        this.compressLevel = ImagePicker.ComperesLevel.NONE;
        this.mode = ImagePicker.Mode.CAMERA;
        this.directory = Environment.getExternalStorageDirectory() + ImageTags.Tags.IMAGE_PICKER_DIR;
        this.reqHeight = 0;
        this.reqWidth = 0;
        this.allowMultiple = false;
        this.allowOnlineImages = false;
    }

    @Override
    public String toString() {
        return "ImageConfig{" +
                "extension=" + extension +
                ", compressLevel=" + compressLevel +
                ", mode=" + mode +
                ", directory='" + directory + '\'' +
                ", reqHeight=" + reqHeight +
                ", reqWidth=" + reqWidth +
                ", allowMultiple=" + allowMultiple +
                ", isImgFromCamera=" + isImgFromCamera +
                ", allowOnlineImages=" + allowOnlineImages +
                ", debug=" + debug +
                '}';
    }
}
