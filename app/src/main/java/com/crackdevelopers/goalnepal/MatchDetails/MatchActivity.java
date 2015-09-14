package com.crackdevelopers.goalnepal.MatchDetails;

import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.crackdevelopers.goalnepal.R;
import com.crackdevelopers.goalnepal.Volley.VolleySingleton;

import io.karim.MaterialTabs;

public class MatchActivity extends ActionBarActivity
{

    //JUST THE KEYS TO PULL THE BUNDLE DATA FORM THE INTENT
    private static final String MATCH_BUNDLE="match_bundle";
    private static final String MATCH_SCORE_A="score_A";
    private static final String MATCH_SCORE_B="score_B";
    private static final String MATCH_CLUB_NAME_A="club_name_a";
    private static final String MATCH_CLUB_NAME_B="club_name_b";
    private static final String MATCH_ICON_A="match_icon_url_a";
    private static final String MATCH_ICON_B="match_icon_url_b";
    private static final String MATCH_ID_KEY="match_id";
    static long MATCH_ID=-1;
    static String TEAM_A="";
    static String TEAM_B="";
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        init();
    }

    public void init()
    {
        Toolbar toolbar=(Toolbar)findViewById(R.id.app_bar);
        setToolBar(toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new MatchPagerAdapter(getSupportFragmentManager()));

        MaterialTabs tabs = (MaterialTabs) findViewById(R.id.material_tabs);
        tabs.setViewPager(viewPager);
        tabs.setTextColorResource(R.color.unselected_tabs);
    }
    
    public void setToolBar(final Toolbar toolbar)
    {
        Bundle bundle=getIntent().getBundleExtra(MATCH_BUNDLE);

        MATCH_ID=bundle.getLong(MATCH_ID_KEY, -1);
        TEAM_A=bundle.getString(MATCH_CLUB_NAME_A, "");
        TEAM_B=bundle.getString(MATCH_CLUB_NAME_B,"");
        ((TextView)toolbar.findViewById(R.id.match_nameA)).setText(TEAM_A);
        ((TextView)toolbar.findViewById(R.id.match_nameB)).setText(TEAM_B);

        ((TextView)toolbar.findViewById(R.id.match_score)).setText(bundle.getString(MATCH_SCORE_A,"")+" : "+bundle.getString(MATCH_SCORE_B,""));

        String teamAIcon=bundle.getString(MATCH_ICON_A,"");
        String teamBIcon=bundle.getString(MATCH_ICON_B,"");

        ImageLoader imageLoader= VolleySingleton.getInstance().getmImageLoader();
        imageLoader.get(teamAIcon,
                
        new ImageLoader.ImageListener() 
        {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) 
            {
                ((ImageView)toolbar.findViewById(R.id.match_iconA)).setImageBitmap(response.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError error) 
            {
                ((ImageView)toolbar.findViewById(R.id.match_iconA)).setImageResource(R.drawable.bayren);
            }
        });


        imageLoader.get(teamBIcon,

                new ImageLoader.ImageListener()
                {
                    @Override
                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate)
                    {
                        ((ImageView)toolbar.findViewById(R.id.match_iconB)).setImageBitmap(response.getBitmap());
                    }

                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        ((ImageView)toolbar.findViewById(R.id.match_iconB)).setImageResource(R.drawable.bayren);
                    }
                });
        
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id=item.getItemId();

        if(id==android.R.id.home)
        {
            this.finish();
            return true;
        }

        return false;
    }
}
