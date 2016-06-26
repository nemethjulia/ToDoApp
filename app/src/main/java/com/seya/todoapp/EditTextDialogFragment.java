package com.seya.todoapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.seya.todoapp.data.ToDo;

public class EditTextDialogFragment extends DialogFragment {

    private EditText etEditItem;
    private ToDo toDo;

    public EditTextDialogFragment() {
    }

    public static EditTextDialogFragment newInstance(ToDo toDo) {
        EditTextDialogFragment frag = new EditTextDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("toDo", toDo);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_todo, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().setTitle("@string/edit_below_label");
        // Get field from view
        etEditItem = (EditText) view.findViewById(R.id.etEditItem);
        // Fetch arguments from bundle and set values
        toDo = (ToDo) getArguments().getSerializable("toDo");
        etEditItem.setText(toDo.text);
        // Show soft keyboard automatically and request focus to field
        etEditItem.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        Button btnSave = (Button) view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDo.text = etEditItem.getText().toString();
                EditToDoDialogListener activity = (EditToDoDialogListener) getActivity();
                activity.toDoChanged(toDo);
                dismiss();
            }
        });
    }
}
