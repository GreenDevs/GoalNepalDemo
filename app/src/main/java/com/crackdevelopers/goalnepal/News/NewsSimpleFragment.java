package com.crackdevelopers.goalnepal.News;

import android.content.Context;

import android.content.Intent;
import android.opengl.Visibility;

import android.graphics.Color;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.crackdevelopers.goalnepal.FileManager;
import com.crackdevelopers.goalnepal.Miscallenous.Preferences.PreferenceActivity;
import com.crackdevelopers.goalnepal.Miscallenous.Preferences.PreferenceAdapter;
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

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

/**
 * Created by trees on 9/17/15.
 */
public class NewsSimpleFragment  extends Fragment implements View.OnClickListener
{
    private static final String NEWS_URL="http://www.goalnepal.com/json_news_2015.php?page=";
    private static final String FEATURED_NEWS_URL="http://www.goalnepal.com/json_news_feature_2015.php?page=";
    private static final String IMAGE_PATH="http://www.goalnepal.com/graphics/article/";
    private static final String IMAGE_PATH_THUMNAIL="http://www.goalnepal.com/graphics/article/thumb/";
    private static final String TOURNAMENT_ID="tournament_id";
    private static final String DATA_FORMAT="yyyy-MM-dd HH-mm-ss";
    private static final String NEWS_DATE="pubdate";

    private static final String NEWS_SUB_TITLE="sub_heading";
    private static final String NEWS_TITLE="title";
    private static final String NEWS_IMAGE_ID="inner_image";
    private static final String NEWS="news";
    private static final String NEWS_DESCRIPTIOIN="description";

    private static final String FEATURED_NEWS="news";
    private static final String FEATURED_IMAGE_ID="home_image";
    private static final String FEATURED_SUB_TITLE="top_sub_heading";
    private static final String FEATURED_TITLE="sub_heading";
    private static int PAGE_N0=1;
    private static final String RECYCLER_STATE_KEY="recycler state";
    public static final String PREFERRED_FILE="pref_id.txt";
    private List<NewsSingleRow> totalList;

    private RecyclerView news;
    private RequestQueue requestQueue;
    private WaveSwipeRefreshLayout mPullToRefreshView;
    private final int  REFRESH_DELAY = 1500;
    private Context context;
    private NewsSimpleAdapter newsAdapter;
    private LinearLayoutManager mManager;
    private boolean loading = true;
    private Button loadMore;

    private CircularProgressView progressView;
    private TextView tornmntSelPrompt;

    public NewsSimpleFragment()
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
        View v=inflater.inflate(R.layout.news_fragment, container, false);
        news=(RecyclerView)v.findViewById(R.id.newsList);
        loadMore=(Button)v.findViewById(R.id.load_more);
        tornmntSelPrompt=(TextView)v.findViewById(R.id.tournament_prompt);

        tornmntSelPrompt.setOnClickListener(this);
        loadMore.setOnClickListener(this);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        this.context=getActivity();

        mManager=new LinearLayoutManager(context);
        news.setLayoutManager(mManager);

        ///###################### PROGRESS BAR
        progressView = (CircularProgressView)getActivity().findViewById(R.id.progress_view_latest);
        progressView.startAnimation();

//        sendFeaturedNewsRequest();
        sendNewsRequest();

        /////////############################## RECYCLER VIEW LISTENER FROM MORE SCROLL########################################


        news.addOnScrollListener(

                new RecyclerView.OnScrollListener() {

                    private int pastVisiblesItems, visibleItemsCount, totalItemsCount;

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                        visibleItemsCount = news.getChildCount();
                        totalItemsCount = mManager.getItemCount();
                        pastVisiblesItems = mManager.findFirstVisibleItemPosition();


                        if (loading && ((pastVisiblesItems + visibleItemsCount) >= totalItemsCount)) {
                            loading = false;
                            sendNewsScrollRequest();
                        }


                    }
                });

        /////################################# PULLL TO REFRESH ########################################
        mPullToRefreshView = (WaveSwipeRefreshLayout) getActivity().findViewById(R.id.pull_to_refresh_news);
        mPullToRefreshView.setWaveColor(Color.parseColor("#c62828"));
        mPullToRefreshView.setColorSchemeColors(Color.WHITE);

        mPullToRefreshView.setOnRefreshListener(

                new WaveSwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        // do what you want to do when refreshing

                        if (requestQueue != null) requestQueue.cancelAll(this);
                        sendNewsRequest();
//                        sendFeaturedNewsRequest();
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


    }


    @Override
    public void onStart()
    {
        super.onStart();

        FileManager fm=new FileManager(context, PREFERRED_FILE);
        Log.i("test in start", fm.readFromFile()+"CONTEST");
        if(fm.readFromFile().isEmpty()) { tornmntSelPrompt.setVisibility(View.VISIBLE); loadMore.setVisibility(View.GONE); }
        else
        {
            tornmntSelPrompt.setVisibility(View.GONE);

            if(totalList.size()<6)
            {
                loadMore.setVisibility(View.VISIBLE);
            }

        }

        if(PreferenceAdapter.hasPrefChanged)
        {
            sendNewsRequest();
        }
    }

    @Override
    public void onStop()
    {
        super.onResume();
        requestQueue.cancelAll(this);

    }

    private void sendNewsRequest()
    {

        totalList=new ArrayList<>();
        PAGE_N0=1;
        progressView.setVisibility(View.VISIBLE);
        newsAdapter = new NewsSimpleAdapter(context);
        news.setAdapter(newsAdapter);

        CacheRequest newsRequest=new CacheRequest(Request.Method.GET, NEWS_URL+PAGE_N0,

                new Response.Listener<NetworkResponse>()
                {
                    @Override
                    public void onResponse(NetworkResponse response)
                    {
                        try
                        {
                            final String jsonResponseString=new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            JSONObject responseJson=new JSONObject(jsonResponseString);

                            totalList=parseNews(responseJson);
                            newsAdapter.setData(totalList);
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


        newsRequest.setTag(this);
        requestQueue.add(newsRequest);
    }


    ////THIS TRIES TO UPDATE THE NEWS AFTER LIST SCROLLING FINISHED

    private void sendNewsScrollRequest()
    {
        PAGE_N0++;
        progressView.startAnimation();
        progressView.setVisibility(View.VISIBLE);
        loadMore.setVisibility(View.GONE);
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

                            List<NewsSingleRow> tmp=parseNews(responseJson);
                            for(NewsSingleRow item:tmp)
                            {
                                totalList.add(item);
                            }
                            newsAdapter.setScrollUpdate(tmp);
                            loading=true;
                            progressView.setVisibility(View.GONE);

                            if(totalList.size()<6)  {sendNewsRequest();}
                            else {loadMore.setVisibility(View.GONE);}
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


        newsScrollRequest.setTag(this);
        requestQueue.add(newsScrollRequest);


    }

    //// THIS HEP TO DOWNLOAD THE FEATURED NEWS AND IMAGES
    private void sendFeaturedNewsRequest()
    {
//
//        CacheRequest featuredRequest=new CacheRequest(Request.Method.GET, FEATURED_NEWS_URL+PAGE_N0,
//
//                new Response.Listener<NetworkResponse>()
//                {
//                    @Override
//                    public void onResponse(NetworkResponse response)
//                    {
//                        try
//                        {
//                            final String jsonResponseString=new String(response.data, HttpHeaderParser.parseCharset(response.headers));
//                            JSONObject responseJson=new JSONObject(jsonResponseString);
//
//
//                            ImageLoader imageLoader= VolleySingleton.getInstance().getmImageLoader();
//                            ArrayList<NewsSingleRow> featuredImages=parseFeaturedNews(responseJson);
//
//                            FrameLayout fl=(FrameLayout)inflater.inflate(R.layout.news_header_image, news, false);
//                            TextView title=(TextView)fl.findViewById(R.id.news_header_text);
//
//                            title.setText(featuredImages.get(0).title);
//                            final ImageView header_image=(ImageView)fl.findViewById(R.id.news_header_image);
//
//                            imageLoader.get(featuredImages.get(0).imageUrl,
//                                    new ImageLoader.ImageListener()
//                                    {
//                                        @Override
//                                        public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
//                                            header_image.setImageBitmap(response.getBitmap());
//                                        }
//
//                                        @Override
//                                        public void onErrorResponse(VolleyError error) {
//                                            header_image.setImageResource(R.mipmap.ic_launcher);
//                                        }
//                                    });
//                            newsAdapter.setParallaxHeader(fl, news);
//                        }
//                        catch (UnsupportedEncodingException | JSONException e)
//                        {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//                ,
//                new Response.ErrorListener()
//                {
//                    @Override
//                    public void onErrorResponse(VolleyError error)
//                    {
//
//                    }
//                });
//
//
//        featuredRequest.setTag(this);
//        requestQueue.add(featuredRequest);

    }



    private ArrayList<NewsSingleRow> parseNews(JSONObject rootObj)
    {
        ArrayList<NewsSingleRow> tempList=new ArrayList<>();
        if(rootObj!=null)
        {

            if(rootObj.length()!=0)
            {
                try
                {
                    if(rootObj.has(NEWS))
                    {
                        JSONArray newsArray = rootObj.getJSONArray(NEWS);
                        FileManager fm=new FileManager(context, PREFERRED_FILE);
                        String slctdTornmnt=fm.readFromFile();

                        for(int i=0;i<newsArray.length();i++)
                        {
                            JSONObject news=newsArray.getJSONObject(i);
                            String imageId="", subtitle="", title="", imageUrl="", description="", date="", tornmnt_id="";

                            if(news.has(TOURNAMENT_ID)) tornmnt_id=news.getString(TOURNAMENT_ID);

                            if(slctdTornmnt.contains(tornmnt_id))
                            {
                                if(news.has(NEWS_SUB_TITLE)) subtitle=news.getString(NEWS_SUB_TITLE);
                                if(news.has(NEWS_TITLE))     title=news.getString(NEWS_TITLE);
                                if(news.has(NEWS_IMAGE_ID))      imageId=news.getString(NEWS_IMAGE_ID); imageUrl+=IMAGE_PATH_THUMNAIL+imageId;
                                if(news.has(NEWS_DESCRIPTIOIN)) description=news.getString(NEWS_DESCRIPTIOIN);
                                if(news.has(NEWS_DATE)) date=news.getString(NEWS_DATE);
                                tempList.add(new NewsSingleRow(imageUrl, subtitle, title, description, date, imageId));
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

        return tempList;
    }

    private ArrayList<NewsSingleRow> parseFeaturedNews(JSONObject rootJson)
    {

        ArrayList<NewsSingleRow> tempList=new ArrayList<>();
        if(rootJson==null || rootJson.length()==0)
        {
            Toast.makeText(context, "API LENGTH IS 0", Toast.LENGTH_LONG).show();
        }
        else
        {


            try
            {
                if(rootJson.has(FEATURED_NEWS))
                {
                    JSONArray featuredNews = rootJson.getJSONArray(FEATURED_NEWS);

                    for(int i=0;i<featuredNews.length();i++)
                    {

                        JSONObject news=featuredNews.getJSONObject(i);
                        String imageId="", subtitle="", title="", imageUrl="", date="";
                        if(news.has(FEATURED_SUB_TITLE)) subtitle=news.getString(FEATURED_SUB_TITLE);
                        if(news.has(FEATURED_TITLE))     title=news.getString(FEATURED_TITLE);
                        if(news.has(FEATURED_IMAGE_ID))      imageId=news.getString(FEATURED_IMAGE_ID); imageUrl+=IMAGE_PATH+imageId;
                        tempList.add(new NewsSingleRow(imageUrl, subtitle, title, "", date, imageId));

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
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.load_more:
                sendNewsScrollRequest();
                break;

            case R.id.tournament_prompt:
                PreferenceAdapter.hasPrefChanged=false;
                context.startActivity(new Intent(context, PreferenceActivity.class));
                break;
        }

    }
}

