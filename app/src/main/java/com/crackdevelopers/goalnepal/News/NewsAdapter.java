package com.crackdevelopers.goalnepal.News;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.crackdevelopers.goalnepal.MainActivity;
import com.crackdevelopers.goalnepal.R;
import com.crackdevelopers.goalnepal.Volley.VolleySingleton;
import com.poliveira.parallaxrecyclerview.ParallaxRecyclerAdapter;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by trees on 8/31/15.
 */
public class NewsAdapter extends ParallaxRecyclerAdapter
{
    private LayoutInflater inflater;
    private Context context;
    Date date;
    private List<NewsSingleRow> data= Collections.emptyList();
    private ImageLoader mImageLoader;
    public NewsAdapter(List<NewsSingleRow> data, Context context)
    {
        super(data);
        inflater=LayoutInflater.from(context);
        this.context=context;
        mImageLoader= VolleySingleton.getInstance().getmImageLoader();

    }


    @Override
    public void setData(List data)
    {
        super.setData(data);
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
        notifyItemRangeInserted(previousSize+1,scrollData.size());
    }

    @Override
    public void onBindViewHolderImpl(final RecyclerView.ViewHolder viewHolder, ParallaxRecyclerAdapter parallaxRecyclerAdapter, int i)
    {
        ((MyViewHolder)viewHolder).title.setText(data.get(i).subtitle);
        ((MyViewHolder)viewHolder).details.setText("Details");//data.get(i).title);
        ((MyViewHolder) viewHolder).image.setImageResource(R.drawable.goalnepal_white);
        ((MyViewHolder)viewHolder).date.setText(data.get(i).date);

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
                            ((MyViewHolder) viewHolder).image.setImageBitmap(response.getBitmap());
                        }

                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            ((MyViewHolder) viewHolder).image.setImageResource(R.drawable.goalnepal_white);
                        }
                    });
        }
        else
        {
            ((MyViewHolder) viewHolder).image.setImageResource(R.drawable.goalnepal_white);
        }


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolderImpl(ViewGroup viewGroup, ParallaxRecyclerAdapter parallaxRecyclerAdapter, int i)
    {
        return new MyViewHolder(inflater.inflate(R.layout.news_row, viewGroup, false));
    }

    @Override
    public int getItemCountImpl(ParallaxRecyclerAdapter parallaxRecyclerAdapter)
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
                NewsSingleRow row=data.get(getAdapterPosition()-1);
                bundle.putString(NewsDetailsActivity.IMAGE_ID, row.imageId);
                bundle.putString(NewsDetailsActivity.NEWS_DATE, row.date);
                bundle.putString(NewsDetailsActivity.NEWS_TITLE, row.subtitle);
                bundle.putString(NewsDetailsActivity.NEWS, row.news);

                intent.putExtra(NewsDetailsActivity.BUNDLE, bundle);
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }



}
