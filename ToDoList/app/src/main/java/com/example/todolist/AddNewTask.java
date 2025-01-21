package com.example.todolist;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.todolist.Models.ToDoModel;
import com.example.todolist.Utils.DataBaseHandler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddNewTask extends BottomSheetDialogFragment
{
    public static final String Tag = "ActionBottomDialogNewTask";
    private EditText newTaskText;
    private Button newTaskSaveButton;
    private DataBaseHandler db;
    public static AddNewTask newInstance(int receivedId)
    {
        AddNewTask fragment = new AddNewTask();
        Bundle args = new Bundle();
        args.putInt("receivedId", receivedId); // Pass the integer value
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstance)
    {
        View view = inflater.inflate(R.layout.new_task, container,false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newTaskText = getView().findViewById(R.id.newTaskText);
        newTaskSaveButton = getView().findViewById(R.id.newTaskButton);


        boolean isUpdate = false;
        final  Bundle bundle = getArguments();
//        if(bundle !=null)
//        {
//            isUpdate = true;
//            String task = bundle.getString("task");
//            newTaskText.setText(task);
//            if(task.length()>0)
//            {
//                newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryDark));
//            }
//        }
        db = new DataBaseHandler(getActivity(),getArguments().getInt("receivedId", -1));
        db.openDatabase();
        newTaskText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().equals(""))
                {
                    newTaskSaveButton.setEnabled(false);
                    newTaskSaveButton.setTextColor(Color.GRAY);
                }
                else
                {
                    newTaskSaveButton.setEnabled(true);
                    newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryDark));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        newTaskSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = newTaskText.getText().toString();
                db.UpdateTask(bundle.getInt("id"),text);
                ToDoModel task = new ToDoModel();
                task.setTask(text);
                task.setStatus(0);
                db.insertTask(task);
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if(activity instanceof DialogCloseListener)
        {
            ((DialogCloseListener)activity).handleDialogClose(dialog);
        }
    }
}
