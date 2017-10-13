package com.jaz.budgt;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by jaz on 10/12/17.
 */

public class AccountListFragment extends Fragment {
    public static AccountListFragment newInstance() {
        return new AccountListFragment();
    }
    ArrayList<Account> accounts = new ArrayList<>(0);
    private ListView listview;
    LocalStorage localStorage;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        localStorage  = new LocalStorage(this.getActivity());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){inflater.inflate(R.menu.transaction_list_options, menu);}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.accounts_overview_fragment, container, false);
        accounts = localStorage.loadAccounts();

        listview = view.findViewById(R.id.account_overview_list);

        final AccountListAdapter adapter = new AccountListAdapter(getContext(),accounts);
        listview.setAdapter(adapter);

        return view;
    }
}
