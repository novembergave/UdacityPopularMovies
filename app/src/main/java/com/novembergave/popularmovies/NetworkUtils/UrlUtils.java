package com.novembergave.popularmovies.NetworkUtils;


import android.net.Uri;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlUtils {

  private final static String LOG_TAG = UrlUtils.class.getSimpleName();

  /**
   * Creates and returns an URL.
   *
   * @return URL formatted with parameters for the API
   */
  public static URL getApiUrl(String[] parameters, String apiKey) throws MalformedURLException {
    final String BASE_URL = "https://api.themoviedb.org/3/discover/movie?";
    final String SORT_BY_PARAM = "sort_by";
    final String API_KEY_PARAM = "api_key";

    Uri builtUri = Uri.parse(BASE_URL).buildUpon()
        .appendQueryParameter(SORT_BY_PARAM, parameters[0])
        .appendQueryParameter(API_KEY_PARAM, apiKey)
        .build();

    return new URL(builtUri.toString());
  }

  public static URL getTrailersUrl(long id, String apiKey) {
    String API_BASE_URL = "http://api.themoviedb.org/3/movie/";
    String API_PARAM_KEY = "api_key";
    String API_TRAILERS_PATH = "videos";

    Uri builtUri = Uri.parse(API_BASE_URL).buildUpon()
        .appendPath(Long.toString(id))
        .appendPath(API_TRAILERS_PATH)
        .appendQueryParameter(API_PARAM_KEY, apiKey)
        .build();

    Log.d(LOG_TAG, "Query URI: " + builtUri.toString());

    URL url = null;
    try {
      url = new URL(builtUri.toString());
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }

    return url;
  }

  public static URL getReviewsUrl(long id, String apiKey) {
    String API_BASE_URL = "http://api.themoviedb.org/3/movie/";
    String API_PARAM_KEY = "api_key";
    String API_REVIEWS_PATH = "reviews";

    Uri builtUri = Uri.parse(API_BASE_URL).buildUpon()
        .appendPath(Long.toString(id))
        .appendPath(API_REVIEWS_PATH)
        .appendQueryParameter(API_PARAM_KEY, apiKey)
        .build();

    Log.d(LOG_TAG, "Query URI: " + builtUri.toString());

    URL url = null;
    try {
      url = new URL(builtUri.toString());
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }

    return url;
  }
}
