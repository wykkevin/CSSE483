package edu.rosehulman.wangy16.exam2bywangy16;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by localmgr on 3/31/2018.
 */

public class StateAdapter extends RecyclerView.Adapter<StateAdapter.StateViewHolder> {
    private static final String SCORE = "SCORE";
    private List<Territory> mStates;
    private Context mContext;
    private RecyclerView mRecyclerView;
    private List<Territory> mTerritoryList;
    private View mRoot;
    private int mScore;
    private Activity mActivity;
    private boolean mIsHighlighting = false;
    private int ran1 = 0;
    private int ran2 = 0;

    public StateAdapter(Context context, RecyclerView recyclerView, List<Territory> territoryList, View root, Activity activity) {
        mStates = new ArrayList<>();
        mContext = context;
        mRecyclerView = recyclerView;
        mTerritoryList = territoryList;
        mRoot = root;
        mScore = 0;
        mActivity = activity;
    }

    public List<Territory> getmStates() {
        return mStates;
    }

    public List<Territory> getmTerritoryList() {
        return mTerritoryList;
    }

    public boolean ismIsHighlighting() {
        return mIsHighlighting;
    }

    public void setmScore(int mScore) {
        this.mScore = mScore;
    }

    public void addState() {
        this.mStates.add(0, getRandomState());
        notifyItemInserted(0);
        mRecyclerView.scrollToPosition(0);
    }

    public void removeState(int position) {
        mTerritoryList.add(mStates.get(position));
        this.mStates.remove(position);
        notifyItemRemoved(position);
    }

    public void shuffle() {
        Collections.shuffle(mStates);
        notifyDataSetChanged();
    }

    public void highlight(int random1, int random2) {
        mIsHighlighting = true;
        this.ran1 = random1;
        this.ran2 = random2;
        notifyDataSetChanged();
    }


    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mStates, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mStates, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    public void leftSwipe() {
        mScore -= 1;
        mActivity.setTitle(mActivity.getString(R.string.score, mScore));
    }

    public void rightSwipe() {
        mScore += 2;
        mActivity.setTitle(mActivity.getString(R.string.score, mScore));
    }

    public void finish() {
        Intent finishIntent = new Intent(mContext, FinishFullscreenActivity.class);
        finishIntent.putExtra(SCORE, mScore);
        mActivity.startActivity(finishIntent);
    }

    private Territory getRandomState() {
        int random = (int) (Math.random() * (mTerritoryList.size() - 1));
        Territory tempTerritory = mTerritoryList.get(random);
        mTerritoryList.remove(random);
        return tempTerritory;
    }

    @Override
    public StateAdapter.StateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.state_view, parent, false);
        return new StateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final StateAdapter.StateViewHolder holder, final int position) {
        String name = mStates.get(position).getName();
        holder.mStateTextView.setText(name);
        if (mIsHighlighting) {
            if (position == ran1 || position == ran2) {
                holder.mStateCardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.highlight));
            }
        } else {
            holder.mStateCardView.setCardBackgroundColor(Color.WHITE);
        }
        holder.mStateCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsHighlighting) {
                    Territory t1 = mStates.get(ran1);
                    Territory t2 = mStates.get(ran2);
                    if (position == ran1) {
                        if (t1.getArea() > t2.getArea()) {
                            Snackbar snackbar = Snackbar.make(mRoot, mContext.getResources().getString
                                    (R.string.compare_true, t1.getName(), t1.getArea(), t2.getName(), t2.getArea()), Snackbar.LENGTH_INDEFINITE);
                            snackbar.show();
                            mScore += 4;
                        } else {
                            Snackbar snackbar = Snackbar.make(mRoot, mContext.getResources().getString
                                    (R.string.compare_false, t2.getName(), t2.getArea(), t1.getName(), t1.getArea()), Snackbar.LENGTH_INDEFINITE);
                            snackbar.show();
                            mScore -= 3;
                        }
                        mActivity.setTitle(mContext.getString(R.string.score, mScore));
                        mIsHighlighting = false;
                        notifyDataSetChanged();
                    } else if (position == ran2) {
                        if (t1.getArea() < t2.getArea()) {
                            Snackbar snackbar = Snackbar.make(mRoot, mContext.getResources().getString
                                    (R.string.compare_true, t2.getName(), t2.getArea(), t1.getName(), t1.getArea()), Snackbar.LENGTH_INDEFINITE);
                            snackbar.show();
                            mScore += 4;
                        } else {
                            Snackbar snackbar = Snackbar.make(mRoot, mContext.getResources().getString
                                    (R.string.compare_false, t1.getName(), t1.getArea(), t2.getName(), t2.getArea()), Snackbar.LENGTH_INDEFINITE);
                            snackbar.show();
                            mScore -= 3;
                        }
                        mActivity.setTitle(mContext.getString(R.string.score, mScore));
                        mIsHighlighting = false;
                        notifyDataSetChanged();
                    }

                } else {
                    Snackbar snackbar = Snackbar.make(mRoot, mStates.get(position).getCapital(), Snackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    int snackbarTextId = android.support.design.R.id.snackbar_text;
                    TextView textView = (TextView) snackbarView.findViewById(snackbarTextId);
                    textView.setTextSize(48);
                    snackbar.show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mStates.size();
    }

    public class StateViewHolder extends RecyclerView.ViewHolder {
        private TextView mStateTextView;
        private CardView mStateCardView;

        public StateViewHolder(final View itemView) {
            super(itemView);
            mStateTextView = itemView.findViewById(R.id.state_textView);
            mStateCardView = itemView.findViewById(R.id.state_cardView);
        }
    }
}
