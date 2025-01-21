package com.example.todolist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.Adapters.ToDoAdapter;
import com.example.todolist.Models.ToDoModel;
import com.example.todolist.Utils.DataBaseHandler;
import com.example.todolist.Utils.SaveManager;

import java.util.Collections;
import java.util.List;

public class ToDoListActivity extends AppCompatActivity implements DialogCloseListener{

    private Button AddTaskButton;
    private ImageButton BackButton;
    private RecyclerView taskRecyclerView;
    private ToDoAdapter taskAdapter;
    private DataBaseHandler db;
    private List<ToDoModel> taskList;
    private EditText ToDoListTitle;
    public static ToDoListActivity newInstance()
    {
        return new ToDoListActivity();
    }
    public int receivedId;
    public String defaultName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.to_do_list_activity);
        Intent intent = getIntent();
        receivedId = intent.getIntExtra("KEY_Id", 0);
        db = new DataBaseHandler(this,receivedId);
        db.openDatabase();
        BackButton = findViewById(R.id.backButtonToDoList);
        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to SecondActivity
                Context ToDoListActivity = com.example.todolist.ToDoListActivity.this;
                Intent intent = new Intent(ToDoListActivity, MainActivity.class);
                startActivity(intent);
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
        getSupportActionBar().hide();
        AddTaskButton = findViewById(R.id.addTaskButton);
        taskRecyclerView = findViewById(R.id.taskRecycleView);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        taskAdapter = new ToDoAdapter(db,this);
        taskRecyclerView.setAdapter(taskAdapter);

        taskList = db.GetAllTasks();
        Collections.reverse(taskList);
        taskAdapter.SetTask(taskList);
        AddTaskButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                AddNewTask.newInstance().show(getSupportFragmentManager(),AddNewTask.Tag);
            }
        });
    }

    private void LoadTitle()
    {
        String savedText = SaveManager.getInstance(MyApplication.getAppContext()).GetTaskParent(receivedId); // Default value is an empty string
        ToDoListTitle.setText(savedText);

    }
    private void SaveTitle()
    {
        SaveManager.getInstance(MyApplication.getAppContext())
                .SaveTaskParent(receivedId,ToDoListTitle.getText().toString());
    }
    @Override
    public void handleDialogClose(DialogInterface dialog) {
        taskList = db.GetAllTasks();
        Collections.reverse(taskList);
        taskAdapter.SetTask(taskList);
        taskAdapter.notifyDataSetChanged();
    }
}