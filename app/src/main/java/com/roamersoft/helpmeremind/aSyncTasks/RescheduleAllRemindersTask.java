package com.roamersoft.helpmeremind.aSyncTasks;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

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

        //Schedule all alarms as reminders.
        for (int i = 0; i < alarms.size(); i++){
            ReminderNotification.scheduleReminderNotification(contexts[0], alarms.get(i));
        }

        return null;
    }
}
