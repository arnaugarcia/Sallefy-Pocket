package com.sallefy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.sallefy.R;
import com.sallefy.services.authentication.TokenStoreManager;

import static com.sallefy.services.authentication.AuthenticationUtils.isUserLogged;

public class MainActivity extends AppCompatActivity {

    private static final long SPLASH_SCREEN_DELAY = 2000L;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TokenStoreManager.getInstance().setContext(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Here we need to check the permissions too
        handler.postDelayed(this::checkUserLogin, SPLASH_SCREEN_DELAY);

    }

    private void checkUserLogin() {
        if (!isUserLogged(this)) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

        } else {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
