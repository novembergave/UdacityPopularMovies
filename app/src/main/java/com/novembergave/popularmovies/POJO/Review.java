package com.novembergave.popularmovies.POJO;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class Review implements Parcelable {

  private String id;
  private String author;
  private String content;
  private String url;

  public Review() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
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
    Review review = (Review) o;
    return Objects.equals(id, review.id) &&
        Objects.equals(author, review.author) &&
        Objects.equals(content, review.content) &&
        Objects.equals(url, review.url);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, author, content, url);
  }

  @Override
  public String toString() {
    return "Review{" +
        "id='" + id + '\'' +
        ", author='" + author + '\'' +
        ", content='" + content + '\'' +
        ", url='" + url + '\'' +
        '}';
  }

  protected Review(Parcel in) {
    id = in.readString();
    author = in.readString();
    content = in.readString();
    url = in.readString();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(id);
    dest.writeString(author);
    dest.writeString(content);
    dest.writeString(url);
  }

  @SuppressWarnings("unused")
  public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
    @Override
    public Review createFromParcel(Parcel in) {
      return new Review(in);
    }

    @Override
    public Review[] newArray(int size) {
      return new Review[size];
    }
  };
}
