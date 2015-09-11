package com.crackdevelopers.goalnepal.Leagues;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by trees on 8/24/15.
 */
public class LeagueTable extends Fragment
{

    private static final String URL="http://www.goalnepal.com/json_scoreBoard_2015.php?tournament_id=";
    private final long TOURNAMENT_ID=LeagueActivity.TOURNAMENT_ID;
    private static final String TEAMS="teams";
    private static final String GROUP="pgroup";
    private static final String NAME="short_name";
    private static final String CLUB_ICON="club_logo";
    private static final String MATCH_PLAYED="MP";
    private static final String MATCH_WON="W";
    private static final String MATCH_DRAW="D";
    private static final String MATCH_LOST="L";
    private static final String GOAL_FINISHED="GF";
    private static final String GOAL_ACCEPETED="GA";
    private static final String GOAL_DIFFERENCE="GF_GA";
    private static final String POINTS="PTS";

    private RecyclerView leagueTable;
    private Context context;
    private LeagueTableAdapter leagueTableAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.league_table_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        context=getActivity();

        leagueTable=(RecyclerView)getActivity().findViewById(R.id.league_table);
        leagueTableAdapter=new LeagueTableAdapter(context);
        leagueTable.setAdapter(leagueTableAdapter);
        leagueTable.setLayoutManager(new LinearLayoutManager(context));
        final StickyRecyclerHeadersDecoration decoration=new StickyRecyclerHeadersDecoration(leagueTableAdapter);
        leagueTable.addItemDecoration(decoration);


        ///STICKY ADAPTER THAT MANAGES THE STICKY
        leagueTableAdapter.registerAdapterDataObserver
                (
                        new RecyclerView.AdapterDataObserver()
                        {
                            @Override
                            public void onChanged()
                            {
                                decoration.invalidateHeaders();
                            }
                        }
                );


        sendTableReuest();
    }


    private void sendTableReuest()
    {
        CacheRequest recentMatchCacheRequest=new CacheRequest(Request.Method.GET, URL+TOURNAMENT_ID,

                new Response.Listener<NetworkResponse>()
                {
                    @Override
                    public void onResponse(NetworkResponse response)
                    {
                        try
                        {
                            final String jsonResponseString=new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            JSONObject responseJson=new JSONObject(jsonResponseString);
                            leagueTableAdapter.setData(parseData(responseJson));
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

        RequestQueue queue= VolleySingleton.getInstance().getQueue();
        queue.add(recentMatchCacheRequest);
    }

    private List<LeagueTableItem> parseData(JSONObject rootJson)
    {
        List<LeagueTableItem> tempList=new ArrayList<>();
        if(rootJson!=null)
        {
            if(rootJson.length()!=0)
            {
                if(rootJson.has(TEAMS))
                {
                    try
                    {
                        List<LeagueTableItem> rawList=new ArrayList<>();
                        JSONArray teams=rootJson.getJSONArray(TEAMS);
                        Set<String> groups=new TreeSet<>();
                        for(int i=0; i<teams.length();i++)
                        {
                            JSONObject team=teams.getJSONObject(i);
                            String pGroup="", name="", club_icon_url="", match_played="", match_won="",
                                    match_draw="", match_lost="", goal_finished="", goal_accepted="", goal_diff="", points="";

                            if(team.has(GROUP)) pGroup=team.getString(GROUP);
                            if(team.has(NAME))  name=team.getString(NAME);
                            if(team.has(CLUB_ICON)) club_icon_url=team.getString(CLUB_ICON);
                            if(team.has(MATCH_PLAYED)) match_played=team.getString(MATCH_PLAYED);
                            if(team.has(MATCH_WON)) match_won=team.getString(MATCH_WON);
                            if(team.has(MATCH_DRAW)) match_draw=team.getString(MATCH_DRAW);
                            if(team.has(MATCH_LOST)) match_lost=team.getString(MATCH_LOST);
                            if(team.has(GOAL_FINISHED)) goal_finished=team.getString(GOAL_FINISHED);
                            if(team.has(GOAL_ACCEPETED)) goal_accepted=team.getString(GOAL_ACCEPETED);
                            if(team.has(GOAL_DIFFERENCE)) goal_diff=team.getString(GOAL_DIFFERENCE);
                            if(team.has(POINTS)) points=team.getString(POINTS);

                            groups.add(pGroup);
                            rawList.add(new LeagueTableItem(pGroup, name, club_icon_url, match_played, match_won, match_draw, match_lost
                            , goal_finished, goal_accepted, goal_diff, points));
                        }


                        for(String group:groups)
                        {
                            int i=0;
                            for(LeagueTableItem item:rawList)
                            {
                                if(item.pGroup.equals(group))
                                tempList.add(item);
                                i++;
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

        return tempList;
    }
}
