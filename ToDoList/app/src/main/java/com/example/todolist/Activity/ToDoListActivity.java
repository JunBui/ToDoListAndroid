package com.example.todolist.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.Adapters.ToDoAdapter;
import com.example.todolist.AddNewTask;
import com.example.todolist.Interface.DialogCloseListener;
import com.example.todolist.Models.ToDoModel;
import com.example.todolist.Utils.MyApplication;
import com.example.todolist.R;
import com.example.todolist.Utils.DataBaseHandler;
import com.example.todolist.Utils.SaveManager;

import java.util.List;

public class ToDoListActivity extends AppCompatActivity implements DialogCloseListener {

    private Button AddTaskButton;
    private ImageButton BackButton;
    private RecyclerView taskRecyclerView;
    private ToDoAdapter taskAdapter;
    private DataBaseHandler db;
    private List<ToDoModel> taskList;
    private EditText ToDoListTitle;
    public int receivedId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.to_do_list_activity);
        getSupportActionBar().hide();
        InitData();
        InitUi();
    }
    private void InitData()
    {
        Intent intent = getIntent();
        receivedId = intent.getIntExtra("KEY_Id", 0);
        db = new DataBaseHandler(this,receivedId);
        db.openDatabase();
        taskAdapter = new ToDoAdapter(db,this);
        taskList = db.GetAllTasks();
        taskAdapter.SetTask(taskList);
    }
    private void InitUi()
    {
        BackButton = findViewById(R.id.backButtonToDoList);
        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickBack();
            }
        });

        ToDoListTitle = findViewById(R.id.taskTitle);

        ToDoListTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Before text is changed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // During text change
                String newText = s.toString().replaceAll("\\s", ""); // Remove spaces and line breaks
                if (!newText.equals(s.toString())) {
                    ToDoListTitle.setText(newText);
                    ToDoListTitle.setSelection(newText.length()); // Move cursor to the end
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                SaveTitle();
            }
        });
        LoadTitle();
        AddTaskButton = findViewById(R.id.addTaskButton);
        taskRecyclerView = findViewById(R.id.taskRecycleView);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskRecyclerView.setAdapter(taskAdapter);
        AddTaskButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                AddNewTask.newInstance(receivedId).show(getSupportFragmentManager(),AddNewTask.Tag);
            }
        });
    }
    private void OnClickBack()
    {
        Context ToDoListActivity = com.example.todolist.Activity.ToDoListActivity.this;
        Intent intent = new Intent(ToDoListActivity, MainActivity.class);
        startActivity(intent);
    }

    private void LoadTitle()
    {
        String savedText = SaveManager.getInstance(MyApplication.getAppContext()).GetTaskParentName(receivedId); // Default value is an empty string
        ToDoListTitle.setText(savedText);
    }
    private void SaveTitle()
    {
        SaveManager.getInstance(MyApplication.getAppContext())
                .SaveTaskParentName(receivedId,ToDoListTitle.getText().toString());
    }
    @Override
    public void handleDialogClose(DialogInterface dialog) {
        taskList = db.GetAllTasks();
        taskAdapter.SetTask(taskList);
        taskAdapter.notifyDataSetChanged();
    }
    
    public void CheckAllItem()
    {
        SaveManager saveManager = SaveManager.getInstance(MyApplication.getAppContext());
        taskList = db.GetAllTasks();
        Log.i("Check All Item", "task Parent Name " + receivedId);
        for (ToDoModel n1: taskList) {
            Log.i("Check All Item", "task Name " + n1.getTask() + " status: " + n1.getStatus());
            if (n1.getStatus() == 0) {
                saveManager.SaveTaskParentCompleted(receivedId,false);
                return;
            }
        }
        saveManager.SaveTaskParentCompleted(receivedId,true);
    }
}