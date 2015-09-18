package com.crackdevelopers.goalnepal.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.crackdevelopers.goalnepal.Latest.LatestFragment;
import com.crackdevelopers.goalnepal.News.NewsFragment;
import com.crackdevelopers.goalnepal.News.NewsSimpleFragment;
import com.crackdevelopers.goalnepal.RecentMatch.RecentMatchFragment;

public class MainPagerAdapter extends FragmentStatePagerAdapter
{
    private final String[] TITLES = {"LATEST","NEWS","MATCHES"};
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
                return new LatestFragment();


            case 1:
                return new NewsSimpleFragment();

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
