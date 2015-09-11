package com.crackdevelopers.goalnepal.MatchDetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crackdevelopers.goalnepal.R;

/**
 * Created by trees on 8/25/15.
 */
public class MatchLineUps extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v=inflater.inflate(R.layout.match_lineups_fragment, container, false);
        return v;
    }
}
