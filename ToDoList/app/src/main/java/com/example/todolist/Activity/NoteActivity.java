package com.example.todolist.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.todolist.Adapters.ToDoAdapter;
import com.example.todolist.AddNewTask;
import com.example.todolist.R;
import com.example.todolist.Utils.DataBaseHandler;
import com.example.todolist.Utils.MyApplication;
import com.example.todolist.Utils.SaveManager;

public class NoteActivity extends AppCompatActivity {

    public int receivedId;
    private ImageButton BackButton;
    private EditText Title;
    private EditText Notes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        InitData();
        InitUi();
    }

    private void InitData()
    {
        Intent intent = getIntent();
        receivedId = intent.getIntExtra("KEY_Id", 0);
    }
    private void InitUi()
    {
        BackButton = findViewById(R.id.backButtonNote);
        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickBack();
            }
        });

        Title = findViewById(R.id.noteTitle);
        Notes = findViewById(R.id.note);

        Title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Before text is changed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // During text change
                String newText = s.toString().replaceAll("\\s", ""); // Remove spaces and line breaks
                if (!newText.equals(s.toString())) {
                    Title.setText(newText);
                    Title.setSelection(newText.length()); // Move cursor to the end
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                SaveTitle();
            }
        });
        Notes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Before text is changed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                SaveNote();
            }
        });
        LoadTitle();
        LoadNote();
    }

    private void OnClickBack()
    {
        Context noteActivity = this;
        Intent intent = new Intent(noteActivity, MainActivity.class);
        startActivity(intent);
    }
    private void LoadTitle()
    {
        String savedText = SaveManager.getInstance(MyApplication.getAppContext()).GetTaskParentName(receivedId); // Default value is an empty string
        Title.setText(savedText);
    }
    private void SaveTitle()
    {
        SaveManager.getInstance(MyApplication.getAppContext())
                .SaveTaskParentName(receivedId,Title.getText().toString());
    }
    private void LoadNote()
    {
        String savedText = SaveManager.getInstance(MyApplication.getAppContext()).GetNote(receivedId); // Default value is an empty string
        Notes.setText(savedText);
    }
    private void SaveNote()
    {
        SaveManager.getInstance(MyApplication.getAppContext())
                .SaveNote(receivedId,Notes.getText().toString());
    }
}