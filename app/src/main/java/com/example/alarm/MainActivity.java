package com.example.alarm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.* ;

public class MainActivity extends AppCompatActivity {

    private    TextView currentTime ;
    private Button  setAlarmbtn ;
    private RecyclerView alarmrecyclerView;

    private List<Alarm> alarmList = new ArrayList<>() ;

    private AlarmAdapter alarmAdapter = new AlarmAdapter();




    private final Handler handler = new Handler();
    private final Runnable updateTimeRunnable = new Runnable() {
        @Override
        public void run() {
            updateCurrentTime(); // Update the displayed time
            handler.postDelayed(this, 1000); // Schedule the next update after 1 second
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentTime = findViewById(R.id.currentTime);
        setAlarmbtn = findViewById(R.id.addAlarmButton);
        alarmrecyclerView = findViewById(R.id.alarmRecyclerView);


        alarmAdapter = new AlarmAdapter(alarmList, this);
        alarmrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        alarmrecyclerView.setAdapter(alarmAdapter);



        handler.post(updateTimeRunnable);





        updateCurrentTime();




        setAlarmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult(new Intent(MainActivity.this,SetAlarmActivity.class),1);


            }
        });
    }


    public List getalarmlist(){
        return alarmList ;
    }

    private void updateCurrentTime() {
        String time = new SimpleDateFormat("hh:mm a, dd MMM yyyy", Locale.getDefault()).format(new Date());
        currentTime.setText(time);
    }



    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Alarm newAlarm =(Alarm) data.getSerializableExtra("newAlarm");
            alarmList.add(newAlarm);
            alarmAdapter.notifyItemInserted(alarmList.size()-1);
        }
    }
}