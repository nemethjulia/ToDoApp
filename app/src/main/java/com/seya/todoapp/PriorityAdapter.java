package com.seya.todoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.seya.todoapp.data.Priority;

import java.util.List;

public class PriorityAdapter extends ArrayAdapter<Priority> {

    public PriorityAdapter(Context context, List<Priority> priorities) {
        super(context, 0, priorities);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        Priority priority = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_priority, parent, false);
        }
        TextView tvPriority = (TextView) convertView.findViewById(R.id.tvPriority);
        tvPriority.setText(priority.getName());
        return convertView;
    }
}
