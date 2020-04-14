package com.sallefy.managers.registration;

public interface RegistrationCallback {
    void onRegistrationSuccess();
    void onRegistrationFailure(Throwable throwable);
}
