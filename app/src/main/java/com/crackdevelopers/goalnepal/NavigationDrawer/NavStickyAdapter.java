package com.crackdevelopers.goalnepal.NavigationDrawer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.crackdevelopers.goalnepal.Leagues.LeagueActivity;
import com.crackdevelopers.goalnepal.MainActivity;
import com.crackdevelopers.goalnepal.Miscallenous.Gallery.AlbumActivity;
import com.crackdevelopers.goalnepal.Miscallenous.ExpertNews.ExpertNewsActivity;
import com.crackdevelopers.goalnepal.Miscallenous.GuessWho.GuessWhoActivity;
import com.crackdevelopers.goalnepal.Miscallenous.Preferences.PreferenceActivity;
import com.crackdevelopers.goalnepal.Miscallenous.Preferences.PreferenceAdapter;
import com.crackdevelopers.goalnepal.Miscallenous.Radio.RadioActivity;
import com.crackdevelopers.goalnepal.Miscallenous.Ranking.RankingActivity;
import com.crackdevelopers.goalnepal.Miscallenous.video.gallery.VideoGallery;
import com.crackdevelopers.goalnepal.Miscallenous.liveTV.LiveTVActivity;
import com.crackdevelopers.goalnepal.R;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by trees on 9/14/15.
 */
public class NavStickyAdapter extends RecyclerView.Adapter<NavStickyAdapter.MyViewHolder>
        implements StickyRecyclerHeadersAdapter<NavStickyAdapter.StickyHolder> {

    private static final String TOURNAMENT_ID = "tournament_id";
    private static final String TOURNAMENT_NAME = "tournament_name";
    private LayoutInflater inflater;
    private Context context;
    private List<String> stickyData;
    private List<NavigationRow> data = Collections.emptyList();
    private DrawerLayout thisDrawerLayout  ;

    public NavStickyAdapter(Context context, List<NavigationRow> data ,DrawerLayout mDrawerLayout)
    {
        this.context = context;
        thisDrawerLayout=mDrawerLayout;
        inflater = LayoutInflater.from(context);
        this.data = data;

        stickyData = new ArrayList<>();
        for (NavigationRow item : data)
        {
            stickyData.add(item.stickyTitle);
        }
    }

    public void updateData(List<NavigationRow> data)
    {
        this.data = data;
        stickyData = new ArrayList<>();

        for (NavigationRow item : data) {
            stickyData.add(item.stickyTitle);
        }
        notifyItemRangeChanged(0, data.size());
    }

    @Override
    public NavStickyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(inflater.inflate(R.layout.navigation_drawer_item, parent, false));
    }

    @Override
    public void onBindViewHolder(NavStickyAdapter.MyViewHolder holder, int position) {
        NavigationRow item = data.get(position);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "icomoon.ttf");

        holder.icon.setTypeface(typeface);
        holder.icon.setText(item.icon);
        holder.icon.setTypeface(typeface);
        holder.name.setText(item.name);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    @Override
    public long getHeaderId(int position) {
        if (position == -1) {
            return -1;
        } else {
            Log.i("sticky data", stickyData.get(position));
            return stickyData.get(position).charAt(position);

        }
    }

    @Override
    public StickyHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
        return new StickyHolder(inflater.inflate(R.layout.sticky_layout, viewGroup, false));
    }

    @Override
    public void onBindHeaderViewHolder(StickyHolder holder, int position) {
        holder.name.setText(stickyData.get(position));
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView icon, name;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            icon = (TextView) itemView.findViewById(R.id.nav_item_icon);
            name = (TextView) itemView.findViewById(R.id.navig_item_text);

        }

        @Override
        public void onClick(View v) {

            try {
                ((LeagueActivity) context).finish();

            }catch (Exception e) {



            }
            thisDrawerLayout.closeDrawers();

            int pos = getAdapterPosition();
            switch (pos)
            {
                case 0:

                    if (!(context instanceof MainActivity)) {
                        try {
                            NavUtils.navigateUpFromSameTask((LeagueActivity) context);

                        } catch (Exception e) {
                            context.startActivity(new Intent(context, MainActivity.class));


                        }
                       //
                    }
                    break;

                case 1:
                    context.startActivity(new Intent(context, RadioActivity.class));
                    break;

                case 2:
                    context.startActivity(new Intent(context, LiveTVActivity.class));
                    break;

                case 3:
                    context.startActivity(new Intent(context, VideoGallery.class));
                    break;

                case 4:

                    PreferenceAdapter.hasPrefChanged = false;

                    context.startActivity(new Intent(context, AlbumActivity.class));
                    break;

                case 5:
                    context.startActivity(new Intent(context, RankingActivity.class));
                    break;

                case 6:

                    context.startActivity(new Intent(context, ExpertNewsActivity.class));
                    break;

                case 7:

                    context.startActivity(new Intent(context, GuessWhoActivity.class));
                    break;

                case 8:

                    context.startActivity(new Intent(context, PreferenceActivity.class));
                    break;

                default:
                    Intent intent = new Intent(context, LeagueActivity.class);
                    intent.putExtra(TOURNAMENT_NAME, data.get(pos).name);
                    intent.putExtra(TOURNAMENT_ID, data.get(pos).tournamet_id);
                    context.startActivity(intent);

            }

//            icon.setTextColor(context.getResources().getColor(R.color.primaryColor));
//            name.setTextColor(context.getResources().getColor(R.color.primaryColor));

        }
    }


    class StickyHolder extends RecyclerView.ViewHolder {

        TextView name;

        public StickyHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.stickyText);

        }
    }

}
