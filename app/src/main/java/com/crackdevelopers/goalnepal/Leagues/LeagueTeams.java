package com.crackdevelopers.goalnepal.Leagues;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
 * Created by trees on 8/24/15.
 * kjlsjdlfsd
 * laskdf
 * klsdsd
 * kads
 * adssd
 *
 */
public class LeagueTeams extends Fragment
{
    private static final String URL="http://www.goalnepal.com/json_participatingTeams.php?tournament_id=";
    private long TOURNAMENT_ID;
    private static final String PARTICIPATING_TEAMS ="participating_teams";
    private static final String TEAM_ID="clubId";
    private static final String TEAM_ICON="club_logo";
    private static final String TEAM_NAME="club_name";
    private static final String TEAM_SHORT_NAME="short_name";
    private LeagueTeamsAdapter mAdapter;
    private RequestQueue queue;

    private CircularProgressView progressView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        TOURNAMENT_ID=LeagueActivity.TOURNAMENT_ID;
        queue= VolleySingleton.getInstance().getQueue();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.league_teams_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) 
    {
        super.onActivityCreated(savedInstanceState);

        mAdapter=new LeagueTeamsAdapter();
        RecyclerView teamList=(RecyclerView)getActivity().findViewById(R.id.team_list);
        teamList.setLayoutManager(new LinearLayoutManager(getActivity()));
        teamList.setAdapter(mAdapter);

        ///###################### PROGRESS BAR
        progressView = (CircularProgressView)getActivity().findViewById(R.id.progress_view_teams);
        progressView.startAnimation();
    }


    @Override
    public void onStart()
    {
        super.onPause();
        sendTeamsRequest();

    }

    @Override
    public void onStop()
    {
        super.onResume();
        queue.cancelAll(this);

    }

    private void sendTeamsRequest()
    {
        progressView.setVisibility(View.VISIBLE);
        CacheRequest teamsRequest=new CacheRequest(Request.Method.GET, URL+TOURNAMENT_ID,

                new Response.Listener<NetworkResponse>()
                {
                    @Override
                    public void onResponse(NetworkResponse response)
                    {
                        try
                        {
                            final String jsonString=new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            JSONObject jsonObject=new JSONObject(jsonString);
                            List<ParticipatingTeamsRow> teams=parseResponse(jsonObject);

                            mAdapter.updateData(teams);
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

        teamsRequest.setTag(this);
        queue.add(teamsRequest);
    }


    private List<ParticipatingTeamsRow> parseResponse(JSONObject rootJson)
    {
        List<ParticipatingTeamsRow> tempGoals=new ArrayList<>();

        if(rootJson!=null)
        {
            if(rootJson.length()!=0)
            {
                if(rootJson.has(PARTICIPATING_TEAMS))
                {
                    try
                    {
                        JSONArray teams=rootJson.getJSONArray(PARTICIPATING_TEAMS);

                        for(int i=0;i<teams.length();i++)
                        {
                            JSONObject team=teams.getJSONObject(i);

                            String team_name="", team_short_name="", icon_url=""; long team_id=0;

                            if(team.has(TEAM_ICON)) icon_url=team.getString(TEAM_ICON);
                            if(team.has(TEAM_NAME)) team_name=team.getString(TEAM_NAME);
                            if(team.has(TEAM_SHORT_NAME)) team_short_name=team.getString(TEAM_SHORT_NAME);
                            if(team.has(TEAM_ID)) team_id=team.getLong(TEAM_ID);


                            tempGoals.add(new ParticipatingTeamsRow(icon_url, team_name, team_short_name, team_id));
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
