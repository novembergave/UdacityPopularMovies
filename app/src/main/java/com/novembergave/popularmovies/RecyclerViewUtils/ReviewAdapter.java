package com.novembergave.popularmovies.RecyclerViewUtils;


import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.novembergave.popularmovies.POJO.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewViewHolder> {

  private List<Review> reviews;

  public ReviewAdapter() {
    reviews = new ArrayList<>();
  }

  @Override
  public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return ReviewViewHolder.inflateItemViewFrom(parent);
  }

  @Override
  public void onBindViewHolder(ReviewViewHolder holder, int position) {
    holder.bindTo(reviews.get(position));
  }

  @Override
  public int getItemCount() {
    return reviews.size();
  }

  public void setData(List<Review> reviewsArray) {
    reviews = reviewsArray;
    notifyDataSetChanged();
  }
}
