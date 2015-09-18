package com.crackdevelopers.goalnepal.Miscallenous.video.gallery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.crackdevelopers.goalnepal.News.NewsDetailsActivity;
import com.crackdevelopers.goalnepal.News.NewsSingleRow;
import com.crackdevelopers.goalnepal.R;
import com.crackdevelopers.goalnepal.Volley.VolleySingleton;
import com.google.android.youtube.player.YouTubeStandalonePlayer;

import java.util.Collections;
import java.util.List;

/**
 * Created by trees on 9/17/15.
 */
public class VideoGalAdapter extends RecyclerView.Adapter<VideoGalAdapter.MyViewHolder>
{
    private List<VideoItem> data= Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;
    public VideoGalAdapter(Context context)
    {
        this.context=context;
        inflater=LayoutInflater.from(context);
    }

    public void setScrollUpdate(List<VideoItem> scrollData)
    {
        int previousSize=data.size();
        for(VideoItem scrollItem:scrollData)
        {
            data.add(scrollItem);
        }
        notifyItemRangeInserted(previousSize,scrollData.size());
    }

    public void setData(List<VideoItem> data)
    {
        this.data=data;
        notifyItemRangeChanged(0, data.size());
    }
    @Override
    public VideoGalAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new MyViewHolder(inflater.inflate(R.layout.video_gall_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final VideoGalAdapter.MyViewHolder holder, int position)
    {
        VideoItem item=data.get(position);
        holder.vTtitle.setText(item.vtitle);
        holder.vPic.setImageResource(R.drawable.goalnepal_white);

        ImageLoader imageLoader= VolleySingleton.getInstance().getmImageLoader();

        imageLoader.get(item.vPic, new ImageLoader.ImageListener()
        {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate)
            {

                holder.vPic.setImageBitmap(response.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError error)
            {
                holder.vPic.setImageResource(R.drawable.goalnepal_white);
            }



        });
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener
    {
        ImageView vPic;
        TextView vTtitle;
        public MyViewHolder(View itemView)
        {
            super(itemView);
            vPic=(ImageView)itemView.findViewById(R.id.video_pic);
            vTtitle=(TextView)itemView.findViewById(R.id.video_title);
        }


        @Override
        public void onClick(View v)
        {

            Bundle bundle=new Bundle();
            VideoItem row=data.get(getAdapterPosition());



            ///  calling youtube intenet starts here
            // everything starts here
            String videoLink = row.vlink;
            String [] temp = videoLink.split("/embed/");
            String id = temp[1];
            Toast.makeText(context , "itemClicked" , Toast.LENGTH_SHORT).show();

            Intent intent = YouTubeStandalonePlayer.createVideoIntent((VideoGallery)context, "AIzaSyD2EawPYwjp7i_2Bt4APkcONoK0mQyRAgI", id);



            context.startActivity(intent);

            // eth ends here
            // calling youtube intent ends here

        }
    }
}
