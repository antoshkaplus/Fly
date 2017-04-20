package com.antoshkaplus.fly.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.antoshkaplus.fly.dialog.AddItemDialog;
import com.antoshkaplus.fly.dialog.RetryDialog;

/**
 * We need this application to check that everything works as expected visually
 *
 * extends usual view beacuse of static buttons in linear layout
 * maybe make it scrollable if would be many
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showListSearch(View view) {
        Intent i = new Intent(this, ListSearchActivity.class);
        startActivity(i);
    }

    public void showAddItemDialog(View view) {
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        AddItemDialog dialog = new AddItemDialog();
        Bundle args = new Bundle();
        args.putString(AddItemDialog.ARG_TITLE, getString(R.string.dialog__add_title));
        dialog.setArguments(args);
        dialog.setAddStringDialogListener(new AddItemDialog.AddStringDialogListener() {
            @Override
            public void onAddStringDialogSuccess(CharSequence string) {
                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onAddStringDialogCancel() {
                Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show(ft, "dialog");

    }

    public void showAlertDialog(View view) {
        View dialogView = getLayoutInflater().inflate(R.layout.view_string_dialog, null);
        dialogView.setBackgroundColor(Color.YELLOW);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle("Your Title");

        // set dialog message
        alertDialogBuilder
                .setView(dialogView)
                .setMessage("Click yes to exit!")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });



        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }
}
