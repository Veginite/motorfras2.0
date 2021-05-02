package com.example.motorfras;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class Schedule extends AppCompatActivity {
    android.widget.TextView timer1;
    int tHour, tMinute;
    SharedPreferences sp;
    String timestr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        setSupportActionBar(findViewById(R.id.mainToolbar));
        getSupportActionBar().setTitle("Schedule");

        Set<Integer> switchID = new HashSet<Integer>();
        switchID.addAll(Arrays.asList(
                R.id.switchMonday,
                R.id.switchTuesday,
                R.id.switchWednesday,
                R.id.switchThursday,
                R.id.switchFriday,
                R.id.switchSaturday,
                R.id.switchSunday));

        Set<Integer> textViewID = new HashSet<Integer>();
        textViewID.addAll(Arrays.asList(
                R.id.textViewMonday,
                R.id.textViewTuesday,
                R.id.textViewWednesday,
                R.id.textViewTuesday,
                R.id.textViewFriday,
                R.id.textViewSaturday,
                R.id.textViewSunday));

        //---------------------------------------------------------------------

        SharedPreferences dateStatePref = getSharedPreferences( "dateStates"
                ,MODE_PRIVATE);
        SharedPreferences dateContentPref = getSharedPreferences( "dateContent"
                ,MODE_PRIVATE);

        String days[] = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};

        Iterator<Integer> itViewText = textViewID.iterator();
        Iterator<Integer> itSwitch = switchID.iterator();
        for(int i = 0; i < 7; i++)
        {
            //---------------------------------------------------------------------
            SwitchCompat sView = findViewById(itSwitch.next());
            sView.setChecked(dateStatePref.getBoolean("dateState" + days[i], false));
            int finalI = i;
            //Switch Event Listener
            sView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (sView.isChecked()){
                        //When switch checked
                        SharedPreferences.Editor editor = getSharedPreferences("dateStates"
                                ,MODE_PRIVATE).edit();
                        editor.putBoolean("dateState" + days[finalI], true);
                        editor.apply();
                        sView.setChecked(true);
                    }
                    else {
                        //When switch unchecked
                        SharedPreferences.Editor editor = getSharedPreferences("dateStates"
                                ,MODE_PRIVATE).edit();
                        editor.putBoolean("dateState" + days[finalI], false);
                        editor.apply();
                        sView.setChecked(false);
                    }
                }
            });
            //---------------------------------------------------------------------
            //TextView Event Listener
            TextView tView = findViewById(itViewText.next());
            tView.setText(dateContentPref.getString("dateContent" + days[i], ""));
            tView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                            Schedule.this,
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    //Setup hour and minute
                                    tHour = hourOfDay;
                                    tMinute = minute;
                                    timestr = tHour + ":" +tMinute;
                                    //Store the value as a string
                                    //Setup 24 hour time format
                                    SimpleDateFormat f24Hours = new SimpleDateFormat(
                                            "HH:mm"
                                    );

                                    try {
                                        Date date = f24Hours.parse(timestr);
                                        //Setup 12 hour time format
                                        SimpleDateFormat f12hours = new SimpleDateFormat(
                                                "hh:mm aa"
                                        );
                                        //User can chose time on text view
                                        tView.setText(f12hours.format(date));

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },24,0,true
                    );
                }
            });
        }


    }

    //------------- MENU OPTIONS -------------

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.basic_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.about)
        {
            //Do stuff
            return true;
        }
        else if(item.getItemId() == R.id.support)
        {
            //Do stuff
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //----------------------------------------
}