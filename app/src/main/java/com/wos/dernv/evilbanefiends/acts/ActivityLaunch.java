package com.wos.dernv.evilbanefiends.acts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.wos.dernv.evilbanefiends.R;
import com.wos.dernv.evilbanefiends.myapp.MyApp;
import com.wos.dernv.evilbanefiends.network.notification.RegisterApp;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by der_w on 5/9/2016.
 */
public class ActivityLaunch extends AppCompatActivity {

    private int SPLASH_TIME = 3000;

    //VARIABLES EMPLEADAS PARA Push notifications
    private static final  int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String TAG = "GCMRelated";
    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    String regid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);


        if(getRegistrationId(getApplicationContext()).equals("") ){
            if(checkPlayServices()) {
                gcm=GoogleCloudMessaging.getInstance(getApplicationContext());
                regid=getRegistrationId(getApplicationContext());

                if(regid.isEmpty()){

                    new RegisterApp(
                            getApplicationContext(),
                            gcm,
                            getAppVersion(getApplicationContext()))
                            .execute();

                    goToMain();
                }

            }
        }
        else{goToMain();}



    }

    public void goToMain(){
        SPLASH_TIME=2000;
        YoYo.with(Techniques.Shake)
                .duration(1000)
                .playOn(findViewById(R.id.imageView));
        new AperturaDeAplicacion().execute();
    }




//LANZADOR DE APLICACION

    public class AperturaDeAplicacion extends AsyncTask {

        private Intent myIntent;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            myIntent = new Intent(ActivityLaunch.this, ActivityMain.class);
        }

        @Override
        protected Object doInBackground(Object[] params) {

            try {
                Thread.sleep(SPLASH_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            startActivity(myIntent);
            finish();
        }

    }


    //GCM  REGISTRATION and functions neededs
    private boolean checkPlayServices(){
        int resultCode = GooglePlayServicesUtil.
                isGooglePlayServicesAvailable(this);
        if(resultCode != ConnectionResult.SUCCESS){
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)){
                GooglePlayServicesUtil.getErrorDialog(resultCode,this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }else{
                Log.i(TAG, "Device Not Supported");
                finish();
            }return false;
        }return true;
    }

    private String getRegistrationId(Context context){
        final SharedPreferences prefs= getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID,"");
        if(registrationId.isEmpty()){
            Log.i(TAG, "Registration Not Found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
                Integer.MIN_VALUE);
        int currentVersion = getAppVersion(getApplicationContext());
        if(registeredVersion != currentVersion){
            Log.i(TAG,"App version changed");
            return "";
        }return registrationId;
    }

    private SharedPreferences getGCMPreferences(Context context){
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return  getSharedPreferences(ActivityMain.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
}
