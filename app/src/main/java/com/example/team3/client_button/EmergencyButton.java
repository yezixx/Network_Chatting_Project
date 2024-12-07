package com.example.team3.client_button;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.team3.R;

import androidx.appcompat.app.AppCompatActivity;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class EmergencyButton extends AppCompatActivity {
    private static final String SERVER_IP = "192.168.219.101";  // 서버 IP 주소
    private static final int SERVER_PORT = 3000;              // 서버 포트 번호

    private String patientId;
    private Socket socket;
    private DataOutputStream outputStream;
    private String mUsername;
    private String mUserType;  // userType 추가

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);  // XML 파일 연결

        // Intent로 전달받은 데이터 초기화
        mUsername = getIntent().getStringExtra("username");
        mUserType = getIntent().getStringExtra("userType");

        patientId = getIntent().getStringExtra("PATIENT_ID");  // Login에서 전달받은 ID

        TextView patientInfo = findViewById(R.id.patient_info);
        Button callButton = findViewById(R.id.call_btn);

        // 환자 정보를 설정
        patientInfo.setText("환자 ID: " + patientId);

        // 긴급 호출 버튼 클릭 이벤트 처리
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmergencySignal();
            }
        });

        // 서버 연결
        getSocket();
    }

    public void getSocket() {
        new Thread(() -> {
            try {
                socket = new Socket(SERVER_IP, SERVER_PORT);
                outputStream = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    // 서버에 신호 보내기
    private void sendEmergencySignal() {
        if (socket == null || outputStream == null) {
            Toast.makeText(this, "서버와 연결되지 않았습니다.", Toast.LENGTH_LONG).show();
            return;
        }

        new Thread(() -> {
            try {
                String signal = "EMERGENCY_CALL:" + patientId + ":긴급 호출 발생!";
                outputStream.writeUTF(signal);
                outputStream.flush();

                // 긴급 호출 성공 시 화면 전환
                runOnUiThread(() -> {
                    Toast.makeText(EmergencyButton.this, "긴급 호출 전송 성공", Toast.LENGTH_SHORT).show();

                    // activity_call_done.xml로 이동
                    Intent intent = new Intent(EmergencyButton.this, CallDoneActivity.class);
                    intent.putExtra("username", mUsername); // 필요한 데이터 전달
                    intent.putExtra("userType", mUserType);
                    intent.putExtra("PATIENT_ID", patientId);
                    startActivity(intent);
                });
            } catch (IOException e) {
                runOnUiThread(() -> Toast.makeText(EmergencyButton.this, "긴급 호출 전송 실패", Toast.LENGTH_LONG).show());
                e.printStackTrace();
            }
        }).start();
    }

    public void endSocket() {
        super.onDestroy();
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();  // 소켓 종료
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
