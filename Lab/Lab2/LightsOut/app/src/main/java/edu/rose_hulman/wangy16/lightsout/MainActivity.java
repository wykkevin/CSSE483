package edu.rose_hulman.wangy16.lightsout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private LightsOutGame mGame;
    private Button mnew_game_button;
    private Button[] mButtons;
    private TextView mGameStateTextView;
    private boolean mWin;
    private int[] mValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.mGame = new LightsOutGame(7);
        this.mnew_game_button = findViewById(R.id.new_button);
        mnew_game_button.setOnClickListener(this);
        this.mGameStateTextView = findViewById(R.id.textView);

        this.mWin = false;
        this.mValues = new int[7];
        this.mButtons = new Button[7];
        for (int i = 0; i < 7; i++) {
            int id = getResources().getIdentifier("button" + i, "id", getPackageName());
            mButtons[i] = findViewById(id);
            mButtons[i].setOnClickListener(this);
            if (savedInstanceState != null) {
                mValues = savedInstanceState.getIntArray("CurrentState");
                mGame.setAllValues(mValues);
                mGameStateTextView.setText(savedInstanceState.getString("CurrentText"));
                mWin = savedInstanceState.getBoolean("CheckWin");
                mGame.setNumPresses(savedInstanceState.getInt("CurrentPress"));
            }
        }
        updateView();
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("CurrentText", mGameStateTextView.getText().toString());
        outState.putBoolean("CheckWin", mWin);
        for (int i = 0; i < 7; i++) {
            mValues[i] = mGame.getValueAtIndex(i);
        }
        outState.putIntArray("CurrentState", mValues);
        outState.putInt("CurrentPress",mGame.getNumPresses());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.new_button) {
            mGame = new LightsOutGame(7);
            mWin = false;
            for (int i = 0; i < 7; i++) {
                mButtons[i].setEnabled(true);
            }
            updateView();
        }
        for (int i = 0; i < 7; i++) {
            if (v.getId() == mButtons[i].getId()) {
                mWin = mGame.pressedButtonAtIndex(i);
                updateView();
            }
        }
    }

    private void updateView() {
        for (int i = 0; i < 7; i++) {
            mButtons[i].setText(mGame.getValueAtIndex(i) + "");
        }
        if (mGame.getNumPresses() != 0) {
            String s = getResources().getQuantityString(R.plurals.message_format, mGame.getNumPresses(), mGame.getNumPresses());
            mGameStateTextView.setText(s);
        } else {
            mGameStateTextView.setText(getResources().getText(R.string.start_string));
        }
        if (mWin == true) {
            mGameStateTextView.setText(getResources().getText(R.string.win));
            for (int i = 0; i < 7; i++) {
                mButtons[i].setEnabled(false);
            }
        }
    }
}