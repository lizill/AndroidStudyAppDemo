package com.example.studyapp.ui.group;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.studyapp.R;

import java.util.List;

public class GroupListAdapter extends BaseAdapter {

    private Context context;
    private List<Group> groupList;


    public GroupListAdapter(Context context, List<Group> groupList) {
        this.context = context;
        this.groupList = groupList;
    }

    @Override
    public int getCount() {
        return groupList.size();
    }

    @Override
    public Object getItem(int position) {
        return groupList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.group_item, null);
        TextView groupText = (TextView)v.findViewById(R.id.groupText);
        TextView peopleCountText = (TextView)v.findViewById(R.id.peopleCountText);
        TextView categoryText = (TextView)v.findViewById(R.id.categoryText);
        TextView goalTimeText = (TextView)v.findViewById(R.id.goalTimeText);
        TextView masterText = (TextView)v.findViewById(R.id.masterText);

        groupText.setText(groupList.get(position).getGroup());
        peopleCountText.setText(groupList.get(position).getPeopleCount());
        categoryText.setText(groupList.get(position).getCategory());
        goalTimeText.setText(groupList.get(position).getGoalTime());
        masterText.setText(groupList.get(position).getMaster());

        v.setTag(groupList.get(position).getGroup());
        return v;
    }


}
