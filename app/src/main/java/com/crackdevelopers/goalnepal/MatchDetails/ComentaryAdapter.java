package com.crackdevelopers.goalnepal.MatchDetails;

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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trees on 8/31/15.
 */
public class ComentaryAdapter extends RecyclerView.Adapter<ComentaryAdapter.MyViewHolder>
{
    private LayoutInflater inflater;
    private List<CommentaryItem> data;
    public ComentaryAdapter(Context context)
    {
        inflater=LayoutInflater.from(context);
        data=new ArrayList<>();

    }


    public void setData(List<CommentaryItem> data)
    {
        this.data=data;
        notifyItemRangeChanged(0, data.size());
    }
    @Override
    public ComentaryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new MyViewHolder(inflater.inflate(R.layout.match_commentary_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final ComentaryAdapter.MyViewHolder holder, int position)
    {
        ((TextView)holder.time).setText(data.get(position).time);
        ((TextView)holder.text).setText(data.get(position).text);

        ImageLoader imageLoader=VolleySingleton.getInstance().getmImageLoader();
        imageLoader.get(data.get(position).icon_url, new ImageLoader.ImageListener()
        {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate)
            {
                ((ImageView)holder.icon).setImageBitmap(response.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError error)
            {
                ((ImageView)holder.icon).setImageResource(R.drawable.chelse);
            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView time, text;
        ImageView icon;
        public MyViewHolder(View itemView)
        {
            super(itemView);
            time=(TextView)itemView.findViewById(R.id.commentary_time);
            text=(TextView)itemView.findViewById(R.id.commentary_text);
            icon=(ImageView)itemView.findViewById(R.id.commentary_image);
        }
    }
}
