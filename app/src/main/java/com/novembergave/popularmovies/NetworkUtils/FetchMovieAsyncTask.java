package com.novembergave.popularmovies.NetworkUtils;


import android.os.AsyncTask;
import android.util.Log;

import com.novembergave.popularmovies.POJO.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.novembergave.popularmovies.NetworkUtils.UrlUtils.getPopularApiUrl;
import static com.novembergave.popularmovies.NetworkUtils.UrlUtils.getTopRatedApiUrl;
import static com.novembergave.popularmovies.Preferences.SharedPreferencesUtils.PREF_SORTING_POPULARITY;

public class FetchMovieAsyncTask extends AsyncTask<String, Void, List<Movie>> {

  public interface OnTaskCompleted {
    void onFetchMoviesTaskCompleted(List<Movie> movies);
  }

  private final String LOG_TAG = FetchMovieAsyncTask.class.getSimpleName();
  private final OnTaskCompleted listener;

  public FetchMovieAsyncTask(OnTaskCompleted listener) {
    super();

    this.listener = listener;
  }

  @Override
  protected List<Movie> doInBackground(String... params) {
    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;

    // Holds data returned from the API
    String moviesJsonStr;

    try {
      URL url;
      // try to obtain the user's preference
      if (params.length == 1) {
        url = params[0].equals(PREF_SORTING_POPULARITY) ? getPopularApiUrl() : getTopRatedApiUrl();
      } else {
        // default to getting the popular Api and log as exception
        url = getPopularApiUrl();
        Log.e(LOG_TAG, "Error changing query ");
      }

      // Start connecting to get JSON
      urlConnection = (HttpURLConnection) url.openConnection();
      urlConnection.setRequestMethod("GET");
      urlConnection.connect();

      InputStream inputStream = urlConnection.getInputStream();
      StringBuilder builder = new StringBuilder();

      if (inputStream == null) {
        return null;
      }
      reader = new BufferedReader(new InputStreamReader(inputStream));

      String line;
      while ((line = reader.readLine()) != null) {
        builder.append(line).append("\n");
      }

      if (builder.length() == 0) {
        // No data found. Return null
        return null;
      }

      moviesJsonStr = builder.toString();
    } catch (IOException e) {
      Log.e(LOG_TAG, "Error ", e);
      return null;
    } finally {
      // Release url connection and buffered reader
      if (urlConnection != null) {
        urlConnection.disconnect();
      }
      if (reader != null) {
        try {
          reader.close();
        } catch (final IOException e) {
          Log.e(LOG_TAG, "Error closing stream", e);
        }
      }
    }

    try {
      // Parse the JSON
      return parseJson(moviesJsonStr);
    } catch (JSONException e) {
      Log.e(LOG_TAG, e.getMessage(), e);
      e.printStackTrace();
    }

    return null;
  }

  /**
   * Extracts data from the JSON object and returns an Arraylist of movie objects.
   */
  private List<Movie> parseJson(String moviesJsonStr) throws JSONException {
    // JSON tags
    final String TAG_RESULTS = "results";
    final String TAG_ID = "id";
    final String TAG_ORIGINAL_TITLE = "original_title";
    final String TAG_POSTER_PATH = "poster_path";
    final String TAG_OVERVIEW = "overview";
    final String TAG_VOTE_AVERAGE = "vote_average";
    final String TAG_RELEASE_DATE = "release_date";
    final String TAG_VIDEO = "video";

    // Get the array containing the movies found
    JSONObject moviesJson = new JSONObject(moviesJsonStr);
    JSONArray resultsArray = moviesJson.getJSONArray(TAG_RESULTS);

    // Create arraylist of Movie objects that stores data from the JSON string
    List<Movie> movies = new ArrayList<>();

    // Loop through movies and get data
    for (int i = 0; i < resultsArray.length(); i++) {
      // Initialize each object before it can be used
      Movie movie = new Movie();

      // Object contains all tags we're looking for
      JSONObject movieInfo = resultsArray.getJSONObject(i);

      // Store data in movie object
      movie.setId(movieInfo.getLong(TAG_ID));
      movie.setTitle(movieInfo.getString(TAG_ORIGINAL_TITLE));
      movie.setPosterPath(movieInfo.getString(TAG_POSTER_PATH));
      movie.setOverview(movieInfo.getString(TAG_OVERVIEW));
      movie.setAverageVote(movieInfo.getDouble(TAG_VOTE_AVERAGE));
      movie.setReleaseDate(movieInfo.getString(TAG_RELEASE_DATE));
      movie.setHasVideo(movieInfo.getBoolean(TAG_VIDEO));
      // Add this to the list
      movies.add(movie);
    }

    return movies;
  }

  @Override
  protected void onPostExecute(List<Movie> movies) {
    super.onPostExecute(movies);

    // Notify UI
    listener.onFetchMoviesTaskCompleted(movies);
  }
}