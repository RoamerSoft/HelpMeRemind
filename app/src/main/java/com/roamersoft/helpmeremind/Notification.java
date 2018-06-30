package com.roamersoft.helpmeremind;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.util.UUID;

public class Notification {

    private Context mContext;
    private String mChannelId;
    private NotificationCompat.Builder mBuilder;
    private String mNotificationTitle;
    private String mNotificationText;


    public Notification(Context context, String notificationTitle, String notificationContent) {

        this.mContext = context;
        this.mNotificationTitle = notificationTitle;
        this.mNotificationText = notificationContent;
        this.mChannelId = UUID.randomUUID().toString();

        this.createNotificationChannel();
        this.createNotification();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = this.mContext.getString(R.string.channel_name);
            String description = this.mContext.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(this.mChannelId, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = this.mContext.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Sets only notification title when no text is given.
     */
    private void createNotification(){
        if (mNotificationText == null || mNotificationText.equals("")) {
            this.mBuilder = new NotificationCompat.Builder(this.mContext, this.mChannelId)
                    .setSmallIcon(R.drawable.ic_notification_icon)
                    .setContentTitle(this.mNotificationTitle)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        } else {
            this.mBuilder = new NotificationCompat.Builder(this.mContext, this.mChannelId)
                    .setSmallIcon(R.drawable.ic_notification_icon)
                    .setContentTitle(this.mNotificationTitle)
                    .setContentText(this.mNotificationText)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        }
    }

    public void showNotification(){
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this.mContext);
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, this.mBuilder.build());
    }

    public String getCHANNEL_ID() {
        return this.mChannelId;
    }

    public String getNotificationTitle() {
        return mNotificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.mNotificationTitle = notificationTitle;
    }

    public String getNotificationContent() {
        return mNotificationText;
    }

    public void setNotificationContent(String notificationContent) {
        this.mNotificationText = notificationContent;
    }
}
