
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
 * File Name:  ImagePicker.java
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
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;

import java.lang.ref.WeakReference;


public class ImagePicker {

    /**
     * The constant IMAGE_PICKER_REQUEST_CODE.
     */
    public static final int IMAGE_PICKER_REQUEST_CODE = 42141;
    /**
     * The constant EXTRA_IMAGE_PATH.
     */
    public static final String EXTRA_IMAGE_PATH = "EXTRA_IMAGE_PATH";

    /**
     * Instantiates a new Image picker.
     *
     * @param builder the builder
     */
    ImagePicker(Builder builder) {

        // Required
        WeakReference<Activity> context = builder.context;

        // Optional
        ImageConfig imageConfig = builder.imageConfig;
        Intent callingIntent = ImageActivity.getCallingIntent(context.get(), imageConfig);

        context.get().startActivityForResult(callingIntent, IMAGE_PICKER_REQUEST_CODE);
    }


    /**
     * The enum Extension.
     */
    public enum Extension {
        /**
         * Png extension.
         */
        PNG(".png"), /**
         * Jpg extension.
         */
        JPG(".jpg");
        private final String value;

        Extension(String value) {
            this.value = value;
        }

        /**
         * Gets value.
         *
         * @return the value
         */
        public String getValue() {
            return value;
        }
    }


    /**
     * The enum Comperes level.
     */
    public enum ComperesLevel {
        /**
         * Hard comperes level.
         */
        HARD(20), /**
         * Medium comperes level.
         */
        MEDIUM(50), /**
         * Soft comperes level.
         */
        SOFT(80), /**
         * None comperes level.
         */
        NONE(100);
        private final int value;

        ComperesLevel(int value) {
            this.value = value;
        }

        /**
         * Gets value.
         *
         * @return the value
         */
        public int getValue() {
            return value;
        }

    }

    /**
     * The enum Mode.
     */
    public enum Mode {
        /**
         * Camera mode.
         */
        CAMERA(0), /**
         * Gallery mode.
         */
        GALLERY(1), /**
         * Camera and gallery mode.
         */
        CAMERA_AND_GALLERY(2);
        private final int value;

        Mode(int value) {
            this.value = value;
        }

        /**
         * Gets value.
         *
         * @return the value
         */
        public int getValue() {
            return value;
        }
    }

    /**
     * The enum Directory.
     */
    public enum Directory {
        /**
         * Default directory.
         */
        DEFAULT(0);
        private final int value;

        Directory(int value) {
            this.value = value;
        }

        /**
         * Gets value.
         *
         * @return the value
         */
        public int getValue() {
            return value;
        }
    }

    /**
     * The type Builder.
     */
    public static class Builder implements ImagePickerBuilderBase {

        // Required params
        private final WeakReference<Activity> context;

        private ImageConfig imageConfig;

        /**
         * Instantiates a new Builder.
         *
         * @param context the context
         */
        public Builder(Activity context) {
            this.context = new WeakReference<>(context);
            this.imageConfig = new ImageConfig();
        }

        @Override
        public Builder compressLevel(ComperesLevel compressLevel) {
            this.imageConfig.compressLevel = compressLevel;
            return this;
        }

        @Override
        public Builder mode(Mode mode) {
            this.imageConfig.mode = mode;
            return this;
        }

        @Override
        public Builder directory(String directory) {
            this.imageConfig.directory = directory;
            return this;
        }

        @Override
        public Builder directory(Directory directory) {
            switch (directory) {
                case DEFAULT:
                    this.imageConfig.directory = Environment.getExternalStorageDirectory() + ImageTags.Tags.IMAGE_PICKER_DIR;
                    break;
                default:
                    break;
            }
            return this;
        }

        @Override
        public Builder extension(Extension extension) {
            this.imageConfig.extension = extension;
            return this;
        }

        @Override
        public Builder scale(int minWidth, int minHeight) {
            this.imageConfig.reqHeight = minHeight;
            this.imageConfig.reqWidth = minWidth;
            return this;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public Builder allowMultipleImages(boolean allowMultiple) {
            this.imageConfig.allowMultiple = allowMultiple;
            return this;
        }

        @Override
        public Builder enableDebuggingMode(boolean debug) {
            this.imageConfig.debug = debug;
            return this;
        }

        @Override
        public Builder allowOnlineImages(boolean allowOnlineImages) {
            this.imageConfig.allowOnlineImages = allowOnlineImages;
            return this;
        }


        @Override
        public ImagePicker build() {
            return new ImagePicker(this);
        }


        /**
         * Gets context.
         *
         * @return the context
         */
        public Activity getContext() {
            return context.get();
        }

    }
}
