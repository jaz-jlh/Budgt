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
    ArrayList<Account> accounts = new ArrayList<>(0);

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        localStorage  = new LocalStorage(this.getActivity());
        accounts = localStorage.loadAccounts();
        String[] accountsArray = new String[accounts.size()];
        for(int i = 0; i< accounts.size(); i++) {
            accountsArray[i] = accounts.get(i).getName();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.select_account)
                .setItems(accountsArray, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position of the selected item
                        onSelectedListener.accountSelected(accountsArray[which]);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    interface OnSelectedListener {
        void accountSelected(String accountName);
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