package com.example.alarm;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.alarm.AlarmReciver;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class Alarm implements Serializable {
    private String time;
    private int ID;
    private boolean isEnabled;
    public static int nextID = 0;

    public Alarm(String time, boolean isEnabled) {
        this.time = time;
        this.isEnabled = isEnabled;
        this.ID = nextID++;
    }

    public int getID() {
        return ID;
    }

    public String getTime() {
        return time;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alarm alarm = (Alarm) o;
        return isEnabled == alarm.isEnabled &&
                Objects.equals(time, alarm.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(time, isEnabled);
    }

    public long getTimeInMillis() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            Date alarmTime = sdf.parse(time);
            calendar.setTime(alarmTime);
            calendar.set(Calendar.SECOND, 0);

            // If the alarm time is in the past, set it for the next day
            if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
                calendar.add(Calendar.DAY_OF_YEAR, 1);
            }

            return calendar.getTimeInMillis();
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public PendingIntent getPendingIntent(Context context) {
        Intent intent = new Intent(context, AlarmReciver.class);
        intent.putExtra("alarmID", ID); // Pass the alarm ID to uniquely identify the PendingIntent
        return PendingIntent.getBroadcast(context, ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
