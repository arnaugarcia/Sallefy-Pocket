package com.sallefy.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlaylistRequest {

    @SerializedName("cover")
    private String cover;

    @SerializedName("description")
    private String description;

    @SerializedName("id")
    private Integer id;

    @SerializedName("name")
    private String name;

    @SerializedName("publicAccessible")
    private boolean publicAccessible;

    @SerializedName("thumbnail")
    private String thumbnail;

    @SerializedName("tracks")
    private List<Track> tracks = null;

    public PlaylistRequest() {
    }

    public PlaylistRequest(Playlist playlist) {
        this.cover = playlist.getCover();
        this.description = playlist.getDescription();
        this.id = playlist.getId();
        this.name = playlist.getName();
        this.publicAccessible = playlist.getPublicAccessible();
        this.thumbnail = playlist.getThumbnail();
        this.tracks = playlist.getTracks();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPublicAccessible() {
        return publicAccessible;
    }

    public void setPublicAccessible(boolean publicAccessible) {
        this.publicAccessible = publicAccessible;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public void addTrack(Track track){
        this.tracks.add(track);
    }
}
