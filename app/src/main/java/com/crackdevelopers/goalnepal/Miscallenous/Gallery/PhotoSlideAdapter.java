package com.crackdevelopers.goalnepal.Miscallenous.Gallery;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.crackdevelopers.goalnepal.R;
import com.crackdevelopers.goalnepal.Volley.VolleySingleton;

import java.util.List;


/**
 * Created by script on 9/9/15.
 */


public class PhotoSlideAdapter extends PagerAdapter
{
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<String> imageUrls=PhotosActivity.allUrls;

    public PhotoSlideAdapter(Context context)
    {
        this.mContext=context;
        mLayoutInflater=LayoutInflater.from(context);
    }


    @Override
    public int getCount()
    {
        return imageUrls.size();
    }



    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view == object;
    }




    @Override
    public Object instantiateItem(ViewGroup container,  int position)
    {
        View itemView = mLayoutInflater.inflate(R.layout.photo_pager_item, container, false);

       String imageUrl=imageUrls.get(position);


        final ImageView imageView = (ImageView) itemView.findViewById(R.id.slide_pager_image);

        imageView.setImageResource(R.drawable.wait);
        if(imageUrl!= null)
        {
            ImageLoader imageLoader=VolleySingleton.getInstance().getmImageLoader();
            imageLoader.get(imageUrl, new ImageLoader.ImageListener()
            {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate)
                {

                    imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    imageView.setImageBitmap(response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError error)
                {
                    imageView.setImageResource(R.drawable.wait);
                }
            });
        }

        container.addView(itemView);
        return itemView;
    }



    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        container.removeView((LinearLayout) object);
    }

}
