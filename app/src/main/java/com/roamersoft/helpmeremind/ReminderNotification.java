package com.roamersoft.helpmeremind;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.roamersoft.helpmeremind.broadcastReceivers.AlarmReceiver;
import com.roamersoft.helpmeremind.models.Alarm;

import java.util.Date;
import java.util.UUID;

import static android.content.Context.ALARM_SERVICE;

public class ReminderNotification {

    private Context mContext;
    private String mChannelId;
    private int mNotificationId;
    private NotificationCompat.Builder mBuilder;
    private String mReminderTitle;
    private String mReminderText;

    public ReminderNotification(Context context, String notificationTitle, String notificationContent, int notificationId) {

        this.mContext = context;
        this.mReminderTitle = notificationTitle;
        this.mReminderText = notificationContent;
        this.mChannelId = UUID.randomUUID().toString();
        this.mNotificationId = notificationId;

        this.createNotificationChannel();
        this.createNotification();
    }

    public int getmNotificationId() {
        return mNotificationId;
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
        Intent notifyIntent = new Intent(this.mContext, NotificationPressedActivity.class);

        notifyIntent.putExtra("mNotificationId", this.mNotificationId);

        // Set the Activity to start in a new, empty task
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Create the PendingIntent
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                this.mContext, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
        );

        if (mReminderText == null || mReminderText.equals("")) {
            this.mBuilder = new NotificationCompat.Builder(this.mContext, this.mChannelId)
                    .setSmallIcon(R.drawable.ic_notification_icon)
                    .setContentTitle(this.mReminderTitle)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setContentIntent(notifyPendingIntent);

        } else {
            this.mBuilder = new NotificationCompat.Builder(this.mContext, this.mChannelId)
                    .setSmallIcon(R.drawable.ic_notification_icon)
                    .setContentTitle(this.mReminderTitle)
                    .setContentText(this.mReminderText)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setContentIntent(notifyPendingIntent);
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

    /**
     * Schedules the notification with an alarm.
     * @param context
     * @param alarm
     */
    public static void scheduleReminderNotification (Context context, Alarm alarm) {
        //alarm service
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        //Add data
        Intent intent = new Intent(context,  AlarmReceiver.class);
        intent.putExtra("reminderId", alarm.getId());
        intent.putExtra("reminderTitle", alarm.getTitle());
        intent.putExtra("reminderText", alarm.getNote());

        //create broadcast
        PendingIntent broadcast = PendingIntent.getBroadcast(context, alarm.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //set alarm
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarm.getDateTime(), broadcast);

    }
}
