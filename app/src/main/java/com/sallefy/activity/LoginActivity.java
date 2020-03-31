package com.sallefy.activity;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.sallefy.R;
import com.sallefy.managers.authentication.AuthenticationCallback;
import com.sallefy.model.JWTToken;

public class LoginActivity extends AppCompatActivity implements AuthenticationCallback {

    private Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // mLoginButton = findViewById(R.id)
    }

    @Override
    public void onAuthenticationSuccess(JWTToken token) {

    }

    @Override
    public void onAuthenticationFailure(Throwable throwable) {

    }
}
