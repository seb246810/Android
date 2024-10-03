package com.example.mentalhealth;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseInstanceService extends FirebaseMessagingService {




    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Handle FCM message
        if (remoteMessage.getData().size() > 0) {
            Log.d("FCM Message", "Message data payload: " +
                    remoteMessage.getData());
            // Handle data payload
        }
        if (remoteMessage.getNotification() != null) {
            Log.d("FCM Message", "Message Notification Body: " +
                    remoteMessage.getNotification().getBody());

            handleNotification(remoteMessage);
            // Handle notification payload
        }
    }
    private void handleNotification(RemoteMessage remoteMessage) {

        Intent intent = new Intent(this, MainActivity.class); //create intent to class
        intent.putExtra("diaryfragment","diary"); //add intent put extra to navigate to diary fragment

        PendingIntent pintent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT); //create pending intent to use intent



        NotificationCompat.Builder builder = new
                NotificationCompat.Builder(getApplicationContext(), "firebase")
                .setSmallIcon(R.drawable.baseline_arrow_back_ios_24)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pintent); //set the content intent to go to mainactivity and load fragment.


        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(1, builder.build());
    }
}
