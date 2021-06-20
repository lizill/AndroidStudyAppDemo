package com.example.studyapp.ui.group;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.studyapp.FirstActivity;
import com.example.studyapp.JSONTask;
import com.example.studyapp.R;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.socket.client.IO;
import io.socket.client.Socket;

//import static com.example.studyapp.ui.home.HomeFragment.mSocket;

public class ChatActivity extends AppCompatActivity {

    private String userID;
    private String roomName;

    private Socket mSocket;
    private Gson gson = new Gson();

    private RecyclerView recyclerView;
    private ChatAdapter adapter;

    private Button sendButton;
    private EditText sendText;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // 로딩
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        // ------------------------------------------------------------------------------------
        // 이전화면에서 데이터 넘어오는 정보
        // ------------------------------------------------------------------------------------
        Intent intent = getIntent();
        roomName = intent.getStringExtra("group");
        userID = FirstActivity.userInfo.getString(FirstActivity.USER_ID,null);
        
        
        // ------------------------------------------------------------------------------------
        // 리사이클러뷰 세팅
        // ------------------------------------------------------------------------------------
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<ChatItem> list = new ArrayList<>();
        adapter = new ChatAdapter(list);

        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);

        // ------------------------------------------------------------------------------------
        // 입력창, 전송 버튼
        // ------------------------------------------------------------------------------------
        sendText = findViewById(R.id.send_text);

        sendButton = (Button) findViewById(R.id.send_btn);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        // ------------------------------------------------------------------------------------
        // 해당 room 채팅정보 불러오기
        // ------------------------------------------------------------------------------------
        progressBar.setVisibility(View.VISIBLE);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("room_name", roomName);

            ChatActivity.MessageDataTask messageDataTask = new ChatActivity.MessageDataTask(jsonObject, "message", "POST");
            messageDataTask.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        // ------------------------------------------------------------------------------------
        // 소켓 연결
        // ------------------------------------------------------------------------------------
        try {
            mSocket = IO.socket("http://132.226.20.103:9876");
            Log.d("SOCKET", "Connection success : " + mSocket.id());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        mSocket.connect();

        mSocket.on(Socket.EVENT_CONNECT, args -> {
            mSocket.emit("enter", gson.toJson(new RoomData(userID, roomName, System.currentTimeMillis())));
        });

        mSocket.emit("join", gson.toJson(roomName));

        mSocket.on("update", args -> {
            MessageData data = gson.fromJson(args[0].toString(), MessageData.class);
            addChat(data);
        });
    }

    // 날짜 포맷 설정
    private String toDate(long currentMillis) {
        return new SimpleDateFormat("hh:mm a").format(new Date(currentMillis));
    }

    // 소켓으로 json객체 전송
    private void sendMessage() {
        String content = sendText.getText().toString().trim();
        if(content.length() >= 300) {
            System.out.println("300자 이상은 입력 불가");
            return;
        }
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

    // 소켓 update 시 실행
    private void addChat(MessageData data) {
        runOnUiThread(() -> {
            if (data.getType().equals("ENTER") || data.getType().equals("LEFT")) {
                adapter.addItem(new ChatItem(data.getFrom(), data.getContent(), toDate(data.getSendTime()), ChatType.CENTER_MESSAGE));
            }
            else if (!userID.equals(data.getFrom())) {
                adapter.addItem(new ChatItem(data.getFrom(), data.getContent(), toDate(data.getSendTime()), ChatType.LEFT_MESSAGE));
            }
            recyclerView.scrollToPosition(adapter.getItemCount() - 1);
        });
    }

    // 화면을 떠났을 때
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.emit("left", gson.toJson(new RoomData(userID, roomName, System.currentTimeMillis())));
        mSocket.disconnect();
    }

    class MessageDataTask extends JSONTask {

        public MessageDataTask(JSONObject jsonObject, String urlPath, String method) {
            super(jsonObject, urlPath, method);
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                String resultNum = jsonObject.get("result").toString();
                JSONArray jsonArray = jsonObject.getJSONArray("data");

                if (resultNum.equals("1")) { // 1 : 메세지 데이터가 있을 경우

                    for(int i=0; i<jsonArray.length(); i++) {
                        JSONObject messageData = new JSONObject(jsonArray.get(i).toString());

                        String type = messageData.get("type").toString();
                        String from = messageData.get("f_rom").toString();
                        String content = messageData.get("content").toString();
                        String sendTime = messageData.get("sendTime").toString();

                        if (type.equals("ENTER") || type.equals("LEFT")) {
                            adapter.addItem(new ChatItem(from, content, toDate(Long.valueOf(sendTime)), ChatType.CENTER_MESSAGE));
                        }
                        else if (!userID.equals(from)) {
                            adapter.addItem(new ChatItem(from, content, toDate(Long.valueOf(sendTime)), ChatType.LEFT_MESSAGE));
                        } else {
                            adapter.addItem(new ChatItem(from, content, toDate(Long.valueOf(sendTime)), ChatType.RIGHT_MESSAGE));
                        }
                        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                progressBar.setVisibility(View.GONE);
            }
        }
    }
}