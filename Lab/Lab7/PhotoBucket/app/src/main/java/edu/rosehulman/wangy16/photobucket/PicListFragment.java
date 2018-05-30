package edu.rosehulman.wangy16.photobucket;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.security.auth.callback.Callback;


public class PicListFragment extends Fragment implements PicAdapter.Callback {
    //    private OnFragmentInteractionListener mListener;
    private PicAdapter mAdapter;
    private OnLogoutListener mListener;
    private DatabaseReference mPictureRef;
    private boolean mShowingAll;
    private String userID;

    public PicListFragment() {
        // Required empty public constructor
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
                addNewPic();
            }
        });
        return view;
    }

    public void addNewPic() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.add_dialog_title);
        View view = getLayoutInflater().inflate(R.layout.change_dialog, null, false);
        builder.setView(view);
        final EditText captionEditText = view.findViewById(R.id.caption_editText);
        final EditText urlEditText = view.findViewById(R.id.url_editText);
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAdapter.addPic(captionEditText.getText().toString(), urlEditText.getText().toString(), userID);
            }
        });
        builder.create().show();
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