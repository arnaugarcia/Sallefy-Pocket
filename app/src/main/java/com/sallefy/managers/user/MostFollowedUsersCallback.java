package com.sallefy.managers.user;

import com.sallefy.model.User;

import java.util.List;

public interface MostFollowedUsersCallback {
    void onMostFollowedUsersSuccess(List<User> users);
    void onMostFollowedUsersFailure(Throwable throwable);
}
