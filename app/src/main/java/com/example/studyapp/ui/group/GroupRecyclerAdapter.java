package com.example.studyapp.ui.group;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studyapp.R;

import java.util.ArrayList;

import static com.example.studyapp.FirstActivity.USER_ID;
import static com.example.studyapp.FirstActivity.userInfo;

public class GroupRecyclerAdapter extends RecyclerView.Adapter<GroupRecyclerAdapter.ViewHolder> {

    private ArrayList<Group> groupList;
    private Context mContext;

    public GroupRecyclerAdapter(Context mContext, ArrayList<Group> list) {
        this.mContext = mContext;
        groupList = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView groupText;
        TextView peopleCountText;
        TextView categoryText;
        TextView goalTimeText;
        TextView masterText;
        ImageButton chatButton;

        public ViewHolder(View itemView) {
            super(itemView);

            groupText = (TextView)itemView.findViewById(R.id.groupText);
            peopleCountText = (TextView)itemView.findViewById(R.id.peopleCountText);
            categoryText = (TextView)itemView.findViewById(R.id.categoryText);
            goalTimeText = (TextView)itemView.findViewById(R.id.goalTimeText);
            masterText = (TextView)itemView.findViewById(R.id.masterText);
            chatButton = (ImageButton) itemView.findViewById(R.id.chatButton);
            
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String group = (String)((TextView)v.findViewById(R.id.groupText)).getText();
                    Intent intent = new Intent(v.getContext(),  GroupPage.class);
                    intent.putExtra("group", group);
                    mContext.startActivity(intent);
                }
            });
        }

    }
    @NonNull
    @Override
    public GroupRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.group_item, parent, false);
        GroupRecyclerAdapter.ViewHolder vh = new GroupRecyclerAdapter.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(GroupRecyclerAdapter.ViewHolder holder, int position) {
        holder.groupText.setText(groupList.get(position).getGroup());
        holder.peopleCountText.setText(groupList.get(position).getPeopleCount());
        holder.categoryText.setText(groupList.get(position).getCategory());
        holder.goalTimeText.setText(groupList.get(position).getGoalTime());
        holder.masterText.setText(groupList.get(position).getMaster());

        holder.chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("클릭" + groupList.get(position).getGroup());
                Intent intent = new Intent(v.getContext(), ChatActivity.class);
                intent.putExtra("userID", userInfo.getString(USER_ID,null));
                intent.putExtra("group", groupList.get(position).getGroup());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }
}
