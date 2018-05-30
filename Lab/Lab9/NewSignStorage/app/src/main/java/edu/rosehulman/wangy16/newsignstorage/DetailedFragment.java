package edu.rosehulman.wangy16.newsignstorage;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;

public class DetailedFragment extends Fragment implements GetImageTask.ImageConsumer {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PICTURE = "picture";
    private Pic mPic;
    private PhotoView mImageView;

    public DetailedFragment() {
        // Required empty public constructor
    }

    public static DetailedFragment newInstance(Pic pic) {
        DetailedFragment fragment = new DetailedFragment();
        Bundle args = new Bundle();
        args.putParcelable(PICTURE, pic);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPic = getArguments().getParcelable(PICTURE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detailed, container, false);
        TextView titleTextView = view.findViewById(R.id.photo_title_textView);
        titleTextView.setText(mPic.getCaption());
        mImageView = view.findViewById(R.id.photo_view);
        (new GetImageTask(this)).execute(mPic.getLocation());
        return view;
    }

    @Override
    public void onImageLoaded(Bitmap image) {
        mImageView.setImageBitmap(image);
    }
}

