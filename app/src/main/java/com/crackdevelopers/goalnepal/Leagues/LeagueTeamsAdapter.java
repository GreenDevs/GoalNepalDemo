package com.crackdevelopers.goalnepal.Leagues;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.crackdevelopers.goalnepal.R;
import com.crackdevelopers.goalnepal.Volley.VolleySingleton;

import java.util.Collections;
import java.util.List;

/**
 * Created by trees on 9/9/15.
 */
public class LeagueTeamsAdapter extends RecyclerView.Adapter<LeagueTeamsAdapter.MyViewHolder>
{

    private List<ParticipatingTeamsRow> data= Collections.emptyList();

    public LeagueTeamsAdapter()
    {

    }

    public void updateData(List<ParticipatingTeamsRow> data)
    {
        this.data=data;
        notifyItemRangeChanged(0, data.size());
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Context context=parent.getContext();
        LayoutInflater inflater=LayoutInflater.from(context);
        return  new MyViewHolder(inflater.inflate(R.layout.participating_teams_row, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position)
    {
        ParticipatingTeamsRow item=data.get(position);
        holder.teamShortName.setText(item.team_short_name);
        holder.teamName.setText(item.team_name);
        holder.icon.setImageResource(R.drawable.soccer_black);

        ImageLoader imageLoader= VolleySingleton.getInstance().getmImageLoader();
        imageLoader.get(item.icon_url, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate)
            {
                holder.icon.setImageBitmap(response.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError error)
            {
                holder.icon.setImageResource(R.drawable.soccer_black);
            }
        });
    }


    @Override
    public int getItemCount()
    {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        ImageView icon;
        TextView teamShortName, teamName;
        public MyViewHolder(View itemView)
        {
            super(itemView);
            teamName=(TextView)itemView.findViewById(R.id.team_name);
            teamShortName=(TextView)itemView.findViewById(R.id.team_short_name);
            icon=(ImageView)itemView.findViewById(R.id.team_icon);


        }
    }
}
