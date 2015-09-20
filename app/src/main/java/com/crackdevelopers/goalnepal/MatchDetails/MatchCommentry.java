package com.crackdevelopers.goalnepal.MatchDetails;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
import java.util.List;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

/**
 * Created by trees on 8/25/15.
 */
public class MatchCommentry extends Fragment {

    ///API KEY ATTRIBUTES
    private static final String URL = "http://www.goalnepal.com/json_livecommentary_2015.php?match_id=";
    private static final String IMAGE_PATH = "http://www.goalnepal.com/";
    private long MATCH_ID;
    private static final String MATCH_COMMENTARY = "comments";
    private static final String MATCH_MINUTE = "minute";
    private static final String MATCH_TEXT = "text";
    private static final String ICON_URL = "icon";


    private WaveSwipeRefreshLayout mPullToRefreshView;
    private final int REFRESH_DELAY = 1500;
    private ComentaryAdapter commentaryAdapter;
    private Context context;
    private RequestQueue queue;
    private TextView promptText;
    private boolean isdataAvailable=false;
    private CircularProgressView progressView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MATCH_ID = MatchActivity.MATCH_ID;
        queue = VolleySingleton.getInstance().getQueue();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.match_commentry_fragment, container, false);

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.context = getActivity();

        promptText= (TextView) getActivity().findViewById(R.id.prompt_text);
        RecyclerView commentryList = (RecyclerView) getActivity().findViewById(R.id.commentryList);
        commentaryAdapter = new ComentaryAdapter(getActivity());
        commentryList.setAdapter(commentaryAdapter);
        commentryList.setLayoutManager(new LinearLayoutManager(getActivity()));

        mPullToRefreshView = (WaveSwipeRefreshLayout) getActivity().findViewById(R.id.pull_to_refresh_commentry);
        mPullToRefreshView.setWaveColor(Color.parseColor("#c62828"));
        mPullToRefreshView.setColorSchemeColors(Color.WHITE);

        mPullToRefreshView.setOnRefreshListener(

                new WaveSwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        // do what you want to do when refreshing
                        mPullToRefreshView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mPullToRefreshView.setRefreshing(false);
                            }
                        }, REFRESH_DELAY);
                    }
                }
        );

        ///###################### PROGRESS BAR
        progressView = (CircularProgressView) getActivity().findViewById(R.id.progress_view_commentry);
        progressView.startAnimation();
    }

    @Override
    public void onStart() {
        super.onPause();
        sendCommentryRequest();

    }

    @Override
    public void onStop() {
        super.onResume();
        queue.cancelAll(this);

    }

    private void sendCommentryRequest() {

        progressView.setVisibility(View.VISIBLE);
        CacheRequest commntrCacheRequest = new CacheRequest(Request.Method.GET, URL + MATCH_ID,

                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            final String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            JSONObject jsonObject = new JSONObject(jsonString);
                            commentaryAdapter.setData(parseData(jsonObject));
                            progressView.setVisibility(View.GONE);
                        } catch (UnsupportedEncodingException | JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
                ,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressView.setVisibility(View.GONE);
                        Toast.makeText(context, "couldn't load", Toast.LENGTH_SHORT).show();
                    }
                });

        commntrCacheRequest.setTag(this);
        queue.add(commntrCacheRequest);
    }


    private List<CommentaryItem> parseData(JSONObject rootJson) {
        List<CommentaryItem> tempList = new ArrayList<>();
        if (rootJson != null) {
            if (rootJson.length() != 0) {
                if (rootJson.has(MATCH_COMMENTARY)) {
                    try {

                        JSONArray commentaries = rootJson.getJSONArray(MATCH_COMMENTARY);
                        for (int i = 0; i < commentaries.length(); i++)
                        {

                            isdataAvailable=true;
                            promptText.setVisibility(View.GONE);
                            JSONObject commentary = commentaries.getJSONObject(i);
                            String time = "", icon_url = "", text = "";

                            if (commentary.has(MATCH_MINUTE))
                                time = commentary.getString(MATCH_MINUTE);
                            if (commentary.has(MATCH_TEXT)) text = commentary.getString(MATCH_TEXT);
                            if (commentary.has(ICON_URL))
                                icon_url = IMAGE_PATH + commentary.getString(ICON_URL);

                            tempList.add(new CommentaryItem(time, icon_url, text));
                        }

                        if(!isdataAvailable){ promptText.setVisibility(View.VISIBLE);}
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }

        return tempList;
    }
}
