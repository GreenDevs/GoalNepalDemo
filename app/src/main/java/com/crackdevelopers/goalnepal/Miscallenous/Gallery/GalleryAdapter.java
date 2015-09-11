package com.crackdevelopers.goalnepal.Miscallenous.Gallery;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.crackdevelopers.goalnepal.R;
import com.crackdevelopers.goalnepal.Volley.VolleySingleton;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by trees on 8/28/15.
 */
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder>
        implements StickyRecyclerHeadersAdapter<GalleryAdapter.StickyHolder>
{

    private ArrayList<String> stickyData=new ArrayList<>();
    private ArrayList<GalleryItem> data=new ArrayList<>();
    private LayoutInflater inflater;
    private ImageLoader mImageLoader;
    private Context context;

    public GalleryAdapter(Context context)
    {
        this.context=context;
        inflater=LayoutInflater.from(context);
        mImageLoader=VolleySingleton.getInstance().getmImageLoader();

    }

    public void setGalleryData(ArrayList<GalleryItem> galleries)
    {
        stickyData=((GalleryActivity)context).getStickyList();
        this.data=galleries;
        notifyItemRangeChanged(0, galleries.size());
    }

    @Override
    public GalleryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new MyViewHolder(inflater.inflate(R.layout.gallery_single_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final GalleryAdapter.MyViewHolder holder, int position)
    {
        String imageUrl=data.get(position).imageUrl;
        if(imageUrl!=null)
        {
            mImageLoader.get(imageUrl,

                    new ImageLoader.ImageListener()
                    {
                        @Override
                        public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate)
                        {
                            holder.image.setImageBitmap(response.getBitmap());
                        }

                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            holder.image.setImageResource(R.mipmap.ic_launcher);
                        }
                    }
            );
        }
        else
        {
            holder.image.setImageResource(R.mipmap.ic_launcher);
        }
    }

    @Override
    public int getItemCount()
    {

        return data.size();
    }

    class MyViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ImageView image;
        public MyViewHolder(View itemView)
        {
            super(itemView);
            itemView.setOnClickListener(this);
            image=(ImageView)itemView.findViewById(R.id.single_image);
        }

        @Override
        public void onClick(View v)
        {
            Toast.makeText(context, data.get(getPosition()).venu,Toast.LENGTH_SHORT).show();
        }
    }



    ///STICKY PART

    @Override
    public long getHeaderId(int position)
    {
        if (position == -1)
        {
            Log.i("ID","ID"+stickyData.get(position).charAt(0));
            return -1;
        }
        else
        {
            Log.i("ID","ID"+stickyData.get(position).charAt(0));
            return stickyData.get(position).charAt(0);
        }
    }


    @Override
    public GalleryAdapter.StickyHolder onCreateHeaderViewHolder(ViewGroup viewGroup)
    {
        return new StickyHolder(inflater.inflate(R.layout.sticky_layout, viewGroup, false));
    }

    @Override
    public void onBindHeaderViewHolder(GalleryAdapter.StickyHolder holder, int position)
    {
        holder.name.setText(stickyData.get(position));
        holder.name.setBackgroundColor(getRandomColor());
    }

    private void setData()
    {
        stickyData.add("L");
        stickyData.add("L");
        stickyData.add("B");
        stickyData.add("E");
        stickyData.add("S");
        stickyData.add("S");
        stickyData.add("S");
        stickyData.add("W");
        stickyData.add("C");
        stickyData.add("C");

        stickyData.add("L");
        stickyData.add("L");
        stickyData.add("B");
        stickyData.add("E");
        stickyData.add("S");
        stickyData.add("S");
        stickyData.add("S");
        stickyData.add("W");
        stickyData.add("C");
        stickyData.add("C");

        stickyData.add("L");
        stickyData.add("L");
        stickyData.add("B");
        stickyData.add("E");
        stickyData.add("S");
        stickyData.add("S");
        stickyData.add("S");
        stickyData.add("W");
        stickyData.add("C");
        stickyData.add("C");

        stickyData.add("L");
        stickyData.add("L");
        stickyData.add("B");

    }

    private int getRandomColor()
    {
        SecureRandom rgen = new SecureRandom();
        return Color.HSVToColor(150, new float[]
                {
                        rgen.nextInt(359), 1, 1
                });
    }


    public class StickyHolder extends RecyclerView.ViewHolder
    {
        TextView name;
        public StickyHolder(View itemView)
        {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.stickyText);

        }

    }
}
