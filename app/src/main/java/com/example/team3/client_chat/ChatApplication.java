package com.example.team3.client_chat;

import android.app.Application;

import java.io.IOException;
import java.net.Socket;

public class ChatApplication extends Application {

    private Socket mSocket;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    // 소켓 연결을 관리하는 메소드
    public Socket getSocket(String serverIP, int serverPort) {
        try {
            // 서버와 연결
            if (mSocket == null || mSocket.isClosed()) {
                mSocket = new Socket(serverIP, serverPort); // 서버 IP와 포트 번호로 연결
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mSocket;
    }

    // 소켓 객체 반환
    public void closeSocket() {
        try {
            if (mSocket != null && !mSocket.isClosed()) {
                mSocket.close();  // 연결 종료
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
