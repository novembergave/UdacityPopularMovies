package com.novembergave.popularmovies.RecyclerViewUtils;


import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.novembergave.popularmovies.POJO.Movie;

import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainViewHolder> {

  public interface MovieClickListener {
    void viewMovie(Movie movie);
  }

  private List<Movie> moviesList;
  private MovieClickListener listener;

  public MainAdapter(MovieClickListener listener) {
    this.listener = listener;
    moviesList = new ArrayList<>();
  }

  @Override
  public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return MainViewHolder.inflateItemViewFrom(parent);
  }

  @Override
  public void onBindViewHolder(MainViewHolder holder, int position) {
    holder.bindTo(moviesList.get(position), listener);
  }

  @Override
  public int getItemCount() {
    return moviesList.size();
  }

  public void setData(List<Movie> moviesArray) {
    moviesList = moviesArray;
    notifyDataSetChanged();
  }
}
