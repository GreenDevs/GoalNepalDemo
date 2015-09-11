package com.crackdevelopers.goalnepal.MatchDetails;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by trees on 8/25/15.
 */
public class MatchPagerAdapter extends FragmentStatePagerAdapter {

    private final String[] TITLES = {"COMNTRY", "STATS", "LINE UPS"};

    public MatchPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int i)
    {
        switch (i)
        {
            case 0:
                return new MatchCommentry();

            case 1:
                return new MatchStats();

            case 2:
                return new MatchLineUps();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }

}
