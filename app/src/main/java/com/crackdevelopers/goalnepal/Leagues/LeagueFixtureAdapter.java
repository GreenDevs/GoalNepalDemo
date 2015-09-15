package com.crackdevelopers.goalnepal.Leagues;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableRow;
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
 * Created by trees on 9/8/15.
 */
public class LeagueFixtureAdapter extends RecyclerView.Adapter<LeagueFixtureAdapter.MyViewHolder>
        implements StickyRecyclerHeadersAdapter<LeagueFixtureAdapter.StickyHolder>
{
    private Context context;
    private LayoutInflater inflater;
    private List<FixtureRow> data;
    private List<String> stickyData;

    private static final String MATCH_BUNDLE="match_bundle";
    private static final String MATCH_SCORE_A="score_A";
    private static final String MATCH_SCORE_B="score_B";
    private static final String MATCH_CLUB_NAME_A="club_name_a";
    private static final String MATCH_CLUB_NAME_B="club_name_b";
    private static final String MATCH_ICON_A="match_icon_url_a";
    private static final String MATCH_ICON_B="match_icon_url_b";
    private static final String MATCH_ID="match_id";

    public LeagueFixtureAdapter(Context context)
    {
        this.context=context;
        inflater=LayoutInflater.from(context);
        data=new ArrayList<>();
        stickyData=new ArrayList<>();

    }

    public void setData(List<FixtureRow> data)
    {
        this.data=data;

        stickyData.clear();
        for(FixtureRow item:data)
        {
            stickyData.add(item.stage_title);
        }
        notifyItemRangeChanged(0, data.size());
    }


    @Override
    public LeagueFixtureAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new MyViewHolder(inflater.inflate(R.layout.league_fixture_row, parent, false));
    }

    @Override
    public void onBindViewHolder(final LeagueFixtureAdapter.MyViewHolder holder, int position)
    {
        FixtureRow item=data.get(position);
        String name1=item.club_a_name;
        String name2=item.club_b_name;
        String score=item.club_a_score+" : "+item.club_b_score;
        String iconA=item.club_a_icon;
        String iconB=item.club_b_icon;
        String date=item.match_date;

        holder.nameA.setText(name1);
        holder.nameB.setText(name2);
        holder.score.setText(score);
        holder.date.setText(date);
        holder.iconA.setImageResource(R.drawable.soccer_black);
        holder.iconB.setImageResource(R.drawable.soccer_black);

        ///THIS WILL SET THE CLUBA ICON
        ImageLoader imageLoader= VolleySingleton.getInstance().getmImageLoader();
        imageLoader.get(iconA,

                new ImageLoader.ImageListener() {
                    @Override
                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate)
                    {
                        holder.iconA.setImageBitmap(response.getBitmap());
                    }

                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        holder.iconA.setImageResource(R.drawable.soccer_black);

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
                        holder.iconB.setImageResource(R.drawable.soccer_black);
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
        TextView nameA, nameB, score, date;
        public MyViewHolder(View itemView)
        {
            super(itemView);
            itemView.setOnClickListener(this);
            nameA=(TextView)itemView.findViewById(R.id.club_a_name);
            nameB=(TextView)itemView.findViewById(R.id.club_b_name);
            score=(TextView)itemView.findViewById(R.id.match_score);
            iconA=(ImageView)itemView.findViewById(R.id.club_a_icon);
            iconB=(ImageView)itemView.findViewById(R.id.club_b_icon);
            date=(TextView)itemView.findViewById(R.id.match_date);

        }

        @Override
        public void onClick(View v)
        {
            Intent intent=new Intent(context, MatchActivity.class);
            Bundle bundle=new Bundle();
            FixtureRow item=data.get(getAdapterPosition());
            bundle.putString(MATCH_CLUB_NAME_A, item.club_a_name);
            bundle.putString(MATCH_CLUB_NAME_B, item.club_b_name);

            bundle.putString(MATCH_ICON_A, item.club_a_icon);
            bundle.putString(MATCH_ICON_B, item.club_b_icon);

            bundle.putString(MATCH_SCORE_A, item.club_a_score);
            bundle.putString(MATCH_SCORE_B, item.club_b_score);
            bundle.putLong(MATCH_ID, item.match_id);
            intent.putExtra(MATCH_BUNDLE, bundle);

            context.startActivity(intent);
        }
    }
    @Override
    public long getHeaderId(int position)
    {
        if (position == -1)
        {
            return -1;
        }
        else
        {
            char returnchar=':';
            for(char c:stickyData.get(position).toCharArray())
            {
                returnchar+=c;
            }
            return returnchar;
        }
    }

    @Override
    public LeagueFixtureAdapter.StickyHolder onCreateHeaderViewHolder(ViewGroup viewGroup)
    {
        return new StickyHolder(inflater.inflate(R.layout.sticky_layout, viewGroup, false));
    }

    @Override
    public void onBindHeaderViewHolder(LeagueFixtureAdapter.StickyHolder stickyHolder, int i)
    {
        stickyHolder.stage_title.setText(stickyData.get(i));
    }

    class StickyHolder extends RecyclerView.ViewHolder
    {
        TextView stage_title;
        public StickyHolder(View itemView) {
            super(itemView);
            stage_title=(TextView)itemView.findViewById(R.id.stickyText);

        }

    }

}
