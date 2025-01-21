package com.example.todolist.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

public class SaveManager {
    private static final String PREF_NAME = "MyPreferences";
    private static SaveManager instance;
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public static synchronized SaveManager getInstance(Context context) {
        if (instance == null) {
            instance = new SaveManager(context);
        }
        return instance;
    }
    // Constructor
    public SaveManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Save a String
    public void saveString(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    // Retrieve a String
    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    // Save an Integer
    public void saveInt(String key, int value) {
        editor.putInt(key, value);
        editor.apply();
    }

    // Retrieve an Integer
    public int getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    // Save a Boolean
    public void saveBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.apply();
    }

    // Retrieve a Boolean
    public boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    // Remove a value
    public void remove(String key) {
        editor.remove(key);
        editor.apply();
    }

    // Clear all data
    public void clear() {
        editor.clear();
        editor.apply();
    }
    public void SaveTaskParent(int id, String name)
    {
        saveString("taskParentName_"+id,name);
    }
    public String GetTaskParent(int id)
    {
        String taskParentName = getString("taskParentName_"+id,"");
        return taskParentName;
    }
    public List<ToDoParentList> GetAllTask()
    {
        List<ToDoParentList> allTaskParent = new ArrayList<>();
        for (int i = 0; i<GetCurrentTaskParentId();i++)
        {
            ToDoParentList parentName = new ToDoParentList("",i);
            parentName.Text = instance.GetTaskParent(i);
            if(parentName.Text != null && !parentName.Text.trim().isEmpty())
            {
                allTaskParent.add(parentName);
            }
        }
        return allTaskParent;
    }
    public void GainCurrentTaskParentId()
    {
        int current = instance.GetCurrentTaskParentId();
        current++;
        instance.saveInt("CurrentTaskParentId",current);
    }
    public int GetCurrentTaskParentId()
    {
        return getInt("CurrentTaskParentId",0);
    }
}
