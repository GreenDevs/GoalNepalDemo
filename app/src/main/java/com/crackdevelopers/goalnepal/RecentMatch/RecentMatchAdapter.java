package com.crackdevelopers.goalnepal.RecentMatch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.crackdevelopers.goalnepal.MatchDetails.MatchActivity;
import com.crackdevelopers.goalnepal.R;
import com.crackdevelopers.goalnepal.Volley.VolleySingleton;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by trees on 8/26/15.
 */
public class RecentMatchAdapter extends RecyclerView.Adapter<RecentMatchAdapter.MyViewHolder>
        implements StickyRecyclerHeadersAdapter<RecentMatchAdapter.StickyHolder>
{

    private Context context;
    private LayoutInflater inflater;
    private List<MatchItem> data= Collections.emptyList();
    private List<String> stickyData= Collections.emptyList();
    private ImageLoader imageLoader;

    //FOR INTENT PURPOSE
    private static final String MATCH_BUNDLE="match_bundle";
    private static final String MATCH_SCORE_A="score_A";
    private static final String MATCH_SCORE_B="score_B";
    private static final String MATCH_CLUB_NAME_A="club_name_a";
    private static final String MATCH_CLUB_NAME_B="club_name_b";
    private static final String MATCH_ICON_A="match_icon_url_a";
    private static final String MATCH_ICON_B="match_icon_url_b";
    private static final String MATCH_ID="match_id";

    public RecentMatchAdapter(Context context)
    {
        inflater = LayoutInflater.from(context);
        this.context = context;
        data = new ArrayList<>();
        imageLoader= VolleySingleton.getInstance().getmImageLoader();
        setData();


    }

    public void setData(List<MatchItem> data)
    {
        this.data=data;
        stickyData=new ArrayList<>();
        for(MatchItem item:data)
        {
            stickyData.add((item.date).toString());
        }
        notifyItemRangeChanged(0, data.size());
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new MyViewHolder(inflater.inflate(R.layout.recent_match_row, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position)
    {
        String name1=data.get(position).nameA;
        String name2=data.get(position).nameB;
        String score=data.get(position).scoreA+" : "+data.get(position).scoreB;
        String iconA=data.get(position).iconA;
        String iconB=data.get(position).iconB;

        holder.nameA.setText(name1);
        holder.nameB.setText(name2);
        holder.score.setText(score);
        holder.iconA.setImageResource(R.drawable.chelse);
        holder.iconB.setImageResource(R.drawable.bayren);

        ///THIS WILL SET THE CLUBA ICON
        imageLoader.get(iconA,

                new ImageLoader.ImageListener() {
                    @Override
                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate)
                    {
                        holder.iconA.setImageBitmap(response.getBitmap());
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        ///THIS WILL SET THE CLUB B ICON
        imageLoader.get(iconB,

                new ImageLoader.ImageListener()
                {
                    @Override
                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate)
                    {
                        holder.iconB.setImageBitmap(response.getBitmap());
                    }

                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                    }
                });

    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        ImageView iconA, iconB;
        TextView nameA, nameB, score;
        public MyViewHolder(View itemView)
        {
            super(itemView);
            itemView.setOnClickListener(this);
            nameA=(TextView)itemView.findViewById(R.id.today_single_row_team1_name);
            nameB=(TextView)itemView.findViewById(R.id.today_single_row_team2_name);
            score=(TextView)itemView.findViewById(R.id.today_single_row_score);
            iconA=(ImageView)itemView.findViewById(R.id.today_single_row_logo1);
            iconB=(ImageView)itemView.findViewById(R.id.today_single_row_logo2);

        }

        @Override
        public void onClick(View v)
        {
            Intent intent=new Intent(context, MatchActivity.class);
            Bundle bundle=new Bundle();

            bundle.putString(MATCH_CLUB_NAME_A, data.get(getPosition()).nameA);
            bundle.putString(MATCH_CLUB_NAME_B, data.get(getPosition()).nameB);

            bundle.putString(MATCH_ICON_A, data.get(getPosition()).iconA);
            bundle.putString(MATCH_ICON_B, data.get(getPosition()).iconB);

            bundle.putString(MATCH_SCORE_A, data.get(getPosition()).scoreA);
            bundle.putString(MATCH_SCORE_B, data.get(getPosition()).scoreB);
            bundle.putLong(MATCH_ID, data.get(getPosition()).match_id);
            intent.putExtra(MATCH_BUNDLE, bundle);

            context.startActivity(intent);
        }
    }

//    //STICKY PART BELOW THIS

    @Override
    public long getHeaderId(int position)
    {
        if (position == -1)
        {

            return -1;
        }
        else
        {

            return stickyData.get(position).charAt(0);
        }
    }


    @Override
    public RecentMatchAdapter.StickyHolder onCreateHeaderViewHolder(ViewGroup viewGroup)
    {
        return new StickyHolder(inflater.inflate(R.layout.sticky_layout, viewGroup, false));
    }

    @Override
    public void onBindHeaderViewHolder(RecentMatchAdapter.StickyHolder holder, int position)
    {
        holder.name.setText(stickyData.get(position));

    }

    private void setData()
    {
        stickyData = new ArrayList<>();
        stickyData.add("L");
        stickyData.add("L");

    }


    class StickyHolder extends RecyclerView.ViewHolder
    {
        TextView name;
        public StickyHolder(View itemView) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.stickyText);

        }

    }
}