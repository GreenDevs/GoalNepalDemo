package com.crackdevelopers.goalnepal.News;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.crackdevelopers.goalnepal.R;
import com.crackdevelopers.goalnepal.Volley.VolleySingleton;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class NewsDetailsActivity extends AppCompatActivity
{

    public static final String IMAGE_PATH="http://www.goalnepal.com/graphics/article/";
    public static final String BUNDLE="bundlle";
    public static final String IMAGE_ID="image_id";
    public static final String NEWS_DATE="news_date";
    public static final String NEWS_TITLE="news_title";
    public static final String NEWS="news";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        init();
        displayads();
    }

    private void init()
    {
        Toolbar toolbar=(Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final ImageView newsImage=(ImageView)findViewById(R.id.news_image);

        TextView date=(TextView)findViewById(R.id.news_date);
        TextView title=(TextView)findViewById(R.id.news_title);
        TextView news=(TextView)findViewById(R.id.news_text);


        Bundle bundle=getIntent().getBundleExtra(BUNDLE);

        if(bundle!=null)
        {
            String imageUrl=IMAGE_PATH+bundle.getString(IMAGE_ID, "");
            String datee=bundle.getString(NEWS_DATE, "");
            String titlee=bundle.getString(NEWS_TITLE, "");
            String newsText=bundle.getString(NEWS);
            assert newsText != null;
            newsText=newsText.replace("&quot;","\"").replace("&nbsp;"," ").replace("&amp;","&").replace("&ndash;","-").replace("&mdash;","-").replace("&rsquo;","'").replace("&ldquo;","\"").replace("&rdquo;","\"").replace("&lsquo;","'");


            ImageLoader imageLoader= VolleySingleton.getInstance().getmImageLoader();
            date.setText(datee);
            title.setText(titlee);
            news.setText(newsText);


            imageLoader.get(imageUrl, new ImageLoader.ImageListener()
            {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate)
                {
                    newsImage.setImageBitmap(response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError error)
                {
                    newsImage.setImageResource(R.drawable.goalnepal_white);
                }
            });

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home)
        {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {

      super.onBackPressed();
    }

    private  void displayads() {
        AdView mAdView = (AdView) findViewById(R.id.adViewNewsDetails);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("DF748C37109613E8C305043552A7F153").build();
        mAdView.loadAd(adRequest);
    }


}
