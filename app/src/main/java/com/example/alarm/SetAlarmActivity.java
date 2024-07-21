package com.example.alarm;

import android.*;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar ;
import java.util.List;

public class SetAlarmActivity extends AppCompatActivity {

   MainActivity m = new MainActivity();
    private TimePicker timePicker;

    private List<Alarm> alarmlist = new ArrayList<>() ;
    private AlarmAdapter alarmAdapter = new AlarmAdapter();
    private Button setAlarmToneBTN;
    private Button saveAlarmBTN;
    private Uri AlarmTone;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);

        timePicker = findViewById(R.id.timePicker);
        setAlarmToneBTN = findViewById(R.id.setAlarmToneBTN);
        saveAlarmBTN = findViewById(R.id.saveAlarmBTN);


        alarmAdapter = new AlarmAdapter(alarmlist, this);

        setAlarmToneBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);

                i.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
                i.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Alarm tone");
                i.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, AlarmTone);
                startActivityForResult(i, 1);

                PendingIntent pendingIntent = PendingIntent.getBroadcast
                        (SetAlarmActivity.this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);



            }
        });

        saveAlarmBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();
                saveAlarm(hour, minute, AlarmTone);
                String alarmtime = String.format("%02d:%02d",hour,minute);






            }
        });
    }






    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            AlarmTone = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
        }
    }

    private void saveAlarm(int hour, int minute, Uri alarmTone) {

        String alarmTime = String.format("%02d:%02d", hour, minute);
        Alarm alarm = new Alarm(alarmTime, true);

        if (AlarmTone == null) {

            Toast.makeText(getApplicationContext(), "Please select an alarm tone first", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save or update alarm list
        m.getalarmlist().add(alarm);
        alarmAdapter.notifyItemInserted(m.getalarmlist().size() - 1); // Notify adapter of change
        alarmAdapter.notifyDataSetChanged(); // Optionally notify data set changed

        // Schedule the alarm using AlarmManager
        alarmAdapter.scheduleAlarm(alarm, this);





        Log.d("alarm","alarm id is :"+alarm.getID());

        Intent resultIntent = new Intent();
        resultIntent.putExtra("newAlarm", alarm);
        setResult(RESULT_OK, resultIntent);
        finish();





        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReciver.class);
        intent.putExtra("alarmTone", alarmTone.toString());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, alarm.getID(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }

    }





    }

