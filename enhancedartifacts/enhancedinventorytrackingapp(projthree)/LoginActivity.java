package com.example.projthree;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private EditText userEdit, passEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        db = DatabaseHelper.getInstance(this);

        userEdit = findViewById(R.id.editTextText2);
        passEdit = findViewById(R.id.editTextTextPassword);
        Button loginBtn = findViewById(R.id.button);
        Button registerBtn = findViewById(R.id.button2);

        loginBtn.setOnClickListener(v -> {
            String user = userEdit.getText().toString().trim();
            String pass = passEdit.getText().toString().trim();

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Fields cannot be blank", Toast.LENGTH_SHORT).show();
                return;
            }

            if (db.checkUser(user, pass)) {
                Intent intent = new Intent(LoginActivity.this, DataActivity.class);
                intent.putExtra("LOGGED_IN_USER", user);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
            }
        });

        registerBtn.setOnClickListener(v -> {
            String user = userEdit.getText().toString().trim();
            String pass = passEdit.getText().toString().trim();

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Registration requires a username and password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (db.addUser(user, pass)) {
                Toast.makeText(this, "User Registered Successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Registration Failed. User might already exist.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}