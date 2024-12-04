package com.example.team3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.team3.ui.theme.Team3Theme
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Firebase Realtime Database 초기화
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()

        // 의료진 정보 추가
        val employeesRef: DatabaseReference = database.getReference("Employees")
        val employee1 = mapOf(
            "emp_id" to "e1234567",
            "password" to "1234567",
            "name" to "doctor A"
        )
        val employee2 = mapOf(
            "emp_id" to "e1122334",
            "password" to "1122334",
            "name" to "doctor B"
        )
        employeesRef.child("e1234567").setValue(employee1)
        employeesRef.child("e1122334").setValue(employee2)

        // 환자 정보 추가
        val patientsRef: DatabaseReference = database.getReference("Patients")
        val patient1 = mapOf(
            "pat_id" to "p1234567",
            "password" to "1234567",
            "name" to "patient A",
            "room" to "101"
        )
        val patient2 = mapOf(
            "pat_id" to "p1111111",
            "password" to "1111111",
            "name" to "patient B",
            "room" to "102"
        )
        patientsRef.child("p1234567").setValue(patient1)
        patientsRef.child("p1111111").setValue(patient2)

        // 채팅 내용 정보 추가
        val chatsRef: DatabaseReference = database.getReference("Chats")
        val chat1 = mapOf(
            "chat_id" to "1", // Auto Increment는 클라이언트에서 관리하거나 서버 로직 구현 필요
            "sender" to "John Doe",
            "message" to "Hello, how are you?",
            "timestamp" to System.currentTimeMillis()
        )
        val chat2 = mapOf(
            "chat_id" to "2",
            "sender" to "Jane Smith",
            "message" to "Need help in room 101",
            "timestamp" to System.currentTimeMillis()
        )
        chatsRef.child("1").setValue(chat1)
        chatsRef.child("2").setValue(chat2)

        // 호출 정보 추가
        val callsRef: DatabaseReference = database.getReference("Emergency_calls")
        val call1 = mapOf(
            "call_id" to "1",
            "patient" to "Alice Brown",
            "room" to "101",
            "timestamp" to System.currentTimeMillis()
        )
        val call2 = mapOf(
            "call_id" to "2",
            "patient" to "Bob Green",
            "room" to "102",
            "timestamp" to System.currentTimeMillis()
        )
        callsRef.child("1").setValue(call1)
        callsRef.child("2").setValue(call2)
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Team3Theme {
        Greeting("Android")
    }
}
