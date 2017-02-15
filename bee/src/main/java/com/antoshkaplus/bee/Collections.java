package com.antoshkaplus.bee;

/**
 * Created by antoshkaplus on 7/3/16.
 */
public class Collections {

    public static int leftSearch(int a[], int key, int l, int h) {
        if (l<=h) {
            int mid = (l+h)/2;
            if (a[mid] == key) {
                if (mid > 0 && a[mid-1] == key) {
                    return leftSearch(a, key, l, mid-1);
                } else {
                    return mid;
                }
            }
            if (a[mid] > key) {
                return leftSearch(a, key, l, mid-1);
            } else {
                return leftSearch(a, key, mid+1, h);
            }
        }
        return -1;
    }

    public static int rightSearch(int a[], int key, int l, int h) {
        if (l<=h) {
            int mid = (l+h)/2;
            if (a[mid] == key) {
                if (mid<h && a[mid+1] == key) {
                    return rightSearch(a, key, mid + 1, h);
                } else {
                    return mid;
                }
            }
            if (a[mid] > key) {
                return rightSearch(a, key, l, mid-1);
            } else {
                return rightSearch(a, key, mid+1, h);
            }
        }
        return -1;
    }


}
