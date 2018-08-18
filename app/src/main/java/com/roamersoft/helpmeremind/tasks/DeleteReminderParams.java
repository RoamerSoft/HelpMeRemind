package com.roamersoft.helpmeremind.tasks;

import android.content.Context;

public class DeleteReminderParams {
    public Context dContext;
    public int dReminderId;

    public DeleteReminderParams(Context dContext, int dReminderId) {
        this.dContext = dContext;
        this.dReminderId = dReminderId;
    }
}
