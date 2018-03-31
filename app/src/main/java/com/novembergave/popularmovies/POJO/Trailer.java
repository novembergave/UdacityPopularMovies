package com.novembergave.popularmovies.POJO;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class Trailer implements Parcelable {

  private String id;
  private String key;
  private String name;
  private String site;

  public Trailer() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSite() {
    return site;
  }

  public void setSite(String site) {
    this.site = site;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Trailer trailer = (Trailer) o;
    return Objects.equals(id, trailer.id) &&
        Objects.equals(key, trailer.key) &&
        Objects.equals(name, trailer.name) &&
        Objects.equals(site, trailer.site);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, key, name, site);
  }

  @Override
  public String toString() {
    return "Trailer{" +
        "id=" + id +
        ", key='" + key + '\'' +
        ", name='" + name + '\'' +
        ", site='" + site + '\'' +
        '}';
  }

  protected Trailer(Parcel in) {
    id = in.readString();
    key = in.readString();
    name = in.readString();
    site = in.readString();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(id);
    dest.writeString(key);
    dest.writeString(name);
    dest.writeString(site);
  }

  @SuppressWarnings("unused")
  public static final Parcelable.Creator<Trailer> CREATOR = new Parcelable.Creator<Trailer>() {
    @Override
    public Trailer createFromParcel(Parcel in) {
      return new Trailer(in);
    }

    @Override
    public Trailer[] newArray(int size) {
      return new Trailer[size];
    }
  };
}
