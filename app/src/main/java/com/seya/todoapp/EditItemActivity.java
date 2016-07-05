package com.seya.todoapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.seya.todoapp.data.Priority;
import com.seya.todoapp.data.ToDo;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class EditItemActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final DateFormat dateFormat = DateFormat.getDateInstance();

    private EditText etText;
    private EditText etDescription;
    private EditText etDueDate;
    private Spinner spPriority;

    private ToDo toDo;

    public void onSave(View view) throws ParseException {
        String text = etText.getText().toString();
        if (text.equals("")) {
            Toast.makeText(this, "Can't save the todo without text!", Toast.LENGTH_SHORT).show();
        } else {
            toDo.text = text;
            toDo.description = etDescription.getText().toString();
            toDo.priority = (Priority) spPriority.getSelectedItem();
            Intent data = new Intent();
            data.putExtra("todo", toDo);
            setResult(RESULT_OK, data);
            this.finish();
        }
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        toDo = (ToDo) getIntent().getSerializableExtra("todo");
        if (toDo == null) {
            toDo = new ToDo();
        }
        setProperties();
    }

    private void setProperties() {
        setText();
        setDescription();
        setDueDate();
        setPriority();
    }

    private void setText() {
        etText = (EditText) findViewById(R.id.etText);
        etText.setText(toDo.text);
    }

    private void setDescription() {
        etDescription = (EditText) findViewById(R.id.etDescription);
        etDescription.setText(toDo.description);
    }

    private void setDueDate() {
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

    private void setPriority() {
        spPriority = (Spinner) findViewById(R.id.spPriority);
        spPriority.setAdapter(new PriorityAdapter(this, Arrays.asList(Priority.values())));
        if (toDo.priority != null) {
            spPriority.setSelection(toDo.priority.getPriority());
        }
    }
}