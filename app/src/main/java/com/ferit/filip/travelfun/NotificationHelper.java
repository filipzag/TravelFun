package com.ferit.filip.travelfun;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;

@TargetApi(26)
public class NotificationHelper extends ContextWrapper {


    private static final String FF_Channel_id="com.ferit.filip.travelfun.FF";
    private static  final String FF_Channel_name="FF channel";
    private NotificationManager manager;

    public NotificationHelper(Context base) {


        super(base);
        createChannels();
    }

    private void createChannels() {

        NotificationChannel ff_channel = new NotificationChannel(FF_Channel_id,FF_Channel_name,manager.IMPORTANCE_DEFAULT);
        ff_channel.enableLights(true);
        ff_channel.enableVibration(true);
        ff_channel.setLightColor(Color.GREEN);
        ff_channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        getManager().createNotificationChannel(ff_channel);
    }

    public NotificationManager getManager() {

        if(manager==null){


            manager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;

    }

    public Notification.Builder getFFChannelNotification(String title,String body){


        return  new Notification.Builder(getApplicationContext(),FF_Channel_id)
                .setContentText(body)
                .setContentTitle(title)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setAutoCancel(true);
    }
}
