package com.crackdevelopers.goalnepal.Miscallenous.liveTV;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.crackdevelopers.goalnepal.R;
import com.crackdevelopers.goalnepal.Volley.VolleySingleton;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

public class LiveTVActivity extends AppCompatActivity
{
    private static final String Url="http://www.goalnepal.com/json_tv.php/";
    private  String livetvUrl;

    YouTubeThumbnailView mYouTubeThumbnailView;
    YouTubeThumbnailLoader mYouTubeThumbnailLoader;
    private RequestQueue queue;
    private Context mContext;
    private  String [] temp;
    ImageButton playLiveTv;


    public LiveTVActivity()
    {
        this.mContext=this;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_tv);
        queue= VolleySingleton.getInstance().getQueue();
        sendVideoRequest();
        playLiveTv = (ImageButton)findViewById(R.id.playLive);
        playLiveTv.setVisibility(View.GONE);


        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        mYouTubeThumbnailLoader = new YouTubeThumbnailLoader() {
            @Override
            public void setOnThumbnailLoadedListener(OnThumbnailLoadedListener onThumbnailLoadedListener) {

            }

            @Override
            public void setVideo(String s) {

            }

            @Override
            public void setPlaylist(String s) {

            }

            @Override
            public void setPlaylist(String s, int i) {

            }

            @Override
            public void next() {

            }

            @Override
            public void previous() {

            }

            @Override
            public void first() {

            }

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }

            @Override
            public void release() {

            }
        };
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Live TV");
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mYouTubeThumbnailView = (YouTubeThumbnailView) findViewById(R.id.videoThumbalil);
        displayads();


    }




    public void videoClicked(View v)
    {


        try {
            Intent intent = YouTubeStandalonePlayer.createVideoIntent(this, "AIzaSyD2EawPYwjp7i_2Bt4APkcONoK0mQyRAgI", temp[1]);


            startActivity(intent);
        }catch ( Exception e) {

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mYouTubeThumbnailLoader.release();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mYouTubeThumbnailLoader.release();

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

    }




    private void sendVideoRequest()
    {
        StringRequest request=new StringRequest(Request.Method.GET, Url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
               livetvUrl = response;
                doafterloadingUrl();
            }
        }
        ,

        new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {

            }
        });


        queue.add(request);
    }


    private  void doafterloadingUrl() {

         temp = livetvUrl.split("=");


        mYouTubeThumbnailView.setTag(temp[1]);


        mYouTubeThumbnailView.initialize("AIzaSyD2EawPYwjp7i_2Bt4APkcONoK0mQyRAgI", new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {

               /* String videoId = (String) view.getTag();
                loaders.put(youTubeThumbnailView, youTubeThumbnailLoader);*/

                youTubeThumbnailView.setImageResource(R.drawable.livetv);
                youTubeThumbnailLoader.setVideo(temp[1]);
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });
        playLiveTv.setVisibility(View.VISIBLE);
        mYouTubeThumbnailView.setVisibility(View.VISIBLE);

    }

    private  void displayads() {
        AdView mAdView = (AdView) findViewById(R.id.adViewLiveTv);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("DF748C37109613E8C305043552A7F153").build();
        mAdView.loadAd(adRequest);
    }


}
