package com.example.todolist.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.todolist.MainActivity;
import com.example.todolist.Models.ToDoModel;
import com.example.todolist.ToDoListActivity;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHandler extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String Name = "toDoListDatabase";
    public String TODO_TABLE = "todoTable_";
    public int TODO_TABLE_Id=0;
    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String STATUS = "status";
    private String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK + " TEXT, "
            + STATUS + " INTEGER)";

    private SQLiteDatabase db;


    public DataBaseHandler(Context context, int id)
    {
        super(context,Name,null,VERSION);
        TODO_TABLE_Id = id;
        TODO_TABLE = "todoTable_" + TODO_TABLE_Id;
        Log.i("database","To do table name: " + TODO_TABLE);
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK + " TEXT, "
                + STATUS + " INTEGER)";
        db.execSQL(CREATE_TODO_TABLE);
        Log.i("database","To do table at create: " + TODO_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer)
    {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        // Create tables again
        onCreate(db);
    }
    public void openDatabase()
    {
        db = this.getWritableDatabase();
    }
    public void insertTask(ToDoModel task)
    {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task.getTask());
        cv.put(STATUS, 0);
        db.insert(TODO_TABLE, null, cv);
        Log.i("Add new task","Table: " + TODO_TABLE);
    }
    private int GetColumnIndex(Cursor cursor,String name)
    {
        int columnIndex = cursor.getColumnIndex(name);
        Log.i("column index","get column index " + columnIndex);
        return columnIndex;
    }
    public List<ToDoModel> GetAllTasks()
    {
        List<ToDoModel> taskList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        Log.i("database","query: " + TODO_TABLE);
        try{
            cur = db.query(TODO_TABLE, null, null, null, null, null, null, null);
            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        ToDoModel task = new ToDoModel();
                        task.setId(cur.getInt(GetColumnIndex(cur,ID)));
                        task.setTask(cur.getString(GetColumnIndex(cur,TASK)));
                        task.setStatus(cur.getInt(GetColumnIndex(cur,STATUS)));
                        taskList.add(task);
                    }
                    while(cur.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }

        return taskList;
    }
    public void UpdateStatus(int id, int status)
    {
        ContentValues cv = new ContentValues();
        cv.put(STATUS,status);
        db.update(TODO_TABLE,cv,ID + "= ?", new String[]{String.valueOf(id)});
    }
    public void UpdateTask(int id, String task)
    {
        ContentValues cv = new ContentValues();
        cv.put(TASK,task);
        db.update(TODO_TABLE,cv,ID + "= ?", new String[]{String.valueOf(id)});
    }
    public void DeleteTask(int id)
    {
        db.delete(TODO_TABLE,ID + "= ?",new String[]{String.valueOf(id)});
    }
    public void CheckExistAndCreateNewTable(int id)
    {
        TODO_TABLE_Id = id;
        TODO_TABLE = "todoTable_" + TODO_TABLE_Id;
        CREATE_TODO_TABLE = "CREATE TABLE IF NOT EXISTS " + TODO_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK + " TEXT, "
                + STATUS + " INTEGER)";
        SQLiteDatabase db = this.getWritableDatabase();

        // Execute the query
        db.execSQL(CREATE_TODO_TABLE);
        db.close();
    }
}
