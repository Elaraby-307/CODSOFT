package com.example.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alarm.R;

import java.util.List;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder> {

    private List<Alarm> alarmList;
    private Context context;

    public AlarmAdapter (){}
    public AlarmAdapter(List<Alarm> alarmList, Context context) {
        this.alarmList = alarmList;
        this.context = context;
    }



    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarm, parent, false);
        return new AlarmViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {
        Alarm alarm = alarmList.get(position);
        holder.timeTextView.setText(alarm.getTime());
        holder.alarmSwitch.setChecked(alarm.isEnabled()); // Set switch state based on isEnabled

        holder.alarmSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            alarm.setEnabled(isChecked); // Update the isEnabled state of Alarm
            if (isChecked) {
                // Enable alarm
                scheduleAlarm(alarm, context);
            } else {
                // Disable alarm
                cancelAlarm(alarm, context);
            }
        });
    }


    @Override
    public int getItemCount() {
        return alarmList.size();
    }

    public void scheduleAlarm(Alarm alarm, Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            PendingIntent pendingIntent = alarm.getPendingIntent(context);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarm.getTimeInMillis(), pendingIntent);

            Log.d("alarm","scheduleAlarm  Called");
        }
    }

    private void cancelAlarm(Alarm alarm, Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            PendingIntent pendingIntent = alarm.getPendingIntent(context);
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }

        Log.d("alarm","canCleAlarm  Called");
    }


    public static class AlarmViewHolder extends RecyclerView.ViewHolder {
        TextView timeTextView;
        Switch alarmSwitch;

        public AlarmViewHolder(@NonNull View itemView) {
            super(itemView);
            timeTextView = itemView.findViewById(R.id.text_view_alarm_time);
            alarmSwitch = itemView.findViewById(R.id.switch_alarm);
        }
    }
}
