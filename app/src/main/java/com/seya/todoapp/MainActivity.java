
package com.seya.todoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.seya.todoapp.data.ToDo;
import com.seya.todoapp.data.ToDosDatabaseHelper;

public class MainActivity extends AppCompatActivity {

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == EDIT_REQUEST_CODE) {
            String item = data.getExtras().getString("item");
            int position = data.getExtras().getInt("position");
            ToDo toDo = aToDoAdapter.getItem(position);
            toDo.text = item;
            aToDoAdapter.notifyDataSetChanged();
            dbHelper.updateTodo(toDo);
        }
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
                Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
                intent.putExtra("item", aToDoAdapter.getItem(position).text);
                intent.putExtra("position", position);
                startActivityForResult(intent, EDIT_REQUEST_CODE);
            }
        });
    }

    private void reloadData() {
        aToDoAdapter.clear();
        aToDoAdapter.addAll(dbHelper.getAllToDos());
    }
}
