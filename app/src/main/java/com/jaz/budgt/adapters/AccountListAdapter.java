package com.jaz.budgt.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jaz.budgt.Account;
import com.jaz.budgt.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by jaz on 10/12/17.
 */

public class AccountListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Account> mDataSource;

    public AccountListAdapter(Context context, ArrayList<Account> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() { return mDataSource.size(); }

    @Override
    public Object getItem(int position) { return mDataSource.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get view for row item
        //todo fix this warning
        View rowView = mInflater.inflate(R.layout.account_list_item, parent, false);
        TextView accountNameTextView, accountValueTextView;
        accountNameTextView = (TextView) rowView.findViewById(R.id.account_name);
        accountValueTextView = rowView.findViewById(R.id.account_total_value);

        Account account = mDataSource.get(position);

        accountNameTextView.setText(account.getName());
        NumberFormat formatter = new DecimalFormat("#0.00");
        String amount = "$" + formatter.format(account.getTotalValue());
        accountValueTextView.setText(amount);

        return rowView;
    }
}
