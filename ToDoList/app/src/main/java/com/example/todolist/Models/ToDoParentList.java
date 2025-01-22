package com.example.todolist.Models;

public class ToDoParentList {
    public String Text;
    public String ModifyTime;
    public int id;
    public boolean IsCompleted;
    public TaskParentType Type;
    public ToDoParentList(int id,TaskParentType type,String text, String modifyTime,boolean isCompleted) {
        this.ModifyTime = modifyTime;
        this.Text = text;
        this.id = id;
        this.IsCompleted = isCompleted;
        this.Type = type;
    }
    public enum TaskParentType
    {
        task,
        note
    }
}
