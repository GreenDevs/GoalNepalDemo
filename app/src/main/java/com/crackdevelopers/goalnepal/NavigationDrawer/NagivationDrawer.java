package com.crackdevelopers.goalnepal.NavigationDrawer;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.crackdevelopers.goalnepal.FileManager;
import com.crackdevelopers.goalnepal.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class NagivationDrawer extends Fragment
{
    private final static String SHARED_PREF="com.crackdevelopers.goalnepal.Miscallenous.Preferences";
    private final static String INTERNATIONAL="internationals";
    private final static String DOMESTICS="domestics";
    private final static String TOURNAMENT_TITLE="title";
    private final static String TOURNAMENT_ID="id";
    private final static String TOURNAMENTS="tournaments";
    private final static String START_DATE="start_date";

    private RecyclerView navList;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private DrawerLayout thisDrawerLayout;
    private Context context;
    private NavStickyAdapter mAdapter;
    private List<NavigationRow> navigationRowList;


    public NagivationDrawer()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.nagivation_drawer, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        context=getActivity();
        navList=(RecyclerView)getActivity().findViewById(R.id.nav_recycler);
        navList.setLayoutManager(new LinearLayoutManager(context));
   
        setNavDrawer();
    }


    @Override
    public void onStart()
    {
        super.onStart();
        setNavDrawer();

    }

    private void setNavDrawer()
    {
        FileManager fileManager=new FileManager(context);
        try
        {
            JSONObject reciviedJson=new JSONObject(fileManager.readFromFile());
            navigationRowList=pareseMenu(reciviedJson);
            mAdapter=new NavStickyAdapter(context, navigationRowList);
            navList.setAdapter(mAdapter);
        }
        catch (JSONException e)
        {
            navigationRowList=new ArrayList<>();
            Resources res=context.getResources();
            navigationRowList.add(new NavigationRow("Home", res.getString(R.string.home), "Features", -1));
            navigationRowList.add(new NavigationRow("Radio", res.getString(R.string.radio),"Features", -1));
            navigationRowList.add(new NavigationRow("Live TV", res.getString(R.string.video),"Features", -1));
            navigationRowList.add(new NavigationRow("Video Gallery", res.getString(R.string.video),"Features", -1));
            navigationRowList.add(new NavigationRow("Gallery", res.getString(R.string.gallery),"Features", -1));
            navigationRowList.add(new NavigationRow("World Ranking", res.getString(R.string.gallery),"Features", -1));
            navigationRowList.add(new NavigationRow("Select Tournaments", res.getString(R.string.preferences),"Features", -1));
            mAdapter=new NavStickyAdapter(context,navigationRowList);
            navList.setAdapter(mAdapter);
            e.printStackTrace();
        }
    }


    public void setup(DrawerLayout mDrawerLayout, Toolbar mToolbar)
    {


        thisDrawerLayout = mDrawerLayout;
        mActionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_closed) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }


        };

        thisDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
        thisDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mActionBarDrawerToggle.syncState();
            }
        });

    }

      
        private List<NavigationRow> pareseMenu(JSONObject menuJson)
        {
            navigationRowList=new ArrayList<>();
            Resources res=context.getResources();
            navigationRowList.add(new NavigationRow("Home", res.getString(R.string.home), "Features", -1));
            navigationRowList.add(new NavigationRow("Radio", res.getString(R.string.radio),"Features", -1));
            navigationRowList.add(new NavigationRow("Live TV", res.getString(R.string.video),"Features", -1));
            navigationRowList.add(new NavigationRow("Video Gallery", res.getString(R.string.video),"Features", -1));
            navigationRowList.add(new NavigationRow("Gallery", res.getString(R.string.gallery),"Features", -1));
            navigationRowList.add(new NavigationRow("World Ranking", res.getString(R.string.gallery),"Features", -1));
            navigationRowList.add(new NavigationRow("Select Tournaments", res.getString(R.string.preferences),"Features", -1));
            if (menuJson != null)
                try
                {
                    String TYPE = "";
                    String tornmntIcon=res.getString(R.string.leagues);
                    SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
                    for (int z = 0; z < 2; z++) {
                        if (z == 0) TYPE = INTERNATIONAL;
                        if (z == 1) TYPE = DOMESTICS;


                        if (menuJson.has(TYPE)) {


                            JSONArray internationals = (JSONArray) menuJson.get(TYPE);
                            final int internationSize = internationals.length();
                            for (int i = 0; i < internationSize; i++) {


                                JSONObject international = internationals.getJSONObject(i);

                                if (international.has(TOURNAMENTS)) {
                                    JSONArray tournaments = (JSONArray) international.get(TOURNAMENTS);
                                    final int tournaments_count = tournaments.length();
                                    for (int j = 0; j < tournaments_count; j++) {
                                        JSONObject tournament = tournaments.getJSONObject(j);
                                        String child_title = "";
                                        Long child_id = 0L;
                                        String start_date, year = "0000";


                                        if (tournament.has(START_DATE)) {
                                            start_date = tournament.getString(START_DATE);
                                            year = start_date.substring(0, 4);
                                        }
                                        int currnetYear = Calendar.getInstance().get(Calendar.YEAR);

                                        if (year.equals(currnetYear + "") || year.equals((currnetYear - 1) + "")) {
                                            if (tournament.has(TOURNAMENT_ID))
                                                child_id = tournament.getLong(TOURNAMENT_ID);
                                            if (tournament.has(TOURNAMENT_TITLE))
                                                child_title = tournament.getString(TOURNAMENT_TITLE);

                                            boolean checked = preferences.getBoolean(child_id + "", false);          //PULLING PREFERRED SETTIG FROM DATA
                                            if (checked)
                                                navigationRowList.add(new NavigationRow(child_title, tornmntIcon, "Tournaments",child_id));
                                        }

                                    }

                                }


                            }

                        }
                    }

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            return navigationRowList;

        }
    }




