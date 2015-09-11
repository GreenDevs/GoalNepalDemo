package com.crackdevelopers.goalnepal.Leagues;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class LeaguePagerAdapter extends FragmentStatePagerAdapter {

    private final String[] TITLES = {"Match Day", "TABLE", "TEAMS", "NEWS"};

    public LeaguePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i)
    {
        switch (i)
        {
            case 0:
                return new LeagueFixtures();

            case 1:
                return new LeagueTable();

            case 2:
                return new LeagueTeams();

//            case 3:
//                return new LeagueStats();

            case 3:
                return new LeagueNews();

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