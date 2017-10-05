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

public class SelectAccountFragment extends DialogFragment {
    LocalStorage localStorage;
    ArrayList<String> paymentTypeList = new ArrayList<>(0);
    String[] paymentTypes = {"Discover card","PNC Debit card","Cash"};

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        localStorage  = new LocalStorage(this.getActivity());
        paymentTypeList = localStorage.loadPaymentTypes();
        paymentTypes = paymentTypeList.toArray(new String[0]);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.select_payment_type)
                .setItems(paymentTypes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        onSelectedListener.paymentTypeSelected(which);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    interface OnSelectedListener {
        void paymentTypeSelected(int index);
    }

    private OnSelectedListener onSelectedListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.onSelectedListener = (OnSelectedListener) activity;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement SelectAccountFragment.OnSelectedListener");
        }
    }

}