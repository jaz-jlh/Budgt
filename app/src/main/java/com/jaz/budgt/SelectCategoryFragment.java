package com.jaz.budgt;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import java.util.ArrayList;

/**
 * Created by jaz on 8/28/17.
 */

public class SelectCategoryFragment extends DialogFragment {
    LocalStorage localStorage;
    ArrayList<CategoryGroup> categoryList = new ArrayList<>(0);
    String[] categories = {"Groceries","Transportation","Meals Out"};

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        localStorage  = new LocalStorage(this.getActivity());
        categoryList = localStorage.loadCategories();
        int categoryCount = 0;
        for(int i = 0; i < categoryList.size(); i++) {
            categoryCount += categoryList.get(i).getSubcategories().size();
        }
        categories = new String[categoryCount];
        for(int i = 0; i < categoryList.size(); i++) {
            for(int j = 0; j < categoryList.get(i).getSubcategories().size(); j++) {
                categories[i+j] = categoryList.get(i).getSubcategories().get(j).toString();
            }
        }
        //todo maybe make this less convoluted? could at least create getStringArray method to CategoryGroup
        //todo make this show categories in groups
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.select_category)
                .setItems(categories, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        onSelectedListener.categorySelected(which);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    interface OnSelectedListener {
        void categorySelected(int index);
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