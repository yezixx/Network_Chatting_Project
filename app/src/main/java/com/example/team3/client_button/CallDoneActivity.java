package com.example.team3.client_button;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.team3.R;

public class CallDoneActivity extends AppCompatActivity {

    private String mUsername;
    private String mUserType;
    private String mPatientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_done);

        // Intent로 전달된 데이터 받기
        mUsername = getIntent().getStringExtra("username");
        mUserType = getIntent().getStringExtra("userType");
        mPatientId = getIntent().getStringExtra("PATIENT_ID");

        // 환자 정보 표시
        TextView patientInfo = findViewById(R.id.patient_info);
        patientInfo.setText("환자 ID: " + mPatientId+"\n 환자 이름: "+mUsername+ "\n정상적으로 호출되었습니다");

        // 돌아가기 버튼 설정
        Button backButton = findViewById(R.id.back_btn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // EmergencyButton으로 이동
                Intent intent = new Intent(CallDoneActivity.this, EmergencyButton.class);
                intent.putExtra("username", mUsername);
                intent.putExtra("userType", mUserType);
                intent.putExtra("PATIENT_ID", mPatientId);
                startActivity(intent);
                finish(); // 현재 Activity 종료
            }
        });
    }
}
