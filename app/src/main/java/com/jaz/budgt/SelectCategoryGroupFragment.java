package com.jaz.budgt;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jaz on 10/11/17.
 */

public class SelectCategoryGroupFragment extends DialogFragment{
    LocalStorage localStorage;
    Map<String,ArrayList<String>> categories = new HashMap<>(0);
    ArrayList<String> headers = new ArrayList<>(0);

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.onSelectedListener = (SelectCategoryGroupFragment.OnSelectedListener) getParentFragment();
        //todo fix this stupid error and maybe actually understand fragment transactions
        // Use the Builder class for convenient dialog construction
        localStorage  = new LocalStorage(this.getActivity());
        categories = localStorage.loadCategories();
        headers = new ArrayList<>(categories.keySet());
        String[] categoryHeaders = new String[headers.size()];
        categoryHeaders = headers.toArray(categoryHeaders);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.select_category_group)
                .setItems(categoryHeaders, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position of the selected item
                        onSelectedListener.categoryGroupSelected(headers.get(which));
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    interface OnSelectedListener {
        void categoryGroupSelected(String categoryGroup);
    }

    private SelectCategoryGroupFragment.OnSelectedListener onSelectedListener;

//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        try {
//            this.onSelectedListener = (SelectCategoryGroupFragment.OnSelectedListener) getParentFragment();
//        }
//        catch (final ClassCastException e) {
//            throw new ClassCastException(activity.toString() + " must implement SelectCategoryGroupFragment.OnSelectedListener");
//        }
//    }
}
