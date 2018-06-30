package com.roamersoft.helpmeremind;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

    private Notification mNotification;

    /**
     * Runs when the broadcast is received,
     * @param context The context to use.
     * @param intent The intent to start.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        this.mNotification = new Notification(context, intent.getExtras().getString("mReminderTitle"), intent.getExtras().getString("mReminderText"));
        this.mNotification.showNotification();
    }
}
