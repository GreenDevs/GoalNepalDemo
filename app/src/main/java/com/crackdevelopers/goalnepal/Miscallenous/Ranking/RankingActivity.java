package com.crackdevelopers.goalnepal.Miscallenous.Ranking;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
import java.util.Calendar;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

public class RankingActivity extends AppCompatActivity
{

    private static final String URL =  "http://www.goalnepal.com/json_fifa_ranking_2015.php?year=";

   private static final String RANK_ID="rank_id";
    private static final String RANK_MONTH="rank_month";
    private static final String RANK="rank";
    private static final String RANK_YEAR="rank_year";
    private static final String NEWS="news";
    private Context mContext;
    private RecyclerView mRecyclerView;
    private  FIFARankAdapter rankAdapter;
    private static final int CURRENT_YEAR=Calendar.getInstance().get(Calendar.YEAR);
    private int YEAR_COUNT=CURRENT_YEAR;
    private LinearLayoutManager manager;
    private boolean loading=true;
    private CircularProgressView progressView;
    private WaveSwipeRefreshLayout mPullToRefreshView;
    private final int  REFRESH_DELAY = 1500;

    RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        mQueue=VolleySingleton.getInstance().getQueue();

        initInstances();

        ///###################### PROGRESS BAR
        progressView = (CircularProgressView)findViewById(R.id.progress_view_ranking);
        progressView.startAnimation();

        /////################################# PULLL TO REFRESH ########################################
        mPullToRefreshView = (WaveSwipeRefreshLayout)findViewById(R.id.pull_to_refresh_ranking);
        mPullToRefreshView.setWaveColor(Color.parseColor("#c62828"));
        mPullToRefreshView.setColorSchemeColors(Color.WHITE);
        mPullToRefreshView.setOnRefreshListener(

                new WaveSwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        // do what you want to do when refreshing


                        VolleySingleton.getInstance().getQueue().cancelAll(this);
                        sendRankRequest();
                        mPullToRefreshView.postDelayed
                                (

                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                mPullToRefreshView.setRefreshing(false);
                                            }
                                        }, REFRESH_DELAY);
                    }
                }
        );

        sendRankRequest();


        /////////############################## RECYCLER VIEW LISTENER FROM MORE SCROLL########################################


        mRecyclerView.addOnScrollListener(

                new RecyclerView.OnScrollListener() {

                    private int pastVisiblesItems, visibleItemsCount, totalItemsCount;

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                        visibleItemsCount = manager.getChildCount();
                        totalItemsCount = manager.getItemCount();
                        pastVisiblesItems = manager.findFirstVisibleItemPosition();


                        if (loading && ((pastVisiblesItems + visibleItemsCount) >= totalItemsCount)) {
                            Log.i("last", " LAST");
                            loading = false;
                            sendScrollRequest();
                        }


                    }
                });
    }

    private void initInstances()
    {
        mContext=this;
        Toolbar toolbar=(Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView= (RecyclerView) findViewById(R.id.fifaRanking_recycler);
        manager=new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(manager);
//        Toast.makeText(mContext,"array length="+rankingSingleRowList.size(),Toast.LENGTH_SHORT).show();

    }

    private void sendRankRequest()
    {
        progressView.setVisibility(View.VISIBLE);
        YEAR_COUNT=CURRENT_YEAR;
        CacheRequest cacheRequest=new CacheRequest(Request.Method.GET, URL+YEAR_COUNT, new Response.Listener<NetworkResponse>()
        {
            @Override
            public void onResponse(NetworkResponse response)
            {
                try
                {
                    ArrayList<RankingSingleRow>  rankingSingleRowList=new ArrayList<>();
                    String jsonStringResponse=new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    JSONObject responseJson=new JSONObject(jsonStringResponse);
                    ArrayList<RankingSingleRow>tmp=parseRank(responseJson);
                    for(RankingSingleRow singleRow: tmp)
                    {
                        rankingSingleRowList.add(singleRow);
                    }

                    rankAdapter=new FIFARankAdapter(rankingSingleRowList,mContext);
                    mRecyclerView.setAdapter(rankAdapter);
                    progressView.setVisibility(View.GONE);
                    while(YEAR_COUNT>=CURRENT_YEAR-2)
                    {
                        sendScrollRequest();
                    }
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
                progressView.setVisibility(View.GONE);
            }
        });

        cacheRequest.setTag(mContext);
        mQueue.add(cacheRequest);
    }



    private void sendScrollRequest()
    {
        progressView.setVisibility(View.VISIBLE);
        YEAR_COUNT--;
        CacheRequest cacheRequest=new CacheRequest(Request.Method.GET, URL+YEAR_COUNT, new Response.Listener<NetworkResponse>()
        {
            @Override
            public void onResponse(NetworkResponse response)
            {
                try
                {
                    ArrayList<RankingSingleRow>  rankingSingleRowList=new ArrayList<>();
                    String jsonStringResponse=new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    JSONObject responseJson=new JSONObject(jsonStringResponse);
                    ArrayList<RankingSingleRow>tmp=parseRank(responseJson);
                    for(RankingSingleRow singleRow: tmp)
                    {
                        rankingSingleRowList.add(singleRow);
                    }
                    rankAdapter.scrollList(rankingSingleRowList);
                    loading=true;
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
                        progressView.setVisibility(View.GONE);
                    }
                });

        cacheRequest.setTag(mContext);
        mQueue.add(cacheRequest);
    }

    private ArrayList<RankingSingleRow> parseRank(JSONObject rootObj)
    {
        ArrayList<RankingSingleRow>tmpList=new ArrayList<>();
        if(!(rootObj==null|| rootObj.length()==0)&& rootObj.has(NEWS))
        {
            try
            {
                JSONArray rankArray=rootObj.getJSONArray(NEWS);
                for(int i=0;i<rankArray.length();i++)
                {
                    JSONObject ranksObject=rankArray.getJSONObject(i);
                    String rankMonth="",rank="",rankYear="";
                    if(ranksObject.has(RANK_MONTH)) rankMonth=ranksObject.getString(RANK_MONTH);
                    if(ranksObject.has(RANK)) rank=ranksObject.getString(RANK);
                    if(ranksObject.has(RANK_YEAR)) rankYear=ranksObject.getString(RANK_YEAR);
                    tmpList.add(new RankingSingleRow(rankMonth,rank,rankYear));
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        return tmpList;
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        mQueue.cancelAll(this);
    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if(id==android.R.id.home)
        {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}

