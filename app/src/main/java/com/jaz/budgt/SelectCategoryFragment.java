package com.jaz.budgt;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jaz on 8/28/17.
 */

public class SelectCategoryFragment extends DialogFragment {
    LocalStorage localStorage;
    Map<String,ArrayList<String>> categories = new HashMap<>(0);

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        localStorage  = new LocalStorage(this.getActivity());
        categories = localStorage.loadCategories();

        //todo make this show categories in groups
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.select_category);
//                .setItems(categories, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        // The 'which' argument contains the index position
//                        // of the selected item
//                        //todo fix this
//                        onSelectedListener.categorySelected(which);
//                    }
//                });

        ExpandableListView myList = new ExpandableListView(getActivity());
        ExpandableListAdapter myAdapter = new ExpandableListAdapter() {
        };
        myList.setAdapter(myAdapter);

        builder.setView(myList);
        AlertDialog dialog = builder.create();
        dialog.show();

        // Create the AlertDialog object and return it
        return builder.create();
    }

    interface OnSelectedListener {
        void categorySelected(Category category);
    }

    private OnSelectedListener onSelectedListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.onSelectedListener = (OnSelectedListener) activity;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement SelectCategoryFragment.OnSelectedListener");
        }
    }

}