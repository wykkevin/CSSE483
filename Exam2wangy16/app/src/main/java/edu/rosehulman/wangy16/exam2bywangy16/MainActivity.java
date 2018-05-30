package edu.rosehulman.wangy16.exam2bywangy16;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Territory> territoryList;
    private StateAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        territoryList = FileUtils.loadFromJsonArray(this);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new StateAdapter(this, recyclerView, territoryList, findViewById(R.id.coordinator_layout), this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);

        ItemTouchHelper.Callback callback = new StateTouchCallBackHelper(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        for (int i = 0; i < 5; i++) {
            mAdapter.addState();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            if (!mAdapter.ismIsHighlighting()) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final View view = getLayoutInflater().inflate(R.layout.dialog_choose, null, false);
                builder.setView(view);
                TextView beginner = view.findViewById(R.id.dialog_beginner);
                TextView intermediate = view.findViewById(R.id.dialog_intermediate);
                TextView expert = view.findViewById(R.id.dialog_expert);
                final AlertDialog alertDialog = builder.create();
                beginner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int temp = mAdapter.getmStates().size();
                        for (int i = 0; i < temp; i++) {
                            mAdapter.removeState(0);
                        }
                        mAdapter.setmScore(0);
                        for (int i = 0; i < 5; i++) {
                            mAdapter.addState();
                        }
                        alertDialog.dismiss();
                        setTitle(getString(R.string.score, 0));
                    }
                });
                intermediate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int temp = mAdapter.getmStates().size();
                        for (int i = 0; i < temp; i++) {
                            mAdapter.removeState(0);
                        }
                        mAdapter.setmScore(0);
                        for (int i = 0; i < 10; i++) {
                            mAdapter.addState();
                        }
                        alertDialog.dismiss();
                        setTitle(getString(R.string.score, 0));
                    }
                });
                expert.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int temp = mAdapter.getmStates().size();
                        for (int i = 0; i < temp; i++) {
                            mAdapter.removeState(0);
                        }
                        mAdapter.setmScore(0);
                        for (int i = 0; i < 50; i++) {
                            mAdapter.addState();
                        }
                        alertDialog.dismiss();
                        setTitle(getString(R.string.score, 0));
                    }
                });

                alertDialog.show();
            }
            return true;
        } else if (id == R.id.action_swap) {
            if (!mAdapter.ismIsHighlighting()) {
                mAdapter.shuffle();
            }
            return true;
        } else if (id == R.id.action_choose_two) {
            if (!mAdapter.ismIsHighlighting()) {
                int random1 = (int) (Math.random() * (mAdapter.getmStates().size()));
                int random2 = (int) (Math.random() * (mAdapter.getmStates().size()));
                while (random2 == random1) {
                    random2 = (int) (Math.random() * (mAdapter.getmStates().size()));
                }
                mAdapter.highlight(random1, random2);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}