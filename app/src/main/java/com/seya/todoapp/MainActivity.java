
package com.seya.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.seya.todoapp.data.ToDo;
import com.seya.todoapp.data.ToDosDatabaseHelper;

public class MainActivity extends AppCompatActivity implements EditToDoDialogListener {

    private static final int EDIT_REQUEST_CODE = 3017;

    private ToDoAdapter aToDoAdapter;
    private ListView lvItems;
    private EditText etEditText;

    private ToDosDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new ToDosDatabaseHelper(this);

        lvItems = (ListView) findViewById(R.id.lvItems);
        createTodoList();

        etEditText = (EditText) findViewById(R.id.etEditText);
    }

    public void onAddItem(View view) {
        ToDo toDo = new ToDo();
        toDo.text = etEditText.getText().toString();
        dbHelper.addToDo(toDo);
        reloadData(); // to have all the Id-s
        etEditText.setText("");
    }

    private void createTodoList() {
        aToDoAdapter = new ToDoAdapter(this, dbHelper.getAllToDos());
        lvItems.setAdapter(aToDoAdapter);
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ToDo toDo = aToDoAdapter.getItem(position);
                dbHelper.remove(toDo);
                aToDoAdapter.remove(toDo);
                return true;
            }
        });
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showEditDialog(aToDoAdapter.getItem(position));
            }
        });
    }

    private void reloadData() {
        aToDoAdapter.clear();
        aToDoAdapter.addAll(dbHelper.getAllToDos());
    }

    private void showEditDialog(ToDo toDo) {
        FragmentManager fm = getSupportFragmentManager();
        EditTextDialogFragment editTextDialogFragment = EditTextDialogFragment.newInstance(toDo);
        editTextDialogFragment.show(fm, "fragment_edit_name");
    }

    @Override
    public void toDoChanged(ToDo toDo) {
        dbHelper.updateTodo(toDo);
        reloadData();
    }
}

interface EditToDoDialogListener {
    void toDoChanged(ToDo toDo);
}
