package com.wos.dernv.evilbanefiends.myapp;

import android.app.Application;
import android.content.Context;

import com.wos.dernv.evilbanefiends.database.DBFiend;

/**
 * Created by der_w on 5/9/2016.
 */
public class MyApp extends Application {

    private static MyApp sInstance;

    private static DBFiend mDatabase;

    public static MyApp getInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }

    public synchronized static DBFiend getWritableDatabase() {
        if (mDatabase == null) {
            mDatabase = new DBFiend(getAppContext());
        }
        return mDatabase;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        mDatabase = new DBFiend(this);
    }
}