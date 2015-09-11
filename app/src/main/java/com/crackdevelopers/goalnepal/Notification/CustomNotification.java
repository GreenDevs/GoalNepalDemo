package com.crackdevelopers.goalnepal.Notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;

import com.crackdevelopers.goalnepal.MainActivity;
import com.crackdevelopers.goalnepal.R;

/**
 * Created by script on 9/1/15.
 */
public class CustomNotification implements Runnable
{
    Context mContext;
    NotificationManager mNotificationManager;
    private static final byte NOTIFICATION_ID=123;


    public CustomNotification(Context context)
    {
        this.mContext = context;
        this.mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

    }

    @Override
    public void run()
    {
        makeNotification(mContext);

    }

    private void makeNotification(Context context)
    {
        Intent intent = new Intent(context, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle("And Thats a Goal!")
                .setContentText("Barcelona 1 - 0 Athletico ")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.travel100)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.front_image))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})  //Vibration
                .setLights(Color.RED, 3000, 3000)       // LED
                ;
        Notification n;
        int  numMessages = 0;

// Start of a loop that processes data and then notifies the user
        builder.setContentText("Barcelona 1 - 0 Athletico").setNumber(++numMessages);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
        {
            n = builder.build();
        }
        else
        {
            n = builder.getNotification();   // Depricated.
        }

        // Because the ID remains unchanged, the existing notification is
        // updated.
        mNotificationManager.notify(NOTIFICATION_ID, n);

    }


}
