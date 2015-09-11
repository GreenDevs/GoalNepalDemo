package com.crackdevelopers.goalnepal.Leagues;

import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.crackdevelopers.goalnepal.NavigationDrawer.NagivationDrawer;

import com.crackdevelopers.goalnepal.R;

import io.karim.MaterialTabs;

public class LeagueActivity extends AppCompatActivity
{

    public static final String NAV_BUNDLE_TAG="nav_item_pos";
    private static final String T_ID="tournament_id";
    private static final String T_NAME="tournament_name";
    static long TOURNAMENT_ID=0;
    static String TOURNAMENT_NAME="";
    @Override

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_league);

        TOURNAMENT_ID=getIntent().getLongExtra(T_ID, 0);
        TOURNAMENT_NAME=getIntent().getStringExtra(T_NAME);
        init();
    }


    private void init()
    {
        Toolbar toolbar=(Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null) getSupportActionBar().setTitle(TOURNAMENT_NAME);

        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_for_webpage);
        NagivationDrawer mNagivationDrawer = (NagivationDrawer) getSupportFragmentManager().findFragmentById(R.id.nagivation_drawer);
        mNagivationDrawer.setup(mDrawerLayout, toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new LeaguePagerAdapter(getSupportFragmentManager()));
        MaterialTabs tabs = (MaterialTabs) findViewById(R.id.material_tabs);
        tabs.setViewPager(viewPager);
        tabs.setTextColorResource(R.color.unselected_tabs);
    }



}
