package com.crackdevelopers.goalnepal.Miscallenous.Gallery;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.crackdevelopers.goalnepal.R;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class PhotosSlideActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photopager);

        ViewPager pager=(ViewPager)findViewById(R.id.photo_view_pager);
        PhotoSlideAdapter mSlideAdapter=new PhotoSlideAdapter(this);
        pager.setAdapter(mSlideAdapter);
        pager.setCurrentItem(getIntent().getIntExtra("ImagePosition",0));

        displayads();
    }

//    void setPVisibility(boolean visibility)
//    {
//        if(visibility)
//        {
//            progressView.setVisibility(View.VISIBLE);
//        }
//        else
//        {
//            progressView.setVisibility(View.GONE);
//        }
//    }



    private  void displayads() {
        AdView mAdView = (AdView) findViewById(R.id.adViewSlideShowBottom);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("DF748C37109613E8C305043552A7F153").build();
        mAdView.loadAd(adRequest);
    }

}
