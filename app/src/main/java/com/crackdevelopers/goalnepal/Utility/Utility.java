package com.crackdevelopers.goalnepal.Utility;

import android.content.Context;
import android.content.res.Configuration;

/**
 * Created by trees on 9/14/15.
 */
public class Utility
{
    //GET TO KNOW IS THE DEVICE IS TABLET OR A MOBILE
    public static boolean isTablet(Context context)
    {
        return (context.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK)>= Configuration.SCREENLAYOUT_SIZE_LARGE;

    }
}
