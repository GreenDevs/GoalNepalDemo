package com.crackdevelopers.goalnepal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.crackdevelopers.goalnepal.Adapters.MainPagerAdapter;
import com.crackdevelopers.goalnepal.Miscallenous.Gallery.AlbumActivity;
import com.crackdevelopers.goalnepal.Miscallenous.Preferences.PreferenceActivity;
import com.crackdevelopers.goalnepal.NavigationDrawer.NagivationDrawer;

import io.karim.MaterialTabs;

public class MainActivity extends AppCompatActivity
{

    public static final String NAV_BUNDLE_TAG="nav_item_pos";
    private DrawerLayout mDrawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init()
    {
        Toolbar toolbar=(Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_for_webpage);
        NagivationDrawer mNagivationDrawer = (NagivationDrawer) getSupportFragmentManager().findFragmentById(R.id.nagivation_drawer);
        mNagivationDrawer.setup(mDrawerLayout,toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));

        MaterialTabs tabs = (MaterialTabs) findViewById(R.id.material_tabs);
        tabs.setViewPager(viewPager);
        tabs.setTextColorResource(R.color.unselected_tabs);

    }


    public void galleryClicked(View v)
    {
        switch(v.getId())
        {
            case R.id.gal:
                startActivity(new Intent(this, AlbumActivity.class));
                break;

            case R.id.pref:
                startActivity(new Intent(this, PreferenceActivity.class));
                break;

        }

    }
}
