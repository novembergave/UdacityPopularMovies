package com.novembergave.popularmovies.Database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.novembergave.popularmovies.Database.MovieContract.MovieEntry;

public class MovieDbHelper extends SQLiteOpenHelper {

  public static final int DATABASE_VERSION = 1;
  private static final String DATABASE_NAME = "inventory.db";
  public static final int INDEX_MOVIE_ID = 0;
  public static final int INDEX_TITLE = 1;
  public static final int INDEX_POSTER_PATH = 2;
  public static final int INDEX_OVERVIEW = 3;
  public static final int INDEX_AVERAGE_VOTE = 4;
  public static final int INDEX_RELEASE_DATE = 5;


  public MovieDbHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase sqLiteDatabase) {
    String SQL_CREATE_INVENTORY_TABLE =
        "CREATE TABLE " + MovieEntry.TABLE_NAME + " ("
            + MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, "
            + MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, "
            + MovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, "
            + MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, "
            + MovieEntry.COLUMN_AVERAGE_VOTE + " REAL NOT NULL, "
            + MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL); ";

    sqLiteDatabase.execSQL(SQL_CREATE_INVENTORY_TABLE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
    onCreate(sqLiteDatabase);
  }
}
