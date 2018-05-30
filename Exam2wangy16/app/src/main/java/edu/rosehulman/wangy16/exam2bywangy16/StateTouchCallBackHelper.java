package edu.rosehulman.wangy16.exam2bywangy16;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

/**
 * Created by localmgr on 4/1/2018.
 */

public class StateTouchCallBackHelper extends ItemTouchHelper.Callback {

    private final StateAdapter mAdapter;

    public StateTouchCallBackHelper(StateAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        if (mAdapter.ismIsHighlighting()){
            return false;
        }
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        if (direction == ItemTouchHelper.END) {
            mAdapter.removeState(viewHolder.getAdapterPosition());
            mAdapter.rightSwipe();
        } else if (direction == ItemTouchHelper.START) {
            mAdapter.removeState(viewHolder.getAdapterPosition());
            mAdapter.leftSwipe();
        }
        if (mAdapter.getmStates().size() == 0) {
            mAdapter.finish();
        }
    }
}
