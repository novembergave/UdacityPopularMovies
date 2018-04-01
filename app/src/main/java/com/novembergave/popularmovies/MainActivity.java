package com.novembergave.popularmovies;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.novembergave.popularmovies.Database.MovieContract.MovieEntry;
import com.novembergave.popularmovies.NetworkUtils.FetchMovieAsyncTask;
import com.novembergave.popularmovies.POJO.Movie;
import com.novembergave.popularmovies.Preferences.PreferenceDialog;
import com.novembergave.popularmovies.Preferences.SharedPreferencesUtils;
import com.novembergave.popularmovies.RecyclerViewUtils.MainAdapter;
import com.novembergave.popularmovies.RecyclerViewUtils.MainCursorAdapter;

import java.util.List;

import static com.novembergave.popularmovies.NetworkUtils.UrlUtils.isNetworkAvailable;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

  private static final int SHOW_LOADING_VIEW = 0;
  private static final int SHOW_ERROR_VIEW = 1;
  private static final int SHOW_RESULT_VIEW = 2;

  private static final int ID_MOVIES_LOADER = 33;

  private RecyclerView recyclerView;
  private ProgressBar progressBar;
  private MainAdapter adapter;
  private View errorView;
  private MainCursorAdapter cursorAdapter;

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.sort_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle item selection
    switch (item.getItemId()) {
      case R.id.sort:
        openSettingsDialog();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private void openSettingsDialog() {
    PreferenceDialog dialog = new PreferenceDialog();
    dialog.setPreferenceListener(() -> getMovies(getSortMethod()));
    dialog.show(getFragmentManager(), "dialog");
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    progressBar = findViewById(R.id.main_progress_bar);
    errorView = findViewById(R.id.main_error_view);
    Button retryButton = findViewById(R.id.main_retry_button);
    retryButton.setOnClickListener(click -> getMovies(getSortMethod()));

    recyclerView = findViewById(R.id.main_recycler_view);
    recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    adapter = new MainAdapter(this::openDetailView);
    recyclerView.setAdapter(adapter);

    cursorAdapter = new MainCursorAdapter(this::openDetailView);

    getMovies(getSortMethod());
  }

  private String getSortMethod() {
    return SharedPreferencesUtils.getSortingPreference(this);
  }

  private void openDetailView(Movie movie) {
    startActivity(DetailActivity.launchDetailActivity(this, movie));
  }

  private void getMovies(String sortMethod) {
    setViewsVisibility(SHOW_LOADING_VIEW);

    if (isNetworkAvailable(this)) {
      // Listener for when AsyncTask is ready to update UI
      FetchMovieAsyncTask.OnTaskCompleted taskCompleted = this::displayResult;

      // Execute task
      FetchMovieAsyncTask movieTask = new FetchMovieAsyncTask(taskCompleted);
      movieTask.execute(sortMethod);
    } else {
      setViewsVisibility(SHOW_ERROR_VIEW);
    }
  }

  private void displayResult(List<Movie> movies) {
    setViewsVisibility(SHOW_RESULT_VIEW);
    // As the result it nullable, check and display the correct view to prevent crashes
    if (movies != null) {
      adapter.setData(movies);
    } else {
      setViewsVisibility(SHOW_ERROR_VIEW);
    }
  }

  private void setViewsVisibility(int viewToShow) {
    switch (viewToShow) {
      case SHOW_LOADING_VIEW:
        progressBar.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        break;
      case SHOW_RESULT_VIEW:
        progressBar.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        break;
      default:
      case SHOW_ERROR_VIEW:
        progressBar.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        break;
    }
  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    String[] projection = {
        MovieEntry._ID,
        MovieEntry.COLUMN_MOVIE_ID,
        MovieEntry.COLUMN_TITLE,
        MovieEntry.COLUMN_POSTER_PATH,
        MovieEntry.COLUMN_OVERVIEW,
        MovieEntry.COLUMN_AVERAGE_VOTE,
        MovieEntry.COLUMN_RELEASE_DATE
    };

    switch (id) {
      // If the loader requested is our forecast loader, return the appropriate CursorLoader
      case ID_MOVIES_LOADER:
        // URI for all rows of weather data in our weather table
        Uri forecastQueryUri = MovieEntry.CONTENT_URI;
        // Sort order: Ascending by date
        String sortOrder = MovieEntry.COLUMN_RELEASE_DATE + " ASC";

        return new CursorLoader(this,
            forecastQueryUri,
            projection,
            null,
            null,
            sortOrder);

      default:
        throw new RuntimeException("Loader Not Implemented: " + id);
    }
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    cursorAdapter.swapCursor(data);
    if (data.getCount() != 0) setViewsVisibility(SHOW_RESULT_VIEW);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    cursorAdapter.swapCursor(null);
  }
}
