package com.sallefy.managers.user;

import com.sallefy.model.User;

public interface UserCallback {
    void onUserDataReceived(User user);
    void onUserDataFailure(Throwable throwable);
}
