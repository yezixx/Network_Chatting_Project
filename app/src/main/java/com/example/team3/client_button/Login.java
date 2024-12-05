package com.example.team3.client_button;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Login extends AppCompatActivity {

    private static final String SERVER_IP = "192.168.1.100";  // 서버 IP
    private static final int SERVER_PORT = 3000;              // 서버 포트
    private EditText loginIdField, loginPasswordField;
    private Button loginButton;
    private Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);  // XML 레이아웃 연결

        // UI 요소 초기화
        loginIdField = findViewById(R.id.login_Id);
        loginPasswordField = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_btn);
        mainHandler = new Handler(Looper.getMainLooper());

        // 로그인 버튼 클릭 이벤트 처리
        loginButton.setOnClickListener(v -> {
            String username = loginIdField.getText().toString().trim();
            String password = loginPasswordField.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(Login.this, "ID와 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            // 로그인 요청
            sendLoginRequest(username, password, new LoginCallback() {
                @Override
                public void onSuccess(String message) {
                    Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, EmergencyButton.class);
                    intent.putExtra("PATIENT_ID", username);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(Login.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    // 로그인 요청 메서드
    private void sendLoginRequest(String username, String password, LoginCallback callback) {
        new Thread(() -> {
            try {
                // 서버 연결
                Socket socket = new Socket(SERVER_IP, SERVER_PORT);
                OutputStream outputStream = socket.getOutputStream();
                PrintWriter out = new PrintWriter(outputStream, true);

                // 로그인 데이터 전송
                out.println("login:" + username + ":" + password);

                // 서버와의 통신 종료
                socket.close();

                // 로그인 성공 콜백 호출
                mainHandler.post(() -> callback.onSuccess("로그인 성공"));

            } catch (IOException e) {
                e.printStackTrace();
                // 로그인 실패 콜백 호출
                mainHandler.post(() -> callback.onFailure("서버 연결 실패"));
            }
        }).start();
    }

    // 로그인 결과 처리 콜백 인터페이스
    public interface LoginCallback {
        void onSuccess(String message);
        void onFailure(String errorMessage);
    }
}
