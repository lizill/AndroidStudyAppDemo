package com.example.studyapp.recycle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studyapp.R;

import java.util.ArrayList;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.CustomViewHolder> {

    public RankAdapter() {
        super();
    }

    private ArrayList<RankData> arrayList;

    public RankAdapter(ArrayList<RankData> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public RankAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rank_list, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RankAdapter.CustomViewHolder holder, int position) {

        if(arrayList.size()==position-1){
            System.out.println(position);
        }

        holder.tv_rank_name.setText(arrayList.get(position).getTv_rank_name());
        holder.tv_rank_pf.setText(arrayList.get(position).getTv_rank_pf());
        holder.tv_rank_time.setText(arrayList.get(position).getTv_rank_time());
        holder.pb_rank_progress.setProgress(arrayList.get(position).getPb_rank_progress());
        holder.iv_rank_studying.setImageResource(arrayList.get(position).getIv_rank_studying());

//        this.rv_rank_pf = (TextView) itemView.findViewById(R.id.rank_pf);
//        this.rv_rank_name = (TextView) itemView.findViewById(R.id.rank_name);
//        this.rv_rank_time = (TextView) itemView.findViewById(R.id.rank_time);
//        this.rv_rank_progress = (ProgressBar) itemView.findViewById(R.id.rank_progress);
//        this.rv_rank_studying = (ImageView) itemView.findViewById(R.id.rank_studying);


        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String curName = holder.tv_name.getText().toString();

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

        protected TextView tv_rank_pf;
        protected TextView tv_rank_name;
        protected TextView tv_rank_time;
        protected ProgressBar pb_rank_progress;
        protected ImageView iv_rank_studying;

        public CustomViewHolder(View itemView) {
            super(itemView);
            this.tv_rank_pf = (TextView) itemView.findViewById(R.id.rank_pf);
            this.tv_rank_name = (TextView) itemView.findViewById(R.id.rank_name);
            this.tv_rank_time = (TextView) itemView.findViewById(R.id.rank_time);
            this.pb_rank_progress = (ProgressBar) itemView.findViewById(R.id.rank_progress);
            this.iv_rank_studying = (ImageView) itemView.findViewById(R.id.rank_studying);
        }
    }

}