package edu.rosehulman.wangy16.newsignstorage;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

public class PicListFragment extends Fragment implements PicAdapter.Callback {
    private static final int RC_TAKE_PICTURE = 1;
    private static final int RC_CHOOSE_PICTURE = 2;
    private PicAdapter mAdapter;
    private OnLogoutListener mListener;
    private DatabaseReference mPictureRef;
    private boolean mShowingAll;
    private String userID;
    private Context mContext;

    public PicListFragment() {
        // Required empty public constructor
    }

    public void getmContext(Context context){
        mContext=context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userID = getArguments().getString(MainActivity.USER);
        mPictureRef = FirebaseDatabase.getInstance().getReference().child("pics");
        mShowingAll = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pic_list, container, false);
        setHasOptionsMenu(true);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new PicAdapter(getContext(), this, mPictureRef, userID, mShowingAll);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPhotoDialog();
            }
        });
        return view;
    }

    public void showPhotoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose a photo source");

        builder.setNegativeButton("Choose a picture", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent choosePictureIntent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (choosePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
                    startActivityForResult(choosePictureIntent, RC_CHOOSE_PICTURE);
                }
            }
        });
        builder.setPositiveButton("Take a picture", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, RC_TAKE_PICTURE);
            }
        });
        builder.create().show();

    }

    @Override
    public void onActivityResult(final int requestCode, int resultCode, final Intent data) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("What is the caption");
        final EditText editText = new EditText(getContext());
        builder.setView(editText);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = editText.getText().toString();
                if (requestCode == RC_TAKE_PICTURE) {
                    sendPhotoToAdapter(name, data);
                } else if (requestCode == RC_CHOOSE_PICTURE) {
                    sendGalleryPhotoToAdapter(name, data);
                }
            }
        });
        builder.create().show();
    }

    private void sendGalleryPhotoToAdapter(String name, Intent data) {
        if (data != null && data.getData() != null) {
            Uri uri = data.getData();
//            String location = uri.toString();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
                mAdapter.addPic(name, bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("aaaaaaaaaaaaa","error");
            }

        }
    }

    private void sendPhotoToAdapter(String name, Intent data) {
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//        String location = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), bitmap, name, null);
        mAdapter.addPic(name, bitmap);
    }

    @Override
    public void onClickShowDetail(int position) {
        getPicDetail(position);
    }

    public void getPicDetail(int position) {
        FragmentTransaction ft = ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, DetailedFragment.newInstance(mAdapter.getmPicList().get(position)));
        ft.addToBackStack("list");
        ft.commit();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnLogoutListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnLogoutListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            mListener.onLogout();
            mAdapter.removeAll();
            return true;
        } else if (id == R.id.action_show) {
            if (mShowingAll) {
                mShowingAll = false;
                item.setTitle(R.string.action_show_all);
            } else {
                mShowingAll = true;
                item.setTitle(R.string.action_show_mine);
            }
            mAdapter.showPic(mShowingAll);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public interface OnLogoutListener {
        void onLogout();
    }

}