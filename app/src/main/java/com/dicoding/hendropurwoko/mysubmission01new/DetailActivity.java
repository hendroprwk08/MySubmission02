package com.dicoding.hendropurwoko.mysubmission01new;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {
    TextView tvTitle, tvOverview, tvVote, tvReleaseDate, tvPopularity;
    ImageView ivPoster;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Context c = getApplicationContext();
        String title, overview, releaseDate, popularity, vote, poster;

        tvTitle = (TextView)findViewById(R.id.tv_title_detail);
        tvOverview = (TextView)findViewById(R.id.tv_overview_detail);
        tvVote = (TextView)findViewById(R.id.tv_vote_detail);
        tvReleaseDate = (TextView)findViewById(R.id.tv_date_detail);
        tvPopularity = (TextView)findViewById(R.id.tv_popularity_detail);
        ivPoster = (ImageView)findViewById(R.id.iv_poster_detail);

        bundle = new Bundle();
        bundle = getIntent().getExtras();

        title = bundle.getString("title").toString();
        overview = bundle.getString("overview").toString();
        releaseDate = bundle.getString("release_date").toString();
        vote = bundle.getString("vote").toString();
        popularity = bundle.getString("popularity").toString();
        poster = bundle.getString("poster").toString();

        tvTitle.setText(title);
        tvOverview.setText(overview);
        tvReleaseDate.setText("RELEASE: "+releaseDate);
        tvVote.setText("VOTE: "+vote);
        tvPopularity.setText("RATE: "+popularity);

        Glide.with(c)
                .load(poster)
                .override(350, 350)
                .into(ivPoster);
    }
}