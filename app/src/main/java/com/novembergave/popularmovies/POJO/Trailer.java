package com.novembergave.popularmovies.POJO;


import java.io.Serializable;
import java.util.Objects;

public class Trailer implements Serializable {

  private String title;
  private String url;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Trailer trailer = (Trailer) o;
    return Objects.equals(title, trailer.title) &&
        Objects.equals(url, trailer.url);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, url);
  }

  @Override
  public String toString() {
    return "Trailer{" +
        "title='" + title + '\'' +
        ", url='" + url + '\'' +
        '}';
  }
}
