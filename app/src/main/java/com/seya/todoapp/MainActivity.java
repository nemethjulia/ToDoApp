
package com.seya.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.seya.todoapp.data.ToDo;
import com.seya.todoapp.data.ToDosDatabaseHelper;

public class MainActivity extends AppCompatActivity {

    private static final int EDIT_REQUEST_CODE = 3017;

    private ToDoAdapter aToDoAdapter;
    private ListView lvItems;

    private ToDosDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new ToDosDatabaseHelper(this);

        lvItems = (ListView) findViewById(R.id.lvItems);
        createTodoList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == EDIT_REQUEST_CODE) {
            ToDo item = (ToDo) data.getSerializableExtra("todo");
            dbHelper.addOrUpdateTodo(item);
            reloadData();
        }
    }

    public void onCreateItem(View view) {
        editTodo(new ToDo());
    }

    private void createTodoList() {
        aToDoAdapter = new ToDoAdapter(this, dbHelper.getAllToDos());
        lvItems.setAdapter(aToDoAdapter);
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ToDo toDo = aToDoAdapter.getItem(position);
                dbHelper.remove(toDo);
                reloadData();
                return true;
            }
        });
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editTodo(aToDoAdapter.getItem(position));
            }
        });
    }

    private void reloadData() {
        aToDoAdapter.clear();
        aToDoAdapter.addAll(dbHelper.getAllToDos());
    }

    private void editTodo(ToDo toDo) {
        Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
        intent.putExtra("todo", toDo);
        startActivityForResult(intent, EDIT_REQUEST_CODE);
    }
}
