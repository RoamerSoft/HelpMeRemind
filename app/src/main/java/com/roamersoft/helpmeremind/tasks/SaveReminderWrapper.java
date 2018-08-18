package com.roamersoft.helpmeremind.tasks;

import android.content.Context;

import com.roamersoft.helpmeremind.Alarm;

public class SaveReminderWrapper {
    public Context sContext;
    public Alarm sAlarm;

    public SaveReminderWrapper(Context sContext, Alarm sAlarm) {
        this.sContext = sContext;
        this.sAlarm = sAlarm;
    }
}
