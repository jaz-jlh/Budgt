package com.jaz.budgt.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jaz.budgt.R;

/**
 * Created by jaz on 10/15/17.
 */

public class SettingsListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private String[] mDataSource;
    private boolean isEnabled = true;

    public SettingsListAdapter(Context context, String[] items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() { return mDataSource.length; }

    @Override
    public Object getItem(int position) { return mDataSource[position]; }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get view for row item
        View rowView;
        if(convertView == null) { rowView = mInflater.inflate(R.layout.settings_list_item, parent, false); }
        else { rowView = convertView;}

        TextView itemName = rowView.findViewById(R.id.settings_list_item);
        itemName.setText(mDataSource[position]);
        return rowView;
    }

    public void setIsEnabled(boolean val) {
        isEnabled = val;
    }

    @Override
    public boolean isEnabled(int position) {
        return isEnabled;
    }
}

