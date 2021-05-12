package com.example.motorfras;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static java.lang.Integer.parseInt;


public class ScheduleAfterNoon extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{
    int timePickerButtonID = -1;

    Button resetScheduleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_after_noon);

        resetScheduleButton = findViewById(R.id.buttonReset);

        setSupportActionBar(findViewById(R.id.mainToolbar));
        getSupportActionBar().setTitle("Afternoon schedule");

        Set<Integer> switchID = new HashSet<Integer>();
        switchID.addAll(Arrays.asList(
                R.id.switch01,
                R.id.switch11,
                R.id.switch21,
                R.id.switch31,
                R.id.switch41,
                R.id.switch51,
                R.id.switch61));

        Set<Integer> textViewID = new HashSet<Integer>();
        textViewID.addAll(Arrays.asList(
                R.id.time01,
                R.id.time11,
                R.id.time21,
                R.id.time31,
                R.id.time41,
                R.id.time51,
                R.id.time61));

        //---------------------------------------------------------------------

        SharedPreferences dateStatePref = getSharedPreferences( "dateStates1"
                ,MODE_PRIVATE);
        SharedPreferences timeContentPref = getSharedPreferences( "timeContent1"
                ,MODE_PRIVATE);

        Iterator<Integer> itViewText = textViewID.iterator();
        Iterator<Integer> itSwitch = switchID.iterator();
        for(int i = 0; i < 7; i++)
        {
            //---------------------------------------------------------------------
            SwitchCompat sView = findViewById(itSwitch.next());
            sView.setChecked(dateStatePref.getBoolean("dateState1" + i, false));
            int finalI = i;
            //Switch Event Listener
            sView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton v, boolean isChecked) {
                    SharedPreferences.Editor editor = getSharedPreferences("dateStates1", MODE_PRIVATE).edit();
                    if (isChecked){
                        //When switch checked
                        editor.putBoolean("dateState1" + finalI, true);
                    }
                    else {
                        //When switch unchecked
                        editor.putBoolean("dateState1" + finalI, false);
                    }
                    editor.apply();
                }
            });
            //---------------------------------------------------------------------
            //TextView Event Listener
            TextView tView = findViewById(itViewText.next());
            tView.setText(timeContentPref.getString("timeContent1" + i, "--:--"));
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

        resetScheduleButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Are you sure you wish to clear the schedule?").setPositiveButton("Yes",
                        dialogClickListener).setNegativeButton("No", dialogClickListener).show();
            }
        });

    }

    //------------- MENU OPTIONS -------------


    private void resetSchedule(){
        SharedPreferences.Editor prefEditor = getSharedPreferences( "dateStates1", MODE_PRIVATE).edit();
        prefEditor.clear();
        prefEditor.apply();
        prefEditor = getSharedPreferences( "timeContent1", MODE_PRIVATE).edit();
        prefEditor.clear();
        prefEditor.apply();

        CompoundButton bView = findViewById(R.id.switch01);
        bView.setChecked(false);
        bView = findViewById(R.id.switch11);
        bView.setChecked(false);
        bView = findViewById(R.id.switch21);
        bView.setChecked(false);
        bView = findViewById(R.id.switch31);
        bView.setChecked(false);
        bView = findViewById(R.id.switch41);
        bView.setChecked(false);
        bView = findViewById(R.id.switch51);
        bView.setChecked(false);
        bView = findViewById(R.id.switch61);
        bView.setChecked(false);

        TextView tView = findViewById(R.id.time01);
        tView.setText(R.string.defaultTime);
        tView = findViewById(R.id.time11);
        tView.setText(R.string.defaultTime);
        tView = findViewById(R.id.time21);
        tView.setText(R.string.defaultTime);
        tView = findViewById(R.id.time31);
        tView.setText(R.string.defaultTime);
        tView = findViewById(R.id.time41);
        tView.setText(R.string.defaultTime);
        tView = findViewById(R.id.time51);
        tView.setText(R.string.defaultTime);
        tView = findViewById(R.id.time61);
        tView.setText(R.string.defaultTime);
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch(which)
            {
                case DialogInterface.BUTTON_POSITIVE:
                {
                    resetSchedule();
                    break;
                }
                case DialogInterface.BUTTON_NEGATIVE:
                {
                    //No clicked
                    break;
                }
            }
        }
    };

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView textView = findViewById(timePickerButtonID);

        String timeString = String.format(hourOfDay + ":" + minute);

        if(android.text.format.DateFormat.is24HourFormat(ScheduleAfterNoon.this))
        {
            timeString = "";
            if(hourOfDay < 10)
            {
                timeString += "0";
            }
            timeString += String.format(hourOfDay + ":");
            if(minute < 10)
            {
                timeString += "0";
            }
            timeString += String.format("" + minute);
        }
        textView.setText(timeString);

        //The ID of the view as it appears in a layout XML file.
        //The final byte represents the day in question (Mon-Sun --- 0-6)
        String idAsResourceString = view.getResources().getResourceName(timePickerButtonID);
        int day = parseInt(String.format("" + idAsResourceString.charAt(idAsResourceString.length()-1)));

        SharedPreferences.Editor editor = getSharedPreferences("timeContent1", MODE_PRIVATE).edit();
        editor.putString("timeContent1" + day, timeString);
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