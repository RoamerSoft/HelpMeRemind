package com.roamersoft.helpmeremind.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.roamersoft.helpmeremind.models.Alarm;

@Database(entities = {Alarm.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract AlarmDao alarmDao();
}