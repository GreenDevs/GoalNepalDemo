package com.crackdevelopers.goalnepal.Miscallenous.Preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.crackdevelopers.goalnepal.FileManager;
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
import java.util.List;

public class PreferenceActivity extends AppCompatActivity
{

    final static String SHARED_PREF="com.crackdevelopers.goalnepal.Miscallenous.Preferences";

    private final static String URL="http://www.goalnepal.com/json_setting_2015.php";
    private final static String INTERNATIONAL="internationals";
    private final static String DOMESTICS="domestics";
    private final static String TOURNAMENT_TITLE="title";
    private final static String HAS_CHILD="hasChild";
    private final static String TOURNAMENT_ID="id";
    private final static String TOURNAMENTS="tournaments";
    private final static String START_DATE="start_date";
    private final static String END_DATE="end_date";
    private PreferenceAdapter mAdapter;
    private Context context;
    private CircularProgressView progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);
        context=this;

        init();
    }

    private void init()
    {
        progressView = (CircularProgressView)findViewById(R.id.progress_view_preferences);
        progressView.startAnimation();
        progressView.setVisibility(View.VISIBLE);

        Toolbar toolbar=(Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView tormntList=(RecyclerView)findViewById(R.id.pref_list_recyler);
        tormntList.setLayoutManager(new LinearLayoutManager(context));
        mAdapter=new PreferenceAdapter(context);
        tormntList.setAdapter(mAdapter);

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



    ///API WORKOUT HERE

    private void sendJsonRequest()
    {
            progressView.setVisibility(View.VISIBLE);
            CacheRequest menuRequest = new CacheRequest(Request.Method.GET, URL,


                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            try {
                                final String jsonResponseString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                                JSONObject menuJson = new JSONObject(jsonResponseString);
                                mAdapter.updateMenu(pareseMenu(menuJson));

                                ///WRITING THE INTERNET DATA TO THE FILE
                                FileManager fileManager = new FileManager(context);
                                fileManager.writeToFile(jsonResponseString);
                                progressView.setVisibility(View.GONE);

                            }
                            catch (UnsupportedEncodingException | JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                    ,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });


            menuRequest.setTag(this);
            VolleySingleton.getInstance().getQueue().add(menuRequest);
    }


    @Override
    protected void onStart()
    {
        super.onStart();
        sendJsonRequest();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        VolleySingleton.getInstance().getQueue().cancelAll(this);
    }

    private List<PreferenceRow> pareseMenu(JSONObject menuJson)
    {
        List<PreferenceRow> menuList= new ArrayList<>();
        if(menuJson==null)
        {

        }
        else
        {

            try
            {
                String TYPE="";
                SharedPreferences preferences=getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
                for(int z=0;z<2;z++)
                {
                    if(z==0) TYPE=INTERNATIONAL;
                    if(z==1) TYPE=DOMESTICS;


                    if(menuJson.has(TYPE))
                    {


                        JSONArray internationals=(JSONArray)menuJson.get(TYPE);
                        final int internationSize=internationals.length();
                        for(int i=0;i<internationSize;i++)
                        {


                            JSONObject international=internationals.getJSONObject(i);
//                        String parent_title=""; Long parent_id=0L;
//                        if(international.has(TOURNAMENT_ID))  parent_id=international.getLong(TOURNAMENT_ID);
//                        if(international.has(TOURNAMENT_TITLE))  parent_title=international.getString(TOURNAMENT_TITLE);

//                        menuList.add(parent_title);

                            if(international.has(TOURNAMENTS))
                            {
                                JSONArray tournaments=(JSONArray)international.get(TOURNAMENTS);
                                final int tournaments_count=tournaments.length();
                                for(int j=0;j<tournaments_count;j++)
                                {
                                    JSONObject tournament=tournaments.getJSONObject(j);
                                    String child_title=""; Long child_id=0L; String start_date="0000-00-00", year="0000";


                                    if(tournament.has(START_DATE)) start_date=tournament.getString(START_DATE); year=start_date.substring(0,4);
                                    int currnetYear=Calendar.getInstance().get(Calendar.YEAR);

                                    if(year.equals(currnetYear+"") || year.equals((currnetYear-1)+""))
                                    {
                                        if(tournament.has(TOURNAMENT_ID))  child_id=tournament.getLong(TOURNAMENT_ID);
                                        if(tournament.has(TOURNAMENT_TITLE))  child_title=tournament.getString(TOURNAMENT_TITLE);

                                        boolean checked=preferences.getBoolean(child_id+"",false);          //PULLING PREFERRED SETTIG FROM DATA
                                        menuList.add(new PreferenceRow(child_title, child_id, checked));
                                    }

                                }

                            }



                        }

                    }
                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        return menuList;
    }

}
