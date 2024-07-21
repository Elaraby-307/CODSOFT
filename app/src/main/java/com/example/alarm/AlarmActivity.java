package com.example.alarm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Date;

public class AlarmActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private Button snoozeBTN;
    private Button dismissBTN;

    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        snoozeBTN = findViewById(R.id.snoozeButton);

        dismissBTN = findViewById(R.id.dismissButton);



    Intent intent = getIntent();
    String AlarmTone = intent.getStringExtra("alarmTone");
    Uri alarmToneUri =
        AlarmTone !=  null  ?
        Uri.parse(AlarmTone):
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        mediaPlayer = MediaPlayer.create(this,alarmToneUri);
        mediaPlayer.start();




        snoozeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();

                finish();
            }
        });

        dismissBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                finish();
            }
        });
    }
}
