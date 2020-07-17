package com.wmdd.errandz.main;

import android.app.Application;

public class ErrandzApplication extends Application {

    private static ErrandzApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static ErrandzApplication getInstance() {
        return instance;
    }
}
