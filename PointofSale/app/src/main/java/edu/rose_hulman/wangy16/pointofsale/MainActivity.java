package edu.rose_hulman.wangy16.pointofsale;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {
    private Item mCurrentItem;
    private TextView mNameTextView;
    private TextView mQuantityTextView;
    private TextView mDateTextView;
    private Item mClearedItem;
    private ArrayList<Item> mItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mNameTextView = findViewById(R.id.name_text);
        mQuantityTextView = findViewById(R.id.quantity_text);
        mDateTextView = findViewById(R.id.date_text);
        mItems = new ArrayList<>();

        registerForContextMenu(mNameTextView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addEditItem(false);
            }
        });
    }

    private void addEditItem(final boolean inEditing) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String title = inEditing ? "Edit an item" : "Add an item";
        builder.setTitle(title);

        View view = getLayoutInflater().inflate(R.layout.dialog_add, null, false);

        final EditText nameEditText = view.findViewById(R.id.edit_name);
        final EditText quantityText = view.findViewById(R.id.edit_quantity);
        final CalendarView calenderView = view.findViewById(R.id.calendar_view);
        final GregorianCalendar calendar = new GregorianCalendar();
        calenderView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
            }
        });

        if (inEditing) {
            nameEditText.setText(mCurrentItem.getName());
            quantityText.setText(Integer.toString(mCurrentItem.getQuantity()));
            calenderView.setDate(mCurrentItem.getDeliveryDateTime());
        }

        builder.setView(view);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = nameEditText.getText().toString();
                int quantity = Integer.parseInt(quantityText.getText().toString());
                if (inEditing) {
                    mCurrentItem.setName(name);
                    mCurrentItem.setQuantity(quantity);
                    mCurrentItem.setDeliveryDate(calendar);
                } else {
                    mCurrentItem = new Item(name, quantity, calendar);
                    mItems.add(mCurrentItem);
                }
                showCurrentItem();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.create().show();
    }


    private void showCurrentItem() {
        mNameTextView.setText(getString(R.string.name_format, mCurrentItem.getName()));
        mQuantityTextView.setText(getString(R.string.quantity_format, mCurrentItem.getQuantity()));
        mDateTextView.setText(getString(R.string.date_format, mCurrentItem.getDeliveryDateString()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.context_edit) {
            addEditItem(true);
            return true;
        } else if (id == R.id.context_remove) {
            removeItem();
            return true;
        }

        return super.onContextItemSelected(item);
    }

    private void removeItem() {
        mItems.remove(mCurrentItem);
        mCurrentItem = new Item();
        showCurrentItem();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(Settings.ACTION_SETTINGS));
            return true;
        } else if (id == R.id.action_reset) {
            mClearedItem = mCurrentItem;
            mCurrentItem = new Item();
            showCurrentItem();
            Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator_layout), "Item Cleared", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCurrentItem = mClearedItem;
                    mClearedItem = null;
                    showCurrentItem();
                    Snackbar.make(findViewById(R.id.coordinator_layout), "Item is restored", Snackbar.LENGTH_LONG).show();

                }
            });
            snackbar.show();
            return true;
        } else if (id == R.id.action_search) {
            showSearchDialog();
            return true;
        } else if (id == R.id.action_clear_all) {
            clearall();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void clearall() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Remove");
        builder.setMessage("Are you sure you want to remove all items?");
        builder.setNegativeButton(android.R.string.cancel,null);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mItems = new ArrayList<Item>();
                mCurrentItem = new Item();
                showCurrentItem();
            }
        });
        builder.create().show();
    }

    private void showSearchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an item");
        builder.setItems(getNames(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mCurrentItem = mItems.get(which);
                showCurrentItem();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.create().show();
    }

    private String[] getNames() {
        String[] names = new String[mItems.size()];
        for (int i = 0; i < mItems.size(); i++) {
            names[i] = mItems.get(i).getName();
        }
        return names;
    }
}

//TODO: Get Names