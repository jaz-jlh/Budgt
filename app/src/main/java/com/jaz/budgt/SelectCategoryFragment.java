package com.jaz.budgt;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import com.jaz.budgt.adapters.CategoryExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jaz on 8/28/17.
 */

public class SelectCategoryFragment extends DialogFragment{
    LocalStorage localStorage;
    Map<String,ArrayList<String>> categories = new HashMap<>(0);
    ArrayList<String> headers = new ArrayList<>(0);

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        localStorage  = new LocalStorage(this.getActivity());
        categories = localStorage.loadCategories();

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.select_category);
        ExpandableListView expandableListView = new ExpandableListView(getActivity());
        headers = new ArrayList<>(categories.keySet());
        CategoryExpandableListAdapter categoryExpandableListAdapter = new CategoryExpandableListAdapter(getActivity(),headers,categories);
        expandableListView.setAdapter(categoryExpandableListAdapter);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener(){
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //Toast.makeText(getActivity(), "Category selected", Toast.LENGTH_SHORT).show();
                ArrayList<String> header = categories.get(headers.get(groupPosition));
                onSelectedListener.categorySelected(header.get(childPosition));
                dismiss();
                return true;
            }
        });
        builder.setView(expandableListView);
        AlertDialog dialog = builder.create();
        dialog.show();

        // Create the AlertDialog object and return it
        return dialog;
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