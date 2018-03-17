package com.novembergave.popularmovies.RecyclerViewUtils;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.novembergave.popularmovies.POJO.Movie;
import com.novembergave.popularmovies.R;
import com.squareup.picasso.Picasso;

public class MainViewHolder extends RecyclerView.ViewHolder {

  private static final String imagePath = "http://image.tmdb.org/t/p/w185/";

  private final ImageView imageView;
  private final TextView titleView;
  private final Context context;
  private final View rootView;

  public static MainViewHolder inflateItemViewFrom(ViewGroup viewGroup) {
    View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_grid_holder, viewGroup, false);
    return new MainViewHolder(v);
  }

  public MainViewHolder(View itemView) {
    super(itemView);
    context = itemView.getContext();
    rootView = itemView.findViewById(R.id.item_root);
    imageView = itemView.findViewById(R.id.item_image);
    titleView = itemView.findViewById(R.id.item_title);
  }

  public void bindTo(Movie movie, MainAdapter.MovieClickListener listener) {
    Picasso.with(context).load(imagePath + movie.getPosterPath()).into(imageView);
    titleView.setText(movie.getTitle());
    rootView.setOnClickListener(click -> listener.viewMovie(movie));
  }
}
