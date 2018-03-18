package com.novembergave.popularmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.novembergave.popularmovies.NetworkUtils.FetchMovieAsyncTask;
import com.novembergave.popularmovies.POJO.Movie;
import com.novembergave.popularmovies.Preferences.PreferenceDialog;
import com.novembergave.popularmovies.Preferences.SharedPreferencesUtils;
import com.novembergave.popularmovies.RecyclerViewUtils.MainAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity {

  private static final int SHOW_LOADING_VIEW = 0;
  private static final int SHOW_ERROR_VIEW = 1;
  private static final int SHOW_RESULT_VIEW = 2;


  private RecyclerView recyclerView;
  private ProgressBar progressBar;
  private MainAdapter adapter;
  private View errorView;

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
    adapter = new MainAdapter(this::clickListener);
    recyclerView.setAdapter(adapter);

    getMovies(getSortMethod());
  }

  private String getSortMethod() {
    return SharedPreferencesUtils.getSortingPreference(this);
  }

  private void clickListener(Movie movie) {
    Toast.makeText(this, movie.getTitle(), Toast.LENGTH_SHORT).show();
    // open new activity
  }

  private void getMovies(String sortMethod) {
    setViewsVisibility(SHOW_LOADING_VIEW);

    if (isNetworkAvailable()) {
      // Key needed to get data from TMDb TODO: Set your key to the string file
      String apiKey = getString(R.string.api_key);

      // Listener for when AsyncTask is ready to update UI
      FetchMovieAsyncTask.OnTaskCompleted taskCompleted = this::displayResult;

      // Execute task
      FetchMovieAsyncTask movieTask = new FetchMovieAsyncTask(taskCompleted, apiKey);
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

  private boolean isNetworkAvailable() {
    ConnectivityManager connectivityManager
        = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
  }
}
