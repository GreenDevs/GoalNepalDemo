package com.crackdevelopers.goalnepal.Miscallenous.Gallery;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.crackdevelopers.goalnepal.R;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

public class PhotosPagerActivity extends AppCompatActivity
{
    private CircularProgressView progressView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photopager);

        ViewPager pager=(ViewPager)findViewById(R.id.photo_view_pager);
        PhotoSlideAdapter mSlideAdapter=new PhotoSlideAdapter(this);
        pager.setAdapter(mSlideAdapter);


        ///###################### PROGRESS BAR
        progressView = (CircularProgressView)findViewById(R.id.progress_view_slide_show);
        progressView.startAnimation();
        progressView.setVisibility(View.VISIBLE);
    }

    void setPVisibility(boolean visibility)
    {
        if(visibility){ progressView.setVisibility(View.VISIBLE);}
        else
        {
            progressView.setVisibility(View.GONE);
        }
    }

}
