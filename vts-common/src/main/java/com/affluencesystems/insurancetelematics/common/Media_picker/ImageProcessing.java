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
 * File Name:  ImageProcessing.java
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

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;


import java.util.ArrayList;
import java.util.List;


class ImageProcessing {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static List<String> processMultiImage(Context context, Intent data) {
        List<String> listOfImgs = new ArrayList<>();
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) && (null == data.getData()))
        {
            ClipData clipdata = data.getClipData();
            for (int i = 0; i< (clipdata != null ? clipdata.getItemCount() : 0); i++)
            {
                Uri selectedImage = clipdata.getItemAt(i).getUri();
                String selectedImagePath = FileProcessing.getPath(context, selectedImage);
                listOfImgs.add(selectedImagePath);
            }
        }
        return listOfImgs;
    }

}


