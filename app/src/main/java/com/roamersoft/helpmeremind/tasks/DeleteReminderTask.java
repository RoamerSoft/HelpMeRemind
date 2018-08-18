package com.roamersoft.helpmeremind.tasks;

import android.arch.persistence.room.Room;
import android.os.AsyncTask;

import com.roamersoft.helpmeremind.AppDatabase;

public class DeleteReminderTask extends AsyncTask<DeleteReminderParams, Void, Void> {
    @Override
    protected Void doInBackground(DeleteReminderParams... deleteReminderParams) {
        AppDatabase db = Room.databaseBuilder(deleteReminderParams[0].dContext,
                AppDatabase.class, "helpMeRemind").build();

        db.alarmDao().deleteById(deleteReminderParams[0].dReminderId);

        return null;
    }
}
