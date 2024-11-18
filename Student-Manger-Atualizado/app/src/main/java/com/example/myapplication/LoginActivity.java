package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar as views
        EditText emailField = findViewById(R.id.emailField);
        EditText passwordField = findViewById(R.id.passwordField);
        Button loginButton = findViewById(R.id.loginButton);
        Button registerButton = findViewById(R.id.registerButton);
        Button registerVisitorButton = findViewById(R.id.registerVisitorButton);

        // Inicializar o banco de dados
        LoginDatabaseHelper dbHelper = new LoginDatabaseHelper(this);

        // Login
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailField.getText().toString().trim().toLowerCase();
                String password = passwordField.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String userType = dbHelper.getUserType(email, password);

                if (userType == null) {
                    Toast.makeText(LoginActivity.this, "Credenciais inválidas.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(LoginActivity.this, MainAtiv.class);
                    intent.putExtra("USER_TYPE", userType);
                    startActivity(intent);
                }
            }
        });

        // Registrar Administrador
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailField.getText().toString().trim().toLowerCase();
                String password = passwordField.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (dbHelper.addUser(email, password, "Administrador")) {
                    Toast.makeText(LoginActivity.this, "Administrador registrado com sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Erro: Usuário já existe ou houve falha no registro.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Registrar Visitante
        registerVisitorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailField.getText().toString().trim().toLowerCase();
                String password = passwordField.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (dbHelper.addUser(email, password, "Visitante")) {
                    Toast.makeText(LoginActivity.this, "Visitante registrado com sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Erro: Usuário já existe ou houve falha no registro.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
