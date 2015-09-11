package com.crackdevelopers.goalnepal.Miscallenous.Gallery;

/**
 * Created by trees on 9/1/15.
 */
//public class GalleryTestAdapter extends RecyclerView.Adapter<GalleryTestAdapter.MyViewHolder>
//        implements StickyRecyclerHeadersAdapter<GalleryTestAdapter.StickyHolder>
//{

//    private Context context;
//    private LayoutInflater inflater;
//    private List<MatchItem> data= Collections.emptyList();
//    private List<String> stickyData= Collections.emptyList();
//
//    public GalleryTestAdapter(Context context) {
//        inflater = LayoutInflater.from(context);
//        this.context = context;
//        data = new ArrayList<>();
//
//        setData();
//
//
//    }
//
//    @Override
//    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
//    {
//        return new MyViewHolder(inflater.inflate(R.layout.todays_single_league_row, parent, false));
//    }
//
//    @Override
//    public void onBindViewHolder(MyViewHolder holder, int position)
//    {
//        String name1=data.get(position).nameA;
//        String name2=data.get(position).team2;
//        String score=data.get(position).score;
//        int logo1=data.get(position).logo1;
//        int logo2=data.get(position).log2;
//
//        holder.name1.setText(name1);
//        holder.name2.setText(name2);
//        holder.score.setText(score);
//        holder.logo1.setImageResource(logo2);
//        holder.logo2.setImageResource(logo1);
//
//    }
//
//    @Override
//    public int getItemCount()
//    {
//        return data.size();
//    }
//
//    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
//    {
//
//        ImageView logo1, logo2;
//        TextView name1, name2, score;
//        public MyViewHolder(View itemView)
//        {
//            super(itemView);
//            itemView.setOnClickListener(this);
//            name1=(TextView)itemView.findViewById(R.id.today_single_row_team1_name);
//            name2=(TextView)itemView.findViewById(R.id.today_single_row_team2_name);
//            score=(TextView)itemView.findViewById(R.id.today_single_row_score);
//            logo1=(ImageView)itemView.findViewById(R.id.today_single_row_logo1);
//            logo2=(ImageView)itemView.findViewById(R.id.today_single_row_logo2);
//
//        }
//
//        @Override
//        public void onClick(View v)
//        {
//            Intent intent=new Intent(context, MatchActivity.class);
//            context.startActivity(intent);
//        }
//    }
//
////    //STICKY PART BELOW THIS
////
////    private boolean isHeader(int position)
////    {
////        if(position==0) return true;
////        if(position==4) return true;
////        if(position==7) return true;
////
////        return false;
////    }
//
//    @Override
//    public long getHeaderId(int position)
//    {
//        if (position == -1)
//        {
//
//            return -1;
//        }
//        else
//        {
//
//            return stickyData.get(position).charAt(0);
//        }
//    }
//
//
//    @Override
//    public GalleryTestAdapter.StickyHolder onCreateHeaderViewHolder(ViewGroup viewGroup)
//    {
//        return new StickyHolder(inflater.inflate(R.layout.sticky_layout, viewGroup, false));
//    }
//
//    @Override
//    public void onBindHeaderViewHolder(GalleryTestAdapter.StickyHolder holder, int position)
//    {
//        holder.name.setText(stickyData.get(position));
//        holder.name.setBackgroundColor(getRandomColor());
//    }
//
//    private void setData()
//    {
//        stickyData = new ArrayList<>();
//        stickyData.add("L");
//        stickyData.add("L");
//        stickyData.add("B");
//        stickyData.add("E");
//        stickyData.add("S");
//        stickyData.add("S");
//        stickyData.add("S");
//        stickyData.add("W");
//        stickyData.add("C");
//        stickyData.add("C");
//    }
//
//
//    private int getRandomColor()
//    {
//        SecureRandom rgen = new SecureRandom();
//        return Color.HSVToColor(150, new float[]
//                {
//                        rgen.nextInt(359), 1, 1
//                });
//    }
//
//
//    class StickyHolder extends RecyclerView.ViewHolder
//    {
//        TextView name;
//        public StickyHolder(View itemView) {
//            super(itemView);
//            name=(TextView)itemView.findViewById(R.id.stickyText);
//
//        }
//
//    }
//}
