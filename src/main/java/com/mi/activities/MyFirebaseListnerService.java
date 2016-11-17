package com.mi.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.TextView;

import com.esotericsoftware.minlog.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mi.test.mypiece.R;

import java.util.Map;
import java.util.Random;

import twitter4j.SimilarPlaces;

/**
 * Created by karandsingh on 16-10-29.
 */
public class MyFirebaseListnerService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseListner";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String TYPE = "", value = "";

        System.out.println("FROM:" + remoteMessage.getFrom());

        if(remoteMessage.getData().size() > 0)
        {
            System.out.println("Message Data:" + remoteMessage.getData());

            for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {

                TYPE = entry.getKey();
                value = entry.getValue();
            }

        }

        /*
            GO_TO_SPECIFIC_DEAL = dealID
         */

        if(remoteMessage.getNotification() != null){
            System.out.println("Message Body:" + remoteMessage.getNotification().getBody());

            sendNotification(remoteMessage.getNotification().getBody(), TYPE, value);
        }


    }

    private void sendNotification(String body, String TYPE, String value)
    {
        Intent intent = null;
        System.out.println("NOTIFICATION TYPE : " + TYPE);

        if(TYPE.equals("PLAIN_NOTIFICATION")) {

            intent = new Intent(this, SplashScreenActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        else if(TYPE.equals("GO_TO_SPECIFIC_DEAL"))
        {
            intent = new Intent(this, SplashScreenActivity.class);
            intent.putExtra("NOTIFICATION_ROUTE", "NOTIFICATION_DEAL");
            intent.putExtra("NOTIFICATION_VALUE", value);
            intent.setAction("NOTIFICATION_DEAL");
            intent.setAction(value);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        }
        else if(TYPE.equals("GO_TO_SPECIFIC_PURCHASE"))
        {
            intent = new Intent(this, SplashScreenActivity.class);
            intent.putExtra("NOTIFICATION_ROUTE", "NOTIFICATION_PURCHASE");
            intent.putExtra("NOTIFICATION_VALUE", value);
            intent.setAction("NOTIFICATION_PURCHASE");
            intent.setAction(value);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        }
        else
        {
            intent = new Intent(this, SplashScreenActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }

        int notificationId = new Random().nextInt();

        PendingIntent pendingIntent = PendingIntent.getActivity(this, notificationId, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(getNotificationIcon())
                .setContentTitle("MyPiece")
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(notificationSound)
                .setContentIntent(pendingIntent);



        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notifyBuilder.build());
    }

    public int getNotificationIcon()
    {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.notification_icon : R.drawable.app_icon_mypiece;
    }

}
