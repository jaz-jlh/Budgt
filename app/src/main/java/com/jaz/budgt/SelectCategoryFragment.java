package com.jaz.budgt;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by jaz on 8/28/17.
 */

public class SelectCategoryFragment extends DialogFragment {

    static final String CATEGORIES_TAG = "Categories";
    SharedPreferences sharedPreferences;
    ArrayList<String> categoryList = new ArrayList<>(0);
    String[] categories = {"Groceries","Transportation","Meals Out"};

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        loadCategories();
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

    public void loadCategories() {
        Gson gson = new Gson();
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.categories_file_name), Context.MODE_PRIVATE);
        String jsonCategoryList = sharedPreferences.getString(CATEGORIES_TAG,"");
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        categoryList = gson.fromJson(jsonCategoryList, type);
        if(categoryList == null) {
            categoryList = new ArrayList<>(0);
        }
        categories = new String[categoryList.size()];
        for(int i=0; i <categoryList.size(); i++) {
            categories[i] = categoryList.get(i);
        }
    }
}