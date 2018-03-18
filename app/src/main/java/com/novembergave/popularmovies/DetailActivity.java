package com.novembergave.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.novembergave.popularmovies.POJO.Movie;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

  private static final String EXTRA_MOVIE = "extra_movie";

  private Toolbar toolbar;
  private CollapsingToolbarLayout layout;
  private ImageView displayImage;

  public static Intent launchDetailActivity(Context context, Movie movie) {
    Intent intent = new Intent(context, DetailActivity.class);
    intent.putExtra(EXTRA_MOVIE, movie);
    return intent;
  }

  @Override
  public boolean onSupportNavigateUp() {
    onBackPressed();
    return true;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);

    toolbar = findViewById(R.id.detail_toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    layout = findViewById(R.id.detail_collapsing_toolbar);
    displayImage = findViewById(R.id.detail_image);

    setUpView();
  }

  private void setUpView() {
    Movie movie = (Movie) getIntent().getSerializableExtra(EXTRA_MOVIE);
    layout.setTitle(movie.getTitle());
    Picasso.with(this).load("http://image.tmdb.org/t/p/w185/" + movie.getPosterPath()).into(displayImage);
  }

}