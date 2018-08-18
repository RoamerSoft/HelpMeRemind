package com.roamersoft.helpmeremind.tasks;

import android.content.Context;

import java.util.Date;

public class SaveReminderParams {
    public Context sContext;
    public String sReminderTitle;
    public String sReminderNote;
    public Date sReminderDateTime;

    public SaveReminderParams(Context sContext, String sReminderTitle, String sReminderNote, Date sReminderDateTime) {
        this.sContext = sContext;
        this.sReminderTitle = sReminderTitle;
        this.sReminderNote = sReminderNote;
        this.sReminderDateTime = sReminderDateTime;
    }
}
