package com.example.studyapp.ui.group;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.studyapp.R;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.studyapp.FirstActivity.USER_ID;
import static com.example.studyapp.FirstActivity.userInfo;

public class SearchGroupRecyclerAdapter extends RecyclerView.Adapter<SearchGroupRecyclerAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<Group> groupList;
    private Context mContext;
    private String userID = userInfo.getString(USER_ID,null);

    public SearchGroupRecyclerAdapter(Activity activity, Context mContext, ArrayList<Group> list) {
        this.activity = activity;
        this.mContext = mContext;
        groupList = list;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView groupText;
        TextView contentsText;
        TextView peopleCountText;
        TextView categoryText;
        TextView goalTimeText;
        TextView masterText;
        TextView startDateText;

        public ViewHolder(View itemView) {
            super(itemView);

            groupText = (TextView)itemView.findViewById(R.id.search_groupText);
            contentsText = (TextView)itemView.findViewById(R.id.search_contentsText);
            peopleCountText = (TextView)itemView.findViewById(R.id.search_peopleCountText);
            categoryText = (TextView)itemView.findViewById(R.id.search_categoryText);
            goalTimeText = (TextView)itemView.findViewById(R.id.search_goalTimeText);
            masterText = (TextView)itemView.findViewById(R.id.search_masterText);
            startDateText = (TextView)itemView.findViewById(R.id.search_start_date);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    String group = (String)((TextView)v.findViewById(R.id.search_groupText)).getText();
                    Log.d("?????: ", group);
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle(group).setMessage("그룹에 가입하시겠습니까?");
                    builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Response.Listener<String> responseListener = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try{
                                        JSONObject jsonResponse = new JSONObject(response);
                                        boolean success = jsonResponse.getBoolean("success");
                                        if (success) {
                                            Log.d("성공",":::");
                                            peopleCountIncrease(group);
                                            String group = (String)((TextView)v.findViewById(R.id.groupText)).getText();
                                            Intent intent = new Intent(v.getContext(),  GroupPage.class);
                                            intent.putExtra("group", group);
                                            mContext.startActivity(intent);
                                        }
                                    } catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            };
                            JoinGroupRequest joinGroupRequest = new JoinGroupRequest(userID, group, responseListener);
                            RequestQueue queue = Volley.newRequestQueue(mContext);
                            queue.add(joinGroupRequest);
                        }

                    });

                    builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });
        }

    }
    @NonNull
    @Override
    public SearchGroupRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.search_group_item, parent, false);
        SearchGroupRecyclerAdapter.ViewHolder vh = new SearchGroupRecyclerAdapter.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(SearchGroupRecyclerAdapter.ViewHolder holder, int position) {
        holder.groupText.setText(groupList.get(position).getGroup());
        holder.contentsText.setText(groupList.get(position).getContents());
        holder.peopleCountText.setText(groupList.get(position).getPeopleCount());
        holder.categoryText.setText(groupList.get(position).getCategory());
        holder.goalTimeText.setText(groupList.get(position).getGoalTime());
        holder.masterText.setText(groupList.get(position).getMaster());
        holder.startDateText.setText(groupList.get(position).getStartDate());
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    private void peopleCountIncrease(String group) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        Log.d("성공",":::");
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        PeopleCountIncreaseRequest peopleCountIncreaseRequest = new PeopleCountIncreaseRequest(group, responseListener);
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(peopleCountIncreaseRequest);
    }
}
