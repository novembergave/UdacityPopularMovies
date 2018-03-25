package com.novembergave.popularmovies.POJO;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class Movie implements Parcelable {

  private String title;
  private String posterPath;
  private String overview;
  private double averageVote;
  private String releaseDate;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getPosterPath() {
    return posterPath;
  }

  public void setPosterPath(String posterPath) {
    this.posterPath = posterPath;
  }

  public String getOverview() {
    return overview;
  }

  public void setOverview(String overview) {
    this.overview = overview;
  }

  public double getAverageVote() {
    return averageVote;
  }

  public void setAverageVote(double averageVote) {
    this.averageVote = averageVote;
  }

  public String getReleaseDate() {
    return releaseDate;
  }

  public void setReleaseDate(String releaseDate) {
    this.releaseDate = releaseDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Movie movie = (Movie) o;
    return Double.compare(movie.averageVote, averageVote) == 0 &&
        Objects.equals(title, movie.title) &&
        Objects.equals(posterPath, movie.posterPath) &&
        Objects.equals(overview, movie.overview) &&
        Objects.equals(releaseDate, movie.releaseDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, posterPath, overview, averageVote, releaseDate);
  }

  @Override
  public String toString() {
    return "Movie{" +
        "title='" + title + '\'' +
        ", posterPath='" + posterPath + '\'' +
        ", overview='" + overview + '\'' +
        ", averageVote=" + averageVote +
        ", releaseDate='" + releaseDate + '\'' +
        '}';
  }

  public Movie() {
  }

  private Movie(Parcel in) {
    title = in.readString();
    posterPath = in.readString();
    overview = in.readString();
    averageVote = in.readDouble();
    releaseDate = in.readString();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(title);
    dest.writeString(posterPath);
    dest.writeString(overview);
    dest.writeDouble(averageVote);
    dest.writeString(releaseDate);
  }

  @SuppressWarnings("unused")
  public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
    @Override
    public Movie createFromParcel(Parcel in) {
      return new Movie(in);
    }

    @Override
    public Movie[] newArray(int size) {
      return new Movie[size];
    }
  };
}
