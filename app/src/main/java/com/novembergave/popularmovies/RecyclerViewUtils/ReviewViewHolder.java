package com.novembergave.popularmovies.RecyclerViewUtils;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.novembergave.popularmovies.POJO.Review;
import com.novembergave.popularmovies.R;

public class ReviewViewHolder extends RecyclerView.ViewHolder {

  private final TextView contentView;
  private final TextView nameView;

  public static ReviewViewHolder inflateItemViewFrom(ViewGroup viewGroup) {
    View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_review_holder, viewGroup, false);
    return new ReviewViewHolder(v);
  }

  public ReviewViewHolder(View itemView) {
    super(itemView);
    contentView = itemView.findViewById(R.id.item_comment_review);
    nameView = itemView.findViewById(R.id.item_comment_reviewer);
  }

  public void bindTo(Review review) {
    contentView.setText(review.getContent());
    nameView.setText(review.getAuthor());
  }
}
