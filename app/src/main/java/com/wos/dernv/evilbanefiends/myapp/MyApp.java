package com.wos.dernv.evilbanefiends.myapp;

import android.app.Application;
import android.content.Context;

/**
 * Created by der_w on 5/9/2016.
 */
public class MyApp extends Application {

    private static MyApp sInstance;

  //  private static DBPensum mDatabase;

    public static MyApp getInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }

  /*  public synchronized static DBPensum getWritableDatabase() {
        if (mDatabase == null) {
            mDatabase = new DBPensum(getAppContext());
        }
        return mDatabase;
    }*/

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
       // mDatabase = new DBPensum(this);
    }
}