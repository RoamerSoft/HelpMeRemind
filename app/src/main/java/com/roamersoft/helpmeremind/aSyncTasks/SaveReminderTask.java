package com.roamersoft.helpmeremind.aSyncTasks;

import android.arch.persistence.room.Room;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;

import com.roamersoft.helpmeremind.broadcastReceivers.OnBootReceiver;
import com.roamersoft.helpmeremind.models.Alarm;
import com.roamersoft.helpmeremind.database.AppDatabase;
import com.roamersoft.helpmeremind.ReminderNotification;


public class SaveReminderTask extends AsyncTask<SaveReminderParams, Void, SaveReminderWrapper> {

    /**
     * Saves the given data to the database and schedules the reminder afterwards.
     * @param SaveReminderParams The given data.
     * @return
     */
    @Override
    protected SaveReminderWrapper doInBackground(SaveReminderParams... SaveReminderParams) {
        AppDatabase db = Room.databaseBuilder(SaveReminderParams[0].sContext,
                    AppDatabase.class, "helpMeRemind").build();

            Alarm alarm = new Alarm();
            alarm.setDateTime(SaveReminderParams[0].sReminderDateTime.getTime());
            alarm.setTitle(SaveReminderParams[0].sReminderTitle);
            alarm.setNote(SaveReminderParams[0].sReminderNote);

            Long id = db.alarmDao().insert(alarm);

            SaveReminderWrapper saveReminderWrapper = new SaveReminderWrapper(SaveReminderParams[0].sContext, db.alarmDao().getById(id.intValue()));

            db.close();

            this.enableBroadcastReceiver(SaveReminderParams[0].sContext, OnBootReceiver.class);

            return saveReminderWrapper;
    }

    @Override
    protected void onPostExecute(SaveReminderWrapper SaveReminderWrapper) {
        //schedule reminder
        ReminderNotification.scheduleReminderNotification(SaveReminderWrapper.sContext, SaveReminderWrapper.sAlarm);

    }

    /**
     * Enables the given broadcast receiver
     * @param context The context to use.
     * @param cls The class used by the broadcast receiver.
     */
    private void enableBroadcastReceiver(Context context, Class<?> cls) {
        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }
}
