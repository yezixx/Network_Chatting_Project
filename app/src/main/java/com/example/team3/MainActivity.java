package com.example.team3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;

public class MainActivity extends AppCompatActivity{

    android.widget.Button login_Btn;
    EditText et_login_ID, et_login_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //xml확인용으로 해놓음. 아래부턴 로그인. /**/지우고 setConentView에 activity_login으로 바꿔서 사용할것
        /*login_Btn = (android.widget.Button) findViewById(R.id.login_btn);

        et_login_ID = findViewById(R.id.login_Id);
        et_login_password = findViewById(R.id.login_password);


        login_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_login_ID.getText().toString();
                String password = et_login_password.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "모든 칸을 채워주세요", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                    // 이곳에 환자 or 직원 로그인 구분
                    // Intent to another activity
                    //Intent intent = new Intent(MainActivity.this, AnotherActivity.class);
                    //startActivity(intent);
                }
            }
        });*/
    }
}
