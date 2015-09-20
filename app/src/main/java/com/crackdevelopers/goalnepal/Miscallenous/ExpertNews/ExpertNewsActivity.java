package com.crackdevelopers.goalnepal.Miscallenous.ExpertNews;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
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

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

/**
 * Created by trees on 9/18/15.
 */
public class ExpertNewsActivity extends AppCompatActivity
{

    /*
  VARIABLE INTRODUCTION

  *)latestNews        -> this variable is a recycler view to keep the news of this fragment

  *)context           -> It keeps the context of the Activity

  *)inflater          -> This inflater is required to inflate the parralex header

  *)NEWS_URL               -> url has the link without the changing value

  *)PAGE_NO           -> PAGE_NO is used to navigate through different news pages in that api..  eg PAGE_NO=1 will give the news from the fist page..

  *)requestQueue      -> this is a queue of request.. where any kinds of request are queue.. eg StringRequest, JsonObjectRequest

  *)JSON_REQUEST_TAG  -> This is a tag for the json object request.. it is useful when request is to be cancaled or abort in runtime..

  *) NEWS_SUB_TITLES, NEWS_TITLE, NEWS_IMAGE_ID, NEWS all are the just the json keys to parse Json Data

  *
   *
   */
    private static final String NEWS_URL="http://www.goalnepal.com/json_news_2015.php?tournament_id=";
    private static final String IMAGE_PATH_THUMNAIL="http://www.goalnepal.com/graphics/article/thumb/";

    private static final String PAGE_FLAG="&page=";
    private static final String NEWS_DATE="pubdate";
    private static final String NEWS_SUB_TITLE="sub_heading";
    private static final String NEWS_TITLE="title";
    private static final String NEWS_IMAGE_ID="inner_image";
    private static final String NEWS="news";
    private static final String NEWS_DESCRIPTIOIN="description";
    private static final long TOURNAMENT_ID=137;
    private static int PAGE_N0=1;


    private RecyclerView latestNews;
    private WaveSwipeRefreshLayout mPullToRefreshView;
    private final int  REFRESH_DELAY = 1500;
    private Context context;
    private RequestQueue requestQueue;
    private ExpertNewsAdapter expertNewsAdapter;
    private boolean loading = true;
    private CircularProgressView progressView;
    

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert);

        VolleySingleton singleton=VolleySingleton.getInstance();
        requestQueue=singleton.getQueue();
        context=this;

        Toolbar toolbar=(Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        latestNews=(RecyclerView)findViewById(R.id.leagueNewsRecyceler);
        final LinearLayoutManager manager=new LinearLayoutManager(context);
        latestNews.setLayoutManager(manager);
        expertNewsAdapter = new ExpertNewsAdapter(context);
        latestNews.setAdapter(expertNewsAdapter);

        ///###################### PROGRESS BAR
        progressView = (CircularProgressView)findViewById(R.id.progress_view_expert);
        progressView.startAnimation();

        sendNewsRequest();

        /////////############################## RECYCLER VIEW LISTENER FROM MORE SCROLL########################################


        latestNews.addOnScrollListener(

                new RecyclerView.OnScrollListener() {

                    private int pastVisiblesItems, visibleItemsCount, totalItemsCount;

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                        visibleItemsCount = latestNews.getChildCount();
                        totalItemsCount = manager.getItemCount();
                        pastVisiblesItems = manager.findFirstVisibleItemPosition();


                        if (loading && ((pastVisiblesItems + visibleItemsCount) >= totalItemsCount)) {
                            Log.i("last", " LAST");
                            loading = false;
                            sendNewsScrollRequest();
                        }


                    }
                });

        /////################################# PULLL TO REFRESH ########################################
        mPullToRefreshView = (WaveSwipeRefreshLayout)findViewById(R.id.pull_to_refresh_expert);
        mPullToRefreshView.setWaveColor(Color.parseColor("#c62828"));
        mPullToRefreshView.setColorSchemeColors(Color.WHITE);

        mPullToRefreshView.setOnRefreshListener(

                new WaveSwipeRefreshLayout.OnRefreshListener()
                {
                    @Override
                    public void onRefresh()
                    {
                        // do what you want to do when refreshing
                        PAGE_N0=1;
                        if(requestQueue!=null) requestQueue.cancelAll(this);
                        sendNewsRequest();
                        mPullToRefreshView.postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                mPullToRefreshView.setRefreshing(false);
                            }
                        }, REFRESH_DELAY);
                    }
                }
        );

    }



    @Override
    public void onStart()
    {
        super.onStart();
//        sendNewsRequest();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        requestQueue.cancelAll(this);
    }

    private void sendNewsRequest()
    {
        progressView.setVisibility(View.VISIBLE);
        CacheRequest newsRequest=new CacheRequest(Request.Method.GET, NEWS_URL+TOURNAMENT_ID+PAGE_FLAG+PAGE_N0,

                new Response.Listener<NetworkResponse>()
                {
                    @Override
                    public void onResponse(NetworkResponse response)
                    {
                        try
                        {
                            final String jsonResponseString=new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            JSONObject responseJson=new JSONObject(jsonResponseString);
                            expertNewsAdapter.setData(parseNews(responseJson));
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
                        Toast.makeText(context, "couldn't load", Toast.LENGTH_SHORT).show();
                    }
                });


        newsRequest.setTag(this);
        requestQueue.add(newsRequest);
    }


    ////THIS TRIES TO UPDATE THE NEWS AFTER LIST SCROLLING FINISHED

    private void sendNewsScrollRequest()
    {
        PAGE_N0++;
        progressView.setVisibility(View.VISIBLE);
        CacheRequest newsScrollRequest=new CacheRequest(Request.Method.GET, NEWS_URL+PAGE_N0,

                new Response.Listener<NetworkResponse>()
                {
                    @Override
                    public void onResponse(NetworkResponse response)
                    {
                        try
                        {
                            final String jsonResponseString=new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            JSONObject responseJson=new JSONObject(jsonResponseString);
                            expertNewsAdapter.setScrollUpdate(parseNews(responseJson));
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
                        Toast.makeText(context, "couldn't load", Toast.LENGTH_SHORT).show();
                    }
                });


        newsScrollRequest.setTag(this);
        requestQueue.add(newsScrollRequest);



    }



    private ArrayList<NewsSingleRow> parseNews(JSONObject rootObj)
    {
        ArrayList<NewsSingleRow> tempList=new ArrayList<>();
        if (rootObj != null)
        {

            try
            {
                if(rootObj.has(NEWS))
                {
                    JSONArray newsArray = rootObj.getJSONArray(NEWS);

                    if(newsArray.length()>0)
                    {
                        for(int i=0;i<newsArray.length();i++)
                        {
                            JSONObject news=newsArray.getJSONObject(i);
                            String imageId="", subtitle="", title="", imageUrl="", description="", date="";

                            if(news.has(NEWS_SUB_TITLE)) subtitle=news.getString(NEWS_SUB_TITLE);
                            if(news.has(NEWS_TITLE))     title=news.getString(NEWS_TITLE);
                            if(news.has(NEWS_IMAGE_ID))      imageId=news.getString(NEWS_IMAGE_ID); imageUrl+=IMAGE_PATH_THUMNAIL+imageId;
                            if(news.has(NEWS_DESCRIPTIOIN)) description=news.getString(NEWS_DESCRIPTIOIN);
                            if(news.has(NEWS_DATE)) date=news.getString(NEWS_DATE);
                            tempList.add(new NewsSingleRow(imageUrl, subtitle, title,description, date, imageId));


//                                Toast.makeText(context, "subtitle:"+subtitle+"\ntitle:"+title+"\nimage_url:"+imageUrl, Toast.LENGTH_LONG).show();
                        }


                    }


                }


            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

        }

        return tempList;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home)
        {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
