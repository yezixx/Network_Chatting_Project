package com.example.team3.client_chat;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team3.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends Activity {

    private RecyclerView mRecyclerView;
    private ChatAdapter mAdapter;
    private List<Message> mMessageList;
    private EditText mMessageView;
    private ImageButton mSendButton;
    private Socket mSocket;
    private PrintWriter mOut;
    private BufferedReader mIn;
    private String mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // 로그인한 사용자 이름 받기
        mUsername = getIntent().getStringExtra("username");

        mRecyclerView = findViewById(R.id.Chatting_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mMessageList = new ArrayList<>();
        mAdapter = new ChatAdapter(mMessageList);
        mRecyclerView.setAdapter(mAdapter);

        mMessageView = findViewById(R.id.Chatting_message);
        mSendButton = findViewById(R.id.Chatting_send);

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();  // 메시지 전송 처리
            }
        });

        // 서버와 소켓 연결
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mSocket = new Socket("192.168.1.100", 3000);  // 서버 IP와 포트
                    mOut = new PrintWriter(mSocket.getOutputStream(), true);
                    mIn = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));

                    // 메시지 수신 스레드 시작
                    receiveMessages();

                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ChatActivity.this, "서버 연결 실패", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    // 메시지 전송 메소드
    private void sendMessage() {
        String message = mMessageView.getText().toString().trim();
        if (!message.isEmpty()) {
            try {
                PrintWriter out = new PrintWriter(mSocket.getOutputStream(), true);

                // 채팅 메시지 전송 (message:{sender}:{message})
                out.println("message:" + mUsername + ":" + message);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 메시지를 RecyclerView에 추가
            mMessageList.add(new Message(mUsername, message, "보낸시간"));
            mAdapter.notifyItemInserted(mMessageList.size() - 1);  // RecyclerView 업데이트
            mRecyclerView.scrollToPosition(mMessageList.size() - 1);  // 최신 메시지로 스크롤
            mMessageView.setText("");  // 입력 필드 초기화
        }
    }

    // 메시지 수신 스레드
    private void receiveMessages() {
        try {
            String message;
            while ((message = mIn.readLine()) != null) {
                final String receivedMessage = message;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mMessageList.add(new Message("Server", receivedMessage, "보낸시간"));
                        mAdapter.notifyItemInserted(mMessageList.size() - 1);
                        mRecyclerView.scrollToPosition(mMessageList.size() - 1);
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (mSocket != null && !mSocket.isClosed()) {
                mSocket.close();  // 소켓 종료
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
