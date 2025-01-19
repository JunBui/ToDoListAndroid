package com.example.todolist.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.todolist.Models.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHandler extends SQLiteOpenHelper {
    static final int VERSION = 1;
    static final String Name = "ToDoListDataBase";
    static final String TODO_TABLE = "todoTable";
    static final String ID = "id";
    static final String TASK = "task";
    static final String STATUS = "status";
    static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "(" + ID + " INTEER PRIMARY KEY AUTOINCREMENT, "
            + TASK + "TEXT, " + STATUS + " INTERGER)";

    private SQLiteDatabase db;

    public DataBaseHandler(Context context)
    {
        super(context,Name,null,VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_TODO_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer)
    {
//        String upgradeString = "DROP TABLE IF " + "EXISTS" + TODO_TABLE;
//        db.execSQL(upgradeString);
        onCreate(db);
    }
    public void  openDatabase()
    {
        db = this.getWritableDatabase();
    }
    public void  insertTask(ToDoModel task)
    {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task.getTask());
        cv.put(STATUS, 0);
        db.insert(TODO_TABLE,null,cv);
    }
    private int GetColumnIndex(Cursor cursor,String name)
    {
        int columnIndex = cursor.getColumnIndex(name);
        return columnIndex;
    }
    public List<ToDoModel> GetAllTasks()
    {
        List<ToDoModel> taskList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try{
            cur = db.query(TODO_TABLE, null,null,null,null,null,null,null);
            if(cur!=null)
            {
                if(cur.moveToFirst())
                {
                    do {
                        ToDoModel task = new ToDoModel();
                        task.setId(cur.getInt(GetColumnIndex(cur,ID)));
                        task.setTask(cur.getString(GetColumnIndex(cur,TASK)));
                        task.setStatus(cur.getInt(GetColumnIndex(cur,STATUS)));
                        taskList.add(task);
                    }while (cur.moveToNext());
                }
            }
        }
        finally {

            db.endTransaction();
            cur.close();
        }
        return taskList;
    }
    public void UpdateStatus(int id, int status)
    {
        ContentValues cv = new ContentValues();
        cv.put(STATUS,status);
        db.update(TODO_TABLE,cv,ID + "=?", new String[]{String.valueOf(id)});
    }
    public void UpdateTask(int id, String task)
    {
        ContentValues cv = new ContentValues();
        cv.put(TASK,task);
        db.update(TODO_TABLE,cv,ID + "=?", new String[]{String.valueOf(id)});
    }
    public void DeleteTask(int id)
    {
        db.delete(TODO_TABLE,ID + "=?",new String[]{String.valueOf(id)});
    }
}
