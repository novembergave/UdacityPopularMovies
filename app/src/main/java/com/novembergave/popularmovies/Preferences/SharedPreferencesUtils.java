package com.novembergave.popularmovies.Preferences;


import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtils {

  private static final String KEY = "shared_prefs";
  private static final String PREF_SORTING = "pref_sorting";

  static final String PREF_SORTING_POPULARITY = "popularity.desc";
  static final String PREF_SORTING_VOTES = "vote_average.desc";

  private static SharedPreferences getSharedPreferences(Context context) {
    return context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
  }

  static void updateSortingPreference(Context context, String sortingMethod) {
    getSharedPreferences(context).edit().putString(PREF_SORTING, sortingMethod).apply();
  }

  public static String getSortingPreference(Context context) {
    return getSharedPreferences(context).getString(PREF_SORTING, PREF_SORTING_POPULARITY);
  }
}
