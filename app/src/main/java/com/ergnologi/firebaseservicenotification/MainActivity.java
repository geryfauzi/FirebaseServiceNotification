package com.ergnologi.firebaseservicenotification;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnChatRoom;
    private EditText etUsername;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Hide Action Bar
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        //View Binding
        btnChatRoom = findViewById(R.id.btnChatRoom);
        etUsername = findViewById(R.id.txtUsername);
        //SetOnCLick Listener
        btnChatRoom.setOnClickListener(this);
        //Pengecekan Session
        session = new Session(this);
        HashMap<String, String> user = session.getUsername();
        if (user.get(Session.un) != null) {
            startActivity(new Intent(MainActivity.this, ChatActivity.class));
            finish();
        }
    }

    //Method Untuk memulai aktivitas baru
    private void startChatRoom() {
        //Mengambil text dari TextView
        String username = etUsername.getText().toString().trim();
        //Pengecekan jika pengguna tidak memasukan username
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(getApplicationContext(), "Username Tidak Boleh Kosong!", Toast.LENGTH_SHORT).show();
            return;
        }
        //Memulai Activity Baru
        Intent intent = new Intent(MainActivity.this, ChatActivity.class);
        session.saveUsername(username);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        //Aksi yang akan dilakukan jika pengguna menekan Button Enter Chat Room
        startChatRoom();
    }


}