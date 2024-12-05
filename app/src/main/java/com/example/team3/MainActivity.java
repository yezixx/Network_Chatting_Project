package com.example.team3;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;

import com.example.team3.client_chat.ChatActivity;
import com.example.team3.client_chat.RequestLogin;
import com.example.team3.client_chat.RequestLogin.LoginCallback;

import java.net.Socket;  // Socket 클래스를 임포트
import java.io.IOException;  // IOException 클래스를 임포트

public class MainActivity extends AppCompatActivity {

    android.widget.Button login_Btn;
    EditText et_login_ID, et_login_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_Btn = findViewById(R.id.login_btn);
        et_login_ID = findViewById(R.id.login_Id);
        et_login_password = findViewById(R.id.login_password);

        login_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_login_ID.getText().toString();
                String password = et_login_password.getText().toString();

                // 이메일이 비어있거나 비밀번호가 비어있는 경우 처리
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "모든 칸을 채워주세요", Toast.LENGTH_SHORT).show();
                } else {
                    // 이메일에서 점을 언더스코어로 변경
                    String validEmail = email.replace(".", "_");

                    // 로그인 요청
                    new RequestLogin().sendLoginRequest(validEmail, password, new LoginCallback() {
                        @Override
                        public void onSuccess(String message) {
                            // 로그인 성공 시 처리
                            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();

                            // 로그인 성공 후 서버 연결 (서버와의 연결 코드 추가)
                            connectToServer();

                            // 로그인 성공 후 새로운 화면으로 이동 (예: ChatActivity)
                            Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            // 로그인 실패 시 처리
                            Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void connectToServer() {
        // 서버와 연결하는 부분
        // 예를 들어, 클라이언트가 서버에 연결하는 코드 추가
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 서버 IP와 포트를 정확히 입력하세요.
                String serverIp = "192.168.0.24";
                int serverPort = 3000;  // 예시 포트 번호

                try {
                    // 서버에 연결
                    Socket socket = new Socket(serverIp, serverPort);
                    if (socket.isConnected()) {
                        // 연결 성공 메시지 출력
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "서버에 연결되었습니다!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (IOException e) {
                    // 서버 연결 실패 시 처리
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "서버 연결 실패", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }
}
