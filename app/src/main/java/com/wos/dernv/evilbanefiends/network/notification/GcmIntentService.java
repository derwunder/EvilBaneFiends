package com.wos.dernv.evilbanefiends.network.notification;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.wos.dernv.evilbanefiends.R;
import com.wos.dernv.evilbanefiends.acts.ActivityMain;
import com.wos.dernv.evilbanefiends.logs.L;
import com.wos.dernv.evilbanefiends.myapp.MyApp;
import com.wos.dernv.evilbanefiends.objects.NotifyItem;

/**
 * Created by der_w on 5/14/2016.
 */
public class GcmIntentService extends IntentService{

    public static final int NOTIFICATION_ID = 1;
    private static final  String TAG = "GcmIntentService";
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras  = intent.getExtras();
        GoogleCloudMessaging gcm =GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.

        String messageType = gcm.getMessageType(intent);

        if(!extras.isEmpty()){ // has effect of unparcelling Bundle
             /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */



            if(GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)){

                sendNotification("Send error: " + extras.toString(),"Error","base","noAsig","noAsig");
            }else if(GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)){
                sendNotification("Deleted messages on server: " +
                        extras.toString(),"Error","base","noAsig","noAsig");
            }else if(GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {

                for (int i = 0; i < 5; i++) {
                    Log.i(TAG, "Working... " + (i + 1)
                            + "/5 @ " + SystemClock.elapsedRealtime());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                    }
                }

                //   Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
                // Post notification of received message.
                sendNotification(extras.getString("Msj"), extras.getString("Admin"), extras.getString("Tipo"),
                        extras.getString("Url_noti"),extras.getString("Url_vid"));
                //    Log.i(TAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }


    private void sendNotification(String msg,String admin, String tipo,String url_noti, String url_vid){
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(
                this,0, new Intent(this, ActivityMain.class),0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        //. setSmallIcon(R.drawable.ic)
                        .setContentTitle(admin)
                        .setAutoCancel(true)
                        // .setSmallIcon(R.mipmap.ic_launcher)
                        .setStyle(new NotificationCompat.BigTextStyle())
                        .setContentText(msg)
                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                        .setLights(Color.BLUE, 3000, 3000);

        if(tipo.equals("msj")) {
            mBuilder.setSmallIcon(R.drawable.ic_fiend_00);

            NotifyItem notifyItem= new NotifyItem();
            notifyItem.setTipo(tipo);
            notifyItem.setAdmin_send(admin);
            notifyItem.setMensaje(msg);
            notifyItem.setUrl_noti(url_noti);
            notifyItem.setUrl_vid(url_vid);
            L.t(MyApp.getAppContext(),"admin: "+notifyItem.getAdmin_send()+
            "\nMSJ: "+notifyItem.getMensaje());
            MyApp.getWritableDatabase().insertNotiItem(notifyItem);
        }
        else if(tipo.equals("batalla"))
            mBuilder.setSmallIcon(R.drawable.ic_fiend_00);
        else if(tipo.equals("jugador"))
            mBuilder.setSmallIcon(R.drawable.ic_fiend_00);


        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(
                NOTIFICATION_ID, mBuilder.build()
        );

   /*     NotifyItem notifyItem= new NotifyItem();
        notifyItem.setClase(mt);notifyItem.setMsj(msg);
        notifyItem.setMod(mod);

        MiAplicativo.getWritableDatabase().insertNotiItem(notifyItem);*/


    }

}
