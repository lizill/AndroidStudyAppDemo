package com.example.studyapp.ui.group;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studyapp.R;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ChatItem> itemList = new ArrayList<>();

    public ChatAdapter(ArrayList<ChatItem> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == ChatType.CENTER_MESSAGE) {
            view = inflater.inflate(R.layout.chat_center_item, parent, false);
            return new CenterViewHolder(view);
        } else if (viewType == ChatType.LEFT_MESSAGE) {
            view = inflater.inflate(R.layout.chat_left_item, parent, false);
            return new LeftViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.chat_right_item, parent, false);
            return new RightViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof CenterViewHolder) {
            ChatItem item = itemList.get(position);
            ((CenterViewHolder) viewHolder).setItem(item);
        } else if (viewHolder instanceof LeftViewHolder) {
            ChatItem item = itemList.get(position);
            ((LeftViewHolder) viewHolder).setItem(item);
        } else if (viewHolder instanceof RightViewHolder) {
            ChatItem item = itemList.get(position);
            ((RightViewHolder) viewHolder).setItem(item);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void addItem(ChatItem item) {
        itemList.add(item);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return itemList.get(position).getViewType();
    }

    public class CenterViewHolder extends RecyclerView.ViewHolder {

        TextView contentText;

        public CenterViewHolder(@NonNull View itemView) {
            super(itemView);

            contentText = itemView.findViewById(R.id.content_text);
        }

        public void setItem(ChatItem item) {
            contentText.setText(item.getContent());
        }
    }

    public class LeftViewHolder extends RecyclerView.ViewHolder {

        TextView nameText;
        TextView contentText;
        TextView sendTimeText;

        public LeftViewHolder(@NonNull View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.name_text);
            contentText = itemView.findViewById(R.id.msg_text);
            sendTimeText = itemView.findViewById(R.id.send_time_text);
        }

        public void setItem(ChatItem item) {
            nameText.setText(item.getName());
            contentText.setText(item.getContent());
            sendTimeText.setText(item.getSendTime());
        }
    }

    public class RightViewHolder extends  RecyclerView.ViewHolder {

        TextView contentText;
        TextView sendTimeText;

        public RightViewHolder(@NonNull View itemView) {
            super(itemView);

            contentText = itemView.findViewById(R.id.msg_text);
            sendTimeText = itemView.findViewById(R.id.send_time_text);
        }

        public void setItem(ChatItem item){
            contentText.setText(item.getContent());
            sendTimeText.setText(item.getSendTime());
        }
    }
}
