package com.example.todolist;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.Adapters.ToDoAdapter;
import com.example.todolist.Models.ToDoModel;
import com.example.todolist.Utils.AddNewTask;
import com.example.todolist.Utils.DataBaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener{

    private FloatingActionButton fab;
    private RecyclerView taskRecyclerView;
    private ToDoAdapter taskAdapter;
    private DataBaseHandler db;
    private List<ToDoModel> taskList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DataBaseHandler(this);
        db.openDatabase();
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        fab = findViewById(R.id.fab);
        taskRecyclerView = findViewById(R.id.taskRecycleView);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        taskAdapter = new ToDoAdapter(db,this);
        taskRecyclerView.setAdapter(taskAdapter);

        taskList = db.GetAllTasks();
        Collections.reverse(taskList);
        taskAdapter.SetTask(taskList);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                AddNewTask.newInstance().show(getSupportFragmentManager(),AddNewTask.Tag);
            }
        });
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        taskList = db.GetAllTasks();
        Collections.reverse(taskList);
        taskAdapter.SetTask(taskList);
        taskAdapter.notifyDataSetChanged();
    }
}