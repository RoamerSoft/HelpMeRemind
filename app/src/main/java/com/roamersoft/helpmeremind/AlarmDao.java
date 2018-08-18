package com.roamersoft.helpmeremind;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface AlarmDao {
    @Query("SELECT * FROM alarm")
    List<Alarm> getAll();

    @Query("SELECT * FROM alarm WHERE id = (:alarmId)")
    List<Alarm> loadAllByIds(int alarmId);

    @Insert
    void insert(Alarm alarm);

    @Delete
    void delete(Alarm alarm);
}
