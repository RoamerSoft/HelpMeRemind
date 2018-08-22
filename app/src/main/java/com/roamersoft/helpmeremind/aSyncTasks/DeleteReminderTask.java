package com.roamersoft.helpmeremind.aSyncTasks;

import android.arch.persistence.room.Room;
import android.os.AsyncTask;

import com.roamersoft.helpmeremind.database.AppDatabase;

public class DeleteReminderTask extends AsyncTask<DeleteReminderParams, Void, Void> {

    /**
     * Deletes the alarm by the given alarmId.
     * @param deleteReminderParams
     * @return
     */
    @Override
    protected Void doInBackground(DeleteReminderParams... deleteReminderParams) {
        AppDatabase db = Room.databaseBuilder(deleteReminderParams[0].dContext,
                AppDatabase.class, "helpMeRemind").build();

        db.alarmDao().deleteById(deleteReminderParams[0].dReminderId);

        db.close();

        return null;
    }
}
