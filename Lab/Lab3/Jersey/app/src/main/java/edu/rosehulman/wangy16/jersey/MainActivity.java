package edu.rosehulman.wangy16.jersey;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.provider.Settings;

public class MainActivity extends AppCompatActivity {
    private TextView mNameTextView;
    private TextView mNumberTextView;
    private ImageView mImage;
    private Jersey mCurrentJersey;
    private Jersey mClearedJersey;
    private Switch mSwitchColor;
    private final static String PREFS = "PREFS";
    private static final String KEY_JERSEY_NAME = "KEY_JERSEY_NAME";
    private static final String KEY_JERSEY_NUMBER = "KEY_JERSEY_NUMBER";
    private static final String KEY_JERSEY_COLOR = "KEY_JERSEY_COLOR";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mNameTextView = findViewById(R.id.jersey_name);
        mNumberTextView = findViewById(R.id.jersey_number);
        mImage = findViewById(R.id.jersey_image);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editJersey();
            }
        });

        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        String name = prefs.getString(KEY_JERSEY_NAME,getString(R.string.default_jersey_name));
        int number = prefs.getInt(KEY_JERSEY_NUMBER,17);
        boolean color = prefs.getBoolean(KEY_JERSEY_COLOR,false);
        mCurrentJersey=new Jersey(name,number,color);
        showCurrentJersey();
    }

    private void editJersey() {
//        mCurrentJersey.setColor(false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_edit, null, false);
        final EditText nameEditText = view.findViewById(R.id.edit_name);
        final EditText numberEditText = view.findViewById(R.id.edit_number);
        mSwitchColor = view.findViewById(R.id.edit_color);

        nameEditText.setText(mNameTextView.getText());
        numberEditText.setText(mNumberTextView.getText());
        mSwitchColor.setChecked(mCurrentJersey.getColor());


        mSwitchColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentJersey.setColor(mSwitchColor.isChecked());
            }
        });

        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = nameEditText.getText().toString();
                int number = 0;
                if (!numberEditText.getText().toString().matches("")) {
                    number = Integer.parseInt(numberEditText.getText().toString());
                }
                mCurrentJersey = new Jersey(name, number, mCurrentJersey.getColor());
                showCurrentJersey();
            }
        });

        builder.setView(view);
        builder.create().show();
    }

    private void showCurrentJersey() {
        mNameTextView.setText(mCurrentJersey.getName());
        mNumberTextView.setText(mCurrentJersey.getNumber() + "");
        if (mCurrentJersey.getColor()) {
            mImage.setImageResource(R.drawable.blue_jersey);
        } else {
            mImage.setImageResource(R.drawable.red_jersey);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(intent);
            return true;
        }else if (id ==R.id.action_reset){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.confirmation_dialog_message);
            builder.setNegativeButton(android.R.string.cancel,null);
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mClearedJersey = mCurrentJersey;
                    mCurrentJersey=new Jersey();
                    showCurrentJersey();
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator_layout), "Jersey Cleared", Snackbar.LENGTH_LONG);
                    snackbar.setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mCurrentJersey = mClearedJersey;
                            mClearedJersey = null;
                            showCurrentJersey();
                            Snackbar.make(findViewById(R.id.coordinator_layout), "Jersey is restored", Snackbar.LENGTH_LONG).show();

                        }
                    });
                    snackbar.show();
                }
            });
            builder.create().show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_JERSEY_NAME, mCurrentJersey.getName());
        editor.putInt(KEY_JERSEY_NUMBER, mCurrentJersey.getNumber());
        editor.putBoolean(KEY_JERSEY_COLOR, mCurrentJersey.getColor());
        editor.commit();
    }

}
