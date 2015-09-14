package com.crackdevelopers.goalnepal.News;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageLoader;
import com.crackdevelopers.goalnepal.R;
import com.crackdevelopers.goalnepal.Volley.CacheRequest;
import com.crackdevelopers.goalnepal.Volley.VolleySingleton;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.yalantis.phoenix.PullToRefreshView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


public class NewsFragment extends Fragment
{
    private static final String NEWS_URL="http://www.goalnepal.com/json_news_2015.php?page=";
    private static final String FEATURED_NEWS_URL="http://www.goalnepal.com/json_news_feature_2015.php?page=";
    private static final String IMAGE_PATH="http://www.goalnepal.com/graphics/article/";
    private static final String IMAGE_PATH_THUMNAIL="http://www.goalnepal.com/graphics/article/thumb/";
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

    private RecyclerView news;
    private RequestQueue requestQueue;
    private PullToRefreshView mPullToRefreshView;
    private final int  REFRESH_DELAY = 1500;
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<NewsSingleRow> newsData;
    private NewsAdapter newsAdapter;
    private boolean loading = true;

    private CircularProgressView progressView;

    public NewsFragment()
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
        View v=inflater.inflate(R.layout.news_fragment, container, false);
        news=(RecyclerView)v.findViewById(R.id.newsList);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        this.context=getActivity();


        newsData=new ArrayList<>();
        final LinearLayoutManager manager=new LinearLayoutManager(context);
        news.setLayoutManager(manager);
        newsAdapter = new NewsAdapter(newsData,context);
        news.setAdapter(newsAdapter);


        ///###################### PROGRESS BAR
        progressView = (CircularProgressView)getActivity().findViewById(R.id.progress_view_latest);
        progressView.startAnimation();


        /////////############################## RECYCLER VIEW LISTENER FROM MORE SCROLL########################################


        news.addOnScrollListener(

                new RecyclerView.OnScrollListener() {

                    private int pastVisiblesItems, visibleItemsCount, totalItemsCount;

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                        visibleItemsCount = news.getChildCount();
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
        mPullToRefreshView = (PullToRefreshView) getActivity().findViewById(R.id.pull_to_refresh_news);
        mPullToRefreshView.setOnRefreshListener(

                new PullToRefreshView.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        // do what you want to do when refreshing
                        PAGE_N0 = 1;

                        if(requestQueue!=null) requestQueue.cancelAll(this);
                        sendNewsRequest();
                        sendFeaturedNewsRequest();
                        mPullToRefreshView.postDelayed
                                (

                        new Runnable()
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
        sendFeaturedNewsRequest();

    }

    @Override
    public void onStop()
    {
        super.onResume();
        requestQueue.cancelAll(this);

    }


    private void sendNewsRequest()
    {

       progressView.setVisibility(View.VISIBLE);

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
                            newsAdapter.setData(parseNews(responseJson));
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
                            newsAdapter.setScrollUpdate(parseNews(responseJson));
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

                    }
                });


        newsScrollRequest.setTag(this);
        requestQueue.add(newsScrollRequest);


    }

    //// THIS HEP TO DOWNLOAD THE FEATURED NEWS AND IMAGES
    private void sendFeaturedNewsRequest()
    {

        CacheRequest featuredRequest=new CacheRequest(Request.Method.GET, FEATURED_NEWS_URL+PAGE_N0,

                new Response.Listener<NetworkResponse>()
                {
                    @Override
                    public void onResponse(NetworkResponse response)
                    {
                        try
                        {
                            final String jsonResponseString=new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            JSONObject responseJson=new JSONObject(jsonResponseString);


                            ImageLoader imageLoader= VolleySingleton.getInstance().getmImageLoader();
                            ArrayList<NewsSingleRow> featuredImages=parseFeaturedNews(responseJson);
                            ViewPager viewPager=(ViewPager)inflater.inflate(R.layout.news_header_image, news, false);
                            TextView title=(TextView)viewPager.findViewById(R.id.news_header_text);

                            title.setText(featuredImages.get(0).title);
                            final ImageView header_image=(ImageView)viewPager.findViewById(R.id.news_header_image);
                            imageLoader.get(featuredImages.get(0).imageUrl,
                                    new ImageLoader.ImageListener() {
                                        @Override
                                        public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                                            header_image.setImageBitmap(response.getBitmap());
                                        }

                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            header_image.setImageResource(R.mipmap.ic_launcher);
                                        }
                                    });
                            newsAdapter.setParallaxHeader(viewPager, news);
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


        featuredRequest.setTag(this);
        requestQueue.add(featuredRequest);

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

}
