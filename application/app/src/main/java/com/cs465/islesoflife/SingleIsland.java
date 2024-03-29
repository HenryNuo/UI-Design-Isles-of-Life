package com.cs465.islesoflife;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import 	android.widget.TextView;
import 	android.widget.ImageView;

import com.cs465.islesoflife.Adapters.IslandAdapter;
import com.cs465.islesoflife.Adapters.TasksOnDateAdapter;
import com.cs465.islesoflife.Adapters.TasksOnIslandAdapter;
import com.cs465.islesoflife.Model.SpeciesModel;
import com.cs465.islesoflife.Model.ToDoModel;
import com.cs465.islesoflife.Utils.DatabaseHandler;
import com.cs465.islesoflife.Model.IslandModel;

import net.penguincoders.doit.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class SingleIsland extends AppCompatActivity implements DialogCloseListener{

    private DatabaseHandler db;
    private RecyclerView tasksRecyclerView;
    private TasksOnIslandAdapter tasksAdapter;
    private List<ToDoModel> taskList;

    private List<IslandModel> islandList;
    private int islandListSize;
    private List<SpeciesModel> speciesList;
    private int speciesListSize;
    IslandAdapter islandAdapter;
    int curIslandIdx;

    protected void setIslandView(){
        final TextView islandName = (TextView) findViewById(R.id.islandName);
        final ImageView islandImage = (ImageView) findViewById(R.id.toIslandDetail);
        final ImageView Species234 = (ImageView) findViewById(R.id.level234);
        final ImageView Species569 = (ImageView) findViewById(R.id.level569);
        // TODO: species7
        // TODO: species8
        // TODO: species10

        islandName.setText(islandList.get(curIslandIdx).getName());
        int imageResource = getResources().getIdentifier(islandList.get(curIslandIdx).getImagePath(), null, getPackageName());
        islandImage.setImageResource(imageResource);

        int curIslandLevel = islandList.get(curIslandIdx).getLevel();

        speciesList = db.getAllSpeciesOnIsland(islandList.get(curIslandIdx).getIslandId());
        speciesListSize = islandList.size();

        int speciesResource234;
        int speciesResource569;

        switch (curIslandLevel) {
            case 1:
                Species234.setImageResource(android.R.color.transparent);
                Species569.setImageResource(android.R.color.transparent);
                break;
            case 2: // bird egg
                if(speciesListSize != 0) {
                    speciesResource234 = getResources().getIdentifier(speciesList.get(0).getImagePath(), null, getPackageName());
                    Species234.setImageResource(speciesResource234);
                    Species569.setImageResource(android.R.color.transparent);
                }
                break;
            case 3: // bird child
                speciesResource234 = getResources().getIdentifier(speciesList.get(1).getImagePath(), null, getPackageName());
                Species234.setImageResource(speciesResource234);
                Species569.setImageResource(android.R.color.transparent);
                break;
            case 4: // bird adult
                speciesResource234 = getResources().getIdentifier(speciesList.get(2).getImagePath(), null, getPackageName());
                Species234.setImageResource(speciesResource234);
                Species569.setImageResource(android.R.color.transparent);
                break;
            case 5: // bird adult、sapling tree
                speciesResource234 = getResources().getIdentifier(speciesList.get(2).getImagePath(), null, getPackageName());
                Species234.setImageResource(speciesResource234);
                speciesResource569 = getResources().getIdentifier(speciesList.get(3).getImagePath(), null, getPackageName());
                Species569.setImageResource(speciesResource569);
                break;
            case 6: // bird adult、juvenile tree
                speciesResource234 = getResources().getIdentifier(speciesList.get(2).getImagePath(), null, getPackageName());
                Species234.setImageResource(speciesResource234);
                speciesResource569 = getResources().getIdentifier(speciesList.get(4).getImagePath(), null, getPackageName());
                Species569.setImageResource(speciesResource569);
                break;
            case 7:
                break;
            case 8:
                break;
            case 9:
                break;
            case 10:
                break;
            default:
                break;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_island);
        Objects.requireNonNull(getSupportActionBar()).hide();


        db = new DatabaseHandler(this);
        db.openDatabase();

        Bundle b = getIntent().getExtras();
        if(b != null)
            curIslandIdx = b.getInt("curIdx");

        islandList = db.getAllIslands();
        islandListSize = islandList.size();

        if(islandListSize > 0)
            setIslandView();

        // change image and text content
        ImageButton btn_to_left_island = (ImageButton) findViewById(R.id.toLeftIsland);
        ImageButton btn_to_right_island = (ImageButton) findViewById(R.id.toRightIsland);

        btn_to_left_island.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(curIslandIdx > 0){
                    curIslandIdx--;
                    setIslandView();
                }
            }
        });

        btn_to_right_island.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(curIslandIdx < islandListSize - 1){
                    curIslandIdx++;
                    setIslandView();
                }
            }
        });


        // switch to the other pages
        ImageButton btn_to_new_task = (ImageButton) findViewById(R.id.toDailyTask);
        ImageButton btn_to_island_detail = (ImageButton) findViewById(R.id.toIslandDetail);

        btn_to_new_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLayout();

            }
        });

        btn_to_island_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                Bundle para = new Bundle();
                para.putInt("curIslandIdx", curIslandIdx);

                intent.setClass(SingleIsland.this, IslandDetail.class);
                intent.putExtras(para);

                startActivity(intent);
            }
        });

        ImageButton btn_to_home_page = (ImageButton) findViewById(R.id.toHomePage);

        btn_to_home_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageButton btn_to_calendar_page = (ImageButton) findViewById(R.id.toCalendar);

        btn_to_calendar_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SingleIsland.this, CalendarViewActivity.class);

                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        setIslandView();
    }

    public void setLayout(){
        View view = getLayoutInflater().inflate(R.layout.tasks_date, null);
        setContentView(view);
        db = new DatabaseHandler(this);
        db.openDatabase();

        tasksRecyclerView = findViewById(R.id.tasksBasedOnDate);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter = new TasksOnIslandAdapter(db, SingleIsland.this, this);
        tasksRecyclerView.setAdapter(tasksAdapter);


        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new TaskOnIslandItemTouchHelper(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);

        System.out.println(islandList.get(curIslandIdx).getName());
        taskList = db.getAllTasksBasedOnIslandName(islandList.get(curIslandIdx).getName());
        Collections.reverse(taskList);

        tasksAdapter.setTasks(taskList);

        ImageButton btn_to_calendar = (ImageButton) findViewById(R.id.toCalendar);

        btn_to_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SingleIsland.this, Homepage.class);
                startActivity(intent);
            }
        });

    }
    @Override
    public void handleDialogClose(DialogInterface dialog) {

    }
}