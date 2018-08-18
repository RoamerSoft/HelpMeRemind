package com.roamersoft.helpmeremind.tasks;

import android.arch.persistence.room.Room;
import android.os.AsyncTask;

import com.roamersoft.helpmeremind.Alarm;
import com.roamersoft.helpmeremind.AppDatabase;
import com.roamersoft.helpmeremind.ReminderNotification;


public class SaveReminderTask extends AsyncTask<SaveReminderParams, Void, SaveReminderWrapper> {
    @Override
    protected SaveReminderWrapper doInBackground(SaveReminderParams... SaveReminderParams) {
        AppDatabase db = Room.databaseBuilder(SaveReminderParams[0].sContext,
                    AppDatabase.class, "helpMeRemind").build();

            Alarm alarm = new Alarm();
            alarm.setDateTime(SaveReminderParams[0].sReminderDateTime.getTime());
            alarm.setTitle(SaveReminderParams[0].sReminderTitle);
            alarm.setNote(SaveReminderParams[0].sReminderNote);

            Long id = db.alarmDao().insert(alarm);

            return new SaveReminderWrapper(SaveReminderParams[0].sContext, db.alarmDao().getById(id.intValue()));
    }

    @Override
    protected void onPostExecute(SaveReminderWrapper SaveReminderWrapper) {
        //schedule reminder
        ReminderNotification.scheduleReminderNotification(SaveReminderWrapper.sContext, SaveReminderWrapper.sAlarm);

    }
}
