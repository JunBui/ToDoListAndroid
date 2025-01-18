package com.example.todolist;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.Adapters.ToDoAdapter;
import com.example.todolist.Models.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView taskRecyclerView;
    private ToDoAdapter taskAdapter;
    private List<ToDoModel> taskList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        taskRecyclerView = findViewById(R.id.taskRecycleView);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        taskAdapter = new ToDoAdapter(this);
        taskRecyclerView.setAdapter(taskAdapter);

        ToDoModel task = new ToDoModel();
        task.setTask("Test task");
        task.setStatus(0);
        task.setId(0);

        taskList = new ArrayList<>();
        taskList.add(task);
        taskAdapter.SetTask(taskList);
    }
}