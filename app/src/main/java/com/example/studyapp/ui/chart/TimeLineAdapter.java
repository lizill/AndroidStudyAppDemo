package com.example.studyapp.ui.chart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studyapp.R;
import com.github.vipulasri.timelineview.TimelineView;

import java.util.List;

public class TimeLineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<TimeLineModel> timeLineModelList;
    private Context context;

    public TimeLineAdapter(List<TimeLineModel> timeLineModelList, Context context) {
        this.timeLineModelList = timeLineModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timeline, parent, false);
        return new ViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).tv_title.setText(timeLineModelList.get(position).getTitle());
        ((ViewHolder) holder).tv_time.setText(timeLineModelList.get(position).getTime());
        ((ViewHolder)holder).tv_term.setText(timeLineModelList.get(position).getTerm());


    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

    @Override
    public int getItemCount() {
        return timeLineModelList.size();
    }


    private class ViewHolder extends RecyclerView.ViewHolder {
        protected TimelineView timelineView;
        protected TextView tv_title;
        protected TextView tv_time;
        protected TextView tv_term;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            timelineView = (TimelineView) itemView.findViewById(R.id.timeline);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_term = (TextView) itemView.findViewById(R.id.tv_term);

            timelineView.initLine(viewType);

        }
    }
}
