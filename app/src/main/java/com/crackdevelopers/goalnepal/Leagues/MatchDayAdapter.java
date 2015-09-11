package com.crackdevelopers.goalnepal.Leagues;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.crackdevelopers.goalnepal.MatchDetails.MatchActivity;
import com.crackdevelopers.goalnepal.R;
import com.crackdevelopers.goalnepal.RecentMatch.MatchItem;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by trees on 9/1/15.
 */
public class MatchDayAdapter extends RecyclerView.Adapter<MatchDayAdapter.MyViewHolder>
        implements StickyRecyclerHeadersAdapter<MatchDayAdapter.StickyHolder>
{

    private Context context;
    private LayoutInflater inflater;
    private List<MatchItem> data= Collections.emptyList();
    private List<String> stickyData= Collections.emptyList();

    public MatchDayAdapter(Context context)
    {
        inflater = LayoutInflater.from(context);
        this.context = context;
        data = new ArrayList<>();
        stickyData = new ArrayList<>();
//        for (int i = 0; i < 10; i++)
//        {
//            data.add(new MatchItem(R.drawable.chelse, R.drawable.bayren, "Chelsea", "Baryn", "5 : 4"));
//
//        }
        setData(stickyData);


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new MyViewHolder(inflater.inflate(R.layout.recent_match_row, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
//        String name1=data.get(position).team1;
//        String name2=data.get(position).team2;
//        String score=data.get(position).score;
//        int logo1=data.get(position).logo1;
//        int logo2=data.get(position).log2;
//
//        holder.name1.setText(name1);
//        holder.name2.setText(name2);
//        holder.score.setText(score);
//        holder.logo1.setImageResource(logo2);
//        holder.logo2.setImageResource(logo1);

    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        ImageView logo1, logo2;
        TextView name1, name2, score;
        public MyViewHolder(View itemView)
        {
            super(itemView);
            itemView.setOnClickListener(this);
            name1=(TextView)itemView.findViewById(R.id.today_single_row_team1_name);
            name2=(TextView)itemView.findViewById(R.id.today_single_row_team2_name);
            score=(TextView)itemView.findViewById(R.id.today_single_row_score);
            logo1=(ImageView)itemView.findViewById(R.id.today_single_row_logo1);
            logo2=(ImageView)itemView.findViewById(R.id.today_single_row_logo2);

        }

        @Override
        public void onClick(View v)
        {
            Intent intent=new Intent(context, MatchActivity.class);
            context.startActivity(intent);
        }
    }

//    //STICKY PART BELOW THIS
//
//    private boolean isHeader(int position)
//    {
//        if(position==0) return true;
//        if(position==4) return true;
//        if(position==7) return true;
//
//        return false;
//    }

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
    public MatchDayAdapter.StickyHolder onCreateHeaderViewHolder(ViewGroup viewGroup)
    {
        return new StickyHolder(inflater.inflate(R.layout.sticky_layout, viewGroup, false));
    }

    @Override
    public void onBindHeaderViewHolder(MatchDayAdapter.StickyHolder holder, int position)
    {
        holder.name.setText(stickyData.get(position));
        holder.name.setBackgroundColor(getRandomColor());
    }

    private void setData(List<String> stickyData)
    {
        stickyData.add("1 August");
        stickyData.add("1 August");
        stickyData.add("1 August");
        stickyData.add("3 August");
        stickyData.add("3 August");
        stickyData.add("3 August");
        stickyData.add("4 August");
        stickyData.add("4 August");
        stickyData.add("4 August");
        stickyData.add("8 August");
    }


    private int getRandomColor()
    {
        SecureRandom rgen = new SecureRandom();
        return Color.HSVToColor(150, new float[]
                {
                        rgen.nextInt(359), 1, 1
                });
    }


    class StickyHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView name;
        public StickyHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            name=(TextView)itemView.findViewById(R.id.stickyText);

        }

        @Override
        public void onClick(View v)
        {
            Intent intent=new Intent(context, MatchActivity.class);
            context.startActivity(intent);
        }
    }
}

