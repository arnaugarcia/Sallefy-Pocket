package com.sallefy.model;

import java.io.Serializable;

public class LikedDTO implements Serializable {
    private final boolean liked;

    public LikedDTO(boolean liked) {
        this.liked = liked;
    }

    public boolean isLiked() {
        return liked;
    }

}
