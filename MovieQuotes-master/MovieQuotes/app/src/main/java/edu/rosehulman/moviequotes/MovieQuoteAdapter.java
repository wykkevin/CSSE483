package edu.rosehulman.moviequotes;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt Boutell on 12/15/2015.
 */
public class MovieQuoteAdapter extends RecyclerView.Adapter<MovieQuoteAdapter.ViewHolder> {

    private List<MovieQuote> mMovieQuotes;
    private Callback mCallback;
    private DatabaseReference mQuoteRef;


    public MovieQuoteAdapter(Callback callback) {
        mCallback = callback;
        mMovieQuotes = new ArrayList<>();
        mQuoteRef = FirebaseDatabase.getInstance().getReference().child("quotes");
        mQuoteRef.addChildEventListener(new QuoteChildEventListener());
        mQuoteRef.keepSynced(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_quote_row_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final MovieQuote movieQuote = mMovieQuotes.get(position);
        holder.mQuoteTextView.setText(movieQuote.getQuote());
        holder.mMovieTextView.setText(movieQuote.getMovie());
    }

    public void remove(MovieQuote movieQuote) {
        //TODO: Remove the next line(s) and use Firebase instead
//        mMovieQuotes.remove(movieQuote);
//        notifyDataSetChanged();

        mQuoteRef.child(movieQuote.getKey()).removeValue();
    }

    @Override
    public int getItemCount() {
        return mMovieQuotes.size();
    }

    public void add(MovieQuote movieQuote) {
        //TODO: Remove the next line(s) and use Firebase instead
//        mMovieQuotes.add(0, movieQuote);
//        notifyDataSetChanged();

        mQuoteRef.push().setValue(movieQuote);
    }

    public void update(MovieQuote movieQuote, String newQuote, String newMovie) {
        //TODO: Remove the next line(s) and use Firebase instead
        movieQuote.setQuote(newQuote);
        movieQuote.setMovie(newMovie);
        mQuoteRef.child(movieQuote.getKey()).setValue(movieQuote);
//        notifyDataSetChanged();
    }

    public interface Callback {
        void onEdit(MovieQuote movieQuote);
    }

    class QuoteChildEventListener implements ChildEventListener {

//        public QuoteChildEventListener() {
//        }

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            MovieQuote movieQuote = dataSnapshot.getValue(MovieQuote.class);
            movieQuote.setKey(dataSnapshot.getKey());
            mMovieQuotes.add(0, movieQuote);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            String keyToDelete = dataSnapshot.getKey();
            MovieQuote temp = dataSnapshot.getValue(MovieQuote.class);
            for (MovieQuote mq : mMovieQuotes) {
                if (keyToDelete.equals(mq.getKey())) {
                    mq.setMovie(temp.getMovie());
                    mq.setQuote(temp.getQuote());
                    notifyDataSetChanged();
                    return;
                }
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            String keyToDelete = dataSnapshot.getKey();
            for (MovieQuote mq : mMovieQuotes) {
                if (keyToDelete.equals(mq.getKey())) {
                    mMovieQuotes.remove(mq);
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
            Log.d(Constants.TAG, "DatabaseError" + databaseError);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView mQuoteTextView;
        private TextView mMovieTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mQuoteTextView = itemView.findViewById(R.id.quote_text);
            mMovieTextView = itemView.findViewById(R.id.movie_text);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            MovieQuote movieQuote = mMovieQuotes.get(getAdapterPosition());
            mCallback.onEdit(movieQuote);
        }

        @Override
        public boolean onLongClick(View v) {
            remove(mMovieQuotes.get(getAdapterPosition()));
            return true;
        }
    }
}
