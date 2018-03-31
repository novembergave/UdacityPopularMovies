package com.novembergave.popularmovies.NetworkUtils;


import android.os.AsyncTask;
import android.util.Log;

import com.novembergave.popularmovies.POJO.Review;

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

import static com.novembergave.popularmovies.NetworkUtils.UrlUtils.getReviewsUrl;

public class FetchReviewsAsyncTask extends AsyncTask<Long, Void, List<Review>> {

  public interface OnTaskCompleted {
    void onFetchReviewsTaskCompleted(List<Review> reviews);
  }

  private final String LOG_TAG = FetchTrailersAsyncTask.class.getSimpleName();
  private final OnTaskCompleted listener;

  public FetchReviewsAsyncTask(OnTaskCompleted listener) {
    super();
    this.listener = listener;
  }

  @Override
  protected List<Review> doInBackground(Long... params) {
    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;

    // Holds data returned from the API
    String moviesJsonStr;

    try {
      URL url = getReviewsUrl(params[0]);

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
  private List<Review> parseJson(String reviewsJsonStr) throws JSONException {
    // JSON tags
    final String TAG_RESULTS = "results";
    final String TAG_ID = "id";
    final String TAG_AUTHOR = "author";
    final String TAG_CONTENT = "content";
    final String TAG_URL = "url";

    // Get the array containing the reviews found
    JSONObject moviesJson = new JSONObject(reviewsJsonStr);
    JSONArray resultsArray = moviesJson.getJSONArray(TAG_RESULTS);

    // Create arraylist of Review objects that stores data from the JSON string
    List<Review> reviews = new ArrayList<>();

    // Loop through reviews and get data
    for (int i = 0; i < resultsArray.length(); i++) {
      // Initialize each object before it can be used
      Review review = new Review();

      // Object contains all tags we're looking for
      JSONObject reviewJson = resultsArray.getJSONObject(i);

      // Store data in review object
      review.setId(reviewJson.getString(TAG_ID));
      review.setAuthor(reviewJson.getString(TAG_AUTHOR));
      review.setContent(reviewJson.getString(TAG_CONTENT));
      review.setUrl(reviewJson.getString(TAG_URL));

      // Add this to the list
      reviews.add(review);
    }

    return reviews;
  }

  @Override
  protected void onPostExecute(List<Review> reviews) {
    super.onPostExecute(reviews);

    // Notify UI
    listener.onFetchReviewsTaskCompleted(reviews);
  }
}
