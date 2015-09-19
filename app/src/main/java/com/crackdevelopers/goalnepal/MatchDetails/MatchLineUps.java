package com.crackdevelopers.goalnepal.MatchDetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.crackdevelopers.goalnepal.R;
import com.crackdevelopers.goalnepal.Volley.VolleySingleton;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

/**
 * Created by trees on 8/25/15.
 */


public class MatchLineUps extends Fragment
{
    private static final String URL="http:goalnepal.com/json_lineups.php?match_id=";
    private long MATCH_ID;

    private RequestQueue queue;

    private CircularProgressView progressView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MATCH_ID = MatchActivity.MATCH_ID;
        queue = VolleySingleton.getInstance().getQueue();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v=inflater.inflate(R.layout.match_lineups_fragment, container, false);
        return v;
    }
}
