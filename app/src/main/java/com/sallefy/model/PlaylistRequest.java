package com.sallefy.model;

import com.google.gson.annotations.SerializedName;

public class PlaylistRequest {

    @SerializedName("cover")
    private String cover;

    @SerializedName("description")
    private String description;

    @SerializedName("name")
    private String name;

    @SerializedName("publicAccessible")
    private boolean publicAccessible;

    @SerializedName("thumbnail")
    private String thumbnail;

    public PlaylistRequest() {
    }

    public PlaylistRequest(String cover, String description, String name, boolean publicAccessible, String thumbnail) {
        this.cover = cover;
        this.description = description;
        this.name = name;
        this.publicAccessible = publicAccessible;
        this.thumbnail = thumbnail;
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
}
