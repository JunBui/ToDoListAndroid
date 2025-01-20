package com.example.todolist.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;

import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.MainActivity;
import com.example.todolist.Models.ToDoModel;
import com.example.todolist.R;
import com.example.todolist.AddNewTask;
import com.example.todolist.Utils.DataBaseHandler;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {
    private List<ToDoModel> toDoModelList;
    private MainActivity mainActivity;
    private DataBaseHandler db;

    public ToDoAdapter(DataBaseHandler db,MainActivity activity)
    {
        this.db =db;
        this.mainActivity = activity;
    }
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout,parent,false);
        return new ViewHolder(itemView);
    }
    public void onBindViewHolder(ViewHolder holder,int pos)
    {
        db.openDatabase();
        final ToDoModel item = toDoModelList.get(pos);

        holder.deleteButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                DeleteItemWithDialog(holder.getAdapterPosition());
            }
        });
        holder.task.setText(item.getTask());
        holder.task.setChecked(IntToBoolean(item.getStatus()));
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                Log.i("Adapter","check item index: " + item.getId());
                if(b)
                {
                    db.UpdateStatus(item.getId(),1);
                }
                else
                {
                    db.UpdateStatus(item.getId(),0);
                }
            }
        });
    }
    public int getItemCount()
    {
        return toDoModelList.size();
    }
    private boolean IntToBoolean(int n)
    {
        return n!=0;
    }
    public Context getContext() {
        return mainActivity;
    }
    public void SetTask(List<ToDoModel> toDoModelList)
    {
        this.toDoModelList = toDoModelList;
        notifyDataSetChanged();
    }
    public void  DeleteItemWithDialog(int position)
    {
        //Create dialog
        AlertDialog.Builder deleteItemDialog = new AlertDialog.Builder(getContext());
        deleteItemDialog.setTitle("Delete");
        deleteItemDialog.setMessage("Delete this task?");
        deleteItemDialog.setIcon(R.drawable.baseline_auto_delete_red);
        deleteItemDialog.setPositiveButton("Yes",new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DeleteItem(position);
            }
        });
        deleteItemDialog.setNegativeButton("No", (dialog, which) -> {
            // Dialog will close automatically
        });
        deleteItemDialog.create().show();
    }
    public void DeleteItem(int position) {
        ToDoModel item = toDoModelList.get(position);
        Log.i("Adapter","item index: " + item.getId());
        db.DeleteTask(item.getId());
        toDoModelList.remove(position);
        notifyItemRemoved(position);
    }
    public void EditItem(int pos)
    {
        ToDoModel item = toDoModelList.get(pos);
        Bundle bundle = new Bundle();
        bundle.putInt("id",item.getId());
        bundle.putString("task",item.getTask());
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(mainActivity.getSupportFragmentManager(),AddNewTask.Tag);
    }
    public static class  ViewHolder extends RecyclerView.ViewHolder
    {
        CheckBox task;
        ImageButton deleteButton;
        ViewHolder(View view)
        {
            super(view);
            task = view.findViewById(R.id.toDoCheckBox);
            deleteButton = view.findViewById(R.id.deleteToDoButton);
        }
    }
}
