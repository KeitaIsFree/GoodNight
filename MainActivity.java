package com.example.keita.goodnight;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity  {

    AlarmManager aM;
    EditText hour,minute;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_1);

        aM = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        AudioManager am = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        am.requestAudioFocus(null,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN);

    }

    /*
    @Override
    public void onStart(){
        super.onStart();

        //inputs editviews + ":"
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        hour = new EditText(this,null,R.style.inputs);
        LinearLayout.LayoutParams lP1 = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT);
        lP1.weight=1;
        lP1.topMargin= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,64, metrics);
        lP1.leftMargin= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,16, metrics);
        hour.setLayoutParams(lP1);
        hour.setHint("hr");
        minute = new EditText(this,null,R.style.inputs);
        LinearLayout.LayoutParams lP2 = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT);
        lP2.weight=1;
        lP2.topMargin= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,64, metrics);
        lP2.rightMargin= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,16, metrics);
        minute.setLayoutParams(lP2);
        minute.setHint("mi");

        View colon = findViewById(R.id.colon);
        LinearLayout layout = (LinearLayout) findViewById(R.id.inputLayout);
        layout.removeAllViews();
        layout.addView(hour);
        layout.addView(colon);
        layout.addView(minute);
    }
    */

    public void setTimer (View view) {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);

        Calendar now = Calendar.getInstance();
        TimePicker picker = (TimePicker) findViewById(R.id.timePicker);
        int currentHour = now.get(Calendar.HOUR_OF_DAY); //23
        int currentMinute = now.get(Calendar.MINUTE);  //15
        int hr = picker.getCurrentHour();
        int mi = picker.getCurrentMinute();
        if (hr > currentHour || (hr == currentHour && mi > currentMinute)) {
            now.set(Calendar.HOUR_OF_DAY,hr);
            now.set(Calendar.MINUTE,mi);
        } else {
            if (now.MONTH == 12 && now.DATE == 31) {
                now.add(Calendar.YEAR,1);
                now.set(Calendar.DAY_OF_YEAR,1);
            } else {now.add(Calendar.DAY_OF_YEAR,1);}
            now.set(Calendar.HOUR_OF_DAY,hr);
            now.set(Calendar.MINUTE,mi);
            now.set(Calendar.SECOND,0);
        }
        PendingIntent pD = PendingIntent.getActivity(getApplicationContext(),0,new Intent(getApplicationContext(), WakeUp.class),PendingIntent.FLAG_ONE_SHOT);
        aM.set(AlarmManager.RTC_WAKEUP, now.getTimeInMillis(), pD);


        Toast.makeText(this,"Set Alarm at"+((hr<10)?"0":"")+hr+":"+((mi<10)?"0":"")+mi,Toast.LENGTH_LONG).show();

        move(view);

    }

    public void move(View view) {
        Intent intent = new Intent(this, SleepIn.class);
        startActivity(intent);
    }

}