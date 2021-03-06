package com.crackdevelopers.goalnepal.News;

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
import com.crackdevelopers.goalnepal.Miscallenous.Preferences.PreferenceAdapter;
import com.crackdevelopers.goalnepal.R;
import com.crackdevelopers.goalnepal.Volley.VolleySingleton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by trees on 9/17/15.
 */
public class NewsSimpleAdapter extends RecyclerView.Adapter<NewsSimpleAdapter.MyViewHolder>
{
    private LayoutInflater inflater;
    private Context context;
    private List<NewsSingleRow> data;
    private ImageLoader mImageLoader;

    public NewsSimpleAdapter(Context context)
    {
        inflater=LayoutInflater.from(context);
        this.context=context;
        data=new ArrayList<>();
        mImageLoader= VolleySingleton.getInstance().getmImageLoader();

    }
    public void setData(List<NewsSingleRow> data)
    {
        this.data=data;
        notifyItemRangeChanged(0, data.size());
    }

    public void setScrollUpdate(List<NewsSingleRow> scrollData)
    {
        int previousSize=data.size();
        for(NewsSingleRow scrollItem:scrollData)
        {
            data.add(scrollItem);
        }
        notifyItemRangeInserted(previousSize,scrollData.size());
    }

    @Override
    public NewsSimpleAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        return new MyViewHolder(inflater.inflate(R.layout.news_row, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(final NewsSimpleAdapter.MyViewHolder viewHolder, int i)
    {

        viewHolder.title.setText(data.get(i).subtitle);
        viewHolder.details.setText(data.get(i).title);
        viewHolder.image.setImageResource(R.drawable.goalnepal_white);

        //IMAGE BINDING USING VOLLEY IMAGE LOADER
        String imageUrl=data.get(i).imageUrl;
        if(imageUrl!=null)
        {
            mImageLoader.get(imageUrl,

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

    class  MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
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
            try {
                Intent intent=new Intent(context, NewsDetailsActivity.class);
                Bundle bundle=new Bundle();
                NewsSingleRow row=data.get(getAdapterPosition());
                bundle.putString(NewsDetailsActivity.IMAGE_ID, row.imageId);
                bundle.putString(NewsDetailsActivity.NEWS_DATE, row.date);
                bundle.putString(NewsDetailsActivity.NEWS_TITLE, row.subtitle);
                bundle.putString(NewsDetailsActivity.NEWS, row.news);

                intent.putExtra(NewsDetailsActivity.BUNDLE, bundle);
                context.startActivity(intent);

                PreferenceAdapter.hasPrefChanged=false;   ///THIS IS TO PREVENT FROM RELOADING PREFERNCES NEWS ON ON START at news fragment
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
