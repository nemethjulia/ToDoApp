package com.seya.todoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    private EditText etEditItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        String itemText = getIntent().getStringExtra("item");
        etEditItem = (EditText) findViewById(R.id.etEditItem);
        etEditItem.setText(itemText);
    }

    public void onSave(View view) {
        Intent data = new Intent();
        data.putExtra("item", etEditItem.getText().toString());

        setResult(RESULT_OK, data);
        this.finish();
    }
}
