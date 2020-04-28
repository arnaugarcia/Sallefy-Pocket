package com.sallefy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sallefy.R;
import com.sallefy.managers.authentication.AuthenticationCallback;
import com.sallefy.managers.authentication.AuthenticationManager;
import com.sallefy.model.JWTToken;
import com.sallefy.model.UserCredentials;
import com.sallefy.services.authentication.AuthenticationUtils;

public class LoginActivity extends AppCompatActivity implements AuthenticationCallback {
    private Button mLoginButton;
    private EditText mUsernameField;
    private EditText mPasswordField;
    private TextView mRegisterText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();

        mLoginButton.setOnClickListener(listener -> {
            String username = mUsernameField.getText().toString();
            String password = mPasswordField.getText().toString();
            attemptLogin(username, password);
        });

        mRegisterText.setOnClickListener(listener -> goToRegisterActivity());
    }

    private void initView() {
        mUsernameField = findViewById(R.id.et_login_username);
        mPasswordField = findViewById(R.id.et_login_password);
        mLoginButton = findViewById(R.id.bt_login);
        mRegisterText = findViewById(R.id.tv_login_register);
    }

    private void attemptLogin(String username, String password) {
        UserCredentials userCredentials = new UserCredentials(username, password, true);
        AuthenticationManager.getInstance().authenticate(userCredentials, this);
    }

    @Override
    public void onAuthenticationSuccess(JWTToken token) {
        AuthenticationUtils.resetValues(this);
        AuthenticationUtils.saveToken(this, token);
        AuthenticationUtils.saveLogin(this, this.mUsernameField.getText().toString());
        goToMainActivity();
    }

    @Override
    public void onAuthenticationFailure(Throwable throwable) {
        Toast.makeText(this, "Error authenticating: " + throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void goToRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
