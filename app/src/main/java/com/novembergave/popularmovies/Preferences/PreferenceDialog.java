package com.novembergave.popularmovies.Preferences;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;

import com.novembergave.popularmovies.R;

public class PreferenceDialog extends DialogFragment {

  private RadioGroup selectionGroup;

  public interface OnConfirmPreference {
    void show();
  }

  private OnConfirmPreference preferenceListener;

  public PreferenceDialog() {
  }

  public void setPreferenceListener(OnConfirmPreference preferenceListener) {
    this.preferenceListener = preferenceListener;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    LayoutInflater layoutInflater = getActivity().getLayoutInflater();
    View view = layoutInflater.inflate(R.layout.preferences_dialog, null);
    selectionGroup = view.findViewById(R.id.sort_radio_group);
    selectionGroup.check(SharedPreferencesUtils.getSortingPreference(getActivity())
        .equals(SharedPreferencesUtils.PREF_SORTING_POPULARITY) ?
        R.id.sort_popularity : R.id.sort_number_votes);

    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder
        .setTitle(R.string.sort)
        .setView(view)
        .setNegativeButton(android.R.string.cancel, (a, b) -> a.cancel())
        .setPositiveButton(android.R.string.ok, (a, b) -> {
          updatePreferences();
          a.dismiss();
        });
    return builder.create();
  }

  private void updatePreferences() {
    int checkedRadioButtonId = selectionGroup.getCheckedRadioButtonId();
    SharedPreferencesUtils.updateSortingPreference(getActivity(), checkedRadioButtonId == R.id.sort_popularity ?
        SharedPreferencesUtils.PREF_SORTING_POPULARITY :
        SharedPreferencesUtils.PREF_SORTING_VOTES);
    preferenceListener.show();
  }
}
