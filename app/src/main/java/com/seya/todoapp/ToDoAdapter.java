package com.seya.todoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.seya.todoapp.data.ToDo;

import java.text.DateFormat;
import java.util.List;

public class ToDoAdapter extends ArrayAdapter<ToDo> {

    private static final DateFormat dateFormat = DateFormat.getDateInstance();

    public ToDoAdapter(Context context, List<ToDo> toDos) {
        super(context, 0, toDos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ToDo toDo = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_todo, parent, false);
        }
        // Lookup view for data population
        TextView tvText = (TextView) convertView.findViewById(R.id.tvText);
        // Populate the data into the template view using the data object
        tvText.setText(toDo.text);
        // Lookup view for data population
        TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);
        // Populate the data into the template view using the data object
        if (toDo.dueDate == null) {
            tvDate.setText("");
        } else {
            tvDate.setText(dateFormat.format(toDo.dueDate));
            DateColoringUtil.setDueDateColor(tvDate, toDo.dueDate);
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
