package com.crackdevelopers.goalnepal.Miscallenous.Gallery;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.crackdevelopers.goalnepal.R;
import com.crackdevelopers.goalnepal.Volley.VolleySingleton;

import java.util.Collections;
import java.util.List;

/**
 * Created by trees on 9/11/15.
 */
public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.MyViewHolder>
{
    private Context context;
    private LayoutInflater inflater;
    private List<PhotosItem> data= Collections.emptyList();
    private int position;

    public PhotosAdapter(Context context)
    {
        this.context=context;
        inflater=LayoutInflater.from(context);
    }

    public void setData(List<PhotosItem> data)
    {
        this.data=data;
        notifyItemRangeChanged(0, data.size());
    }
    @Override
    public PhotosAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new MyViewHolder(inflater.inflate(R.layout.photos_row, parent, false));
    }

    @Override
    public void onBindViewHolder(final PhotosAdapter.MyViewHolder holder, int position)
    {
        PhotosItem item=data.get(position);
        ImageLoader loader= VolleySingleton.getInstance().getmImageLoader();


        loader.get(item.thumnailUrl, new ImageLoader.ImageListener()
        {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate)
            {
                holder.thumnail.setImageBitmap(response.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError error)
            {
                holder.thumnail.setImageResource(R.drawable.goalnepal_white);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }




    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        //TextView title;
        ImageView thumnail;

        public MyViewHolder(View itemView)
        {
            super(itemView);

            itemView.setOnClickListener(this);
           // title = (TextView) itemView.findViewById(R.id.album_item_title);
            thumnail = (ImageView) itemView.findViewById(R.id.album_item_image);

        }


        @Override
        public void onClick(View v)
        {

            Intent intent =new Intent(context,PhotosSlideActivity.class);
            intent.putExtra("ImagePosition",getAdapterPosition());
            context.startActivity(intent);

        }
    }


}