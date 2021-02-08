package com.zenrabanes.chat_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.okhttp.internal.DiskLruCache;
import com.zenrabanes.chat_app.Adapter.MessageAdapter;
import com.zenrabanes.chat_app.Model.Chat;
import com.zenrabanes.chat_app.Model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FirebaseUser firebaseUser;
    FirebaseDatabase reference;

    EditText edChatBox;
    Button btnLogout;
    Button btnSend;

    MessageAdapter messageAdapter;
    ArrayList<Chat> mChat;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        edChatBox = (EditText) findViewById(R.id.edChatBox);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnSend = (Button) findViewById(R.id.btnSend);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        btnSend.setOnClickListener(view -> {
            String msg = edChatBox.getText().toString();

            if (!msg.equals("")) {
                sendMessage(firebaseUser.getUid(),firebaseUser.getEmail(), msg);
            } else {
                Toast.makeText(MainActivity.this, "You can't send empty message.", Toast.LENGTH_SHORT).show();
            }
            edChatBox.setText("");
        });

        btnLogout.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });

        reference = FirebaseDatabase.getInstance();
        DatabaseReference myRef = reference.getReference("users");
        getMessage();

    }

    private void sendMessage(String id, String sender, String message) {

        DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", message);
        hashMap.put("sender", sender);
        hashMap.put("id", id);

        firebaseDatabase.child("chats").push().setValue(hashMap);
    }

    private void getMessage() {

        DatabaseReference databaseReference = reference.getReference();
        Query query = databaseReference.child("chats");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat = new ArrayList<>();
                if (dataSnapshot.getChildrenCount() != 0) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        String message = String.valueOf(data.child("message").getValue());
                        String sender = String.valueOf(data.child("sender").getValue());
                        String id = String.valueOf(data.child("id").getValue());
                        Log.wtf("message " + message, "sender " + sender );
                        Chat messageObject = new Chat(sender, message, id);
                        mChat.add(messageObject);
                    }
                }
                if (mChat.size() != 0) {
                    displayMessage(mChat);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void displayMessage(ArrayList<Chat> chatList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        messageAdapter = new MessageAdapter(MainActivity.this, chatList);
        recyclerView.setAdapter(messageAdapter);
    }
}