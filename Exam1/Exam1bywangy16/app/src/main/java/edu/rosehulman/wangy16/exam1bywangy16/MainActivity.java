package edu.rosehulman.wangy16.exam1bywangy16;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.time.temporal.TemporalAdjusters;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {

    PythagoreanGenerator mpythagoreanGenerator = new PythagoreanGenerator();
    int correctNumber = 0;
    int incorrectNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newQuestion();
            }
        });
    }

    private void newQuestion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String title = getResources().getString(R.string.triple_question);
        builder.setTitle(title);

        View view = getLayoutInflater().inflate(R.layout.dialog_add, null, false);
        TextView tripleTextView = view.findViewById(R.id.triple);
        Triple newTriple = mpythagoreanGenerator.generatePotentialTriple();
        tripleTextView.setText(newTriple.toString());
        builder.setView(view);

        final TextView correct_number = findViewById(R.id.correct_number_textview);
        final TextView incorrect_number = findViewById(R.id.incorrect_number_textview);
        final TextView start_text = findViewById(R.id.start_textview);
        final boolean isTriple = mpythagoreanGenerator.isPotentialATrueTriple();

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isTriple) {
                    correct_number.setText((Integer.parseInt(correct_number.getText().toString()) + 1) + "");
                    correctNumber++;
                } else {
                    incorrect_number.setText((Integer.parseInt(incorrect_number.getText().toString()) + 1) + "");
                    incorrectNumber++;
                }
                start_text.setText(getResources().getString(R.string.true_triple) + " " + mpythagoreanGenerator.getTrueTriple().toString());
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!isTriple) {
                    correct_number.setText((Integer.parseInt(correct_number.getText().toString()) + 1) + "");
                    correctNumber++;
                } else {
                    incorrect_number.setText((Integer.parseInt(incorrect_number.getText().toString()) + 1) + "");
                    incorrectNumber++;
                }
                start_text.setText(getResources().getString(R.string.true_triple) + " " + mpythagoreanGenerator.getTrueTriple().toString());
            }
        });
        builder.create().show();
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
        if (id == R.id.action_reset) {
            final int tempCorrect = correctNumber;
            final int tempIncorrect = incorrectNumber;
            correctNumber = 0;
            incorrectNumber = 0;

            final TextView correct_number = findViewById(R.id.correct_number_textview);
            final TextView incorrect_number = findViewById(R.id.incorrect_number_textview);

            correct_number.setText(R.string.correct_number);
            incorrect_number.setText(R.string.incorrect_number);

            Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator_layout), getResources().getText(R.string.reset_scores), Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    correct_number.setText(tempCorrect + "");
                    incorrect_number.setText(tempIncorrect + "");
                    correctNumber = tempCorrect;
                    incorrectNumber = tempIncorrect;
                }
            });
            snackbar.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
