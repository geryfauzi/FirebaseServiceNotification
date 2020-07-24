package com.ergnologi.firebaseservicenotification;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {

    private EditText etChat;
    private Button btnSend;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferenceLatest;
    //Mengambil username dari activity sebelumnya
    private String username;
    //
    private RecyclerView rvChat;
    private ArrayList<ChatModels> list = new ArrayList<>();
    private Session session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //ViewBinding
        etChat = findViewById(R.id.etChat);
        btnSend = findViewById(R.id.btnKirim);
        rvChat = findViewById(R.id.rvChat);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData();
            }
        });
        //Firebase Instance
        databaseReference = FirebaseDatabase.getInstance().getReference("chat");
        databaseReferenceLatest = FirebaseDatabase.getInstance().getReference("terbaru");
        // Pengecekan Session
        session = new Session(this);
        HashMap<String, String> user = session.getUsername();
        username = user.get(Session.un);
        // RecyclerView
        rvChat.setHasFixedSize(true);
        rvChat.setLayoutManager(new LinearLayoutManager(this));
        getData();
        //StartService
        Intent intent = new Intent(getApplicationContext(), MyService.class);
        startService(intent);
    }

    private void getData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int posisi = list.size();
                list.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    ChatModels chatModels = snapshot1.getValue(ChatModels.class);
                    list.add(chatModels);
                }
                ChatAdapter chatAdapter = new ChatAdapter(list);
                rvChat.setAdapter(chatAdapter);
                chatAdapter.notifyItemInserted(posisi);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendData() {
        //Mengambil tanggal Hari ini
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm");
        String tanggal = sdf.format(new Date());

        String chat = etChat.getText().toString().trim();
        //Pengecekan jika user memasukan input yang kosong
        if (TextUtils.isEmpty(chat)) {
            Toast.makeText(getApplicationContext(), "Isi chat tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return;
        }
        String id = databaseReference.push().getKey();
        ChatModels chatModels = new ChatModels(username, tanggal, chat);
        databaseReference.child(id).setValue(chatModels);
        databaseReferenceLatest.setValue(chatModels);
        etChat.setText("");
    }
}