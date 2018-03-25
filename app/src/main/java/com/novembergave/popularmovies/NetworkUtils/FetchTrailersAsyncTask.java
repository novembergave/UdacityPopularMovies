package com.novembergave.popularmovies.NetworkUtils;


import android.os.AsyncTask;
import android.util.Log;

import com.novembergave.popularmovies.POJO.Trailer;

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

public class FetchTrailersAsyncTask extends AsyncTask<String, Void, List<Trailer>> {

  public interface OnTaskCompleted {
    void onFetchTrailersTaskCompleted(List<Trailer> trailers);
  }

  private final String LOG_TAG = FetchTrailersAsyncTask.class.getSimpleName();
  private final String apiKey;
  private final OnTaskCompleted listener;

  public FetchTrailersAsyncTask(OnTaskCompleted listener, String apiKey) {
    super();

    this.listener = listener;
    this.apiKey = apiKey;
  }

  @Override
  protected List<Trailer> doInBackground(String... params) {
    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;

    // Holds data returned from the API
    String moviesJsonStr;

    try {
      URL url = getPopularApiUrl();

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
  private List<Trailer> parseJson(String moviesJsonStr) throws JSONException {
    // JSON tags
    final String TAG_RESULTS = "results";
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
    List<Trailer> movies = new ArrayList<>();

    // Loop through movies and get data
    for (int i = 0; i < resultsArray.length(); i++) {
      // Initialize each object before it can be used
      Trailer movie = new Trailer();

      // Object contains all tags we're looking for
      JSONObject movieInfo = resultsArray.getJSONObject(i);

      // Store data in movie object
      movie.setTitle(movieInfo.getString(TAG_ORIGINAL_TITLE));

      // Add this to the list
      movies.add(movie);
    }

    return movies;
  }

  @Override
  protected void onPostExecute(List<Trailer> movies) {
    super.onPostExecute(movies);

    // Notify UI
    listener.onFetchTrailersTaskCompleted(movies);
  }
}
