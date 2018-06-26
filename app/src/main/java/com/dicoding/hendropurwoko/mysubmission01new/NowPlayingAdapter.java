package com.dicoding.hendropurwoko.mysubmission01new;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class NowPlayingAdapter extends RecyclerView.Adapter<NowPlayingAdapter.ViewHolder> {
    Context c;
    private RecyclerViewClickListener mListener;
    public ArrayList<MovieClass> nowPlayingList;

    public NowPlayingAdapter(Context c) { this.c = c; }

    public void setNowPlayingList(ArrayList<MovieClass> list){
        this.nowPlayingList = list;
    }

    public ArrayList<MovieClass> getListData(){
        return nowPlayingList;
    }

    @Override
    public NowPlayingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movies_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NowPlayingAdapter.ViewHolder holder, int position) {
        final String title, overview, releaseDate, poster;
        final int pos = position;

        title = nowPlayingList.get(position).getTitle().toString().trim();
        overview = nowPlayingList.get(position).getOverview().toString().trim();
        releaseDate =  nowPlayingList.get(position).getReleaseDate().toString().trim();
        poster = nowPlayingList.get(position).getPoster().toString().trim();

        holder.tvTitle.setText(title);
        holder.tvOverview.setText(overview);
        holder.tvReleasedate.setText(releaseDate);

        Glide.with(c)
                .load(poster)
                .override(350, 350)
                .into(holder.ivPoster);

        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Intent detailIntent = new Intent(v.getContext(), DetailActivity.class);
                bundle.putString("title", nowPlayingList.get(pos).getTitle().toString().trim());
                bundle.putString("overview", nowPlayingList.get(pos).getOverview().toString().trim());
                bundle.putString("release_date", nowPlayingList.get(pos).getReleaseDate().toString().trim());
                bundle.putString("vote", nowPlayingList.get(pos).getVote().toString().trim());
                bundle.putString("popularity", nowPlayingList.get(pos).getPopularity().toString().trim());
                bundle.putString("poster", nowPlayingList.get(pos).getPoster().toString().trim());

                detailIntent.putExtras(bundle);
                v.getContext().startActivity(detailIntent);
            }
        });

        holder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent( Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, title);
                sharingIntent.putExtra(Intent.EXTRA_TEXT, overview);

                v.getContext().startActivity(sharingIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return nowPlayingList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle, tvOverview, tvReleasedate;
        Button btnDetail, btnShare;
        ImageView ivPoster;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView)itemView.findViewById(R.id.tv_title_now_playing);
            tvOverview = (TextView)itemView.findViewById(R.id.tv_overview_now_playing);
            tvReleasedate = (TextView)itemView.findViewById(R.id.tv_release_date_now_playing);
            ivPoster = (ImageView)itemView.findViewById(R.id.iv_poster_now_playing);
            btnDetail = (Button)itemView.findViewById(R.id.bt_detail_now_playing);
            btnShare = (Button)itemView.findViewById(R.id.bt_share_now_playing);
        }
    }
}