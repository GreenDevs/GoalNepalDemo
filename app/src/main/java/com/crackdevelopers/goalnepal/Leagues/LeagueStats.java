package com.crackdevelopers.goalnepal.Leagues;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crackdevelopers.goalnepal.R;

/**
 * Created by trees on 8/24/15.
 */
public class LeagueStats extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.league_stats_fragment, container, false);
        return view;
    }
}