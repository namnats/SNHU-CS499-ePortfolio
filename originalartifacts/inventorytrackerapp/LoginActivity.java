package com.example.projthree;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    DatabaseHelper db;
    EditText userEdit, passEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        db = new DatabaseHelper(this);
        userEdit = findViewById(R.id.editTextText2);
        passEdit = findViewById(R.id.editTextTextPassword);
        Button loginBtn = findViewById(R.id.button);
        Button registerBtn = findViewById(R.id.button2);

        loginBtn.setOnClickListener(v -> {
            String user = userEdit.getText().toString();
            String pass = passEdit.getText().toString();
            if (db.checkUser(user, pass)) {
                Intent intent = new Intent(LoginActivity.this, DataActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
            }
        });

        registerBtn.setOnClickListener(v -> {
            String user = userEdit.getText().toString();
            String pass = passEdit.getText().toString();
            if (db.addUser(user, pass)) {
                Toast.makeText(this, "User Registered!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}