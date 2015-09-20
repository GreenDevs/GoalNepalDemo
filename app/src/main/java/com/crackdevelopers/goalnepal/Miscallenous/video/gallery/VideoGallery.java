package com.crackdevelopers.goalnepal.Miscallenous.video.gallery;

import android.content.Context;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.crackdevelopers.goalnepal.R;
import com.crackdevelopers.goalnepal.Volley.CacheRequest;
import com.crackdevelopers.goalnepal.Volley.VolleySingleton;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class VideoGallery extends AppCompatActivity
{

    private static final String URL="http://www.goalnepal.com/json_videos_2015.php?page=";
    private static final String VIDEO_ID="vid";
    private static final String VIDEOS="video_list";
    private static final String VIDEO_PIC="vpic";
    private static final String VIDEO_DES="vdesc";
    private static final String VIDEO_TITLE="vtitle";
    private static int PAGE_N0=1;

    private boolean loading = true;
    private VideoGalAdapter mAdapter;
    private CircularProgressView progressView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_gallery);

        init();
        displayads();
    }


    private void init()
    {
        context=this;
        Toolbar toolbar=(Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        final RecyclerView videoList=(RecyclerView)findViewById(R.id.videoListRecycler);

        final LinearLayoutManager manager=new LinearLayoutManager(this);
        videoList.setLayoutManager(manager);
        mAdapter=new VideoGalAdapter(this);
        videoList.setAdapter(mAdapter);

        ///###################### PROGRESS BAR
        progressView = (CircularProgressView)findViewById(R.id.progress_view_latest);
        progressView.startAnimation();

        videoList.addOnScrollListener(

                new RecyclerView.OnScrollListener() {

                    private int pastVisiblesItems, visibleItemsCount, totalItemsCount;

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                        visibleItemsCount = manager.getChildCount();
                        totalItemsCount = manager.getItemCount();
                        pastVisiblesItems = manager.findFirstVisibleItemPosition();


                        if (loading && ((pastVisiblesItems + visibleItemsCount) >= totalItemsCount)) {
                            loading = false;
                            sendNewsScrollRequest();
                        }


                    }
                });

        sendJsonRequest();

    }


    private void sendJsonRequest()
    {
        PAGE_N0=1;
        progressView.setVisibility(View.VISIBLE);

        CacheRequest newsRequest=new CacheRequest(Request.Method.GET, URL+PAGE_N0,

                new Response.Listener<NetworkResponse>()
                {
                    @Override
                    public void onResponse(NetworkResponse response)
                    {
                        try
                        {
                            final String jsonResponseString=new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            JSONObject responseJson=new JSONObject(jsonResponseString);

                            mAdapter.setData(parseJson(responseJson));
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
        VolleySingleton.getInstance().getQueue().add(newsRequest);
    }



    private void sendNewsScrollRequest()
    {
        PAGE_N0++;
        progressView.setVisibility(View.VISIBLE);
        CacheRequest newsScrollRequest=new CacheRequest(Request.Method.GET, URL+PAGE_N0,

                new Response.Listener<NetworkResponse>()
                {
                    @Override
                    public void onResponse(NetworkResponse response)
                    {
                        try
                        {
                            final String jsonResponseString=new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            JSONObject responseJson=new JSONObject(jsonResponseString);


                            mAdapter.setScrollUpdate( parseJson(responseJson));
                            progressView.setVisibility(View.GONE);
                            loading=true;

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
        VolleySingleton.getInstance().getQueue().add(newsScrollRequest);


    }

    private List<VideoItem> parseJson(JSONObject rootJson)
    {
        List<VideoItem> tmpList=new ArrayList<>();

        if(rootJson!=null)
        {
            if(rootJson.length()!=0)
            {
                if(rootJson.has(VIDEOS))
                {
                    try
                    {
                        JSONArray videos=rootJson.getJSONArray(VIDEOS);
                        for(int i=0;i<videos.length();i++)
                        {
                            JSONObject video=videos.getJSONObject(i);

                            String vlink="", vtitle="", vpic="";
                            long vid=0;

                            if(video.has(VIDEO_DES))
                            {
                                String rawLink[]=video.getString(VIDEO_DES).split(" ");
                                vlink=rawLink[0].replace("\"","").replace(" ","");

                            }
                            if(video.has(VIDEO_PIC)) vpic=video.getString(VIDEO_PIC);
                            if(video.has(VIDEO_TITLE)) vtitle=video.getString(VIDEO_TITLE);
                            if(video.has(VIDEO_ID)) vid=video.getLong(VIDEO_ID);

                            tmpList.add(new VideoItem(vtitle, vlink, vpic, vid));
                            Log.i("VIDEO LINKS", vlink);
                        }
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }

        return tmpList;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_video_gallery, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }
        if(id==android.R.id.home)
        {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }


    private  void displayads() {
        AdView mAdView = (AdView) findViewById(R.id.adViewVideoGallery);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("DF748C37109613E8C305043552A7F153").build();
        mAdView.loadAd(adRequest);
    }

}
