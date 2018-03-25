package com.novembergave.popularmovies.NetworkUtils;


import android.net.Uri;
import android.util.Log;

import com.novembergave.popularmovies.BuildConfig;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlUtils {

  private final static String LOG_TAG = UrlUtils.class.getSimpleName();
  private static final String API_KEY = BuildConfig.API_KEY;

  /**
   * Creates and returns an URL.
   *
   * @return URL formatted with parameters for the API
   */
  public static URL getPopularApiUrl() throws MalformedURLException {
    final String BASE_URL = "https://api.themoviedb.org/3/movie/popular?";
    final String API_KEY_PARAM = "api_key";

    Uri builtUri = Uri.parse(BASE_URL).buildUpon()
        .appendQueryParameter(API_KEY_PARAM, API_KEY)
        .build();

    return new URL(builtUri.toString());
  }

  public static URL getTopRatedApiUrl() throws MalformedURLException {
    final String BASE_URL = "https://api.themoviedb.org/3/movie/top_rated?";
    final String API_KEY_PARAM = "api_key";

    Uri builtUri = Uri.parse(BASE_URL).buildUpon()
        .appendQueryParameter(API_KEY_PARAM, API_KEY)
        .build();

    return new URL(builtUri.toString());
  }

  public static URL getTrailersUrl(long id) {
    String API_BASE_URL = "http://api.themoviedb.org/3/movie/";
    String API_PARAM_KEY = "api_key";
    String API_TRAILERS_PATH = "videos";

    Uri builtUri = Uri.parse(API_BASE_URL).buildUpon()
        .appendPath(Long.toString(id))
        .appendPath(API_TRAILERS_PATH)
        .appendQueryParameter(API_PARAM_KEY, API_KEY)
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

  public static URL getReviewsUrl(long id) {
    String API_BASE_URL = "http://api.themoviedb.org/3/movie/";
    String API_PARAM_KEY = "api_key";
    String API_REVIEWS_PATH = "reviews";

    Uri builtUri = Uri.parse(API_BASE_URL).buildUpon()
        .appendPath(Long.toString(id))
        .appendPath(API_REVIEWS_PATH)
        .appendQueryParameter(API_PARAM_KEY, API_KEY)
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
