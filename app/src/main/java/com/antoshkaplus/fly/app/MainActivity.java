package com.antoshkaplus.fly.app;

import android.Manifest;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.antoshkaplus.fly.fragment.PermissionHelper;

/**
 * We need this application to check that everything works as expected visually
 *
 * extends usual view beacuse of static buttons in linear layout
 * maybe make it scrollable if would be many
 */
public class MainActivity extends Activity implements PermissionHelper.PermissionCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    void showListSearch(View view) {
        Intent i = new Intent(this, ListSearchActivity.class);
        startActivity(i);
    }

    void checkPermissions() {
        PermissionHelper helper = new PermissionHelper();
        Bundle b = new Bundle();
        b.putStringArray("Permissions", new String[]{Manifest.permission.GET_ACCOUNTS});
        helper.setArguments(b);
        helper.setCallback(this);
        helper.checkPermissions();
    }


    @Override
    public void onPermissionGranted() {
        Log.i("PERMISSIONS", "Permission Granted");
    }

    @Override
    public void onPermissionDenied() {
        Log.i("PERMISSIONS", "Permission Denied");
    }
}
