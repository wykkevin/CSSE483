package edu.rosehulman.wangy16.photobucket;

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
    private String url;
    private String key;
    private String uid;

    public Pic() {

    }

    public Pic(String caption, String url, String uid) {
        this.caption = caption;
        this.url = url;
        this.uid = uid;
    }

    protected Pic(Parcel in) {
        caption = in.readString();
        url = in.readString();
        key = in.readString();
        uid = in.readString();
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(caption);
        dest.writeString(url);
        dest.writeString(key);
        dest.writeString(uid);
    }
}
