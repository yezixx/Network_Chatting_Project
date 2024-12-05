package com.example.team3.client_chat;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class RequestLogin {

    private static final String SERVER_IP = "192.168.1.100"; // 서버 IP
    private static final int SERVER_PORT = 3000; // 서버 포트
    private Handler mainHandler;

    public RequestLogin() {
        mainHandler = new Handler(Looper.getMainLooper());  // 메인 UI 스레드에서 Toast 메시지를 띄우기 위해
    }

    // 로그인 요청 메소드
    public void sendLoginRequest(String username, String password, LoginCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 서버와 연결
                    Socket socket = new Socket(SERVER_IP, SERVER_PORT);
                    OutputStream outputStream = socket.getOutputStream();
                    PrintWriter out = new PrintWriter(outputStream, true);

                    // 로그인 정보 전송
                    out.println("login:" + username + ":" + password);

                    // 서버와의 통신 종료
                    socket.close();

                    // 로그인 성공 시 콜백 호출
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onSuccess("로그인 성공");
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                    // 오류 발생 시 콜백 호출
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onFailure("서버 연결 실패");
                        }
                    });
                }
            }
        }).start();
    }

    // 로그인 결과를 처리할 콜백 인터페이스
    public interface LoginCallback {
        void onSuccess(String message);
        void onFailure(String errorMessage);
    }
}
