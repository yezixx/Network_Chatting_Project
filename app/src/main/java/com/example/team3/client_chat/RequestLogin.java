package com.example.team3.client_chat;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class RequestLogin {

    private Handler mainHandler;

    public RequestLogin() {
        mainHandler = new Handler(Looper.getMainLooper());  // 메인 UI 스레드에서 Toast 메시지를 띄우기 위해
    }

    // Firebase에서 로그인 요청 처리
    public void sendLoginRequest(String username, String password, LoginCallback callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // 먼저 Employees에서 로그인 시도
        checkEmployeeLogin(database, username, password, callback);
    }

    private void checkEmployeeLogin(FirebaseDatabase database, String username, String password, LoginCallback callback) {
        DatabaseReference employeeRef = database.getReference("Employees").child(username);

        employeeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String storedPassword = dataSnapshot.child("password").getValue(String.class);
                    String name = dataSnapshot.child("name").getValue(String.class); // 이름 가져오기
                    if (storedPassword != null && storedPassword.equals(password)) {
                        // 직원 로그인 성공
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onSuccess("직원 로그인 성공", name);
                            }
                        });
                    } else {
                        // 비밀번호 불일치
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onFailure("비밀번호가 일치하지 않습니다.");
                            }
                        });
                    }
                } else {
                    // 직원 로그인 실패, 환자 로그인 시도
                    checkPatientLogin(database, username, password, callback);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Firebase 오류 처리
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFailure("Firebase 오류: " + databaseError.getMessage());
                    }
                });
            }
        });
    }

    private void checkPatientLogin(FirebaseDatabase database, String username, String password, LoginCallback callback) {
        DatabaseReference patientRef = database.getReference("Patients").child(username);

        patientRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String storedPassword = dataSnapshot.child("password").getValue(String.class);
                    String name = dataSnapshot.child("name").getValue(String.class); // 이름 가져오기
                    if (storedPassword != null && storedPassword.equals(password)) {
                        // 환자 로그인 성공
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onSuccess("환자 로그인 성공", name);
                            }
                        });
                    } else {
                        // 비밀번호 불일치
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onFailure("비밀번호가 일치하지 않습니다.");
                            }
                        });
                    }
                } else {
                    // 환자 로그인 실패
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onFailure("사용자가 존재하지 않습니다.");
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Firebase 오류 처리
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFailure("Firebase 오류: " + databaseError.getMessage());
                    }
                });
            }
        });
    }

    // 로그인 결과를 처리할 콜백 인터페이스
    public interface LoginCallback {
        void onSuccess(String message, String name);
        void onFailure(String errorMessage);
    }
}
