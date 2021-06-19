package com.example.studyapp.recycle;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studyapp.FirstActivity;
import com.example.studyapp.R;
import com.example.studyapp.ui.plan.PlanFragment;
import com.example.studyapp.ui.plan.PlanTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.CustomViewHolder> {

    public PlanAdapter() {
        super();
    }

    private ArrayList<PlanData> arrayList;

    public PlanAdapter(ArrayList<PlanData> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public PlanAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plan_list, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final PlanAdapter.CustomViewHolder holder, int position) {

        if(arrayList.size()==position-1){
            System.out.println(position);
        }

        holder.iv_profile.setImageResource(arrayList.get(position).getIv_profile());
        holder.tv_name.setText(arrayList.get(position).getTv_name());
        holder.tv_content.setText(arrayList.get(position).getTv_content());
//        holder.iv_profile.setBackgroundColor(arrayList.get(position).getTv_content());
//        holder.iv_profile.setBackgroundColor(Color.parseColor("#dd3080ff"));
        holder.iv_color.setBackgroundColor(Color.parseColor(arrayList.get(position).getIv_color()));

//        System.out.println(arrayList.get(position).getIv_color());
        holder.itemView.setTag(position);
        System.out.println(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(position);
                System.out.println(holder.getAdapterPosition());
                View root = PlanFragment.getRoot();
                AlertDialog.Builder alert = new AlertDialog.Builder(root.getContext());
                alert.setTitle("계획을 지우시겠습니까?").setMessage("공부 한 내역은 사라지지 않습니다.");

                alert.setNegativeButton("네", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            String userID = FirstActivity.userInfo.getString(FirstActivity.USER_ID,null);
                            String userPassword = FirstActivity.userInfo.getString(FirstActivity.USER_PASSWORD,null);
                            int positions = PlanFragment.getRecycleArrayList().get(holder.getAdapterPosition()).
                                    getPosition();
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.accumulate("user_id", userID);
                            jsonObject.accumulate("user_password", userPassword);
                            jsonObject.accumulate("position", positions);
                            PlanTask planTask = new PlanTask(jsonObject, "planDelete", "POST");
                            planTask.execute();

                            planTask = new PlanTask(jsonObject, "plan", "POST");
                            planTask.execute();
                            remove(holder.getAdapterPosition());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        PlanTask.showToast(root.getContext(),"지웠습니다.");

                    }
                });

                alert.setPositiveButton("아니오", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        PlanTask.showToast(root.getContext(),"취소했습니다");
                    }
                });

                AlertDialog alertDialog = alert.create();

                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onShow(DialogInterface dialog) {
                        Button positiveB = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                        Button negativeB = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                        positiveB.setTextColor(R.color.maincolor);
                        negativeB.setTextColor(R.color.maincolor);
                    }
                });
                alertDialog.show();
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                remove(holder.getAdapterPosition());
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    public void remove(int position) {
        try {
            arrayList.remove(position);
            notifyItemRemoved(position);
        } catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected ImageView iv_profile;
        protected TextView tv_name;
        protected TextView tv_content;
        protected ImageView iv_color;

        public CustomViewHolder(View itemView) {
            super(itemView);
            this.iv_profile = (ImageView) itemView.findViewById(R.id.iv_plan_profile);
            this.tv_name = (TextView) itemView.findViewById(R.id.tv_plan_name);
            this.tv_content = (TextView) itemView.findViewById(R.id.tv_plan_content);
            this.iv_color = (ImageView) itemView.findViewById(R.id.iv_color);
        }
    }

}