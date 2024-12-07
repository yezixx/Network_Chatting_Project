package com.example.team3.client_chat;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team3.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ChatAdapter mAdapter;
    private List<Message> mMessageList;
    private EditText mMessageView;
    private ImageButton mSendButton;
    private Socket mSocket;
    private PrintWriter mOut;
    private BufferedReader mIn;
    private String mUsername;
    private static final String SERVER_IP = "192.168.219.105";  // 서버 IP
    private static final int SERVER_PORT = 3000;  // 서버 포트

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        // RecyclerView, Message 입력창, 전송 버튼 초기화
        mRecyclerView = findViewById(R.id.Chatting_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mMessageList = new ArrayList<>();
        mAdapter = new ChatAdapter(mMessageList);
        mRecyclerView.setAdapter(mAdapter);

        mMessageView = findViewById(R.id.Chatting_message);
        mSendButton = findViewById(R.id.Chatting_send);

        mUsername = getIntent().getStringExtra("username");

        // 서버와 연결하고 메시지 수신을 위한 스레드 시작
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mSocket = new Socket(SERVER_IP, SERVER_PORT);
                    mOut = new PrintWriter(new OutputStreamWriter(mSocket.getOutputStream(), "UTF-8"), true);
                    mIn = new BufferedReader(new InputStreamReader(mSocket.getInputStream(), "UTF-8"));


                    // 메시지 수신을 위한 무한 루프
                    while (true) {
                        String message = mIn.readLine();  // 서버로부터 메시지 받기
                        if (message != null) {
                            if (message.startsWith("공지사항:")) {  // "공지사항:"으로 시작하는 메시지 처리
                                String notice = message.substring(5).trim(); // "공지사항:" 이후의 내용을 추출

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // 공지사항 메시지를 UI에 추가
                                        mMessageList.add(new Message("공지", notice, getCurrentTime()));
                                        mAdapter.notifyItemInserted(mMessageList.size() - 1);
                                        mRecyclerView.scrollToPosition(mMessageList.size() - 1);
                                    }
                                });
                            } else { // 일반 메시지 처리
                            String[] parts = message.split(": ", 2);  // 보낸 사람과 메시지 분리
                            String sender = parts.length > 0 ? parts[0] : "Unknown";
                            String msgContent = parts.length > 1 ? parts[1] : "";

                            String currentTime = getCurrentTime();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // 받은 메시지를 RecyclerView에 추가
                                    mMessageList.add(new Message(sender, msgContent, currentTime));
                                    mAdapter.notifyItemInserted(mMessageList.size() - 1);
                                    mRecyclerView.scrollToPosition(mMessageList.size() - 1);  // 최신 메시지로 스크롤
                                }
                            });
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // 메시지 전송 버튼 클릭 시
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = mMessageView.getText().toString().trim();
                if (!message.isEmpty()) {
                    // 메시지 전송
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                mOut.println(mUsername + ": " + message);  // 서버로 메시지 전송
                                // 현재 시간 구하기
                                String currentTime = getCurrentTime();

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // 전송된 메시지를 RecyclerView에 추가
                                        mMessageList.add(new Message(mUsername, message, currentTime));
                                        mAdapter.notifyItemInserted(mMessageList.size() - 1);
                                        mRecyclerView.scrollToPosition(mMessageList.size() - 1);  // 최신 메시지로 스크롤
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    mMessageView.setText("");  // 메시지 입력창 비우기
                }
            }
        });
    }

    private String getCurrentTime() {
        // 현재 시간 구하기
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(new Date());  // 예: "12:30"
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