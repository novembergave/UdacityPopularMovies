package com.novembergave.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.novembergave.popularmovies.NetworkUtils.FetchReviewsAsyncTask;
import com.novembergave.popularmovies.NetworkUtils.FetchTrailersAsyncTask;
import com.novembergave.popularmovies.POJO.Movie;
import com.novembergave.popularmovies.POJO.Review;
import com.novembergave.popularmovies.POJO.Trailer;
import com.squareup.picasso.Picasso;

import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;

import java.util.List;

import static com.novembergave.popularmovies.NetworkUtils.UrlUtils.isNetworkAvailable;

public class DetailActivity extends AppCompatActivity {

  private static final String EXTRA_MOVIE = "extra_movie";

  private Toolbar toolbar;
  private CollapsingToolbarLayout layout;
  private ImageView displayImage;
  private TextView overViewText;
  private TextView date;
  private RatingBar ratingBar;

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
    overViewText = findViewById(R.id.detail_overview);
    date = findViewById(R.id.detail_date);
    ratingBar = findViewById(R.id.detail_rating);

    setUpView();
  }

  private void setUpView() {
    Movie movie = getIntent().getParcelableExtra(EXTRA_MOVIE);
    // set title
    layout.setTitle(movie.getTitle());
    // populate the image in both app bar and poster image
    Picasso.with(this).load("http://image.tmdb.org/t/p/w185/" + movie.getPosterPath()).into(displayImage);
    // populate the overview
    overViewText.setText(movie.getOverview());
    String releaseDate = movie.getReleaseDate();
    if (releaseDate != null && !releaseDate.isEmpty()) {
      // format the release date by first splitting the string at the dash
      String[] strings = releaseDate.split("-");
      // we always expect strings array to contain 3 items - year, month, day
      if (strings.length == 3) {
        // format to localised date string
        ZonedDateTime dateTime = ZonedDateTime.of(Integer.valueOf(strings[0]),
            Integer.valueOf(strings[1]), Integer.valueOf(strings[2]), 1, 1, 1, 1, ZoneId.systemDefault());
        int flags = DateUtils.FORMAT_SHOW_YEAR;
        String displayDate = DateUtils.formatDateTime(this, dateTime.toInstant().toEpochMilli(), flags);
        date.setText(displayDate);
      }
    }
    // if nothing has been set fallback without splitting or formatting
    if (date.getText().toString().isEmpty()) {
      date.setText(releaseDate);
    }

    // display the rating - returns on the scale of 10, so we'll need to divide to show scale of 5
    float averageVote = (float) movie.getAverageVote() / 2;
    ratingBar.setRating(averageVote);

    fetchVideoAndReview(movie.getId());
  }

  private void fetchVideoAndReview(long id) {
    if (isNetworkAvailable(this)) {
      // Listener for when AsyncTask is ready to update UI
      FetchReviewsAsyncTask.OnTaskCompleted taskCompleted = new FetchReviewsAsyncTask.OnTaskCompleted() {
        @Override
        public void onFetchReviewsTaskCompleted(List<Review> reviews) {
          Toast.makeText(getBaseContext(), "Reviews" + reviews.toString(), Toast.LENGTH_SHORT).show();
        }
      };

      // Execute task
      FetchReviewsAsyncTask movieTask = new FetchReviewsAsyncTask(taskCompleted);
      movieTask.execute(id);

      // Listener for when AsyncTask is ready to update UI
      FetchTrailersAsyncTask.OnTaskCompleted trailerCompleted = new FetchTrailersAsyncTask.OnTaskCompleted() {
        @Override
        public void onFetchTrailersTaskCompleted(List<Trailer> trailers) {
          Toast.makeText(getBaseContext(), "Trailers" + trailers.toString(), Toast.LENGTH_SHORT).show();
        }
      };

      // Execute task
      FetchTrailersAsyncTask trailerTask = new FetchTrailersAsyncTask(trailerCompleted);
      trailerTask.execute(id);
    }
  }

}