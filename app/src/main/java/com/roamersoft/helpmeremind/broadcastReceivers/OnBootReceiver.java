package com.roamersoft.helpmeremind.broadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.roamersoft.helpmeremind.aSyncTasks.RescheduleAllRemindersTask;

public class OnBootReceiver extends BroadcastReceiver {
    /**
     * Reschedules all alarms when system has rebooted.
     * @param context The context to use.
     * @param intent The intent to start.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
//        TODO: check if dates are in future or past, when in past show notification directly and when in future use scheduleAllReminderTask.
        new RescheduleAllRemindersTask().execute(context);
    }
}
