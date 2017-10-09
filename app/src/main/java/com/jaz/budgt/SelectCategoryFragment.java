package com.jaz.budgt;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jaz on 8/28/17.
 */

public class SelectCategoryFragment extends DialogFragment implements ExpandableListView.OnChildClickListener {
    LocalStorage localStorage;
    Map<String,ArrayList<String>> categories = new HashMap<>(0);
    ArrayList<String> headers = new ArrayList<>(0);

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        localStorage  = new LocalStorage(this.getActivity());
        categories = localStorage.loadCategories();

        //todo make this show categories in groups
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.select_category);
        ExpandableListView expandableListView = new ExpandableListView(getActivity());
        headers = new ArrayList<>(categories.keySet());
        ExpandableListAdapter expandableListAdapter = new com.jaz.budgt.ExpandableListAdapter(getActivity(),headers,categories);
        expandableListView.setAdapter(expandableListAdapter);

        builder.setView(expandableListView);
        AlertDialog dialog = builder.create();
        dialog.show();

        // Create the AlertDialog object and return it
        return builder.create();
    }

    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        ArrayList<String> header = categories.get(headers.get(groupPosition));
        onSelectedListener.categorySelected(header.get(childPosition));
        return false;
    }

    interface OnSelectedListener {
        void categorySelected(String category);
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