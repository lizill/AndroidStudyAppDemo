package com.example.studyapp.recycle;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.studyapp.FirstActivity;
import com.example.studyapp.R;
import com.example.studyapp.ui.chart.Env;
import com.example.studyapp.ui.home.HomeFragment;
import com.example.studyapp.ui.home.StopwatchActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.studyapp.ui.home.HomeFragment.mSocket;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.CustomViewHolder> {

    public HomeAdapter() {
        super();
    }

    private ArrayList<HomeData> arrayList;
    private String userID;///
    private RequestQueue requestQueue;///
    private HomeFragment homeFragment;///

    public HomeAdapter(ArrayList<HomeData> arrayList)
    {
        this.arrayList = arrayList;
        userID = FirstActivity.userInfo.getString("userId",null);///
        homeFragment = new HomeFragment();///
    }

    @NonNull
    @Override
    public HomeAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_list, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final HomeAdapter.CustomViewHolder holder, int position) {
        holder.tv_name.setText(arrayList.get(position).getTv_name());
        holder.subject_time.setText(arrayList.get(position).getSubject_time());
        //holder.iv_profile.setImageResource(arrayList.get(position).getIv_profile());
        holder.itemView.setTag(position);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String curName = holder.tv_name.getText().toString();
            }
        });

        holder.itemView.setOnClickListener(v->{
           mSocket.emit("start",userID);
           Intent intent = new Intent(v.getContext(), StopwatchActivity.class);
           intent.putExtra("subject",arrayList.get(position).getTv_name());
           intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
           v.getContext().startActivity(intent);
        });

        holder.itemView.setOnLongClickListener(v -> {
            requestQueue = Volley.newRequestQueue(HomeFragment.root.getContext());
            remove(holder.getAdapterPosition());
            return true;
        });

    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    public void remove(int position) {
        try {
            removedSubject(arrayList.get(position).getTv_name());
            arrayList.remove(position);
            notifyItemRemoved(position);
        } catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected ImageView iv_profile;
        protected TextView tv_name;
        protected TextView subject_time;

        public CustomViewHolder(View itemView) {
            super(itemView);
            this.iv_profile = (ImageView) itemView.findViewById(R.id.iv_home_profile);
            this.tv_name = (TextView) itemView.findViewById(R.id.tv_home_name);
            this.subject_time = (TextView) itemView.findViewById(R.id.subject_time);
        }
    }

    public void removedSubject(String removedSubject) {
        String url = Env.DeleteSubjectURL;
        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {

        })  {
            @Override
            protected Map<String, String> getParams() {
                System.out.println(removedSubject);
                Map<String,String> params = new HashMap<>();
                params.put("removedSubject", removedSubject);
                params.put("userID",userID);
                return params;
            }
        };
        request.setShouldCache(false);
        requestQueue.add(request);
    }
}