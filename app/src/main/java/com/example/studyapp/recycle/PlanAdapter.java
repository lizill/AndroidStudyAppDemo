package com.example.studyapp.recycle;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studyapp.R;

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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String curName = holder.tv_name.getText().toString();

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