package com.example.todolist;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.Adapters.ToDoAdapter;
import com.example.todolist.Models.ToDoModel;
import com.example.todolist.Utils.DataBaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener{

    private FloatingActionButton fab;
    private RecyclerView taskRecyclerView;
    private ToDoAdapter taskAdapter;
    private DataBaseHandler db;
    private List<ToDoModel> taskList;
    private EditText ToDoListTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DataBaseHandler(this);
        db.openDatabase();

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

    private void LoadTitle()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        String savedText = sharedPreferences.getString("savedText", ""); // Default value is an empty string
        ToDoListTitle.setText(savedText);

    }
    private void SaveTitle()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("savedText", ToDoListTitle.getText().toString());
        editor.apply();
    }
    @Override
    public void handleDialogClose(DialogInterface dialog) {
        taskList = db.GetAllTasks();
        Collections.reverse(taskList);
        taskAdapter.SetTask(taskList);
        taskAdapter.notifyDataSetChanged();
    }
}