package com.crackdevelopers.goalnepal.NavigationDrawer;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.crackdevelopers.goalnepal.Leagues.LeagueActivity;
import com.crackdevelopers.goalnepal.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by trees on 9/10/15.
 */
public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.MyViewHolder>
{
    private static final String TOURNAMENT_ID="tournament_id";
    private static final String TOURNAMENT_NAME="tournament_name";
    private Context context;
    private LayoutInflater inflater;
    private List<NavigationRow> data= Collections.emptyList();

    public DrawerAdapter(Context context, List<NavigationRow> data)
    {
        this.context=context;
        inflater=LayoutInflater.from(context);
        this.data=data;
    }

    @Override
    public DrawerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new MyViewHolder(inflater.inflate(R.layout.navigation_drawer_item, parent, false));
    }

    @Override
    public void onBindViewHolder(DrawerAdapter.MyViewHolder holder, int position)
    {
        holder.navItem.setText(data.get(position).name);
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        TextView navItem;
        public MyViewHolder(View itemView)
        {
            super(itemView);
//            navItem=(TextView)itemView.findViewById(R.id.navig_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            Intent intent=new Intent(context, LeagueActivity.class);
            int pos=getAdapterPosition();
            intent.putExtra(TOURNAMENT_NAME, data.get(pos).name);
            intent.putExtra(TOURNAMENT_ID, data.get(pos).tournamet_id);
            context.startActivity(intent);
        }
    }
}
