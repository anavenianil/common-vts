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
 * File Name:  Constants.java
 *
 * Description: this file we are declaring constants that are used in different classes.
 *
 * Routines in this file:
 *
 * Configuration Identifier: <Config ID:>
 *
 * Modification History:
 *    Created by:      shiva        1.0         31/10/2018
 *    Description:    his file we are declaring constants that are used in different classes.
 *

 ****************************************************************************/

package com.affluencesystems.insurancetelematics.common.ApiUtils;

public class Constants {

    public static  API_class service = Retrofit_funtion_class.getClient().create(API_class.class);
    public static  API_class service2= Retrofit_fuction_class2.getClient().create(API_class.class);
    public static  API_class service_config = Retrofit_function_class3.getClient().create(API_class.class);
    public static  API_class service_fences = Retrofit_function_class4.getClient().create(API_class.class);

}
