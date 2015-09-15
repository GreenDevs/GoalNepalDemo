package com.crackdevelopers.goalnepal.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.crackdevelopers.goalnepal.Latest.LatestDemoFragment;
import com.crackdevelopers.goalnepal.Latest.LatestFragment;
import com.crackdevelopers.goalnepal.News.NewsFragment;
import com.crackdevelopers.goalnepal.RecentMatch.RecentMatchFragment;

public class MainPagerAdapter extends FragmentStatePagerAdapter
{
    private final String[] TITLES = {"NEWS", "LATEST", "MATCHES"};
    public MainPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int i)
    {
        switch(i)
        {
            case 0:
                return new NewsFragment();

            case 1:
                return new LatestDemoFragment();

            case 2:
                return new RecentMatchFragment();

            default:
                return null;
        }
    }

    @Override
    public int getCount()
    {
        return TITLES.length;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return TITLES[position];
    }
}
