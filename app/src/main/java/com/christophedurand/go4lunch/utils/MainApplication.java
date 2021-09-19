package com.christophedurand.go4lunch.utils;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;


public class MainApplication extends Application {

    private static Application application;

    public static Application getApplication() {
        return application;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        application = this;
    }
}
