package com.antoshkaplus.fly.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.antoshkaplus.fly.R;

/**
 * Created by antoshkaplus on 2/27/17.
 */

public class AddItemDialog extends AddStringDialog {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog dialog = (AlertDialog) super.onCreateDialog(savedInstanceState);
        View view = dialog.findViewById(R.id.content);

        LinearLayout newView = (LinearLayout)getActivity().getLayoutInflater().inflate(R.layout.view_add_item, null);
        dialog.setView(newView);

        //
////        // now we need to inflate add item view

//        newView.addView(view);

        return dialog;
    }
}
