package com.crackdevelopers.goalnepal.MatchDetails;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.crackdevelopers.goalnepal.R;
import com.crackdevelopers.goalnepal.Volley.CacheRequest;
import com.crackdevelopers.goalnepal.Volley.VolleySingleton;
import com.yalantis.phoenix.PullToRefreshView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by trees on 8/25/15.
 */
public class MatchCommentry extends Fragment
{

    ///API KEY ATTRIBUTES
    private static final String URL="http://www.goalnepal.com/json_livecommentary_2015.php?match_id=";
    private static final String IMAGE_PATH="http://www.goalnepal.com/";
    private static final String MATCH_ID="1875";
    private static final String MATCH_COMMENTARY="comments";
    private static final String MATCH_MINUTE="minute";
    private static final String MATCH_TEXT="text";
    private static final String ICON_URL="icon";


    private PullToRefreshView mPullToRefreshView;
    private final int  REFRESH_DELAY = 1500;
    private ComentaryAdapter commentaryAdapter;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.match_commentry_fragment, container, false);

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        this.context=getActivity();
        RecyclerView commentryList=(RecyclerView)getActivity().findViewById(R.id.commentryList);
        commentaryAdapter=new ComentaryAdapter(getActivity());
        commentryList.setAdapter(commentaryAdapter);
        commentryList.setLayoutManager(new LinearLayoutManager(getActivity()));

        mPullToRefreshView = (PullToRefreshView) getActivity().findViewById(R.id.pull_to_refresh_commentry);
        mPullToRefreshView.setOnRefreshListener(

                new PullToRefreshView.OnRefreshListener()
                {
                    @Override
                    public void onRefresh()
                    {
                        // do what you want to do when refreshing
                        Toast.makeText(getActivity(), "Refresh", Toast.LENGTH_SHORT).show();
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

        sendCommentryRequest();
    }

    private void sendCommentryRequest()
    {
        CacheRequest commntrCacheRequest=new CacheRequest(Request.Method.GET, URL+MATCH_ID,

        new Response.Listener<NetworkResponse>()
        {
              @Override
              public void onResponse(NetworkResponse response)
              {
                  try
                  {
                      final String jsonString=new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                      JSONObject jsonObject=new JSONObject(jsonString);
                      commentaryAdapter.setData(parseData(jsonObject));
                  }
                  catch (UnsupportedEncodingException e)
                  {
                      e.printStackTrace();
                  }
                  catch (JSONException e)
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
        RequestQueue queue= VolleySingleton.getInstance().getQueue();
        commntrCacheRequest.setTag(this);
        queue.add(commntrCacheRequest);
    }


    private List<CommentaryItem> parseData(JSONObject rootJson)
    {
        List<CommentaryItem> tempList=new ArrayList<>();
        if(rootJson!=null)
        {
            if(rootJson.length()!=0)
            {
                if(rootJson.has(MATCH_COMMENTARY))
                {
                    try
                    {
                        JSONArray commentaries=rootJson.getJSONArray(MATCH_COMMENTARY);
                        for(int i=0;i<commentaries.length();i++)
                        {
                            JSONObject commentary=commentaries.getJSONObject(i);
                            String time="", icon_url="", text="";

                            if(commentary.has(MATCH_MINUTE)) time=commentary.getString(MATCH_MINUTE);
                            if(commentary.has(MATCH_TEXT))   text=commentary.getString(MATCH_TEXT);
                            if(commentary.has(ICON_URL))     icon_url=IMAGE_PATH+commentary.getString(ICON_URL);

                            tempList.add(new CommentaryItem(time, icon_url, text));
                        }
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                }
            }
        }

        return tempList;
    }
}