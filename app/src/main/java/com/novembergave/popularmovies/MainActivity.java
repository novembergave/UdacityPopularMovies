package com.novembergave.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

  private RecyclerView recyclerView;
  private ProgressBar progressBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    progressBar = findViewById(R.id.main_progress_bar);
    recyclerView = findViewById(R.id.main_recycler_view);
  }
}
