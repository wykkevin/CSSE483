package edu.rosehulman.wangy16.comicviewer;

import android.os.AsyncTask;
import android.provider.SyncStateContract;
import android.util.Log;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by localmgr on 4/2/2018.
 */

public class GetComicTask extends AsyncTask<String, Void, Comic> {
    private ComicConsumer mComicConsumer;

    public GetComicTask(ComicConsumer activity) {
        mComicConsumer = activity;
    }

    @Override
    protected Comic doInBackground(String... urlstrings) {
        String urlString = urlstrings[0];
        Comic comic = null;
        try {
            comic = new ObjectMapper().readValue(new URL(urlString), Comic.class);
        } catch (IOException e) {
            Log.d("ERROR in asynctask", "ERROR: " + e.toString());
        }
        return comic;
    }

    @Override
    protected void onPostExecute(Comic comic) {
        super.onPostExecute(comic);
        mComicConsumer.onComicLoaded(comic);
    }

    public interface ComicConsumer {
        public void onComicLoaded(Comic comic);
    }


}
