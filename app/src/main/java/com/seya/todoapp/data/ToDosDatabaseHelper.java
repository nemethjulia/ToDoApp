package com.seya.todoapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class ToDosDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "TODO";

    // Database Info
    private static final String DATABASE_NAME = "todosDatabase";
    private static final int DATABASE_VERSION = 8;

    // Table Name
    private static final String TABLE_TODOS = "todos";

    // TODO Table Columns
    private static final String KEY_TODO_ID = "id";
    private static final String KEY_TODO_TEXT = "text";
    private static final String KEY_TODO_DUE_DATE = "due_date";
    private static final String KEY_TODO_PRIORITY = "priority";

    private static final DateFormat dateFormat = DateFormat.getDateInstance();

    public ToDosDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TODOS_TABLE = "CREATE TABLE " + TABLE_TODOS +
                "(" +
                KEY_TODO_ID + " INTEGER PRIMARY KEY," +
                KEY_TODO_TEXT + " TEXT," +
                KEY_TODO_DUE_DATE + " TEXT," +
                KEY_TODO_PRIORITY + " INTEGER" +
                ")";

        db.execSQL(CREATE_TODOS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODOS);
            onCreate(db);
        }
    }

    public List<ToDo> getAllToDos() {
        List<ToDo> toDos = new ArrayList<>();

        String TODOS_SELECT_QUERY = String.format("SELECT * FROM %s ORDER BY %s DESC, %s", TABLE_TODOS, KEY_TODO_PRIORITY, KEY_TODO_DUE_DATE);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(TODOS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    ToDo newToDo = new ToDo();
                    newToDo.id = cursor.getInt(cursor.getColumnIndex(KEY_TODO_ID));
                    newToDo.text = cursor.getString(cursor.getColumnIndex(KEY_TODO_TEXT));
                    String dateString = cursor.getString(cursor.getColumnIndex(KEY_TODO_DUE_DATE));
                    newToDo.dueDate = dateString == null ? null : dateFormat.parse(dateString);
                    newToDo.priority = Priority.getPriority(cursor.getInt(cursor.getColumnIndex(KEY_TODO_PRIORITY)));
                    toDos.add(newToDo);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get todos from database " + e.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return toDos;
    }

    public void addOrUpdateTodo(ToDo toDo) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_TODO_TEXT, toDo.text);
            values.put(KEY_TODO_DUE_DATE, toDo.dueDate == null ? null : dateFormat.format(toDo.dueDate));
            values.put(KEY_TODO_PRIORITY, toDo.priority == null ? -1 : toDo.priority.getPriority());

            // First try to update in case the item already exists in the database
            int rows = db.update(TABLE_TODOS, values, KEY_TODO_ID + " = ?", new String[] { String.valueOf(toDo.id) });

            // Check if update succeeded
            if (rows != 1) {
                db.insertOrThrow(TABLE_TODOS, null, values);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add or update todo: " + e.getMessage());
        } finally {
            db.endTransaction();
        }
    }

    public void remove(ToDo toDo) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_TODOS, KEY_TODO_ID + " = ?", new String[] { String.valueOf(toDo.id) });
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete a todo");
        } finally {
            db.endTransaction();
        }
    }
}

