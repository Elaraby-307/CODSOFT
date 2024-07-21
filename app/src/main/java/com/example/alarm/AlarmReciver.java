package com.example.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.alarm.AlarmActivity;

public class AlarmReciver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            int alarmID = intent.getIntExtra("alarmID", -1);
            if (alarmID != -1) {

            }

            // Start the alarm activity
            startAlarmActivity(context, intent);
        } else {
            Log.e("AlarmReceiver", "Received null intent");
        }
    }



    private void startAlarmActivity(Context context, Intent intent) {
        String alarmTone = intent.getStringExtra("alarmTone");
        Intent alarmIntent = new Intent(context, AlarmActivity.class);
        alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        alarmIntent.putExtra("alarmTone", alarmTone);
        context.startActivity(alarmIntent);
    }
}
