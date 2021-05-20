package com.example.studyapp.ui.group;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studyapp.R;

import java.util.ArrayList;

import static com.example.studyapp.FirstActivity.USER_ID;
import static com.example.studyapp.FirstActivity.userInfo;

public class MemberRecyclerAdapter extends RecyclerView.Adapter<MemberRecyclerAdapter.ViewHolder> {

    private ArrayList<MemberData> memberList;

    public MemberRecyclerAdapter(ArrayList<MemberData> list) {
        memberList = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView memberNameText;
        TextView totalTimeText;
        ImageView memberImage;

        public ViewHolder(View itemView) {
            super(itemView);

            memberNameText = (TextView)itemView.findViewById(R.id.memberName);
            totalTimeText = (TextView)itemView.findViewById(R.id.totalTime);
            memberImage = (ImageView)itemView.findViewById(R.id.memberImage);
        }

    }
    @NonNull
    @Override
    public MemberRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.member_item, parent, false);
        MemberRecyclerAdapter.ViewHolder vh = new MemberRecyclerAdapter.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(MemberRecyclerAdapter.ViewHolder holder, int position) {
        holder.memberNameText.setText(memberList.get(position).getUserId());
        holder.totalTimeText.setText(memberList.get(position).getTotalTime());
        if(memberList.get(position).getOnline().equals("1")) {
            holder.memberImage.setColorFilter(Color.parseColor("#3080FF"));
        }
    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }
}
