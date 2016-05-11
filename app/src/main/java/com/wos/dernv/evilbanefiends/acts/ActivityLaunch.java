package com.wos.dernv.evilbanefiends.acts;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.wos.dernv.evilbanefiends.R;

/**
 * Created by der_w on 5/9/2016.
 */
public class ActivityLaunch extends AppCompatActivity {

    private int SPLASH_TIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

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
}
