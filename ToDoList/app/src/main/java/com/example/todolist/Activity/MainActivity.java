package com.example.todolist.Activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.Adapters.TaskParentAdapter;
import com.example.todolist.AddNewTaskParent;
import com.example.todolist.Interface.DialogCloseListener;
import com.example.todolist.Utils.MyApplication;
import com.example.todolist.R;
import com.example.todolist.Utils.SaveManager;
import com.example.todolist.Models.ToDoParentList;

import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {

    private ImageButton AddTaskButton;
    private RecyclerView taskRecyclerView;
    private TaskParentAdapter taskParentAdapter;
    private List<ToDoParentList> taskParent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitUi();
    }
    private void InitUi()
    {
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        AddTaskButton = findViewById(R.id.addTaskButtonParent);
        taskRecyclerView = findViewById(R.id.taskRecycleViewParent);
        taskParent = SaveManager.getInstance(MyApplication.getAppContext()).GetAllTask();
        taskParentAdapter = new TaskParentAdapter(taskParent,this);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskRecyclerView.setAdapter(taskParentAdapter);
        AddTaskButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                OnAddTaskParentButtonClick();
            }
        });
    }
    private void OnAddTaskParentButtonClick()
    {
        AddNewTaskParent.newInstance().show(getSupportFragmentManager(),AddNewTaskParent.Tag);
    }
    @Override
    public void handleDialogClose(DialogInterface dialog) {
        taskParent = SaveManager.getInstance(MyApplication.getAppContext()).GetAllTask();
        taskParentAdapter.SetTask(taskParent);
        taskParentAdapter.notifyDataSetChanged();
    }
}