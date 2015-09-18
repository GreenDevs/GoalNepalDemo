package com.crackdevelopers.goalnepal.Miscallenous.Gallery;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.crackdevelopers.goalnepal.R;
import com.crackdevelopers.goalnepal.Utility.Utility;
import com.crackdevelopers.goalnepal.Volley.VolleySingleton;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PhotosActivity extends AppCompatActivity
{

    static final String ALBUM_PHOTOS_URL="http://goalnepal.com/json_photos_2015.php?gal_id=";

    private static final String ALBUM="photo_album";
    private static final String LOCATION="location";
    private static final String CREATD_DATE="created_date";
    private static final String IMAGE_PATH="fullPath";
    private static final String IMAGE_ID="image_id";

    private static final String ALBUM_NAME_KEY="album_name";
    private static final String ALBUM_ID_KEY="album_id";
    static List<String> allUrls;

    private static long ALBUM_ID=0;
    private static String ALBUM_NAME="";
    private PhotosAdapter mAdapter;
    private Context context;
    private CircularProgressView progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        ALBUM_ID=getIntent().getLongExtra(ALBUM_ID_KEY, 0);
        ALBUM_NAME=getIntent().getStringExtra(ALBUM_NAME_KEY);

        init();
    }


    private void init()
    {
        context=this;
        Toolbar toolbar=(Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setSubtitle(ALBUM_NAME);

        }

        RecyclerView photosGrid=(RecyclerView)findViewById(R.id.album_recycler);


        if(Utility.isTablet(context))
        {
            photosGrid.setLayoutManager(new GridLayoutManager(context,3));
        }
        else
        {
            photosGrid.setLayoutManager(new GridLayoutManager(context, 2));
        }
        mAdapter=new PhotosAdapter(context);
        photosGrid.setAdapter(mAdapter);

        ///###################### PROGRESS BAR
        progressView = (CircularProgressView)findViewById(R.id.progress_view_album);
        progressView.startAnimation();
        sendJsonRequest();

    }


    private void sendJsonRequest()
    {
        progressView.setVisibility(View.VISIBLE);
        JsonObjectRequest albumRequest=new JsonObjectRequest(Request.Method.GET, ALBUM_PHOTOS_URL+ ALBUM_ID,

                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        mAdapter.setData(parseJson(response));
                        progressView.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                    }
                });

        albumRequest.setTag(this);
        RequestQueue queue= VolleySingleton.getInstance().getQueue();
        queue.add(albumRequest);
    }

    private List<PhotosItem> parseJson(JSONObject rootJson)
    {
        List<PhotosItem> tempList=new ArrayList<>();
        allUrls=new ArrayList<>();

        if(rootJson!=null)
        {
            if(rootJson.length()!=0)
            {
                JSONArray photos=null;
                try
                {
                    if(rootJson.has(ALBUM)) photos=rootJson.getJSONArray(ALBUM);

                    assert photos != null;
                    for(int i=0;i<photos.length();i++)
                    {
                        JSONObject photo=photos.getJSONObject(i);
                        String location="", image_path="", created_date="";
                        long image_id=0;

                        if(photo.has(LOCATION)) location=photo.getString(LOCATION);
                        if(photo.has(CREATD_DATE)) created_date=photo.getString(CREATD_DATE);
                        if(photo.has(IMAGE_PATH)) image_path=photo.getString(IMAGE_PATH);
                        if(photo.has(IMAGE_ID)) image_id=photo.getLong(IMAGE_ID);

                        tempList.add(new PhotosItem(location, created_date, image_path, image_id));
                        allUrls.add(image_path);
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

}
