package com.example.motorfras;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;

import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static java.lang.Integer.parseInt;


public class Schedule extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{
    android.widget.TextView timer1;
    int tHour, tMinute;
    SharedPreferences sp;
    String timestr;
    int timePickerButtonID = -1;

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
                R.id.timeMonday,
                R.id.timeTuesday,
                R.id.timeWednesday,
                R.id.timeThursday,
                R.id.timeFriday,
                R.id.timeSaturday,
                R.id.timeSunday));

        //---------------------------------------------------------------------

        SharedPreferences dateStatePref = getSharedPreferences( "dateStates"
                ,MODE_PRIVATE);
        SharedPreferences timeContentPref = getSharedPreferences( "dateContent"
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
            sView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton v, boolean isChecked) {
                    SharedPreferences.Editor editor = getSharedPreferences("dateStates", MODE_PRIVATE).edit();
                    if (isChecked){
                        //When switch checked
                        editor.putBoolean("dateState" + days[finalI], true);
                    }
                    else {
                        //When switch unchecked
                        editor.putBoolean("dateState" + days[finalI], false);
                    }
                    editor.apply();
                }
            });
            //---------------------------------------------------------------------
            //TextView Event Listener
            TextView tView = findViewById(itViewText.next());
            tView.setText(timeContentPref.getString("timeContent" + days[i], "--:--"));
            tView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Save the view's ID so we know what was pressed. Used in onTimeSet() so we know where to place the text
                    timePickerButtonID = v.getId();
                    DialogFragment timePicker = new TimePickerFragment();
                    timePicker.show(getSupportFragmentManager(), "Time Picker");
                }
            });
        }


    }

    //------------- MENU OPTIONS -------------


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView textView = findViewById(timePickerButtonID);
        String timeString = String.format(hourOfDay + ":" + minute);
        textView.setText(timeString);

        SharedPreferences.Editor editor = getSharedPreferences("timeContent", MODE_PRIVATE).edit();
        editor.putString("timeContent", timeString);
        editor.apply();

        //Reset the pressed button
        timePickerButtonID = -1;
    }

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