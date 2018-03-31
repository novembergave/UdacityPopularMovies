package com.novembergave.popularmovies.RecyclerViewUtils;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.novembergave.popularmovies.POJO.Trailer;
import com.novembergave.popularmovies.R;

public class TrailerViewHolder extends RecyclerView.ViewHolder {

  private final View rootView;
  private final TextView nameView;

  public static TrailerViewHolder inflateItemViewFrom(ViewGroup viewGroup) {
    View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_video_holder, viewGroup, false);
    return new TrailerViewHolder(v);
  }

  public TrailerViewHolder(View itemView) {
    super(itemView);
    rootView = itemView.findViewById(R.id.item_trailer_root);
    nameView = itemView.findViewById(R.id.item_trailer_name);
  }

  public void bindTo(Trailer trailer, TrailerAdapter.TrailerClickListener listener) {
    nameView.setText(trailer.getName());
    rootView.setOnClickListener(click -> listener.viewTrailer(trailer));
  }
}
