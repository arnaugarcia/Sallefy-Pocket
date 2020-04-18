package com.sallefy.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.sallefy.R;
import com.sallefy.managers.playlists.CreatePlaylistCallback;
import com.sallefy.managers.playlists.PlaylistManager;
import com.sallefy.model.Playlist;
import com.sallefy.model.PlaylistRequest;

public class CreatePlaylistActivity extends AppCompatActivity implements CreatePlaylistCallback {

    CircularImageView ivCoverPicker;
    EditText etName;
    EditText etDescription;
    SwitchMaterial sPrivate;
    Button btnCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_playlist);

        initViews();
        btnCreate.setOnClickListener(view -> {
            if (etName.getText().toString().equals("")) {
                Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
            } else {
                PlaylistRequest playlist = new PlaylistRequest();
                playlist.setName(etName.getText().toString());
                playlist.setDescription(etDescription.getText().toString());
                playlist.setPublicAccessible(sPrivate.isChecked());
                PlaylistManager.getInstance().createPlaylist(this, playlist, this);
            }
        });
    }

    private void initViews() {
        ivCoverPicker = findViewById(R.id.iv_playlist_thumbnail);
        etName = findViewById(R.id.et_playlist_name);
        etDescription = findViewById(R.id.et_playlist_description);
        sPrivate = findViewById(R.id.switch_private);
        btnCreate = findViewById(R.id.btn_create);
    }

    @Override
    public void onCreatePlaylistSuccess(Playlist playlist) {
        Toast.makeText(this, "Playlist created successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onCreatePlaylistFailure(Throwable throwable) {
        Toast.makeText(this, "Failed to create the playlist", Toast.LENGTH_SHORT).show();
        System.err.println(throwable);
    }
}
