package com.crackdevelopers.goalnepal.Miscallenous.Preferences;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crackdevelopers.goalnepal.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by trees on 9/9/15.
 */
public class PreferenceAdapter extends RecyclerView.Adapter<PreferenceAdapter.MyViewHolder>
{
    private List<PreferenceRow> data=Collections.emptyList();

    private Context context;
    public PreferenceAdapter(Context context)
    {
            this.context=context;
    }

    public void updateMenu(List<PreferenceRow> data)
    {
        this.data=data;
        notifyItemRangeChanged(0, data.size());
    }

    @Override
    public PreferenceAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater=LayoutInflater.from(context);
        return  new MyViewHolder(inflater.inflate(R.layout.preference_item, parent, false));

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(PreferenceAdapter.MyViewHolder holder, int position)
    {
        PreferenceRow item=data.get(position);
        holder.header_title.setText(item.name);
        
        if(item.checked)
        {
            holder.check_button.setBackground(context.getResources().getDrawable(R.drawable.check_circle));
        }
        else 
        {
            holder.check_button.setBackground(context.getResources().getDrawable(R.drawable.uncheck_circle));
        }

        Typeface typeface=Typeface.createFromAsset(context.getAssets(), "icomoon.ttf");
        holder.icon.setTypeface(typeface);
        holder.icon.setText(context.getResources().getString(R.string.leagues));
        
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        TextView header_title, icon;
        View check_button, checkParent;


        public MyViewHolder(View itemView)
        {
            super(itemView);
            header_title = (TextView) itemView.findViewById(R.id.header_title);
            check_button =  itemView.findViewById(R.id.check_button);
            icon=(TextView)itemView.findViewById(R.id.pref_item_icon);
            checkParent=itemView.findViewById(R.id.check_button_parent);
            checkParent.setOnClickListener(this);
//            check_button.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            int position=getAdapterPosition();
            PreferenceRow item=data.get(position);
            SharedPreferences preferences=context.getSharedPreferences(PreferenceActivity.SHARED_PREF, Context.MODE_PRIVATE);
            preferences.edit().putBoolean(item.id+"",!item.checked).apply();
            item.checked=!item.checked;
            notifyItemChanged(position);
        }
    }
}
