package com.crackdevelopers.goalnepal.RecentMatch;

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
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.yalantis.phoenix.PullToRefreshView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class RecentMatchFragment extends Fragment
{
    ///KEYS OF THE JSON DATA
    private static final String URL="http://www.goalnepal.com/json_recent_match_2015.php";
    private static final String MATCH_ARRAY="matches";
    private static final String MATCH_ID="id";
    private static final String CLUB_NAME_A="clubA_short_name";
    private static final String CLUB_NAME_B="clubB_short_name";
    private static final String CLUB_A_ICON="clubA_icon";
    private static final String CLUB_B_ICON="clubB_icon";
    private static final String MATCH_DATE="match_date";
    private static final String MATCH_TIME="match_time";
    private static final String MATCH_STATUS="match_status";
    private static final String MATCH_VENUE="match_venue";
    private static final String CLUB_A_GOAL="score_of_clubA";
    private static final String CLUB_B_GOAL="score_of_clubB";

    private Context contex;
    RecentMatchAdapter mAdapter;
    private RequestQueue queue;
    private CircularProgressView progressView;
    private PullToRefreshView mPullToRefreshView;
    private final int  REFRESH_DELAY = 1500;

    public RecentMatchFragment()
    {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        queue=VolleySingleton.getInstance().getQueue();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.recent_matches_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        contex=getActivity();

        RecyclerView scoreList;
        mAdapter=new RecentMatchAdapter(getActivity());
        final StickyRecyclerHeadersDecoration decoration=new StickyRecyclerHeadersDecoration(mAdapter);
        scoreList=(RecyclerView)getActivity().findViewById(R.id.list_of_score);
        scoreList.setAdapter(mAdapter);
        scoreList.setLayoutManager(new LinearLayoutManager(getActivity()));
        scoreList.addItemDecoration(decoration);

        progressView = (CircularProgressView)getActivity().findViewById(R.id.progress_view);
        progressView.startAnimation();

        ///STICKY ADAPTER THAT MANAGES THE STICKY
        mAdapter.registerAdapterDataObserver
                (
                        new RecyclerView.AdapterDataObserver() {
                            @Override
                            public void onChanged() {
                                decoration.invalidateHeaders();
                            }
                        }
                );


        /////################################# PULLL TO REFRESH ########################################
        mPullToRefreshView = (PullToRefreshView) getActivity().findViewById(R.id.pull_to_refresh_recent);
        mPullToRefreshView.setOnRefreshListener(

                new PullToRefreshView.OnRefreshListener() {
                    @Override
                    public void onRefresh()
                    {
                        // do what you want to do when refreshing
                        sendRecentMatchRequest();
                        mPullToRefreshView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mPullToRefreshView.setRefreshing(false);
                            }
                        }, REFRESH_DELAY);
                    }
                }
        );

    }

    @Override
    public void onStop()
    {
        super.onStop();
        queue.cancelAll(this);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        sendRecentMatchRequest();

    }

    ///THIS METHOD TRIES TO PULL THE DATA FROM RECENT MATCH API AND CALLS THE ADAPTER TO SET THE RECYCLER
    private void sendRecentMatchRequest()
    {
        progressView.setVisibility(View.VISIBLE);
        CacheRequest recentMatchCacheRequest=new CacheRequest(Request.Method.GET, URL,

        new Response.Listener<NetworkResponse>()
        {
            @Override
            public void onResponse(NetworkResponse response)
            {
                try
                {
                    final String jsonResponseString=new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    JSONObject responseJson=new JSONObject(jsonResponseString);
                    mAdapter.setData(parseData(responseJson));
                    progressView.setVisibility(View.GONE);
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

        recentMatchCacheRequest.setTag(this);

        queue.add(recentMatchCacheRequest);
    }

    private List<MatchItem> parseData(JSONObject rootJson)
    {

        List<MatchItem> temMatchList=new ArrayList<>();
        if(rootJson!=null)
        {
            if(rootJson.length()!=0)
            {
                if(rootJson.has(MATCH_ARRAY))
                {
                    try
                    {
                        JSONArray matches=rootJson.getJSONArray(MATCH_ARRAY);
                        for(int i=0;i<matches.length();i++)
                        {
                            JSONObject match=matches.getJSONObject(i);
                            String iconA="", iconB="", nameA="", nameB="", match_time="",match_venue="", scoreA="-", scoreB="-", match_status="";
                            String date="";
                            long match_id=-1;
                            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                            if(match.has(MATCH_DATE)) date=match.getString(MATCH_DATE);

                            if(match.has(CLUB_A_ICON)) iconA=match.getString(CLUB_A_ICON);
                            if(match.has(CLUB_B_ICON)) iconB=match.getString(CLUB_B_ICON);

                            if(match.has(CLUB_NAME_A)) nameA=match.getString(CLUB_NAME_A);
                            if(match.has(CLUB_NAME_B)) nameB=match.getString(CLUB_NAME_B);

                            if(match.has(MATCH_TIME)) match_time=match.getString(MATCH_TIME);
                            if(match.has(MATCH_VENUE)) match_venue=match.getString(MATCH_VENUE);
                            if(match.has(MATCH_ID)) match_id=match.getLong(MATCH_ID);
                            if(match.has(MATCH_STATUS))  match_status=match.getString(MATCH_STATUS);

                            if(!match_status.equals("YTP"))
                            {
                                if(match.has(CLUB_A_GOAL)) scoreA=match.getString(CLUB_A_GOAL);
                                if(match.has(CLUB_B_GOAL)) scoreB=match.getString(CLUB_B_GOAL);
                            }

                            temMatchList.add(new MatchItem(iconA, iconB, nameA, nameB, match_time, match_venue, scoreA, scoreB,
                                    match_status,date, match_id));

                        }
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                }
            }
        }

        return  temMatchList;
    }
}
