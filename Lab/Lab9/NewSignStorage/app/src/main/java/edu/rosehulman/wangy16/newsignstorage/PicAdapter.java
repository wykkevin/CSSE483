package edu.rosehulman.wangy16.newsignstorage;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class PicAdapter extends RecyclerView.Adapter<PicAdapter.ViewHolder> {
    private ArrayList<Pic> mPicList;
    private Context mContext;
    private Callback mCallback;
    private DatabaseReference mPicRef;
    private String mUid;
    private Query myPicsRef;
    private StorageReference mPicStorageRef;

    public PicAdapter(Context context, Callback callback, DatabaseReference reference, String currentUid, boolean showingAll) {
        mPicList = new ArrayList<>();
        mContext = context;
        mCallback = callback;
        mPicRef = reference;
//        mPicRef.addChildEventListener(new PicChildEventListener());
        mUid = currentUid;
        mPicStorageRef = FirebaseStorage.getInstance().getReference().child("pics");
        showPic(showingAll);
    }

    public void showPic(boolean showall) {
        mPicList.clear();
        notifyDataSetChanged();
        if (showall) {
            myPicsRef = mPicRef;
        } else {
            myPicsRef = mPicRef.orderByChild("uid").equalTo(mUid);
        }
        myPicsRef.addChildEventListener(new PicChildEventListener());
    }

    public void addPic(final String caption, Bitmap bitmap) {
        final String photoDatabaseKey = mPicRef.push().getKey();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mPicStorageRef.child(photoDatabaseKey).putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.wtf("Aaaaaaaaaaaaaaaa", "Error send photo to storage");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Pic newPic = new Pic(caption, taskSnapshot.getDownloadUrl().toString());
                mPicRef.child(photoDatabaseKey).setValue(newPic);
            }
        });
    }

    public void removePic(int position) {
//        mPicList.remove(position);
//        notifyDataSetChanged();
        mPicRef.child(mPicList.get(position).getKey()).removeValue();
    }

    public void editPic(int position, String caption, String url) {
        Pic currentPic = mPicList.get(position);
        currentPic.setCaption(caption);
        currentPic.setLocation(url);
        mPicRef.child(currentPic.getKey()).setValue(currentPic);
//        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PicAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pic_row_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PicAdapter.ViewHolder holder, final int position) {
        holder.captionTextView.setText(mPicList.get(position).getCaption());
        holder.urlTextView.setText(mPicList.get(position).getLocation());
        holder.picCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onClickShowDetail(position);
            }
        });
        holder.picCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                editPic(position);
                return true;
            }
        });
    }

    public ArrayList<Pic> getmPicList() {
        return mPicList;
    }

    public void editPic(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.edit_a_weatherpic);
        View view = LayoutInflater.from(mContext).inflate(R.layout.change_dialog, null, false);
        builder.setView(view);
        final EditText captionEditText = view.findViewById(R.id.caption_editText);
        final EditText urlEditText = view.findViewById(R.id.url_editText);
        captionEditText.setText(mPicList.get(position).getCaption());
        urlEditText.setText(mPicList.get(position).getLocation());
        builder.setNeutralButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removePic(position);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editPic(position, captionEditText.getText().toString(), urlEditText.getText().toString());
            }
        });
        builder.create().show();
    }


    @Override
    public int getItemCount() {
        return mPicList.size();
    }

    public void removeAll() {
        mPicList.clear();
        notifyDataSetChanged();
    }

    public interface Callback {
        void onClickShowDetail(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView captionTextView;
        private TextView urlTextView;
        private CardView picCardView;

        public ViewHolder(View itemView) {
            super(itemView);
            captionTextView = itemView.findViewById(R.id.pic_caption);
            urlTextView = itemView.findViewById((R.id.pic_url));
            picCardView = itemView.findViewById(R.id.card_view);
        }
    }

    class PicChildEventListener implements ChildEventListener {

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Pic pic = dataSnapshot.getValue(Pic.class);
            pic.setKey(dataSnapshot.getKey());
            mPicList.add(0, pic);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            String keyToDelete = dataSnapshot.getKey();
            Pic temp = dataSnapshot.getValue(Pic.class);
            for (Pic picture : mPicList) {
                if (keyToDelete.equals(picture.getKey())) {
                    picture.setLocation(temp.getLocation());
                    picture.setCaption(temp.getCaption());
                    notifyDataSetChanged();
                    return;
                }
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            String keyToDelete = dataSnapshot.getKey();
            for (Pic picture : mPicList) {
                if (keyToDelete.equals(picture.getKey())) {
                    mPicList.remove(picture);
                    notifyDataSetChanged();
                    return;
                }
            }
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }
}
