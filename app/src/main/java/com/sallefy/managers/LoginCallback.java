package com.sallefy.managers;

public interface LoginCallback {
    void onSuccess(String token);
    void onFailure(Throwable throwable);
}
