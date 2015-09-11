package com.crackdevelopers.goalnepal.Miscallenous.Gallery;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.crackdevelopers.goalnepal.R;
import com.crackdevelopers.goalnepal.Volley.KEYS;
import com.crackdevelopers.goalnepal.Volley.MyApplication;
import com.crackdevelopers.goalnepal.Volley.VolleySingleton;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GalleryActivity extends AppCompatActivity
{

    /*
    Plz chk attac  >> Album listing    json_albums_2015   donot need parameter  >>Photo listing album wise json_photos_2015 parameter gal_id   >>Photo (when click on photo for big image single photo) json_show_photo_2015 parameter image_id  >>Recent matches (It is fixed now plz chk) json_recent_match_2015.php donot need parameter   Regards Rajendra  ​
     */

    static final String ALBUM_URL="http://goalnepal.com/json_albums_2015.php";
    static final String ALBUM_PHOTOS_URL="http://goalnepal.com/json_photos_2015.php?gal_id=";
    static final String SINGLE_PHOTO_URL="http://goalnepal.com/json_show_photo_2015?match_id=";
    static final String GALLERY_ID="";
    static final String MATCH_ID="";

    private VolleySingleton mVolleySingleton;
    private RequestQueue mRequestQueue;
    private ArrayList<GalleryItem> galleryItemList;
    private RecyclerView recyclerView;
    private GalleryAdapter recylerAdapter;
    private ArrayList<String> stickyList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        init();
    }


    private void init()
    {
        Toolbar toolbar=(Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ////VOLLEY THINNGS HERRE
        galleryItemList =new ArrayList<>();
        mVolleySingleton=VolleySingleton.getInstance();
        mRequestQueue=mVolleySingleton.getQueue();
        sendJsonRequest();


        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        recylerAdapter=new GalleryAdapter(this);
        final StickyRecyclerHeadersDecoration decoration=new StickyRecyclerHeadersDecoration(recylerAdapter);
        recyclerView.setAdapter(recylerAdapter);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recylerAdapter.registerAdapterDataObserver(
                new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                decoration.invalidateHeaders();
            }
        });


    }


    private void sendJsonRequest()
    {
        JsonObjectRequest jObjRequest=new JsonObjectRequest(Request.Method.GET, MyApplication.API_KEY,

                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        galleryItemList =pareJsonObject(response);
                        recylerAdapter.setGalleryData(galleryItemList);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                });

        mRequestQueue.add(jObjRequest);
    }

    private ArrayList<GalleryItem> pareJsonObject(JSONObject response)
    {
        ArrayList<GalleryItem> galleryItemList =new ArrayList<>();
        stickyList=new ArrayList<>();
        if(response==null || response.length()==0)
        {
            return galleryItemList;
        }

        try
        {
            if(response.has(KEYS.GALLERY))
            {
                SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-mm-dd");
                JSONArray galleries=response.getJSONArray(KEYS.GALLERY);
                for(int i=0;i<galleries.length();i++)
                {
                    JSONObject gallery=galleries.getJSONObject(i);
                    String venue=gallery.getString(KEYS.VENUE);
                    Date pubDate=dateFormat.parse(gallery.getString(KEYS.DATE));
                    String imgUrl=gallery.getString(KEYS.IMAGE);
                    galleryItemList.add(new GalleryItem(venue, pubDate, imgUrl));
                    stickyList.add(imgUrl);

                }


            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return galleryItemList;
    }

    public ArrayList<String> getStickyList()
    {
        return stickyList;
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
