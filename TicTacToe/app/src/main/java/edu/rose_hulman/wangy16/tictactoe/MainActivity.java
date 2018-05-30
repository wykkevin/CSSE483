package edu.rose_hulman.wangy16.tictactoe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TicTacToeGame mGame;
    private Button mnew_game_button;
    private Button[][] mButtons;
    private TextView mGameStateTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGame = new TicTacToeGame(this);
        mnew_game_button = findViewById(R.id.new_game_button);
        mnew_game_button.setOnClickListener(this);
        mGameStateTextView = findViewById(R.id.textView);

        mButtons = new Button[TicTacToeGame.NUM_ROWS][TicTacToeGame.NUM_COLUMNS];
        for (int row = 0; row<TicTacToeGame.NUM_ROWS; row++) {
            for (int col = 0; col < TicTacToeGame.NUM_COLUMNS; col++) {
                int id = getResources().getIdentifier("button" + row + col, "id", getPackageName());
                mButtons[row][col] = findViewById(id);
                mButtons[row][col].setOnClickListener(this);
            }
        }
    }

    private void updateView(){
        for (int row = 0; row<TicTacToeGame.NUM_ROWS; row++){
            for (int col = 0;col<TicTacToeGame.NUM_COLUMNS; col++){
                mButtons[row][col].setText(mGame.stringForButtonAtLocation(row,col));
            }
        }
        mGameStateTextView.setText(mGame.stringForGameState());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.new_game_button) {
            Log.d("Tic", "new game button pressed");
            mGame.resetGame();
        }
        for (int row = 0; row<TicTacToeGame.NUM_ROWS; row++){
            for (int col = 0;col<TicTacToeGame.NUM_COLUMNS; col++){
                if (v.getId() == mButtons[row][col].getId()){
                    Log.d("Tic","Button"+row+col+" pressed");
                    mGame.pressedButtonAtLocation(row,col);
                    updateView();
                }
                mButtons[row][col].setText(mGame.stringForButtonAtLocation(row,col));
            }
        }
        mGameStateTextView.setText(mGame.stringForGameState());
    }
}
