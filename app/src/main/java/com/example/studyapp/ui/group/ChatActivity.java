package com.example.studyapp.ui.group;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.studyapp.FirstActivity;
import com.example.studyapp.R;
import com.google.gson.Gson;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.socket.client.IO;
import io.socket.client.Socket;

public class ChatActivity extends AppCompatActivity {

    private Socket mSocket;
    private Gson gson = new Gson();

    private Button sendButton;
    private EditText sendText;

    private RecyclerView recyclerView;
    private ChatAdapter adapter;

    private String userID;
    private String roomName;

    private DbOpenHelper mDbOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        roomName = intent.getStringExtra("group");
        userID = FirstActivity.userInfo.getString(FirstActivity.USER_ID,null);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<ChatItem> list = new ArrayList<>();

        mDbOpenHelper = new DbOpenHelper(this);
        mDbOpenHelper.open();
        mDbOpenHelper.create();

        Cursor iCursor = mDbOpenHelper.selectColumns(roomName);
        while(iCursor.moveToNext()) {
            String roomName = iCursor.getString(iCursor.getColumnIndex("room_name"));
            String type = iCursor.getString(iCursor.getColumnIndex("type"));
            String from = iCursor.getString(iCursor.getColumnIndex("f_rom"));
            String to = iCursor.getString(iCursor.getColumnIndex("t_o"));
            String content = iCursor.getString(iCursor.getColumnIndex("content"));
            String sendTime = iCursor.getString(iCursor.getColumnIndex("sendTime"));
            MessageData data = new MessageData(roomName, type, from, to, content, Long.valueOf(sendTime));

            System.out.println(data);

            if (type.equals("ENTER") || type.equals("LEFT")) {
                list.add(new ChatItem(from, content, toDate(Long.valueOf(sendTime)), ChatType.CENTER_MESSAGE));
            }
            else if (!userID.equals(from)) {
                list.add(new ChatItem(from, content, toDate(Long.valueOf(sendTime)), ChatType.LEFT_MESSAGE));
            } else {
                list.add(new ChatItem(from, content, toDate(Long.valueOf(sendTime)), ChatType.RIGHT_MESSAGE));
            }
        }

        adapter = new ChatAdapter(list);

        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);

        sendText = findViewById(R.id.send_text);

        try {
            mSocket = IO.socket("http://132.226.20.103:9876");
            Log.d("SOCKET", "Connection success : " + mSocket.id());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        mSocket.connect();

        mSocket.on(Socket.EVENT_CONNECT, args -> {
            mSocket.emit("enter", gson.toJson(new RoomData(userID, roomName)));
        });

        mSocket.on("update", args -> {
            MessageData data = gson.fromJson(args[0].toString(), MessageData.class);
            addChat(data);

            // 내부 데이터베이스에 저장
            mDbOpenHelper.insertColumn(data.getRoomName(), data.getType(), data.getFrom(), data.getTo(), data.getContent(), String.valueOf(data.getSendTime()));
        });

        sendButton = (Button) findViewById(R.id.send_btn);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private String toDate(long currentMillis) {
        return new SimpleDateFormat("hh:mm a").format(new Date(currentMillis));
    }

    private void sendMessage() {
        String content = sendText.getText().toString().trim();
        if(!content.equals("")) {
            long currentTime = System.currentTimeMillis();
            mSocket.emit("newMessage", gson.toJson(new MessageData(
                    roomName,
                    "MESSAGE",
                    userID,
                    roomName,
                    content,
                    currentTime
            )));
            adapter.addItem(new ChatItem(userID, content, toDate(currentTime), ChatType.RIGHT_MESSAGE));
            adapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(adapter.getItemCount() - 1);
            sendText.setText("");
        }
    }

    private void addChat(MessageData data) {
        runOnUiThread(() -> {
            if (data.getType().equals("ENTER") || data.getType().equals("LEFT")) {
                adapter.addItem(new ChatItem(data.getFrom(), data.getContent(), toDate(data.getSendTime()), ChatType.CENTER_MESSAGE));
            }
            else if (!userID.equals(data.getFrom())) {
                adapter.addItem(new ChatItem(data.getFrom(), data.getContent(), toDate(data.getSendTime()), ChatType.LEFT_MESSAGE));
            }
            adapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(adapter.getItemCount() - 1);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.emit("left", gson.toJson(new RoomData(userID, roomName)));
        mSocket.disconnect();
    }
}