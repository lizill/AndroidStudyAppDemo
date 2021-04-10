package com.example.studyapp.ui.plan;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studyapp.R;
import com.example.studyapp.ui.group.Group;
import com.example.studyapp.ui.group.GroupListAdapter;

import java.util.List;

public class PlanSetPage extends AppCompatActivity {
    private ListView groupListView;
    private GroupListAdapter adapter;
    private List<Group> groupList;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plan_set_page);

    }
}
