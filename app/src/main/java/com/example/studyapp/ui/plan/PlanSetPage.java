package com.example.studyapp.ui.plan;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.studyapp.R;
import com.example.studyapp.ui.group.Group;
import com.example.studyapp.ui.group.GroupListAdapter;
import com.example.studyapp.ui.group.GroupPage;
import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;
import java.util.List;

public class PlanSetPage extends AppCompatActivity {
    private ListView groupListView;
    private GroupListAdapter adapter;
    private List<Group> groupList;
    private String userID;
    private TimePickerDialog.OnTimeSetListener callbackMethod;

    public static Button st_btn;
    public static Button en_btn;
    public static TextView en_txt;

    static int st_hour;
    static int st_min;
    static int en_hour;
    static int en_min;
    static boolean touch=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plan_set_page);

        EditText editText = findViewById(R.id.plan_subject);
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode== KeyEvent.KEYCODE_ENTER) return true;
                return false;
            }
        });

        Calendar mCal = Calendar.getInstance();
        st_hour = mCal.get(Calendar.HOUR_OF_DAY);
        st_min = mCal.get(Calendar.MINUTE);

        st_btn = findViewById(R.id.start_time_btn);
        en_btn = findViewById(R.id.end_time_btn);
        en_txt = findViewById(R.id.end_time);
        st_btn.setText(PlanTimePicker.hourCal(st_hour, st_min,true));
        en_hour = st_hour;
        en_min = st_min+30;
        en_btn.setText(PlanTimePicker.hourCal(en_hour, en_min,false));

        PlanTimePicker planTimePicker = new PlanTimePicker();
        st_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                touch=true;
                planTimePicker.show(getSupportFragmentManager(),
                        "help");

            }
        });
        en_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                touch=false;
                planTimePicker.show(getSupportFragmentManager(),
                        "help");
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);  // Hostfragment
        NavController navController = navHostFragment.getNavController();
        NavInflater inflater = navHostFragment.getNavController().getNavInflater();
        NavGraph graph = inflater.inflate(R.navigation.mobile_navigation);


        graph.setStartDestination(R.id.navigation_plan);

        navController.setGraph(graph);



        NavigationView navigationView = findViewById(R.id.navigation_plan);
        NavigationUI.setupWithNavController(navigationView, navHostFragment.getNavController());


        Intent intent = new Intent(this, GroupPage.class);
        startActivity(intent);
        return true;
    }
}
