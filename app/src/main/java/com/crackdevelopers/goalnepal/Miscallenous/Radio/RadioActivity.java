package com.crackdevelopers.goalnepal.Miscallenous.Radio;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.crackdevelopers.goalnepal.R;

import co.mobiwise.library.RadioListener;
import co.mobiwise.library.RadioManager;


    public  class RadioActivity extends AppCompatActivity implements RadioListener {
        NotificationManager mNotificationManager;

        /**
         * Example radio stream URL
         */
        private final String RADIO_URL = "http://206.51.239.96:8129";

        /**
         * Radio Manager initialization
         */
        RadioManager mRadioManager = RadioManager.with(this);

        Button mButtonControl;
        TextView mTextViewControl;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_radio);
            Toolbar toolbar=(Toolbar)findViewById(R.id.app_bar);
            setSupportActionBar(toolbar);
            if(getSupportActionBar()!=null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            /**
             * Register this class to manager. @onRadioStarted, @onRadioStopped and @onMetaDataReceived
             * Listeners will be notified.
             */
            mRadioManager.registerListener(this);

            /**
             * initialize layout widgets to play, pause radio.
             */
            initializeUI();

        }

        public void initializeUI(){
            mButtonControl = (Button) findViewById(R.id.playRadio);
            mTextViewControl = (TextView) findViewById(R.id.progressbarRadio);

            mButtonControl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mRadioManager.isPlaying())
                        mRadioManager.startRadio(RADIO_URL);
                    else
                        mRadioManager.stopRadio();
                }
            });
        }

        @Override
        protected void onStart() {
            super.onStart();
            /**
             * Remember to connect manager to start service.
             */
            mRadioManager.connect();
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            /**
             * Remember to disconnect from manager.
             */
            mRadioManager.disconnect();
        }

        //@Override
        public void onRadioConnected() {
            // Called when the service is connected, allowing, for example, for starting the stream as soon as possible
            // mRadioManager.startRadio(RADIO_URL);
        }

        @Override
        public void onRadioStarted() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {




                    mTextViewControl.setText("RADIO STATE : PLAYING...");
                    setNotification();
                }
            });

                 }

        private void setNotification() {


            RemoteViews remoteViews = new RemoteViews(getPackageName(),
                    R.layout.notification_layout);
            NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(
                    this).setSmallIcon(R.mipmap.ic_launcher).setContent(
                    remoteViews);
            mBuilder.setOngoing(true);


            // Creates an explicit intent for an Activity in your app
            Intent resultIntent = new Intent(this, RadioActivity.class);
            // The stack builder object will contain an artificial back stack for
            // the
            // started Activity.
            // This ensures that navigating backward from the Activity leads out of
            // your application to the Home screen.
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            // Adds the back stack for the Intent (but not the Intent itself)
            stackBuilder.addParentStack(RadioActivity.class);
            // Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.button1, resultPendingIntent);


            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            // mId allows you to update the notification later on.
            mNotificationManager.notify(100, mBuilder.build());


        }

        @Override
        public void onRadioStopped() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //TODO Do UI works here
                    mTextViewControl.setText("RADIO STATE : STOPPED.");
                }
            });
            mNotificationManager.cancelAll();
        }

        @Override
        public void onMetaDataReceived(String s, String s1) {
            //TODO Check metadata values. Singer name, song name or whatever you have.
        }



        @Override
        public boolean onOptionsItemSelected(MenuItem item)
        {
            int id=item.getItemId();
            if(id==android.R.id.home)
            {
                this.finish();
                return  true;
            }
            return super.onOptionsItemSelected(item);
        }

    }
