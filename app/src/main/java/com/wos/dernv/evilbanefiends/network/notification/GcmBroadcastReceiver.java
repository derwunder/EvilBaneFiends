package com.wos.dernv.evilbanefiends.network.notification;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by der_w on 5/14/2016.
 */
public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

    //indicamos que GcmIntentService manejara el intent
    @Override
    public void onReceive(Context context, Intent intent) {
        ComponentName comp= new ComponentName(context.getPackageName(),
                GcmIntentService.class.getName());
        //Iniciar Servicio, manteniendo el dispositivo encendido mientras procede
        startWakefulService(context,(intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
}
