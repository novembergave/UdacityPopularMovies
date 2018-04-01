package com.novembergave.popularmovies.Database;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.novembergave.popularmovies.R;

import static com.novembergave.popularmovies.Database.MovieContract.CONTENT_AUTHORITY;
import static com.novembergave.popularmovies.Database.MovieContract.MovieEntry;
import static com.novembergave.popularmovies.Database.MovieContract.PATH_MOVIE;

public class MovieProvider extends ContentProvider {

  public static final String LOG_TAG = MovieProvider.class.getSimpleName();

  private static final int MOVIES = 100;
  private static final int MOVIE_ID = 101;

  private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

  static {
    sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_MOVIE, MOVIES);
    sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_MOVIE + "/#",
        MOVIE_ID);
  }

  private MovieDbHelper helper;

  @Override
  public boolean onCreate() {
    helper = new MovieDbHelper(getContext());
    return true;
  }

  @Override
  public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                      String sortOrder) {
    SQLiteDatabase database = helper.getReadableDatabase();
    Cursor cursor;
    int match = sUriMatcher.match(uri);
    switch (match) {
      case MOVIES:
        cursor = database.query(MovieEntry.TABLE_NAME, projection, selection, selectionArgs,
            null, null, sortOrder);
        break;
      case MOVIE_ID:
        selection = MovieEntry._ID + "=?";
        selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
        cursor = database.query(MovieEntry.TABLE_NAME, projection, selection, selectionArgs,
            null, null, sortOrder);
        break;
      default:
        throw new IllegalArgumentException("Cannot query unknown URI " + uri);
    }
    cursor.setNotificationUri(getContext().getContentResolver(), uri);

    return cursor;
  }

  @Override
  public String getType(Uri uri) {
    final int match = sUriMatcher.match(uri);
    switch (match) {
      case MOVIES:
        return MovieEntry.CONTENT_LIST_TYPE;
      case MOVIE_ID:
        return MovieEntry.CONTENT_ITEM_TYPE;
      default:
        throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
    }
  }

  @Override
  public Uri insert(Uri uri, ContentValues contentValues) {
    final int match = sUriMatcher.match(uri);
    switch (match) {
      case MOVIES:
        return insertMovie(uri, contentValues);
      default:
        throw new IllegalArgumentException("Insertion is not supported for " + uri);
    }
  }

  private Uri insertMovie(Uri uri, ContentValues contentValues) {
    long movieId = contentValues.getAsLong(MovieEntry.COLUMN_MOVIE_ID);
    if (movieId < 0) {
      throw new IllegalArgumentException(getContext().getString(R.string.error_inserting, "movieId"));
    }

    String title = contentValues.getAsString(MovieEntry.COLUMN_TITLE);
    if (title == null) {
      throw new IllegalArgumentException(getContext().getString(R.string.error_inserting, "title"));
    }

    String posterPath = contentValues.getAsString(MovieEntry.COLUMN_POSTER_PATH);
    if (posterPath == null) {
      throw new IllegalArgumentException(getContext().getString(R.string.error_inserting, "posterPath"));
    }

    String overview = contentValues.getAsString(MovieEntry.COLUMN_OVERVIEW);
    if (overview == null) {
      throw new IllegalArgumentException(getContext().getString(R.string.error_inserting, "overview"));
    }

    Double averageVote = contentValues.getAsDouble(MovieEntry.COLUMN_AVERAGE_VOTE);
    if (averageVote == null) {
      throw new IllegalArgumentException(getContext().getString(R.string.error_inserting, "averageVote"));
    }

    String releaseDate = contentValues.getAsString(MovieEntry.COLUMN_RELEASE_DATE);
    if (releaseDate == null) {
      throw new IllegalArgumentException(getContext().getString(R.string.error_inserting, "releaseDate"));
    }

    // Get writeable database
    SQLiteDatabase database = helper.getWritableDatabase();

    // Insert the new movie with the given values
    long id = database.insert(MovieEntry.TABLE_NAME, null, contentValues);
    // If the ID is -1, then the insertion failed. Log an error and return null.
    if (id == -1) {
      Log.e(LOG_TAG, "Failed to insert row for " + uri);
      return null;
    }

    // Notify all listeners that the data has changed for the movie content URI
    getContext().getContentResolver().notifyChange(uri, null);

    // Return the new URI with the ID (of the newly inserted row) appended at the end
    return ContentUris.withAppendedId(uri, id);
  }

  @Override
  public int delete(Uri uri, String selection, String[] selectionArgs) {
    SQLiteDatabase database = helper.getWritableDatabase();

    int rowsDeleted;

    final int match = sUriMatcher.match(uri);
    switch (match) {
      case MOVIES:
        rowsDeleted = database.delete(MovieEntry.TABLE_NAME, selection, selectionArgs);
        break;
      case MOVIE_ID:
        selection = MovieEntry.COLUMN_MOVIE_ID + "=?";
        selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
        rowsDeleted = database.delete(MovieEntry.TABLE_NAME, selection, selectionArgs);
        break;
      default:
        throw new IllegalArgumentException("Deletion is not supported for " + uri);
    }

    if (rowsDeleted != 0) {
      getContext().getContentResolver().notifyChange(uri, null);
    }

    return rowsDeleted;
  }

  @Override
  public int update(Uri uri, ContentValues contentValues, String selection,
                    String[] selectionArgs) {
    final int match = sUriMatcher.match(uri);
    switch (match) {
      case MOVIES:
        return updateMovie(uri, contentValues, selection, selectionArgs);
      case MOVIE_ID:
        selection = MovieEntry._ID + "=?";
        selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
        return updateMovie(uri, contentValues, selection, selectionArgs);
      default:
        throw new IllegalArgumentException("Update is not supported for " + uri);
    }
  }

  private int updateMovie(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
    if (values.containsKey(MovieEntry.COLUMN_MOVIE_ID)) {
      long movieId = values.getAsLong(MovieEntry.COLUMN_MOVIE_ID);
      if (movieId < 0) {
        throw new IllegalArgumentException(getContext().getString(R.string.error_inserting, "movieId"));
      }
    }
    if (values.containsKey(MovieEntry.COLUMN_TITLE)) {
      String title = values.getAsString(MovieEntry.COLUMN_TITLE);
      if (title == null) {
        throw new IllegalArgumentException(getContext().getString(R.string.error_inserting, "title"));
      }
    }

    if (values.containsKey(MovieEntry.COLUMN_POSTER_PATH)) {
      String posterPath = values.getAsString(MovieEntry.COLUMN_POSTER_PATH);
      if (posterPath == null) {
        throw new IllegalArgumentException(getContext().getString(R.string.error_inserting, "posterPath"));
      }
    }

    if (values.containsKey(MovieEntry.COLUMN_OVERVIEW)) {
      String overview = values.getAsString(MovieEntry.COLUMN_OVERVIEW);
      if (overview == null) {
        throw new IllegalArgumentException(getContext().getString(R.string.error_inserting, "overview"));
      }
    }

    if (values.containsKey(MovieEntry.COLUMN_AVERAGE_VOTE)) {
      Double averageVote = values.getAsDouble(MovieEntry.COLUMN_AVERAGE_VOTE);
      if (averageVote == null && averageVote < 0) {
        throw new IllegalArgumentException(getContext().getString(R.string.error_inserting, "averageVote"));
      }
    }

    if (values.containsKey(MovieEntry.COLUMN_RELEASE_DATE)) {
      String releaseDate = values.getAsString(MovieEntry.COLUMN_RELEASE_DATE);
      if (releaseDate == null) {
        throw new IllegalArgumentException(getContext().getString(R.string.error_inserting, "releaseDate"));
      }
    }

    // If there are no values to update, then don't try to update the database
    if (values.size() == 0) {
      return 0;
    }

    // Otherwise, get writeable database to update the data
    SQLiteDatabase database = helper.getWritableDatabase();

    // Perform the update on the database and get the number of rows affected
    int rowsUpdated = database.update(MovieEntry.TABLE_NAME, values, selection, selectionArgs);

    // If 1 or more rows were updated, then notify all listeners that the data at the
    // given URI has changed
    if (rowsUpdated != 0) {
      getContext().getContentResolver().notifyChange(uri, null);
    }

    // Return the number of rows updated
    return rowsUpdated;
  }
}
