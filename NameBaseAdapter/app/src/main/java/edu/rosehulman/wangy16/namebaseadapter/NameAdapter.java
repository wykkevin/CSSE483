package edu.rosehulman.wangy16.namebaseadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by localmgr on 3/19/2018.
 */

public class NameAdapter extends BaseAdapter {
    private List<String> mNames;
    private Context mContext;
    private Random mRandom;

    public NameAdapter(Context mContext) {
        this.mNames = new ArrayList<>();
        this.mContext = mContext;
        this.mRandom = new Random();

        for (int i = 0; i < 5; i++) {
            this.mNames.add(getRandomName());
        }
    }

    public void addName() {
        this.mNames.add(0, getRandomName());
        notifyDataSetChanged();
    }

    public void removeName(int position) {
        this.mNames.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mNames.size();
    }

    // Optional, only if I call
    @Override
    public Object getItem(int position) {
        return null;
    }

    // Optional, only if I call
    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.name_view, parent, false);
        } else {
            view = convertView;
        }
        TextView name_text_view = view.findViewById(R.id.name_text_view);
        name_text_view.setText(mNames.get(position));

        TextView position_text_view = view.findViewById(R.id.position_text_view);
        position_text_view.setText("I'm #" + position);

        return view;
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

}
