package com.seya.todoapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.seya.todoapp.data.ToDo;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class EditItemActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final DateFormat dateFormat = DateFormat.getDateInstance();

    private EditText etEditItem;
    private EditText etDueDate;

    private ToDo toDo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        toDo = (ToDo) getIntent().getSerializableExtra("todo");
        etEditItem = (EditText) findViewById(R.id.etEditItem);
        etEditItem.setText(toDo.text);

        etDueDate = (EditText) findViewById(R.id.etDueDate);
        if (toDo.dueDate != null) {
            etDueDate.setText(dateFormat.format(toDo.dueDate));
            DateColoringUtil.setDueDateColor(etDueDate, toDo.dueDate);
        }
        etDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                DatePickerFragment.newInstance(toDo.dueDate).show(fm, "tag");
            }
        });
    }

    public void onSave(View view) throws ParseException {
        toDo.text = etEditItem.getText().toString();
        String dueDate = etDueDate.getText().toString();
        if (!dueDate.equals("")) {
            toDo.dueDate = dateFormat.parse(dueDate);
        } else {
            toDo.dueDate = null;
        }

        Intent data = new Intent();
        data.putExtra("todo", toDo);

        setResult(RESULT_OK, data);
        this.finish();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        Date dueDate = calendar.getTime();
        toDo.dueDate = dueDate;
        etDueDate.setText(dateFormat.format(dueDate));
        DateColoringUtil.setDueDateColor(etDueDate, dueDate);
    }
}