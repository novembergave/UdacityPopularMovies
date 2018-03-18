package com.novembergave.popularmovies.POJO;


import java.io.Serializable;
import java.util.Objects;

public class Movie implements Serializable {

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
}
