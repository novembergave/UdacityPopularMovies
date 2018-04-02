package com.novembergave.popularmovies;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.novembergave.popularmovies.NetworkUtils.FetchReviewsAsyncTask;
import com.novembergave.popularmovies.NetworkUtils.FetchTrailersAsyncTask;
import com.novembergave.popularmovies.POJO.Movie;
import com.novembergave.popularmovies.POJO.Review;
import com.novembergave.popularmovies.POJO.Trailer;
import com.novembergave.popularmovies.RecyclerViewUtils.ReviewAdapter;
import com.novembergave.popularmovies.RecyclerViewUtils.TrailerAdapter;
import com.squareup.picasso.Picasso;

import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;

import java.util.List;

import static com.novembergave.popularmovies.Database.MovieContract.MovieEntry;
import static com.novembergave.popularmovies.NetworkUtils.UrlUtils.isNetworkAvailable;

public class DetailActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

  private static final String EXTRA_MOVIE = "extra_movie";
  private static final int PERCENTAGE_TO_SHOW_IMAGE = 20;

  private CollapsingToolbarLayout layout;
  private ImageView displayImage;
  private TextView overViewText;
  private TextView date;
  private RatingBar ratingBar;
  private View trailersHolder;
  private View reviewsHolder;
  private AppBarLayout appbar;
  private View fab;

  private int maxScrollSize;
  private boolean isImageHidden;
  private Movie movie;

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

    Toolbar toolbar = findViewById(R.id.detail_toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    appbar = findViewById(R.id.detail_app_bar);
    appbar.addOnOffsetChangedListener(this);

    fab = findViewById(R.id.detail_fab);
    fab.setOnClickListener(click -> saveToFavourites());
    layout = findViewById(R.id.detail_collapsing_toolbar);
    displayImage = findViewById(R.id.detail_image);
    overViewText = findViewById(R.id.detail_overview);
    date = findViewById(R.id.detail_date);
    ratingBar = findViewById(R.id.detail_rating);
    trailersHolder = findViewById(R.id.detail_trailer_holder);
    reviewsHolder = findViewById(R.id.detail_review_holder);

    // initialise the values
    movie = getIntent().getParcelableExtra(EXTRA_MOVIE);

    setUpFavouriteSelection();
    setUpView();
  }

  private void setUpView() {
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

  private void setUpFavouriteSelection() {
    fab.setSelected(isSavedInDb());
  }

  private void fetchVideoAndReview(long id) {
    if (isNetworkAvailable(this)) {
      // Listener for when FetchReviewsAsyncTask is ready to update UI
      FetchReviewsAsyncTask.OnTaskCompleted taskCompleted = this::setReviews;

      // Execute task
      FetchReviewsAsyncTask movieTask = new FetchReviewsAsyncTask(taskCompleted);
      movieTask.execute(id);

      // Listener for when FetchTrailersAsyncTask is ready to update UI
      FetchTrailersAsyncTask.OnTaskCompleted trailerCompleted = this::setTrailers;

      // Execute task
      FetchTrailersAsyncTask trailerTask = new FetchTrailersAsyncTask(trailerCompleted);
      trailerTask.execute(id);
    }
  }

  private void setReviews(List<Review> reviews) {
    if (reviews.size() > 0) {
      reviewsHolder.setVisibility(View.VISIBLE);
      Button showMoreButton = findViewById(R.id.detail_review_show_more_button);
      RecyclerView reviewsRecycler = findViewById(R.id.detail_review_recycler);
      reviewsRecycler.setLayoutManager(new LinearLayoutManager(this));
      ReviewAdapter adapter = new ReviewAdapter();
      reviewsRecycler.setAdapter(adapter);
      // Show only 3 reviews if there's more than 3 and show the show more button
      if (reviews.size() > 3) {
        adapter.setData(reviews.subList(0, 3));
        showMoreButton.setVisibility(View.VISIBLE);
        showMoreButton.setOnClickListener(click -> {
          // display the full list and hide the show more button
          adapter.setData(reviews);
          showMoreButton.setVisibility(View.GONE);
        });
      } else {
        adapter.setData(reviews);
      }
    }
  }

  private void setTrailers(List<Trailer> trailers) {
    if (trailers.size() > 0) {
      trailersHolder.setVisibility(View.VISIBLE);
      RecyclerView trailersRecycler = findViewById(R.id.detail_trailer_recycler);
      trailersRecycler.setLayoutManager(new LinearLayoutManager(this));
      TrailerAdapter adapter = new TrailerAdapter(this::openTrailer);
      trailersRecycler.setAdapter(adapter);
      adapter.setData(trailers);
    }
  }

  private void openTrailer(Trailer trailer) {
    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + trailer.getKey())));
  }

  @Override
  public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
    // Handles animation of the floating action button and when it shows in the menu
    if (maxScrollSize == 0)
      maxScrollSize = appBarLayout.getTotalScrollRange();

    int currentScrollPercentage = (Math.abs(verticalOffset)) * 100
        / maxScrollSize;

    if (currentScrollPercentage >= PERCENTAGE_TO_SHOW_IMAGE) {
      if (!isImageHidden) {
        isImageHidden = true;
        ViewCompat.animate(fab).scaleY(0).scaleX(0).start();
        invalidateOptionsMenu();
      }
    }

    if (currentScrollPercentage < PERCENTAGE_TO_SHOW_IMAGE) {
      if (isImageHidden) {
        isImageHidden = false;
        ViewCompat.animate(fab).scaleY(1).scaleX(1).start();
        invalidateOptionsMenu();
      }
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.favourite_menu, menu);
    return true;
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    // The heart is filled if the user adds item to favourites
    menu.findItem(R.id.favourite).setIcon(fab.isSelected() ?
        R.drawable.ic_favorite_24dp :
        R.drawable.ic_favorite_border_24dp);
    if (isImageHidden) {
      menu.findItem(R.id.favourite).setVisible(true);
    }
    return super.onPrepareOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.favourite:
        saveToFavourites();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private void saveToFavourites() {
    // Update UI to show it is saved
    fab.setSelected(!fab.isSelected());
    // Save to SQLite database if it's saved, else remove from DB
    if (!isSavedInDb()) {
      addToDb();
    } else {
      removeFromDb();
    }
    invalidateOptionsMenu();
  }

  private void addToDb() {
    ContentValues values = new ContentValues();
    values.put(MovieEntry.COLUMN_MOVIE_ID, movie.getId());
    values.put(MovieEntry.COLUMN_TITLE, movie.getTitle());
    values.put(MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
    values.put(MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
    values.put(MovieEntry.COLUMN_AVERAGE_VOTE, movie.getAverageVote());
    values.put(MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
    Uri newUri = getContentResolver().insert(MovieEntry.CONTENT_URI, values);
    if (newUri == null) {
      Toast.makeText(this, getString(R.string.error_inserting, movie.getTitle()), Toast.LENGTH_SHORT).show();
    } else {
      Toast.makeText(this, getString(R.string.added_to_favourites), Toast.LENGTH_SHORT).show();
    }
  }

  private void removeFromDb() {
    Uri uri = ContentUris.withAppendedId(MovieEntry.CONTENT_URI, movie.getId());
    int rowsDeleted = getContentResolver().delete(uri, null, null);
    if (rowsDeleted == 0) {
      Toast.makeText(this, getString(R.string.error_deleting, movie.getTitle()), Toast.LENGTH_SHORT).show();
    } else {
      Toast.makeText(this, getString(R.string.removed_from_favourites), Toast.LENGTH_SHORT).show();
    }
  }

  private boolean isSavedInDb() {
    Uri uri = ContentUris.withAppendedId(MovieEntry.CONTENT_URI, movie.getId());
    String[] projection = {
        MovieEntry.COLUMN_MOVIE_ID    // we only care about the movie id
    };
    Cursor query = getContentResolver().query(uri, projection, null, null, null);
    int count = 0;
    if (query != null) {
      count = query.getCount();
      query.close();
    }
    return count > 0;
  }
}