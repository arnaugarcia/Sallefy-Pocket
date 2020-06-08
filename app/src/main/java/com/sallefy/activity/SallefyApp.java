package com.sallefy.activity;

import android.app.Application;

import com.sallefy.object_box.ObjectBox;

public class SallefyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ObjectBox.init(this);
    }
}
