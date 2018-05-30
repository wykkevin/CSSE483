package edu.rosehulman.wangy16.signstorage;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

public class Pic implements Parcelable {

    public static final Creator<Pic> CREATOR = new Creator<Pic>() {
        @Override
        public Pic createFromParcel(Parcel in) {
            return new Pic(in);
        }

        @Override
        public Pic[] newArray(int size) {
            return new Pic[size];
        }
    };
    private String caption;
    private String location;
    private String key;

    public Pic() {

    }

    public Pic(String caption, String url) {
        this.caption = caption;
        this.location = url;
    }

    protected Pic(Parcel in) {
        caption = in.readString();
        location = in.readString();
        key = in.readString();
    }

    @Exclude
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(caption);
        dest.writeString(location);
        dest.writeString(key);
    }
}
