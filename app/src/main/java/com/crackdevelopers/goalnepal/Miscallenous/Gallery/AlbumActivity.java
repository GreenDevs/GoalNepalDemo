package com.crackdevelopers.goalnepal.Miscallenous.Gallery;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.crackdevelopers.goalnepal.R;
import com.crackdevelopers.goalnepal.Utility.Utility;
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

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

public class AlbumActivity extends AppCompatActivity
{

    /*
    Plz chk attac  >> Album listing    json_albums_2015   donot need parameter  >>Photo listing album wise json_photos_2015 parameter gal_id   >>Photo (when click on photo for big image single photo) json_show_photo_2015 parameter image_id  >>Recent matches (It is fixed now plz chk) json_recent_match_2015.php donot need parameter   Regards Rajendra  ​
     */

    static final String ALBUM_URL="http://goalnepal.com/json_albums_2015.php?page=";
    private static final String ALBUMS="photo_album";
    private static final String ALBUM_NAME="gallery_name";
    private static final String ALBUM_ID="gallery_id";
    private static final String ALBUM_THUMNAIL="thumbnail";
    private static final String ALBUM_PATH="http://goalnepal.com/";
    private int PAGE_NO=1;


    private AlbumAdapter mAdapter;
    private Context context;
    private CircularProgressView progressView;
    private WaveSwipeRefreshLayout mPullToRefreshView;
    private final int  REFRESH_DELAY = 1500;
    private RecyclerView albumGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        init();
        displayads();
    }


    private void init()
    {
        context=this;
        Toolbar toolbar=(Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        albumGrid=(RecyclerView)findViewById(R.id.album_recycler);
        mAdapter=new AlbumAdapter(context);
        albumGrid.setAdapter(mAdapter);

        if(Utility.isTablet(context))
        {
            albumGrid.setLayoutManager(new GridLayoutManager(context,3));
        }
        else
        {
            albumGrid.setLayoutManager(new GridLayoutManager(context, 2));
        }
        ///###################### PROGRESS BAR
        progressView = (CircularProgressView)findViewById(R.id.progress_view_album);
        progressView.startAnimation();
        sendJsonRequest();

        mPullToRefreshView = (WaveSwipeRefreshLayout)findViewById(R.id.pull_to_refresh_album);
        mPullToRefreshView.setWaveColor(Color.parseColor("#c62828"));
        mPullToRefreshView.setColorSchemeColors(Color.WHITE);
        mPullToRefreshView.setOnRefreshListener(

                new WaveSwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        // do what you want to do when refreshing


                        VolleySingleton.getInstance().getQueue().cancelAll(this);
                        sendJsonRequest();
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


    private void sendJsonRequest()
    {
        PAGE_NO=1;

        CacheRequest albumRequest=new CacheRequest(Request.Method.GET, ALBUM_URL+PAGE_NO,

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

//                            while(PAGE_NO<5)
//                            {
//                                sendScrollRequest();
//                            }
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


        albumRequest.setTag(this);
        RequestQueue queue=VolleySingleton.getInstance().getQueue();
        queue.add(albumRequest);
    }

    private void sendScrollRequest()
    {
        PAGE_NO++;
        progressView.setVisibility(View.VISIBLE);
        CacheRequest albumScrollRequest=new CacheRequest(Request.Method.GET, ALBUM_URL+PAGE_NO,

                new Response.Listener<NetworkResponse>()
                {
                    @Override
                    public void onResponse(NetworkResponse response)
                    {
                        try
                        {
                            final String jsonResponseString=new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            JSONObject responseJson=new JSONObject(jsonResponseString);
                            mAdapter.scrollList(parseJson(responseJson));
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


        albumScrollRequest.setTag(this);
        RequestQueue queue=VolleySingleton.getInstance().getQueue();
        queue.add(albumScrollRequest);
    }

    private List<AlbumItem> parseJson(JSONObject rootJson)
    {
        List<AlbumItem> tempList=new ArrayList<>();

        if(rootJson!=null)
        {
            if(rootJson.length()!=0)
            {
                JSONArray albums=null;
                try
                {
                    if(rootJson.has(ALBUMS)) albums=rootJson.getJSONArray(ALBUMS);

                    assert albums != null;
                    for(int i=0;i<albums.length();i++)
                    {
                        JSONObject album=albums.getJSONObject(i);
                        String name="", thumnail_url="";
                        long album_id=0;
                        
                        if(album.has(ALBUM_NAME)) name=album.getString(ALBUM_NAME);
                        if(album.has(ALBUM_ID)) album_id=album.getLong(ALBUM_ID);
                        if(album.has(ALBUM_THUMNAIL)) thumnail_url=ALBUM_PATH+album.getString(ALBUM_THUMNAIL);
                        
                        tempList.add(new AlbumItem(name, thumnail_url, album_id));
                    }
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }

        return tempList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id=item.getItemId();
        if(id==android.R.id.home)
        {
            this.finish();
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop()
    {
        super.onStop();

    }



    private  void displayads() {
        AdView mAdView = (AdView) findViewById(R.id.adViewGallary);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("DF748C37109613E8C305043552A7F153").build();
        mAdView.loadAd(adRequest);
    }


}
