package com.example.studyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

    private RecyclerView recyclerView;
    private ChatAdapter adapter;

    private Button sendButton;
    private EditText sendText;

    private String userID;
    private String roomName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        userID = FirstActivity.userInfo.getString(FirstActivity.USER_ID,null);
        roomName = "testRoom";

        // set recyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<ChatItem> itemList = new ArrayList<>();
        adapter = new ChatAdapter(itemList);
        recyclerView.setAdapter(adapter);

        sendText = (EditText) findViewById(R.id.send_test);

        sendButton = (Button) findViewById(R.id.send_btn);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        socketConnect();
    }

    private void socketConnect() {
        try {
            mSocket = IO.socket("http://www.dong0110.com:9876");
            Log.d("SOCKET", "Connection sucess: " + mSocket.id());
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
        });
    }

    private void addChat(MessageData data) {
        runOnUiThread(() -> {
            if(data.getType().equals("ENTER") || data.getType().equals("LEFT")) {
                adapter.addItem(new ChatItem(data.getFrom(), data.getContent(), toDate(data.getSendTime()), ChatType.CENTER_MESSAGE));
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(adapter.getItemCount() - 1);
            }
            else if (!userID.equals(data.getFrom())) {
                adapter.addItem(new ChatItem(data.getFrom(), data.getContent(), toDate(data.getSendTime()), ChatType.LEFT_MESSAGE));
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(adapter.getItemCount() - 1);
            }
        });
    }

    private void sendMessage() {
        String content = sendText.getText().toString();

        if(!content.equals("")) {
            mSocket.emit("newMessage", gson.toJson(new MessageData("MESSAGE",
                    userID,
                    roomName,
                    content,
                    System.currentTimeMillis())));
            adapter.addItem(new ChatItem(userID, content, toDate(System.currentTimeMillis()), ChatType.RIGHT_MESSAGE));
            adapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(adapter.getItemCount() - 1);
            sendText.setText("");
        }
    }

    private String toDate(long currentMiliis) {
        return new SimpleDateFormat("hh:mm a").format(new Date(currentMiliis));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.emit("left", gson.toJson(new RoomData(userID, roomName)));
        mSocket.disconnect();
    }
}