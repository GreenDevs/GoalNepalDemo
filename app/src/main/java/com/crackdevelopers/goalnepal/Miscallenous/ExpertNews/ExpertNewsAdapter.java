package com.crackdevelopers.goalnepal.Miscallenous.ExpertNews;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.crackdevelopers.goalnepal.News.NewsDetailsActivity;
import com.crackdevelopers.goalnepal.R;
import com.crackdevelopers.goalnepal.Volley.VolleySingleton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by script on 9/12/15.
 */
public class ExpertNewsAdapter extends RecyclerView.Adapter<ExpertNewsAdapter.MyViewHolder>
{
    private LayoutInflater inflater;
    private Context context;
    private List<NewsSingleRow> data= Collections.emptyList();

    public ExpertNewsAdapter(Context context)
    {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setData(List<NewsSingleRow> data)
    {
        this.data=data;
        notifyItemRangeChanged(0, data.size());
    }

    public void setScrollUpdate(List scrollData)
    {
        int previousSize=data.size();
        for(Object scrollItem:scrollData)
        {
            data.add((NewsSingleRow)scrollItem);
        }
        notifyItemRangeInserted(previousSize, scrollData.size());

    }

    @Override
    public ExpertNewsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new MyViewHolder(inflater.inflate(R.layout.news_row, parent, false));
    }

    @Override
    public void onBindViewHolder(final ExpertNewsAdapter.MyViewHolder viewHolder, int i)
    {

            NewsSingleRow item=data.get(i);
            viewHolder.title.setText(item.subtitle);
            viewHolder.details.setText(item.title);
            viewHolder.image.setImageResource(R.drawable.goalnepal_white);

            ImageLoader imageLoader= VolleySingleton.getInstance().getmImageLoader();

            //IMAGE BINDING USING VOLLEY IMAGE LOADER
            String imageUrl=item.imageUrl;
            if(imageUrl!=null)
            {
                imageLoader.get(imageUrl,

                        new ImageLoader.ImageListener()
                        {
                            @Override
                            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate)
                            {
                                 viewHolder.image.setImageBitmap(response.getBitmap());
                            }

                            @Override
                            public void onErrorResponse(VolleyError error)
                            {
                                viewHolder.image.setImageResource(R.drawable.goalnepal_white);
                            }
                        });
            }
            else
            {
                viewHolder.image.setImageResource(R.drawable.goalnepal_white);
            }

        // Setting date format
        SimpleDateFormat inputFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat outputFormat=new SimpleDateFormat("MMM dd");
        Date date=new Date();
        try{
            date = inputFormat.parse(data.get(i).date);

        }catch(ParseException e){
            Log.d("Exception", "Invalid Date Format");
        }
        viewHolder.date.setText(outputFormat.format(date));

    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        TextView title, details,date;
        ImageView image;
        public MyViewHolder(View itemView)
        {
            super(itemView);
            itemView.setOnClickListener(this);
            title=(TextView)itemView.findViewById(R.id.newsTitle);
            details=(TextView)itemView.findViewById(R.id.newsDetails);
            image=(ImageView)itemView.findViewById(R.id.newsImage);
            date= (TextView) itemView.findViewById(R.id.newsDate);

        }

        @Override
        public void onClick(View v)
        {
            Intent intent=new Intent(context, NewsDetailsActivity.class);
            Bundle bundle=new Bundle();
            NewsSingleRow row=data.get(getAdapterPosition());
            bundle.putString(NewsDetailsActivity.IMAGE_ID, row.imageId);
            bundle.putString(NewsDetailsActivity.NEWS_DATE, row.date);
            bundle.putString(NewsDetailsActivity.NEWS_TITLE, row.subtitle);
            bundle.putString(NewsDetailsActivity.NEWS, row.news);

            intent.putExtra(NewsDetailsActivity.BUNDLE, bundle);
            context.startActivity(intent);

        }
    }
}
