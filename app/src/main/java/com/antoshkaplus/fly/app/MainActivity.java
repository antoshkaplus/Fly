package com.antoshkaplus.fly.app;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

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

    void showListSearch(View view) {
        Intent i = new Intent(this, ListSearchActivity.class);
        startActivity(i);
    }


}
