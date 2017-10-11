package com.jaz.budgt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jaz on 9/2/17.
 */

public class TransactionListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Transaction> mDataSource;

    public TransactionListAdapter(Context context, ArrayList<Transaction> items) {
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
        View rowView = mInflater.inflate(R.layout.list_item_transaction, parent, false);
        TextView dateTextView, amountTextView, descriptionTextView, categoryTextView, accountTextView;
        dateTextView = (TextView) rowView.findViewById(R.id.list_item_date);
        amountTextView = (TextView) rowView.findViewById(R.id.list_item_amount);
        descriptionTextView = (TextView) rowView.findViewById(R.id.list_item_description);
        categoryTextView = (TextView) rowView.findViewById(R.id.list_item_category);
        accountTextView = (TextView) rowView.findViewById(R.id.list_item_account);

        Transaction transaction = (Transaction) getItem(position);
        dateTextView.setText(transaction.getMonth() + "/" + transaction.getDay());
        String amount = "$" + transaction.getDollarAmount();
        if(transaction.getDollarAmount()>99) amountTextView.setText(amount);
        else amountTextView.setText(transaction.getStringAmount());
        descriptionTextView.setText(transaction.getDescription());
        categoryTextView.setText(transaction.getCategory());
        accountTextView.setText(transaction.getAccount());

//        if(transaction.isExpense() == 1) {
//            float fraction = (float) (transaction.getDollarAmount() / 300.);
//            int color = (int) (fraction * 0xff);
//            if(transaction.getDollarAmount() > 300) color = 0xff;
//            String colorString = "ff" + Integer.toHexString(color) + "0000";
//            color = (int) Long.parseLong(colorString, 16);
//            amountTextView.setTextColor(color);
//        } else amountTextView.setTextColor(Color.GREEN);
        //TODO fix this weirdness with colors - like wtf??
        if(!transaction.isExpense()) {
            amountTextView.setTextColor(0xFF4CAF50);
        }

        return rowView;
    }
}
