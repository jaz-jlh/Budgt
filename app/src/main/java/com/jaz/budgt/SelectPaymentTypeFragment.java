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
import java.sql.Array;
import java.util.ArrayList;

import static com.jaz.budgt.TransactionListFragment.PAYMENT_TYPE_TAG;

/**
 * Created by jaz on 8/28/17.
 */

public class SelectPaymentTypeFragment extends DialogFragment {
    SharedPreferences sharedPreferences;
    ArrayList<String> paymentTypeList = new ArrayList<>(0);
    String[] paymentTypes = {"Discover card","PNC Debit card","Cash"};

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        loadPaymentTypes();
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
            throw new ClassCastException(activity.toString() + " must implement SelectPaymentTypeFragment.OnSelectedListener");
        }
    }

    public void loadPaymentTypes() {
        Gson gson = new Gson();
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.payment_types_file_name), Context.MODE_PRIVATE);
        String jsonPaymentTypeList = sharedPreferences.getString(PAYMENT_TYPE_TAG,"");
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        paymentTypeList = gson.fromJson(jsonPaymentTypeList, type);
        if(paymentTypeList == null) {
            paymentTypeList = new ArrayList<>(0);
        }
        paymentTypes = new String[paymentTypeList.size()];
        for(int i=0; i <paymentTypeList.size(); i++) {
            paymentTypes[i] = paymentTypeList.get(i);
        }
    }

}