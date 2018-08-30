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

import java.util.List;

public class RescheduleAllRemindersTask extends AsyncTask<Context, Void, Void> {

    /**
     * Reschedules all alarms from the database.
     * @param contexts The context to use.
     * @return null
     */
    @Override
    protected Void doInBackground(Context... contexts) {
        AppDatabase db = Room.databaseBuilder(contexts[0],
                AppDatabase.class, "helpMeRemind").build();

        //Get all alarms from db.
        List<Alarm> alarms = db.alarmDao().getAll();

        if (alarms.size() == 0){
            //Disable broadcast receiver when there are no alarms to schedule.
            this.disableBroadcastReceiver(contexts[0], OnBootReceiver.class);
        } else {
            //Schedule all alarms as reminders.
            for (int i = 0; i < alarms.size(); i++){
                ReminderNotification.scheduleReminderNotification(contexts[0], alarms.get(i));
            }
        }

        db.close();

        return null;
    }

    /**
     * Disables the given broadcast receiver
     * @param context The context to use.
     * @param cls The class used by the broadcast receiver.
     */
    private void disableBroadcastReceiver(Context context, Class<?> cls) {
        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
}
