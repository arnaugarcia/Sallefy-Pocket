package com.sallefy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sallefy.R;
import com.sallefy.managers.registration.RegistrationCallback;
import com.sallefy.managers.registration.RegistrationManager;
import com.sallefy.model.UserRegister;

public class RegisterActivity extends AppCompatActivity implements RegistrationCallback {

    private EditText usernameField;
    private EditText emailField;
    private EditText passwordField;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();

        registerButton.setOnClickListener(listener -> {
            String username = usernameField.getText().toString();
            String email = emailField.getText().toString();
            String password = passwordField.getText().toString();
            attemptRegister(username, email, password);
        });
    }

    private void initView() {
        usernameField = findViewById(R.id.et_register_username);
        emailField = findViewById(R.id.et_register_email);
        passwordField = findViewById(R.id.et_register_password);
        registerButton = findViewById(R.id.btn_register);
    }

    private void attemptRegister(String username, String email, String password) {
        UserRegister userRegister = new UserRegister(email, username, password);
        RegistrationManager.getInstance().register(userRegister, this);
    }

    @Override
    public void onRegistrationSuccess() {
        goToLoginActivity();
    }

    @Override
    public void onRegistrationFailure(Throwable throwable) {
        Toast.makeText(this, "Error registering: " + throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
