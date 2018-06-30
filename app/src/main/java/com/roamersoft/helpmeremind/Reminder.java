package com.roamersoft.helpmeremind;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.util.Date;
import java.util.UUID;

public class Reminder {

    private Context mContext;
    private String mChannelId;
    private int mNotificationId;
    private NotificationCompat.Builder mBuilder;
    private String mReminderTitle;
    private String mReminderText;

    public Reminder(Context context, String notificationTitle, String notificationContent) {

        this.mContext = context;
        this.mReminderTitle = notificationTitle;
        this.mReminderText = notificationContent;
        this.mChannelId = UUID.randomUUID().toString();
        this.mNotificationId = this.createUniqueID();

        this.createNotificationChannel();
        this.createNotification();
    }

    /**
     * Create the NotificationChannel, but only on API 26+ because the NotificationChannel class is new and not in the support library
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = this.mContext.getString(R.string.channel_name);
            String description = this.mContext.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(this.mChannelId, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = this.mContext.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Creates the reminder notification with title and text.
     * When no notification text is given, the notification will then only show the title.
     */
    private void createNotification(){
        if (mReminderText == null || mReminderText.equals("")) {
            this.mBuilder = new NotificationCompat.Builder(this.mContext, this.mChannelId)
                    .setSmallIcon(R.drawable.ic_notification_icon)
                    .setContentTitle(this.mReminderTitle)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        } else {
            this.mBuilder = new NotificationCompat.Builder(this.mContext, this.mChannelId)
                    .setSmallIcon(R.drawable.ic_notification_icon)
                    .setContentTitle(this.mReminderTitle)
                    .setContentText(this.mReminderText)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        }
    }

    /**
     * Shows the current reminder notification.
     */
    public void showNotification(){
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this.mContext);
        notificationManager.notify(this.mNotificationId, this.mBuilder.build());
    }

    /**
     * Generates an unique ID based on the current date and time.
     * @return Returns date in milliseconds as int
     */
    public int createUniqueID(){
        Date date = new Date();
        int id = (int) date.getTime();
        return id;
    }
}
