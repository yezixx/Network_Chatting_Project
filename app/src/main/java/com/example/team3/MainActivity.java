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
}
