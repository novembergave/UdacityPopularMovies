package com.novembergave.popularmovies.Database;


import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {

  public static final String CONTENT_AUTHORITY = "com.novembergave.popularmovies";
  public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
  public static final String PATH_MOVIE = "movie";

  private void MovieContract() {}

  public static final class MovieEntry implements BaseColumns {
    public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MOVIE);
    public static final String CONTENT_LIST_TYPE =
        ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
    public static final String CONTENT_ITEM_TYPE =
        ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
    public final static String TABLE_NAME = "movies";
    public final static String _ID = BaseColumns._ID;

    public final static String COLUMN_MOVIE_ID = "id";
    public final static String COLUMN_TITLE = "title";
    public final static String COLUMN_POSTER_PATH = "posterpath";
    public final static String COLUMN_OVERVIEW = "overview";
    public final static String COLUMN_AVERAGE_VOTE = "averagevote";
    public final static String COLUMN_RELEASE_DATE = "releasedate";

    public static String getMovieId(Uri uri) {
      return uri.getPathSegments().get(1);
    }
  }
}
