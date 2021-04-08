package com.example.studyapp.recycle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studyapp.R;

import java.util.ArrayList;

public class PlanBtnAdapter extends RecyclerView.Adapter<PlanBtnAdapter.CustomViewHolder> {

    public PlanBtnAdapter() {
        super();
    }

    private ArrayList<PlanBtnData> arrayList;

    public PlanBtnAdapter(ArrayList<PlanBtnData> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public PlanBtnAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plan_btn_list, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final PlanBtnAdapter.CustomViewHolder holder, int position) {
        holder.btnon.setText(arrayList.get(position).getBtnon());
        holder.btnoff.setText(arrayList.get(position).getBtnoff());

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String curName = holder.btnoff.getText().toString();

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
        protected TextView btnon;
        protected TextView btnoff;

        public CustomViewHolder(View itemView) {
            super(itemView);
            this.btnon = (TextView) itemView.findViewById(R.id.btn_add_plan);
            this.btnoff = (TextView) itemView.findViewById(R.id.btn_min_plan);
        }
    }

}