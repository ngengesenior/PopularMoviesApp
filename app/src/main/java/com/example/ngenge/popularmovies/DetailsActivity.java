package com.example.ngenge.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ngenge.popularmovies.models.Movie;
import com.example.ngenge.popularmovies.utils.Utils;
import com.squareup.picasso.Picasso;


public class DetailsActivity extends AppCompatActivity {


    TextView textViewVoteAverage;


    TextView releasedDate;


    ImageView moviePoster;


    TextView textViewOverview;

    TextView movieName;
    private TextView voteCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details);
        textViewVoteAverage = findViewById(R.id.textViewVoteAverage);
        releasedDate = findViewById(R.id.textViewReleaseDate);
        movieName = findViewById(R.id.movieName);
        textViewOverview = findViewById(R.id.textViewOverview);
        moviePoster = findViewById(R.id.imageView);
        voteCount = findViewById(R.id.textViewVoteCount);


        //Tried using butterknife but objects are returned as null
        //ButterKnife.bind(this);

        Movie movie =  getIntent().getParcelableExtra(Utils.movieKey);
        Picasso.get()
                .load(movie.getPoster_path())
        .into(moviePoster);
        textViewOverview.setText(movie.getOverview());
        releasedDate.setText(movie.getRelease_date());
        textViewVoteAverage.setText(String.format("%s%%", String.valueOf(movie.getVote_average() * 10)));
        movieName.setText(movie.getTitle());
        voteCount.setText(String.valueOf(movie.getVote_count()));
    }
}
