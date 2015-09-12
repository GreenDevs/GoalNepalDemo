package com.crackdevelopers.goalnepal.Miscallenous.Gallery;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.crackdevelopers.goalnepal.R;

public class PhotosPagerActivity extends AppCompatActivity
{


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photopager);

        ViewPager pager=(ViewPager)findViewById(R.id.photo_view_pager);
        PhotoSlideAdapter mSlideAdapter=new PhotoSlideAdapter(this);
        pager.setAdapter(mSlideAdapter);
    }

}
