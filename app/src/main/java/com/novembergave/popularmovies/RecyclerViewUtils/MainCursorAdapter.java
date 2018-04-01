package com.novembergave.popularmovies.RecyclerViewUtils;


import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.novembergave.popularmovies.POJO.Movie;

import static com.novembergave.popularmovies.Database.MovieDbHelper.INDEX_AVERAGE_VOTE;
import static com.novembergave.popularmovies.Database.MovieDbHelper.INDEX_MOVIE_ID;
import static com.novembergave.popularmovies.Database.MovieDbHelper.INDEX_OVERVIEW;
import static com.novembergave.popularmovies.Database.MovieDbHelper.INDEX_POSTER_PATH;
import static com.novembergave.popularmovies.Database.MovieDbHelper.INDEX_RELEASE_DATE;
import static com.novembergave.popularmovies.Database.MovieDbHelper.INDEX_TITLE;
import static com.novembergave.popularmovies.RecyclerViewUtils.MainAdapter.MovieClickListener;

public class MainCursorAdapter extends RecyclerView.Adapter<MainViewHolder> {

  private MovieClickListener listener;
  private Cursor cursor;

  public MainCursorAdapter(MovieClickListener listener) {
    this.listener = listener;
  }

  @Override
  public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return MainViewHolder.inflateItemViewFrom(parent);
  }

  @Override
  public void onBindViewHolder(MainViewHolder holder, int position) {
    holder.bindTo(getMovieFromDb(position), listener);
  }

  @Override
  public int getItemCount() {
    return cursor == null ? 0 : cursor.getCount();
  }

  public void swapCursor(Cursor newCursor) {
    cursor = newCursor;
    notifyDataSetChanged();
  }

  private Movie getMovieFromDb(int position) {
    // extract details from db
    cursor.moveToPosition(position);
    long movieId = cursor.getLong(INDEX_MOVIE_ID);
    String title = cursor.getString(INDEX_TITLE);
    String posterPath = cursor.getString(INDEX_POSTER_PATH);
    String overview = cursor.getString(INDEX_OVERVIEW);
    double averageVote = cursor.getDouble(INDEX_AVERAGE_VOTE);
    String releaseDate = cursor.getString(INDEX_RELEASE_DATE);
    // create a new Movie object and return this object
    Movie movie = new Movie();
    movie.setId(movieId);
    movie.setTitle(title);
    movie.setPosterPath(posterPath);
    movie.setOverview(overview);
    movie.setAverageVote(averageVote);
    movie.setReleaseDate(releaseDate);
    return movie;
  }
}
