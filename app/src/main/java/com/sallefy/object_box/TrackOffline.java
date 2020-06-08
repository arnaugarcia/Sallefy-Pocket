package com.sallefy.object_box;

import com.sallefy.model.Track;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class TrackOffline {

    @Id long id;
    String name;
    byte[] audio;

    public TrackOffline(Track track) {
        this.id = track.getId();
        this.name = track.getName();

        // Read audio
    }

    @Override
    public String toString() {
        return "TrackOffline{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
