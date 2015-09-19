package com.crackdevelopers.goalnepal.MatchDetails;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.crackdevelopers.goalnepal.R;
import com.crackdevelopers.goalnepal.Volley.CacheRequest;
import com.crackdevelopers.goalnepal.Volley.VolleySingleton;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by trees on 8/25/15.
 */
public class MatchStats extends Fragment
{
    private static final String GOALS="goals";
    private static final String MINUTE="minute";
    private static final String PLAYER_NAME="playerName";
    private static final String TEAM="team_AorB";
    private static final String URL="http://www.goalnepal.com/json_goals.php?match_id=";
    private long MATCH_ID;
    private MatchStatsAdapter mAdapter;
    private RequestQueue queue;

    private CircularProgressView progressView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        MATCH_ID=MatchActivity.MATCH_ID;
        queue= VolleySingleton.getInstance().getQueue();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return  inflater.inflate(R.layout.match_stats_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        mAdapter=new MatchStatsAdapter();
        RecyclerView statsList=(RecyclerView)getActivity().findViewById(R.id.score_holder_recycler);
        statsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        statsList.setAdapter(mAdapter);

        ///###################### PROGRESS BAR
        progressView = (CircularProgressView)getActivity().findViewById(R.id.progress_view_stats);
        progressView.startAnimation();
    }


    @Override
    public void onStart()
    {
        super.onPause();
        sendScoreRequest();

    }

    @Override
    public void onStop()
    {
        super.onResume();
        queue.cancelAll(this);

    }
    private void sendScoreRequest()
    {
        progressView.setVisibility(View.VISIBLE);
        CacheRequest goalsRequest=new CacheRequest(Request.Method.GET, URL+MATCH_ID,

                new Response.Listener<NetworkResponse>()
                {
                    @Override
                    public void onResponse(NetworkResponse response)
                    {
                        try
                        {
                            final String jsonString=new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            JSONObject jsonObject=new JSONObject(jsonString);
                            List<GoalScoredRow> goals=parseResponse(jsonObject);
                            mAdapter.updateData(goals);
                            progressView.setVisibility(View.GONE);
                        }
                        catch (UnsupportedEncodingException | JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }

                }
                ,
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                    }
                });

        goalsRequest.setTag(this);
        queue.add(goalsRequest);
    }


    private List<GoalScoredRow> parseResponse(JSONObject rootJson)
    {
        List<GoalScoredRow> tempGoals=new ArrayList<>();

        if(rootJson!=null)
        {
            if(rootJson.length()!=0)
            {
                if(rootJson.has(GOALS))
                {
                    try
                    {
                        JSONArray goals=rootJson.getJSONArray(GOALS);

                        for(int i=0;i<goals.length();i++)
                        {

                            JSONObject goal=goals.getJSONObject(i);

                            String playerName="", teamGroup="", minute="";
                            if(goal.has(PLAYER_NAME)) playerName=goal.getString(PLAYER_NAME);
                            if(goal.has(TEAM))  teamGroup=goal.getString(TEAM);
                            if(goal.has(MINUTE)) minute=goal.getString(MINUTE)+"'";

                            if(teamGroup.equals("A")) {teamGroup= MatchActivity.TEAM_A;}
                            if(teamGroup.equals("B")) {teamGroup= MatchActivity.TEAM_B;}

                            tempGoals.add(new GoalScoredRow(playerName, minute, teamGroup));
                        }
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }

        return tempGoals;
    }




}
