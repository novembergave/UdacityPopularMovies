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

import static com.novembergave.popularmovies.NetworkUtils.UrlUtils.getTrailersUrl;

public class FetchTrailersAsyncTask extends AsyncTask<Long, Void, List<Trailer>> {

  public interface OnTaskCompleted {
    void onFetchTrailersTaskCompleted(List<Trailer> trailers);
  }

  private final String LOG_TAG = FetchTrailersAsyncTask.class.getSimpleName();
  private final OnTaskCompleted listener;

  public FetchTrailersAsyncTask(OnTaskCompleted listener) {
    super();
    this.listener = listener;
  }

  @Override
  protected List<Trailer> doInBackground(Long... params) {
    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;

    // Holds data returned from the API
    String moviesJsonStr;

    try {
      URL url = getTrailersUrl(params[0]);

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
    final String TAG_ID = "id";
    final String TAG_KEY = "key";
    final String TAG_NAME = "name";
    final String TAG_SITE = "site";

    // Get the array containing the trailers found
    JSONObject trailerJson = new JSONObject(moviesJsonStr);
    JSONArray resultsArray = trailerJson.getJSONArray(TAG_RESULTS);

    // Create arraylist of Trailer objects that stores data from the JSON string
    List<Trailer> trailers = new ArrayList<>();

    // Loop through trailers and get data
    for (int i = 0; i < resultsArray.length(); i++) {
      // Initialize each object before it can be used
      Trailer trailer = new Trailer();

      // Object contains all tags we're looking for
      JSONObject movieInfo = resultsArray.getJSONObject(i);

      // Store data in trailer object
      trailer.setId(movieInfo.getString(TAG_ID));
      trailer.setKey(movieInfo.getString(TAG_KEY));
      trailer.setName(movieInfo.getString(TAG_NAME));
      trailer.setSite(movieInfo.getString(TAG_SITE));

      // Add this to the list
      trailers.add(trailer);
    }

    return trailers;
  }

  @Override
  protected void onPostExecute(List<Trailer> movies) {
    super.onPostExecute(movies);

    // Notify UI
    listener.onFetchTrailersTaskCompleted(movies);
  }
}
