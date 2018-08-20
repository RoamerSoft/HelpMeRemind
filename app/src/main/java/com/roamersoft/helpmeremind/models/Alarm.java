package com.roamersoft.helpmeremind.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Alarm {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "date_time")
    private Long dateTime;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "note")
    private String note;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long getDateTime() {
        return dateTime;
    }

    public void setDateTime(Long dateTime) {
        this.dateTime = dateTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "Alarm: " + this.id + " " + this.title + " " + this.note + " " + this.dateTime;
    }
}
