package com.sallefy.managers.user;

import com.sallefy.model.User;

public interface UserDataCallback {
    void onUserDataSuccess(User user);
    void onUserDataFailure(Throwable throwable);
}
