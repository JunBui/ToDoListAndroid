package com.example.todolist.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.MainActivity;
import com.example.todolist.Models.ToDoModel;
import com.example.todolist.R;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {
    private List<ToDoModel> toDoModelList;
    private MainActivity mainActivity;

    public ToDoAdapter(MainActivity activity)
    {
        this.mainActivity = activity;
    }
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout,parent,false);
        return new ViewHolder(itemView);
    }
    public void onBindViewHolder(ViewHolder holder,int pos)
    {
        ToDoModel item = toDoModelList.get(pos);
        holder.task.setText(item.getTask());
        holder.task.setChecked(IntToBoolean(item.getStatus()));
    }
    public int getItemCount()
    {
        return toDoModelList.size();
    }
    private boolean IntToBoolean(int n)
    {
        return n!=0;
    }
    public void SetTask(List<ToDoModel> toDoModelList)
    {
        this.toDoModelList = toDoModelList;
        notifyDataSetChanged();
    }
    public static class  ViewHolder extends RecyclerView.ViewHolder
    {
        CheckBox task;
        ViewHolder(View view)
        {
            super(view);
            task = view.findViewById(R.id.toDoCheckBox);
        }
    }
}
