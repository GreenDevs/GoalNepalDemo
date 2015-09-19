package com.crackdevelopers.goalnepal.Miscallenous.Ranking;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.crackdevelopers.goalnepal.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by script on 9/18/15.
 */
public class FIFARankAdapter extends  RecyclerView.Adapter<FIFARankAdapter.MyViewHolder>
{
    private Context mContext;
    private LayoutInflater layoutInflater;
    private ArrayList<RankingSingleRow> data;

    public FIFARankAdapter(ArrayList<RankingSingleRow> data, Context context)
    {
        this.mContext=context;
        layoutInflater=LayoutInflater.from(context);
        this.data=data;
    }

    public void scrollList(List<RankingSingleRow> data)
    {
        int prevSize=this.data.size();
        for(RankingSingleRow item:data)
        {
            this.data.add(item);
        }

        notifyItemRangeChanged(prevSize, data.size());
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return  new MyViewHolder(layoutInflater.inflate(R.layout.single_row_fifa_rank,parent,false));

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        RankingSingleRow singleRow=data.get(position);
        holder.rankYear.setText(singleRow.rankYear);
        holder.rank.setText(singleRow.rank);
        holder.rankMonth.setText(singleRow.rankMonth);
        holder.rankImage.setImageResource(R.drawable.flag120);
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView rankYear,rank,rankMonth;
        ImageView rankImage;
        public MyViewHolder(View itemView)
        {
            super(itemView);
            rankYear=(TextView)itemView.findViewById(R.id.fifaRanking_year);
            rank=(TextView)itemView.findViewById(R.id.fifaRanking_rank);
            rankMonth=(TextView)itemView.findViewById(R.id.fifaRanking_month);
            rankImage=(ImageView)itemView.findViewById(R.id.fifaRanking_image);
        }
    }
}


