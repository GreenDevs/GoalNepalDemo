package com.crackdevelopers.goalnepal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.crackdevelopers.goalnepal.AboutUS.AboutActivity;
import com.crackdevelopers.goalnepal.Adapters.MainPagerAdapter;
import com.crackdevelopers.goalnepal.NavigationDrawer.NagivationDrawer;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import io.karim.MaterialTabs;

public class MainActivity extends AppCompatActivity
{

    public static final String NAV_BUNDLE_TAG="nav_item_pos";
    private DrawerLayout mDrawerLayout;
    NagivationDrawer mNagivationDrawer;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        displayads();
    }

    private void init()
    {
        Toolbar toolbar=(Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_for_webpage);
        mNagivationDrawer = (NagivationDrawer) getSupportFragmentManager().findFragmentById(R.id.nagivation_drawer);
        mNagivationDrawer.setup(mDrawerLayout,toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));

        MaterialTabs tabs = (MaterialTabs) findViewById(R.id.material_tabs);
        tabs.setViewPager(viewPager);
        tabs.setTextColorResource(R.color.unselected_tabs);


    }

    ////FOR ABOUT US///
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about)
        {
            vibrate(20);
            startActivity(new Intent(this, AboutActivity.class));
            overridePendingTransition(R.anim.right_to_left, R.anim.static_anim);
            return true;
        }


        if (id == R.id.action_share)
        {
            vibrate(20);
            String shareBody = "Hello I am using New goalnepal app for android , give it a try    https://play.google.com/store/apps/details?id=com.crackdevelopers.goalnepal";
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Goal Nepal");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.Share)));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    public void vibrate (int time)
    {

        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        vibrator.vibrate(time);
    }


    private  void displayads() {
        AdView mAdView = (AdView) findViewById(R.id.adViewHome);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("DF748C37109613E8C305043552A7F153").build();
        mAdView.loadAd(adRequest);
    }


}
