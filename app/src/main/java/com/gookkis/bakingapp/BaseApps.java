package com.gookkis.bakingapp;

import android.app.Application;
import android.content.Context;

import timber.log.Timber;

/**
 * Created by herikiswanto on 8/22/17.
 */

public class BaseApps extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        BaseApps.context = getApplicationContext();

        //Timber Debug
        Timber.plant(new Timber.DebugTree());
    }

    public static Context getAppContext() {
        return BaseApps.context;
    }
}
