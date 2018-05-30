package edu.rosehulman.wangy16.comicviewer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by localmgr on 4/2/2018.
 */

public class GetImageTask extends AsyncTask<String, Void, Bitmap> {
    private ImageConsumer mImageConsumer;

    public GetImageTask(ImageConsumer activity) {
        mImageConsumer = activity;
    }

    @Override
    protected Bitmap doInBackground(String... urlstrings) {
        String urlString = urlstrings[0];
        Bitmap image = null;
        try {
            InputStream in = new java.net.URL(urlString).openStream();
            image = BitmapFactory.decodeStream(in);

        } catch (IOException e) {
            Log.d("ERROR in asynctask", "ERROR: " + e.toString());
        }
        return image;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        mImageConsumer.onImageLoaded(bitmap);
    }

    public interface ImageConsumer {
        public void onImageLoaded(Bitmap image);
    }
}
