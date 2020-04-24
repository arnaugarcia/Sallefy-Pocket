package com.sallefy.managers.user;

import com.sallefy.model.User;

import java.util.List;

public interface PopularUsersCallback {
    void onPopularUsersSuccess(List<User> users);
    void onPopularUsersFailure(Throwable throwable);
}
