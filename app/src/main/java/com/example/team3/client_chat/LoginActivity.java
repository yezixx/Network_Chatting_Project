package com.example.team3.client_chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.team3.R;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class LoginActivity extends Activity {

    private EditText mIdView;
    private EditText mPasswordView;
    private String mUsername;
    private String mPassword;
    private Socket mSocket;
    private PrintWriter mOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mIdView = findViewById(R.id.login_Id);
        mPasswordView = findViewById(R.id.login_password);

        // 로그인 버튼 클릭 시 이벤트 처리
        Button loginButton = findViewById(R.id.login_btn);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();  // 로그인 시도
            }
        });
    }

    private void attemptLogin() {
        String id = mIdView.getText().toString().trim();
        String password = mPasswordView.getText().toString().trim();

        if (TextUtils.isEmpty(id)) {
            mIdView.setError("ID를 입력하세요.");
            mIdView.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError("비밀번호를 입력하세요.");
            mPasswordView.requestFocus();
            return;
        }

        mUsername = id;  // 로그인한 사용자 이름 저장
        mPassword = password;

        // 소켓 연결
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mSocket = new Socket("192.168.1.100", 3000);  // 서버 IP와 포트
                    OutputStream outputStream = mSocket.getOutputStream();
                    mOut = new PrintWriter(outputStream, true);

                    // 사용자 ID와 비밀번호를 서버로 전송
                    mOut.println("login:" + mUsername + ":" + mPassword);

                    // 로그인 성공 후 채팅 화면으로 이동
                    Intent intent = new Intent(LoginActivity.this, ChatActivity.class);
                    intent.putExtra("username", mUsername);
                    startActivity(intent);
                    finish();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
