package com.crackdevelopers.goalnepal.Latest;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.toolbox.HttpHeaderParser;
import com.crackdevelopers.goalnepal.R;
import com.crackdevelopers.goalnepal.Volley.CacheRequest;
import com.yalantis.phoenix.PullToRefreshView;
import android.widget.ImageView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.crackdevelopers.goalnepal.Volley.VolleySingleton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by trees on 8/21/15.
 */
public class LatestFragment extends Fragment
{

    /*
    VARIABLE INTRODUCTION

    *)latestNews        -> this variable is a recycler view to keep the news of this fragment

    *)context           -> It keeps the context of the Activity

    *)inflater          -> This inflater is required to inflate the parralex header

    *)LATEST_NEWS_URL               -> url has the link without the changing value

    *)PAGE_NO           -> PAGE_NO is used to navigate through different news pages in that api..  eg PAGE_NO=1 will give the news from the fist page..

    *)requestQueue      -> this is a queue of request.. where any kinds of request are queue.. eg StringRequest, JsonObjectRequest

    *)JSON_REQUEST_TAG  -> This is a tag for the json object request.. it is useful when request is to be cancaled or abort in runtime..

    *) NEWS_SUB_TITLES, NEWS_TITLE, NEWS_IMAGE_ID, NEWS all are the just the json keys to parse Json Data

    *
     *
     */
    private static final String LATEST_NEWS_URL="http://www.goalnepal.com/json_news_2015.php?page=";
    private static final String IMAGE_PATH_THUMNAIL="http://www.goalnepal.com/graphics/article/thumb/";

    private static final String NEWS_DATE="pubdate";
    private static final String NEWS_SUB_TITLE="sub_heading";
    private static final String NEWS_TITLE="title";
    private static final String NEWS_IMAGE_ID="inner_image";
    private static final String NEWS="news";
    private static final String NEWS_DESCRIPTIOIN="description";
    private static int PAGE_N0=1;


    private RecyclerView latestNews;
    private PullToRefreshView mPullToRefreshView;
    private final int  REFRESH_DELAY = 1500;
    private Context context;
    private LayoutInflater inflater;
    private RequestQueue requestQueue;
    private ArrayList<NewsSingleRow> newsData;
    private LatestNewsAdapter latestNewsAdapter;
    private boolean loading = true;



    public LatestFragment() 
    {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        VolleySingleton singleton=VolleySingleton.getInstance();
        requestQueue=singleton.getQueue();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        this.inflater=inflater;
        View v=inflater.inflate(R.layout.latest_fragment, container, false);
        latestNews=(RecyclerView)v.findViewById(R.id.latestNewsList);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        this.context=getActivity();


        newsData=new ArrayList<>();
        final LinearLayoutManager manager=new LinearLayoutManager(context);
        latestNews.setLayoutManager(manager);
        latestNewsAdapter = new LatestNewsAdapter(newsData,context);
        latestNews.setAdapter(latestNewsAdapter);


        /////////############################## RECYCLER VIEW LISTENER FROM MORE SCROLL########################################


        latestNews.addOnScrollListener(

                new RecyclerView.OnScrollListener()
                {

                    private int pastVisiblesItems, visibleItemsCount, totalItemsCount;

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                        visibleItemsCount = latestNews.getChildCount();
                        totalItemsCount = manager.getItemCount();
                        pastVisiblesItems = manager.findFirstVisibleItemPosition();


                        if (loading && ((pastVisiblesItems + visibleItemsCount) >= totalItemsCount))
                        {
                            Log.i("last", " LAST");
                            loading=false;
                            sendNewsScrollRequest();
                        }


                    }
                });

        /////################################# PULLL TO REFRESH ########################################
        mPullToRefreshView = (PullToRefreshView) getActivity().findViewById(R.id.pull_to_refresh_latest);
        mPullToRefreshView.setOnRefreshListener(

                new PullToRefreshView.OnRefreshListener()
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
        super.onPause();
        sendNewsRequest();

    }

    @Override
    public void onStop()
    {
        super.onResume();
        requestQueue.cancelAll(this);

    }

    private void sendNewsRequest()
    {
        CacheRequest newsRequest=new CacheRequest(Request.Method.GET, LATEST_NEWS_URL+PAGE_N0,

                new Response.Listener<NetworkResponse>()
                {
                    @Override
                    public void onResponse(NetworkResponse response)
                    {
                        try
                        {
                            final String jsonResponseString=new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            JSONObject responseJson=new JSONObject(jsonResponseString);
                            latestNewsAdapter.setData(parseNews(responseJson));
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


        newsRequest.setTag(this);
        requestQueue.add(newsRequest);
    }


    ////THIS TRIES TO UPDATE THE NEWS AFTER LIST SCROLLING FINISHED

    private void sendNewsScrollRequest()
    {
        PAGE_N0++;

        CacheRequest newsScrollRequest=new CacheRequest(Request.Method.GET, LATEST_NEWS_URL+PAGE_N0,

                new Response.Listener<NetworkResponse>()
                {
                    @Override
                    public void onResponse(NetworkResponse response)
                    {
                        try
                        {
                            final String jsonResponseString=new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            JSONObject responseJson=new JSONObject(jsonResponseString);
                            latestNewsAdapter.setScrollUpdate(parseNews(responseJson));
                            loading=true;
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


        newsScrollRequest.setTag(this);
        requestQueue.add(newsScrollRequest);



    }



    private ArrayList<NewsSingleRow> parseNews(JSONObject rootObj)
    {
           ArrayList<NewsSingleRow> tempList=new ArrayList<>();
            if(rootObj==null || rootObj.length()==0)
            {
                Toast.makeText(context, "API LENGTH IS 0", Toast.LENGTH_LONG).show();
            }
            else
            {

                    try
                    {
                        if(rootObj.has(NEWS))
                        {
                            JSONArray newsArray = rootObj.getJSONArray(NEWS);

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
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

            }

        return tempList;
    }


}
