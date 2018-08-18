package com.roamersoft.helpmeremind;

import android.arch.persistence.room.Room;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.roamersoft.helpmeremind.tasks.DeleteReminderParams;
import com.roamersoft.helpmeremind.tasks.DeleteReminderTask;

public class AlarmReceiver extends BroadcastReceiver {
    /**
     * Creates and shows a reminderNotification when a broadcast is received.
     * Runs DeleteReminderTask afterwards.
     * @param context The context to use.
     * @param intent The intent to start.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        ReminderNotification reminderNotification = new ReminderNotification(context, intent.getExtras().getString("reminderTitle"), intent.getExtras().getString("reminderText"), intent.getExtras().getInt("reminderId"));
        reminderNotification.showNotification();

        new DeleteReminderTask().execute(new DeleteReminderParams(context, reminderNotification.getmNotificationId()));
    }
}
