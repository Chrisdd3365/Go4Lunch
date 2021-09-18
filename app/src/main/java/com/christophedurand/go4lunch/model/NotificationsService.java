package com.christophedurand.go4lunch.model;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.christophedurand.go4lunch.R;
import com.christophedurand.go4lunch.ui.MainActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class NotificationsService extends FirebaseMessagingService {

    private final int NOTIFICATION_ID = 1;
    private final String NOTIFICATION_TAG = "GO4LUNCH";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getNotification() != null) {
            RemoteMessage.Notification notification = remoteMessage.getNotification();
            sendVisualNotification(notification);
        }
    }

    private void sendVisualNotification(RemoteMessage.Notification notification) {

        // Create an Intent that will be shown when user will click on the Notification
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        // Create a Channel (Android 8)
        String channelId = "channelId";

        UserManager.getInstance().getCurrentUserData().addOnSuccessListener(currentUser -> {

            UserManager.getInstance().getAllUsers().whereEqualTo("restaurant.id", currentUser.getRestaurant().getId()).get().addOnSuccessListener(queryDocumentSnapshots -> {

                StringBuilder workmatesList = new StringBuilder();
                for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()){
                    workmatesList.append(document.get("name"));
                    workmatesList.append(" ");
                }
                // Build a Notification object
                NotificationCompat.Builder notificationBuilder =
                        new NotificationCompat.Builder(this, channelId)
                                .setSmallIcon(R.drawable.ic_restaurant_menu)
                                .setContentTitle(notification.getTitle())
                                .setContentText(notification.getBody() + "at " + currentUser.getRestaurant().getName() + ", " + currentUser.getRestaurant().getAddress() + " with " + workmatesList)
                                .setAutoCancel(true)
                                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                .setContentIntent(pendingIntent);

                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                // Support Version >= Android 8
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    CharSequence channelName = "Go4Lunch Messages";
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
                    notificationManager.createNotificationChannel(mChannel);
                }

                // Show notification
                notificationManager.notify(NOTIFICATION_TAG, NOTIFICATION_ID, notificationBuilder.build());
            });
        });
    }

}
