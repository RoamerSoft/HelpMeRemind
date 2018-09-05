package com.roamersoft.helpmeremind;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.roamersoft.helpmeremind.aSyncTasks.DeleteReminderParams;
import com.roamersoft.helpmeremind.aSyncTasks.DeleteReminderTask;


public class NotificationPressedActivity extends Activity {



    /**
     * SHow information on how to delete the notification after pressing it.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivity.showToastOnTop(this, getResources().getString(R.string.reminder_notification_touch_text));

        finish();
    }
}
