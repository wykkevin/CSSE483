package edu.rosehulman.wangy16.comicviewer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;

/**
 * A placeholder fragment containing a simple view.
 */
public class ComicFragment extends Fragment implements GetComicTask.ComicConsumer, GetImageTask.ImageConsumer {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String COMIC = "Comic";
    private ComicWrapper mComicWrapper;
    private TextView mTextView;
    private PhotoView mImageView;

    public ComicFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ComicFragment newInstance(ComicWrapper comicWrapper) {
        ComicFragment fragment = new ComicFragment();
        Bundle args = new Bundle();
        args.putParcelable(COMIC, comicWrapper);
        fragment.setArguments(args);
        return fragment;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_info) {
            showInfo();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showInfo() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        View view = getLayoutInflater().inflate(R.layout.dialog_info, null, false);
        final TextView titleTextView = view.findViewById(R.id.dialog_title);
        final TextView contentTextView = view.findViewById(R.id.dialog_content);
        titleTextView.setText(getString(R.string.dialog_title,mComicWrapper.getXkcdIssue()));
        contentTextView.setText(mComicWrapper.getComic().getAlt());
        builder.setView(view);
        builder.create().show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mComicWrapper = getArguments().getParcelable(COMIC);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mTextView = rootView.findViewById(R.id.section_label);
        mImageView = rootView.findViewById(R.id.photo_view);
        mImageView.setZoomable(true);
        mImageView.setHorizontalScrollBarEnabled(true);
        mImageView.setVerticalScrollBarEnabled(true);
        rootView.setBackgroundColor(mComicWrapper.getColor());
        String urlString = String.format("https://xkcd.com/%d/info.0.json", mComicWrapper.getXkcdIssue());
        (new GetComicTask(this)).execute(urlString);
        return rootView;
    }

    @Override
    public void onComicLoaded(Comic comic) {
        Log.d("COMIC", "Comic Object\n" + comic);
        mComicWrapper.setComic(comic);
        mTextView.setText(comic.getSafe_title());
        String imageURL = comic.getImg();
        (new GetImageTask(this)).execute(imageURL);
    }

    @Override
    public void onImageLoaded(Bitmap image) {
        Log.d("IMAGE", "Image Object\n" + image);
        mImageView.setImageBitmap(image);
    }
}
