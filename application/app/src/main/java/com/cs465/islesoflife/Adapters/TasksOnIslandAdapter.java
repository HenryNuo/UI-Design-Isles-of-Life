package com.cs465.islesoflife.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cs465.islesoflife.AddNewTask;
import com.cs465.islesoflife.CalendarViewActivity;
//import com.cs465.islesoflife.IslandTasksActivity;
import com.cs465.islesoflife.LevelUp;
import com.cs465.islesoflife.Model.ToDoModel;
import com.cs465.islesoflife.SingleIsland;
import com.cs465.islesoflife.Utils.DatabaseHandler;

import net.penguincoders.doit.R;

import java.util.List;

public class TasksOnIslandAdapter extends RecyclerView.Adapter<TasksOnIslandAdapter.ViewHolder> {

    private List<ToDoModel> todoList;
    private DatabaseHandler db;
    private SingleIsland activity;
    private Context context;

    public TasksOnIslandAdapter(DatabaseHandler db, SingleIsland activity, Context context) {
        this.db = db;
        this.activity = activity;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        db.openDatabase();

        final ToDoModel item = todoList.get(position);
        holder.task.setText(item.getTask());
        holder.taskCategory.setText(item.getCategory());
        holder.taskDueDate.setText(item.getTaskDueDate());
        holder.taskDueTime.setText(item.getTaskDueTime());
        holder.task.setChecked(toBoolean(item.getStatus()));

        String defaultImportance = (String) item.getImportance();
        String importance = "+" + defaultImportance;
        holder.taskImportance.setText(importance);
        if(defaultImportance.equals("5")){
            holder.taskImportance.setTextColor(this.activity.getResources().getColor(R.color.five));
        }else if(defaultImportance.equals("4")){
            holder.taskImportance.setTextColor(this.activity.getResources().getColor(R.color.four));
        }else if(defaultImportance.equals("3")){
            holder.taskImportance.setTextColor(this.activity.getResources().getColor(R.color.three));
        }else if(defaultImportance.equals("2")){
            holder.taskImportance.setTextColor(this.activity.getResources().getColor(R.color.two));
        }else if(defaultImportance.equals("1")){
            holder.taskImportance.setTextColor(this.activity.getResources().getColor(R.color.one));
        }

        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int currEXP = db.getIslandEXP(item.getCategory());
                if (isChecked) {
                    db.updateStatus(item.getId(), 1);
                    currEXP = currEXP + Integer.valueOf(item.getImportance());
                    if(currEXP >= 100){
                        currEXP = currEXP - 100;
                        int level = db.getIslandLevel(item.getCategory());
                        level += 1;
                        db.updateLevel(item.getCategory(), level);

                        Intent intent = new Intent();
                        Bundle para = new Bundle();
                        para.putInt("curLevel", level);
                        para.putInt("curIslandIdx", db.getIslandId(item.getCategory()));
                        para.putString("curIslandName", item.getCategory());

                        intent.setClass(context, LevelUp.class);
                        intent.putExtras(para);
                        context.startActivity(intent);
                    }

                    db.updateEXP(item.getCategory(), currEXP);
                } else {
                    db.updateStatus(item.getId(), 0);
                    currEXP = currEXP - Integer.valueOf(item.getImportance());
                    if(currEXP < 0)
                        currEXP = 0;
                    db.updateEXP(item.getCategory(),currEXP);
                }
            }
        });
    }


    private boolean toBoolean(int n) {
        return n != 0;
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }


    public void setTasks(List<ToDoModel> todoList) {
        this.todoList = todoList;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        ToDoModel item = todoList.get(position);
        db.deleteTask(item.getId());
        todoList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position) {
        ToDoModel item = todoList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());
        bundle.putString("category", item.getCategory());
        bundle.putString("taskDueDate", item.getTaskDueDate());
        bundle.putString("taskDueTime", item.getTaskDueTime());
        bundle.putString("taskImportance", item.getImportance());
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
//        fragment.show(activity.getSupportFragmentManager(), AddNewTask.TAG);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox task;
        TextView taskCategory;
        TextView taskDueDate;
        TextView taskDueTime;
        TextView taskImportance;

        ViewHolder(View view) {
            super(view);
            task = view.findViewById(R.id.todoCheckBox);
            taskCategory = view.findViewById(R.id.islandCategory_display);
            taskDueDate = view.findViewById(R.id.taskDueDate);
            taskDueTime = view.findViewById(R.id.taskDueTime);
            taskImportance = view.findViewById(R.id.taskImportance);
        }
    }
}