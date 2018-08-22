package com.roamersoft.helpmeremind.aSyncTasks;

import android.arch.persistence.room.Room;
import android.os.AsyncTask;

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

//            TODO: Database has to be closed.

            return new SaveReminderWrapper(SaveReminderParams[0].sContext, db.alarmDao().getById(id.intValue()));
    }

    @Override
    protected void onPostExecute(SaveReminderWrapper SaveReminderWrapper) {
        //schedule reminder
        ReminderNotification.scheduleReminderNotification(SaveReminderWrapper.sContext, SaveReminderWrapper.sAlarm);

    }
}
