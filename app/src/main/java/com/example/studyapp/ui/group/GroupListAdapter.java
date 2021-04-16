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
        View v = View.inflate(context, R.layout.group, null);
        TextView groupText = (TextView)v.findViewById(R.id.groupText);
        TextView contentsText = (TextView)v.findViewById(R.id.contentsText);
        TextView peopleCountText = (TextView)v.findViewById(R.id.peopleCount);

        groupText.setText(groupList.get(position).getGroup());
        contentsText.setText(groupList.get(position).getContents());
        peopleCountText.setText(groupList.get(position).getPeopleCount());

        v.setTag(groupList.get(position).getGroup());
        return v;
    }


}
