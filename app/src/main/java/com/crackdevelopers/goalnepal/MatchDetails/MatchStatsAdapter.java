package com.crackdevelopers.goalnepal.MatchDetails;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crackdevelopers.goalnepal.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by trees on 9/8/15.
 */

public class MatchStatsAdapter extends RecyclerView.Adapter<MatchStatsAdapter.MyViewHolder>
{

    private List<GoalScoredRow> data= Collections.emptyList();

    public MatchStatsAdapter()
    {

    }

    public void updateData(List<GoalScoredRow> data)
    {
        this.data=data;
        notifyItemRangeChanged(0, data.size());
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Context context=parent.getContext();
        LayoutInflater inflater=LayoutInflater.from(context);
        return  new MyViewHolder(inflater.inflate(R.layout.match_scores_single_row, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        GoalScoredRow item=data.get(position);
        holder.playerName.setText(item.playeName);
        holder.scoredTime.setText(item.minutes);
        holder.teamName.setText(item.teamGroup);
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        View itemView;
        TextView playerName, teamName, scoredTime;
        public MyViewHolder(View itemView)
        {
            super(itemView);
            playerName=(TextView)itemView.findViewById(R.id.player_name);
            teamName=(TextView)itemView.findViewById(R.id.team_name);
            scoredTime=(TextView)itemView.findViewById(R.id.scored_minute);
            this.itemView=itemView;

        }
    }
}
