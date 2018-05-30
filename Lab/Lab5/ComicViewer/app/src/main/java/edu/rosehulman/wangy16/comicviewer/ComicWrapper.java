package edu.rosehulman.wangy16.comicviewer;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by localmgr on 3/29/2018.
 */

public class ComicWrapper implements Parcelable {
    public static final Creator<ComicWrapper> CREATOR = new Creator<ComicWrapper>() {
        @Override
        public ComicWrapper createFromParcel(Parcel in) {
            return new ComicWrapper(in);
        }

        @Override
        public ComicWrapper[] newArray(int size) {
            return new ComicWrapper[size];
        }
    };
    private int xkcdIssue;
    private int color;
    private Comic comic = null;
    private int[] colors = {0xff99cc00, 0xff33b5e5, 0xffffbb33, 0xffff4444};

    public ComicWrapper(int i) {
        xkcdIssue = Utils.getRandomCleanIssue();
        color = colors[i % 4];
    }

    protected ComicWrapper(Parcel in) {
        xkcdIssue = in.readInt();
        color = in.readInt();
    }

    public int getXkcdIssue() {
        return xkcdIssue;
    }

    public int getColor() {
        return color;
    }

    public Comic getComic() {
        return comic;
    }

    public void setComic(Comic comic) {
        this.comic = comic;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(xkcdIssue);
        dest.writeInt(color);
    }
}
