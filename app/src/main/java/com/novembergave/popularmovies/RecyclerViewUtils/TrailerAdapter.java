package com.novembergave.popularmovies.RecyclerViewUtils;


import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.novembergave.popularmovies.POJO.Trailer;

import java.util.ArrayList;
import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerViewHolder> {

  public interface TrailerClickListener {
    void viewTrailer(Trailer trailer);
  }

  private TrailerClickListener listener;
  private List<Trailer> trailers;

  public TrailerAdapter(TrailerClickListener listener) {
    this.listener = listener;
    trailers = new ArrayList<>();
  }

  @Override
  public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return TrailerViewHolder.inflateItemViewFrom(parent);
  }

  @Override
  public void onBindViewHolder(TrailerViewHolder holder, int position) {
    holder.bindTo(trailers.get(position), listener);
  }

  @Override
  public int getItemCount() {
    return trailers.size();
  }

  public void setData(List<Trailer> trailersArray) {
    trailers = trailersArray;
    notifyDataSetChanged();
  }
}
