package edu.rosehulman.wangy16.foodrater;

import android.content.Context;
import android.media.Rating;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by localmgr on 3/21/2018.
 */

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private List<String> mFoods;
    private Context mContext;
    private Random mRandom;
    private RecyclerView mRecyclerView;
    private HashMap<String, Integer> sDefaultNamesAndIds;

    public FoodAdapter(Context context, RecyclerView recyclerView) {
        mFoods = new ArrayList<>();
        mContext = context;
        mRandom = new Random();
        mRecyclerView = recyclerView;

        sDefaultNamesAndIds = new HashMap<>();
        sDefaultNamesAndIds.put("banana", R.drawable.banana);
        sDefaultNamesAndIds.put("broccoli", R.drawable.broccoli);
        sDefaultNamesAndIds.put("homemade bread", R.drawable.bread);
        sDefaultNamesAndIds.put("chicken", R.drawable.chicken);
        sDefaultNamesAndIds.put("chocolate", R.drawable.chocolate);
        sDefaultNamesAndIds.put("ice cream", R.drawable.icecream);
        sDefaultNamesAndIds.put("lima beans", R.drawable.limabeans);
        sDefaultNamesAndIds.put("steak", R.drawable.steak);
    }

    public void addFood() {
        this.mFoods.add(0, getRandomFood());
        notifyItemInserted(0);
        mRecyclerView.scrollToPosition(0);
    }

    public void removeFood(int position) {
        this.mFoods.remove(position);
        notifyItemRemoved(position);
    }

    private String getRandomFood() {
        String[] names = new String[]{"banana", "broccoli", "homemade bread", "chicken", "chocolate", "ice cream", "lima beans", "steak"};
        return names[mRandom.nextInt(names.length)];
    }

    @Override
    public FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.food_view, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FoodViewHolder holder, int position) {
        String name = mFoods.get(position);
        holder.mFoodTextView.setText(name);
        holder.mFoodImageView.setImageResource(sDefaultNamesAndIds.get(name));
        //holder.mRatingBar.setRating(holder.mRate);
    }

    @Override
    public int getItemCount() {
        return mFoods.size();
    }

    public List<String> getmFood() {
        return mFoods;
    }

    class FoodViewHolder extends ViewHolder implements View.OnLongClickListener {
        private TextView mFoodTextView;
        private ImageView mFoodImageView;
        private RatingBar mRatingBar;
       // private float mRate;

        public FoodViewHolder(View itemView) {
            super(itemView);
            mFoodTextView = itemView.findViewById(R.id.food_name);
            mFoodImageView = itemView.findViewById(R.id.food_image);
            mRatingBar = itemView.findViewById(R.id.food_rating);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            removeFood(getAdapterPosition());
            return true;
        }
    }
}
