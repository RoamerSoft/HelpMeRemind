package com.roamersoft.helpmeremind;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.roamersoft.helpmeremind.aSyncTasks.DeleteReminderParams;
import com.roamersoft.helpmeremind.aSyncTasks.DeleteReminderTask;


public class NotificationDeletedActivity extends Activity {

    /**
     * Deletes reminder when notification is swiped and shows toast to inform.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        new DeleteReminderTask().execute(new DeleteReminderParams(this, intent.getExtras().getInt("mNotificationId")));

        MainActivity.showToastOnTop(this, getResources().getString(R.string.reminder_notification_deleted_text));

        finish();
    }
}
