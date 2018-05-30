package edu.rosehulman.wangy16.namerecycleradapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by localmgr on 3/20/2018.
 */

public class NameAdaper extends RecyclerView.Adapter<NameAdaper.NameViewHolder> {

    private List<String> mNames;
    private Context mContext;
    private Random mRandom;
    private RecyclerView mRecyclerView;

    public NameAdaper(Context context, RecyclerView recyclerView) {
        mContext = context;
        mNames = new ArrayList<>();
        mRandom = new Random();
        mRecyclerView = recyclerView;
    }

    public void addName() {
        this.mNames.add(getRandomName());
        notifyItemInserted(mNames.size()-1);
        //notifyItemRangeChanged(0, mNames.size());
        mRecyclerView.scrollToPosition(mNames.size()-1);
    }

    public void removeName(int position) {
        this.mNames.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mNames.size());
    }

    private String getRandomName() {
        String[] names = new String[]{
                "Hannah", "Emily", "Sarah", "Madison", "Brianna",
                "Kaylee", "Kaitlyn", "Hailey", "Alexis", "Elizabeth",
                "Michael", "Jacob", "Matthew", "Nicholas", "Christopher",
                "Joseph", "Zachary", "Joshua", "Andrew", "William"
        };
        return names[mRandom.nextInt(names.length)];
    }


    @Override
    public NameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.name_view, parent, false);
        return new NameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NameViewHolder holder, int position) {
        String name = mNames.get(position);
        holder.mNameTextView.setText(name);
        holder.mPositionTextView.setText("I'm #" + position);
    }

    @Override
    public int getItemCount() {
        return mNames.size();
    }


    class NameViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        private TextView mNameTextView;
        private TextView mPositionTextView;


        public NameViewHolder(View itemView) {
            super(itemView);
            mNameTextView = itemView.findViewById(R.id.name_text_view);
            mPositionTextView = itemView.findViewById(R.id.position_text_view);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            removeName(getAdapterPosition());
            return true;
        }
    }
}
