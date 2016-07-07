package com.antoshkaplus.fly.app;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

import com.antoshkaplus.fly.app.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by antoshkaplus on 6/13/16.
 */
public class ListSearchActivity extends ListActivity implements SearchView.OnQueryTextListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            String[] words = Util.readWords(this);
            ListAdapter adapter = new ListSearchAdapter(this, Arrays.asList(words));

            setListAdapter(adapter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_search, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        SearchView filterView = (SearchView) menu.findItem(R.id.filter).getActionView();
        filterView.setSubmitButtonEnabled(false);
        filterView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String action = intent.getAction();
        if (Intent.ACTION_SEARCH.equals(action)) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Uri uri = WordContentProvider.Words.CONTENT_URI;
            uri = uri.buildUpon().appendQueryParameter("word", query).build();
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor.getCount() == 0) {
                return;
            }
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_INTENT_EXTRA_DATA);
            int position = cursor.getInt(index);
            cursor.close();
            getListView().setSelection(position);
        } else if (Intent.ACTION_VIEW.equals(action)) {
            // Handle a suggestions click (because the suggestions all use ACTION_VIEW)
            Bundle b = intent.getExtras();
            int position = Integer.parseInt(b.getString(SearchManager.EXTRA_DATA_KEY, "0"));
            getListView().setSelection(position);
        }
    }

    @Override
    public boolean onQueryTextChange(String s) {
        ListSearchAdapter adapter = (ListSearchAdapter) getListAdapter();
        adapter.getFilter().filter(s);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }


}
