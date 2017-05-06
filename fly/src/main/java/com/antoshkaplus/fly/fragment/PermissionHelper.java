package com.antoshkaplus.fly.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import java.util.Arrays;

/**
 * Created by antoshkaplus on 5/5/17.
 */

public class PermissionHelper extends Fragment {


    private static final int REQUEST_PERMISSIONS = 10;
    public static final String TAG = "PermissionHelper";

    private PermissionCallback mCallback;
    private static boolean sCameraMicPermissionDenied;

    public void setPermissions(String[] permissions) {
        this.permissions = permissions;
    }

    private String[] permissions = new String[0];


    public static PermissionHelper newInstance() {
        return new PermissionHelper();
    }

    public PermissionHelper() {}

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        permissions = args.getStringArray("Permissions");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    public void checkPermissions() {
        Activity a = getActivity();
        if (all(permissions)) {
            mCallback.onPermissionGranted();
        } else {
            requestPermissions(permissions, REQUEST_PERMISSIONS);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {

        if (requestCode == REQUEST_PERMISSIONS) {
            if (all(grantResults)) {
                mCallback.onPermissionGranted();
            } else {
                Log.i("BaseActivity", "LOCATION permission was NOT granted.");
                mCallback.onPermissionDenied();
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private boolean all(String[] ss) {
        Activity a = getActivity();
        for (String s : ss) {
            if (a.checkSelfPermission(s) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private boolean all(int[] rr) {
        for (Integer r : rr) {
            if (r != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public void setCallback(PermissionCallback mCallback) {
        this.mCallback = mCallback;
    }


    public interface PermissionCallback {
        void onPermissionGranted();
        void onPermissionDenied();
    }


}
