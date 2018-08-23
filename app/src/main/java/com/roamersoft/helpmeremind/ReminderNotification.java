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

import java.util.Calendar;
import java.util.UUID;

import static android.content.Context.ALARM_SERVICE;

public class ReminderNotification {

    private Context rContext;
    private String rChannelId;
    private int rNotificationId;
    private NotificationCompat.Builder rBuilder;
    private String rReminderTitle;
    private String rReminderText;

    public ReminderNotification(Context context, String notificationTitle, String notificationContent, int notificationId) {

        this.rContext = context;
        this.rReminderTitle = notificationTitle;
        this.rReminderText = notificationContent;
        this.rChannelId = UUID.randomUUID().toString();
        this.rNotificationId = notificationId;

        this.createNotificationChannel();
        this.createNotification();
    }


    /**
     * Create the NotificationChannel, but only on API 26+ because the NotificationChannel class is new and not in the support library
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = this.rContext.getString(R.string.channel_name);
            String description = this.rContext.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(this.rChannelId, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = this.rContext.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Creates the reminder notification with title and text.
     * When no notification text is given, the notification will then only show the title.
     */
    private void createNotification(){
        Intent notifyIntent = new Intent(this.rContext, NotificationPressedActivity.class);

        notifyIntent.putExtra("mNotificationId", this.rNotificationId);

        // Set the Activity to start in a new, empty task
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Create the PendingIntent
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                this.rContext, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
        );

        if (rReminderText == null || rReminderText.equals("")) {
            this.rBuilder = new NotificationCompat.Builder(this.rContext, this.rChannelId)
                    .setSmallIcon(R.drawable.ic_notification_icon)
                    .setContentTitle(this.rReminderTitle)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setContentIntent(notifyPendingIntent)
                    .setDeleteIntent(notifyPendingIntent);

        } else {
            this.rBuilder = new NotificationCompat.Builder(this.rContext, this.rChannelId)
                    .setSmallIcon(R.drawable.ic_notification_icon)
                    .setContentTitle(this.rReminderTitle)
                    .setContentText(this.rReminderText)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setContentIntent(notifyPendingIntent)
                    .setDeleteIntent(notifyPendingIntent);
        }
    }

    /**
     * Shows the current reminder notification.
     */
    public void showNotification(){
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this.rContext);
        notificationManager.notify(this.rNotificationId, this.rBuilder.build());
    }

    /**
     * Calls the reminder directly when the alarm date is in the past.
     * It schedules an alarm when the date is in the future.
     * @param context
     * @param alarm
     */
    public static void scheduleReminderNotification (Context context, Alarm alarm) {

        Long currentTimeInMillis = Calendar.getInstance().getTimeInMillis();

        if (currentTimeInMillis >= alarm.getDateTime()){
            // Show reminder directly.
            ReminderNotification reminderNotification = new ReminderNotification(context, alarm.getTitle(), alarm.getNote(), alarm.getId());
            reminderNotification.showNotification();
        } else {
            //Alarm service
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
}
