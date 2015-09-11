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
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by trees on 9/7/15.
 */
public class LeagueTableAdapter extends RecyclerView.Adapter<LeagueTableAdapter.MyViewHolder>
        implements StickyRecyclerHeadersAdapter<LeagueTableAdapter.StickyHolder>
{
    private Context context;
    private LayoutInflater inflater;
    private List<LeagueTableItem> data;
    private List<String> stickyData;


    public LeagueTableAdapter(Context context)
    {
        this.context=context;
        inflater=LayoutInflater.from(context);
        data=new ArrayList<>();
        stickyData=new ArrayList<>();

    }

    public void setData(List<LeagueTableItem> data)
    {
        this.data=data;

        for(LeagueTableItem item:data)
        {
            stickyData.add(item.pGroup);
        }
        notifyItemRangeChanged(0, data.size());
    }


    @Override
    public int getItemCount()
    {
        return data.size();
    }



    @Override
    public LeagueTableAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new MyViewHolder(inflater.inflate(R.layout.league_table_row, parent, false));
    }

    @Override
    public void onBindViewHolder(final LeagueTableAdapter.MyViewHolder holder, int position)
    {
        LeagueTableItem item=data.get(position);
        ImageLoader imageLoader= VolleySingleton.getInstance().getmImageLoader();

        holder.name.setText(item.name);
        holder.match_played.setText(item.match_played);
        holder.match_won.setText(item.match_won);
        holder.match_draw.setText(item.match_draw);
        holder.match_lost.setText(item.match_lost);
        holder.goal_finished.setText(item.goal_finished);
        holder.goal_accepted.setText(item.goal_accepted);
        holder.goal_diff.setText(item.goal_diff);
        holder.points.setText(item.points);
        holder.team_icon.setImageResource(R.drawable.chelse);

        imageLoader.get(item.club_icon_url,

        new ImageLoader.ImageListener()
        {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate)
            {
                holder.team_icon.setImageBitmap(response.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError error)
            {
                holder.team_icon.setImageResource(R.drawable.chelse);
            }
        });
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {

        ImageView team_icon;
        TextView  name, match_played, match_won,
                match_draw, match_lost, goal_finished, goal_accepted, goal_diff, points;

        public MyViewHolder(View itemView)
        {
            super(itemView);

            team_icon=(ImageView)itemView.findViewById(R.id.team_icon);

            name=(TextView)itemView.findViewById(R.id.team_name);
            match_played=(TextView)itemView.findViewById(R.id.match_played);
            match_won=(TextView)itemView.findViewById(R.id.match_won);
            match_draw=(TextView)itemView.findViewById(R.id.match_draw);
            match_lost=(TextView)itemView.findViewById(R.id.match_lost);
            goal_finished=(TextView)itemView.findViewById(R.id.goal_finished);
            goal_accepted=(TextView)itemView.findViewById(R.id.goal_accepted);
            goal_diff=(TextView)itemView.findViewById(R.id.goal_diff);
            points=(TextView)itemView.findViewById(R.id.points);
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
    public StickyHolder onCreateHeaderViewHolder(ViewGroup viewGroup)
    {
        return new StickyHolder(inflater.inflate(R.layout.sticky_layout, viewGroup, false));
    }

    @Override
    public void onBindHeaderViewHolder(StickyHolder stickyHolder, int i)
    {
        stickyHolder.pgroup.setText(stickyData.get(i));
    }


    class StickyHolder extends RecyclerView.ViewHolder
    {

        TextView pgroup;
        public StickyHolder(View itemView)
        {
            super(itemView);
            pgroup=(TextView)itemView.findViewById(R.id.stickyText);

        }
    }

}
