package com.crackdevelopers.goalnepal.Leagues;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
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
        holder.iconA.setImageResource(R.drawable.chelse);
        holder.iconB.setImageResource(R.drawable.bayren);

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
                        holder.iconA.setImageResource(R.drawable.chelse);

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
                        holder.iconB.setImageResource(R.drawable.bayren);
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
//            Intent intent=new Intent(context, MatchActivity.class);
//            Bundle bundle=new Bundle();
//
//            bundle.putString(MATCH_CLUB_NAME_A, data.get(getPosition()).nameA);
//            bundle.putString(MATCH_CLUB_NAME_B, data.get(getPosition()).nameB);
//
//            bundle.putString(MATCH_ICON_A, data.get(getPosition()).iconA);
//            bundle.putString(MATCH_ICON_B, data.get(getPosition()).iconB);
//
//            bundle.putString(MATCH_SCORE_A, data.get(getPosition()).scoreA);
//            bundle.putString(MATCH_SCORE_B, data.get(getPosition()).scoreB);
//            bundle.putLong(MATCH_ID, data.get(getPosition()).match_id);
//            intent.putExtra(MATCH_BUNDLE, bundle);
//
//            context.startActivity(intent);
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
            return stickyData.get(position).charAt(0);
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
