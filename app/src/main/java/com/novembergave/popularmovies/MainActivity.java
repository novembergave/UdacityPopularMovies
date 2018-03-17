package com.novembergave.popularmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.novembergave.popularmovies.NetworkUtils.FetchMovieAsyncTask;
import com.novembergave.popularmovies.POJO.Movie;
import com.novembergave.popularmovies.RecyclerViewUtils.MainAdapter;

public class MainActivity extends AppCompatActivity {

  private RecyclerView recyclerView;
  private ProgressBar progressBar;
  private MainAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    progressBar = findViewById(R.id.main_progress_bar);
    recyclerView = findViewById(R.id.main_recycler_view);

    recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    adapter = new MainAdapter(this::clickListener);
    recyclerView.setAdapter(adapter);

    getMoviesFromTMDb(getSortMethod());
  }

  private String getSortMethod() {
    return getString(R.string.sort_vote);
  }

  private void clickListener(Movie movie) {
    Toast.makeText(this, movie.getTitle(), Toast.LENGTH_SHORT).show();
    // open new activity
  }

  /**
   * If device has Internet the magic happens when app launches. The app will start the process
   * of collecting data from the API and present it to the user.
   * <p/>
   * If the device has no connectivity it will display a Toast explaining that app needs
   * Internet to work properly.
   *
   */
  private void getMoviesFromTMDb(String sortMethod) {
    if (isNetworkAvailable()) {
      // Key needed to get data from TMDb
      String apiKey = getString(R.string.api_key);

      // Listener for when AsyncTask is ready to update UI
      FetchMovieAsyncTask.OnTaskCompleted taskCompleted = new FetchMovieAsyncTask.OnTaskCompleted() {
        @Override
        public void onFetchMoviesTaskCompleted(Movie[] movies) {
          progressBar.setVisibility(View.GONE);
          recyclerView.setVisibility(View.VISIBLE);
          adapter.setData(movies);
        }
      };

      // Execute task
      FetchMovieAsyncTask movieTask = new FetchMovieAsyncTask(taskCompleted, apiKey);
      movieTask.execute(sortMethod);
    } else {
      Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
    }
  }

  private boolean isNetworkAvailable() {
    ConnectivityManager connectivityManager
        = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;

    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
  }
}
