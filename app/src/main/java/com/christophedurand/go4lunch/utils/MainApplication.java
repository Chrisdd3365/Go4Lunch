package com.christophedurand.go4lunch.utils;

import android.app.Application;


public class MainApplication extends Application {

    private static Application application;

    public static Application getApplication() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        application = this;
    }
}
