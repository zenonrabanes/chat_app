package com.zenrabanes.chat_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseDatabase reference;

    EditText edEmail;
    EditText edPassword;
    Button btnSignup;
    TextView tvLogin;
    TextView tvUserError;
    TextView tvPasswordError;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_sign_up);
        super.onCreate(savedInstanceState);

        edEmail = (EditText) findViewById(R.id.edEmail);
        edPassword = (EditText) findViewById(R.id.edPassword);
        btnSignup = (Button) findViewById(R.id.btnSignUp);
        tvLogin = (TextView) findViewById(R.id.tvLogin);
        tvUserError = (TextView) findViewById(R.id.tvUserError);
        tvPasswordError = (TextView) findViewById(R.id.tvPasswordError);

        auth = FirebaseAuth.getInstance();

        btnSignup.setOnClickListener(view -> {
            String email = edEmail.getText().toString().trim();
            String password = edPassword.getText().toString();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                tvUserError.setVisibility(View.VISIBLE);
                tvPasswordError.setVisibility(View.VISIBLE);
            } else if (password.length() < 6) {
                tvPasswordError.setVisibility(View.VISIBLE);
            } else {
                register(email, password);
            }
        });

        tvLogin.setOnClickListener(view -> {
            startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
            finish();
        });
    }

    private void register(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        assert firebaseUser != null;
                        String userId = firebaseUser.getUid();

                        reference = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = reference.getReference("users").child(userId);

                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("id", userId);
                        hashMap.put("email", email);

                        myRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(SignUpActivity.this, "Value incorrect.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
