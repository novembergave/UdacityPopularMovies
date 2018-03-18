package com.novembergave.popularmovies.Preferences;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.novembergave.popularmovies.R;

public class PreferenceDialog extends DialogFragment {

  public interface OnConfirmPreference {
    void show(String preference);
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
    Spinner spinner = view.findViewById(R.id.spinner);
    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.sort_options));
    spinner.setAdapter(arrayAdapter);

    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder
        .setTitle(R.string.sort)
        .setView(view)
        .setNegativeButton(android.R.string.cancel, (a, b) -> a.cancel())
        .setPositiveButton(android.R.string.ok, (a, b) -> {
          doSomething();
          a.dismiss();
        });
    return builder.create();
  }

  private void doSomething() {
    preferenceListener.show(getString(R.string.sort_popularity));
  }

}
