package com.example.todolist;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.todolist.Activity.MainActivity;
import com.example.todolist.Activity.NoteActivity;
import com.example.todolist.Activity.ToDoListActivity;
import com.example.todolist.Interface.DialogCloseListener;
import com.example.todolist.Models.ToDoParentList;
import com.example.todolist.Utils.DataBaseHandler;
import com.example.todolist.Utils.MyApplication;
import com.example.todolist.Utils.SaveManager;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddNewTaskParent extends BottomSheetDialogFragment
{
    public static final String Tag = "ActionBottomDialogNewTaskParent";
    private EditText newTaskText;
    private Button newTaskSaveButton;
    private Button newNoteSaveButton;
    public static AddNewTaskParent newInstance()
    {
        return new AddNewTaskParent();
    }
    private DataBaseHandler db;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstance)
    {
        View view = inflater.inflate(R.layout.new_task_parent, container,false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newTaskText = getView().findViewById(R.id.newTaskTextParent);
        newTaskSaveButton = getView().findViewById(R.id.newTaskButtonParent);
        newNoteSaveButton = getView().findViewById(R.id.newNoteButtonParent);

        newTaskText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().equals(""))
                {
                    newTaskSaveButton.setEnabled(false);
                    newNoteSaveButton.setEnabled(false);
                    newTaskSaveButton.setTextColor(Color.GRAY);
                    newNoteSaveButton.setTextColor(Color.GRAY);
                }
                else
                {
                    newTaskSaveButton.setEnabled(true);
                    newNoteSaveButton.setEnabled(true);
                    newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryDark));
                    newNoteSaveButton.setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryDark));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        newTaskSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnClickNewTaskParent();
            }
        });
        newNoteSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnClickNewNoteParent();
            }
        });
    }
    private void OnClickNewTaskParent()
    {
        ToDoParentList.TaskParentType taskParentType = ToDoParentList.TaskParentType.task;
        String text = newTaskText.getText().toString();
        SaveManager saveManager = SaveManager.getInstance(MyApplication.getAppContext());
        int saveId = saveManager.GetCurrentTaskParentId();
        saveManager.SaveTaskParentName(saveId,text);
        saveManager.SaveTaskParentModifyTime(saveId);
        saveManager.SaveTaskParentType(saveId,taskParentType);
        db = new DataBaseHandler(MyApplication.getAppContext(),saveId);
        db.CheckExistAndCreateNewTable(saveId);
        saveManager.GainCurrentTaskParentId();
        dismiss();
        LoadActivity(taskParentType,saveId);
    }
    private void OnClickNewNoteParent()
    {
        ToDoParentList.TaskParentType taskParentType = ToDoParentList.TaskParentType.note;
        String text = newTaskText.getText().toString();
        SaveManager saveManager = SaveManager.getInstance(MyApplication.getAppContext());
        int saveId = saveManager.GetCurrentTaskParentId();
        saveManager.SaveTaskParentName(saveId,text);
        saveManager.SaveTaskParentModifyTime(saveId);
        saveManager.SaveTaskParentType(saveId,taskParentType);
        saveManager.GainCurrentTaskParentId();
        dismiss();
        LoadActivity(taskParentType,saveId);
    }
    private void LoadActivity(ToDoParentList.TaskParentType Type, int id)
    {
        Intent intent = new Intent();
        switch (Type)
        {
            case task:
                intent = new Intent(this.getContext(), ToDoListActivity.class);
                break;
            case note:
                intent = new Intent(this.getContext(), NoteActivity.class);
                break;
        }
        intent.putExtra("KEY_Id", id);
        startActivity(intent);
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
