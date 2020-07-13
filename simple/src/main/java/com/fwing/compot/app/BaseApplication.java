package com.fwing.compot.app;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;



/**
 * @author Fwing
 * @date 2019\12\13 0013 15:25
 */
public class BaseApplication extends Application {

    private static BaseApplication instance;

    public static BaseApplication getInstance(){
        return instance;
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
