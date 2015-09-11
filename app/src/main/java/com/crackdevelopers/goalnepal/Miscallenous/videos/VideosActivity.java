package com.crackdevelopers.goalnepal.Miscallenous.videos;

import android.os.Bundle;
import android.view.Window;

import com.crackdevelopers.goalnepal.R;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;


public class VideosActivity extends UtubeFailureRecovery implements YouTubePlayer.OnFullscreenListener,YouTubePlayer.OnInitializedListener {


	private String id;
	YouTubePlayerView youTubeView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tv);
		youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);



		id = "pRpeEdMmmQ0";



		youTubeView
		.initialize(
				getString(R.string.youtube_api_key), this);
	}
	@Override
	public void onInitializationSuccess(YouTubePlayer.Provider provider,
			YouTubePlayer player, boolean wasRestored) {

		if (!wasRestored) {
			player.setFullscreen(false);

			player.addFullscreenControlFlag(8);
			player.cueVideo(id);


		}

	}

	@Override
	protected Provider getYouTubePlayerProvider() {

		return (YouTubePlayerView) findViewById(R.id.youtube_view);
	}

	@Override
	protected void onPause() {

		super.onPause();
	}
	@Override
	public void onFullscreen(boolean arg0) {



	}
}
