package com.crackdevelopers.goalnepal.Leagues;

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
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by trees on 8/24/15.
 */
public class LeagueFixtures extends Fragment
{
    private static final String URL="http://www.goalnepal.com/json_fixtures_2015.php?tournament_id=";
    private long TOURNAMENT_ID;
    private static final String CLUB_A_NAME="clubA";
    private static final String CLUB_B_NAME="clubB";
    private static final String CLUB_A_SCORE="score_of_clubA";
    private static final String CLUB_B_SCORE="score_of_clubB";
    private static final String CLUB_A_ICON="clubA_icon";
    private static final String CLUB_B_ICON="clubB_icon";
    private static final String MATCH_DATE="match_date";
    private static final String MATCH_TIME="match_time";
    private static final String MATCH_STATUS="match_status";
    private static final String MATCHES="matches";
    private static final String TITLE="title";
    private static final String MATCH_ID="id";
    private static final String FIXTURES="fixtures";

    private RecyclerView leagueFixtures;
    private Context context;
    private LeagueFixtureAdapter leagueFixtureAdapter;
    private RequestQueue queue;


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
        return inflater.inflate(R.layout.league_fixture_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        context=getActivity();

        leagueFixtures=(RecyclerView)getActivity().findViewById(R.id.fixture_table);
        leagueFixtureAdapter=new LeagueFixtureAdapter(context);
        leagueFixtures.setAdapter(leagueFixtureAdapter);
        leagueFixtures.setLayoutManager(new LinearLayoutManager(context));
        final StickyRecyclerHeadersDecoration decoration=new StickyRecyclerHeadersDecoration(leagueFixtureAdapter);
        leagueFixtures.addItemDecoration(decoration);


        ///STICKY ADAPTER THAT MANAGES THE STICKY
        leagueFixtureAdapter.registerAdapterDataObserver
                (
                        new RecyclerView.AdapterDataObserver() {
                            @Override
                            public void onChanged() {
                                decoration.invalidateHeaders();
                            }
                        }
                );


        sendFixtureReuest();

    }
    private void sendFixtureReuest()
    {
        CacheRequest fixtureRequest=new CacheRequest(Request.Method.GET, URL+TOURNAMENT_ID,

                new Response.Listener<NetworkResponse>()
                {
                    @Override
                    public void onResponse(NetworkResponse response)
                    {
                        try
                        {
                            final String jsonResponseString=new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            JSONObject responseJson=new JSONObject(jsonResponseString);
                            leagueFixtureAdapter.setData(parseData(responseJson));
                        }
                        catch (UnsupportedEncodingException e)
                        {
                            e.printStackTrace();
                        } catch (JSONException e)
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


        fixtureRequest.setTag(this);
        queue.add(fixtureRequest);
    }


    @Override
    public void onStart()
    {
        super.onPause();
        sendFixtureReuest();

    }

    @Override
    public void onStop()
    {
        super.onResume();
        queue.cancelAll(this);

    }


    private List<FixtureRow> parseData(JSONObject rootJson)
    {
        List<FixtureRow> tempList=new ArrayList<>();

        if(rootJson!=null)
        {
            if(rootJson.length()!=0)
            {
                if(rootJson.has(FIXTURES))
                {
                    try
                    {
                        JSONArray fixtures=rootJson.getJSONArray(FIXTURES);

                        for(int i=0;i<fixtures.length();i++)
                        {
                            JSONObject fixture=fixtures.getJSONObject(i);
                            JSONArray matches=null;
                            String title="";
                            if(fixture.has(TITLE)) title=fixture.getString(TITLE);
                            if(fixture.has(MATCHES))
                            {
                                matches=fixture.getJSONArray(MATCHES);

                                for(int j=0;j<matches.length();j++)
                                {
                                    JSONObject match=matches.getJSONObject(j);

                                    String club_a_name="", club_b_name="", club_a_score="-", club_b_score="-", club_a_icon="", club_b_icon="",
                                            match_date="", match_time="", match_status=""; long match_id=-1;

                                    if(match.has(CLUB_A_NAME)) club_a_name=match.getString(CLUB_A_NAME);
                                    if(match.has(CLUB_B_NAME)) club_b_name=match.getString(CLUB_B_NAME);
                                    if(match.has(CLUB_A_ICON)) club_a_icon=match.getString(CLUB_A_ICON);
                                    if(match.has(CLUB_B_ICON)) club_b_icon=match.getString(CLUB_B_ICON);
                                    if(match.has(MATCH_DATE)) match_date=match.getString(MATCH_DATE);
                                    if(match.has(MATCH_TIME)) match_time=match.getString(MATCH_TIME);
                                    if(match.has(MATCH_STATUS)) match_status=match.getString(MATCH_STATUS);
                                    if(match.has(MATCH_ID))     match_id=match.getLong(MATCH_ID);
                                    if(match_status.equals("Played"))
                                    {
                                        if(match.has(CLUB_A_SCORE)) club_a_score=match.getString(CLUB_A_SCORE);
                                        if(match.has(CLUB_B_SCORE)) club_b_score=match.getString(CLUB_B_SCORE);
                                    }

                                    tempList.add(new FixtureRow(club_a_name, club_b_name, club_a_score, club_b_score,
                                            club_a_icon, club_b_icon, match_date, match_time, title, match_id));

                                }
                            }

                        }


                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }

        return  tempList;
    }


}
