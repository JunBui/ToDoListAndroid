package com.example.todolist.Adapters;


import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.MainActivity;
import com.example.todolist.R;
import com.example.todolist.ToDoListActivity;
import com.example.todolist.Utils.ToDoParentList;

import java.util.List;

public class TaskParentAdapter extends RecyclerView.Adapter<TaskParentAdapter.ViewHolder> {

    private List<ToDoParentList> toDoParentList;
    private MainActivity mainActivity;
    public TaskParentAdapter(List<ToDoParentList> toDoParentList, MainActivity activity)
    {
        this.mainActivity = activity;
        this.toDoParentList = toDoParentList;
    }
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_parent_layout,parent,false);
        return new ViewHolder(itemView);
    }
    public void onBindViewHolder(ViewHolder holder, int pos)
    {
        final ToDoParentList item = toDoParentList.get(pos);

        holder.taskButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity, ToDoListActivity.class);
                intent.putExtra("KEY_Id", item.id);
                mainActivity.startActivity(intent);
            }
        });
        holder.taskText.setText(item.Text);
        holder.taskModifyDateText.setText(item.ModifyTime);
        OnTaskParentUiChange(holder,item.IsCompleted);
    }

    private void OnTaskParentUiChange(ViewHolder holder, boolean enable)
    {
        holder.taskCompletedImage.setEnabled(enable);
        if(enable)
        {
            holder.taskText.setPaintFlags(holder.taskText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else
        {
            holder.taskText.setPaintFlags(0);
        }
    }
    public int getItemCount()
    {
        return toDoParentList.size();
    }
    public Context getContext() {
        return mainActivity;
    }
    public void SetTask(List<ToDoParentList> toDoModelList)
    {
        this.toDoParentList = toDoModelList;
        notifyDataSetChanged();
    }
    public static class  ViewHolder extends RecyclerView.ViewHolder
    {
        Button taskButton;
        TextView taskText;
        TextView taskModifyDateText;
        ImageView taskCompletedImage;
        ViewHolder(View view)
        {
            super(view);
            taskButton = view.findViewById(R.id.taskParentButton);
            taskText = view.findViewById(R.id.taskParentText);
            taskModifyDateText = view.findViewById(R.id.taskModifyDateText);
            taskCompletedImage = view.findViewById(R.id.taskCompletedImage);
        }
    }
}
