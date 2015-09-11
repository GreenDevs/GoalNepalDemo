package com.crackdevelopers.goalnepal.Miscallenous.videos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.crackdevelopers.goalnepal.R;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

public class VideoListActivity extends AppCompatActivity {
    YouTubeThumbnailView mYouTubeThumbnailView;
    YouTubeThumbnailLoader mYouTubeThumbnailLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        Toolbar toolbar=(Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mYouTubeThumbnailView = (YouTubeThumbnailView)findViewById(R.id.videoThumbalil);
        mYouTubeThumbnailView.setTag("Cvfvn27Akxk");


        mYouTubeThumbnailView.initialize("AIzaSyD2EawPYwjp7i_2Bt4APkcONoK0mQyRAgI", new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {

               /* String videoId = (String) view.getTag();
                loaders.put(youTubeThumbnailView, youTubeThumbnailLoader);*/
                youTubeThumbnailView.setImageResource(R.drawable.chelse);
                youTubeThumbnailLoader.setVideo("Cvfvn27Akxk");
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });



    }


    public void videoClicked(View v) {




      Intent intent = YouTubeStandalonePlayer.createVideoIntent(this, "AIzaSyD2EawPYwjp7i_2Bt4APkcONoK0mQyRAgI", "Cvfvn27Akxk");


        startActivity(intent);
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

    @Override
    protected void onStop()
    {
        super.onStop();

    }
}
