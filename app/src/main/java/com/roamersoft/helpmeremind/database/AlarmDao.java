package com.roamersoft.helpmeremind.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.roamersoft.helpmeremind.models.Alarm;

import java.util.List;

@Dao
public interface AlarmDao {
    @Query("SELECT * FROM alarm")
    List<Alarm> getAll();

    @Query("SELECT * FROM alarm WHERE id = (:alarmId) LIMIT 1")
    Alarm getById(int alarmId);

    @Query("DELETE FROM alarm WHERE id = (:alarmId)")
    void deleteById(int alarmId);

    @Insert
    long insert(Alarm alarm);

    @Delete
    void delete(Alarm alarm);
}
