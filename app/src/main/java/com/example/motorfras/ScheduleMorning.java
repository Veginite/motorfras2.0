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


public class ScheduleMorning extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{
    int timePickerButtonID = -1;

    Button resetScheduleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_morning);

        resetScheduleButton = findViewById(R.id.buttonReset);

        setSupportActionBar(findViewById(R.id.mainToolbar));
        getSupportActionBar().setTitle("Morning schedule");

        Set<Integer> switchID = new HashSet<Integer>();
        switchID.addAll(Arrays.asList(
                R.id.switch0,
                R.id.switch1,
                R.id.switch2,
                R.id.switch3,
                R.id.switch4,
                R.id.switch5,
                R.id.switch6));

        Set<Integer> textViewID = new HashSet<Integer>();
        textViewID.addAll(Arrays.asList(
                R.id.time0,
                R.id.time1,
                R.id.time2,
                R.id.time3,
                R.id.time4,
                R.id.time5,
                R.id.time6));

        //---------------------------------------------------------------------

        SharedPreferences dateStatePref = getSharedPreferences( "dateStates"
                ,MODE_PRIVATE);
        SharedPreferences timeContentPref = getSharedPreferences( "timeContent"
                ,MODE_PRIVATE);

        Iterator<Integer> itViewText = textViewID.iterator();
        Iterator<Integer> itSwitch = switchID.iterator();
        for(int i = 0; i < 7; i++)
        {
            //---------------------------------------------------------------------
            SwitchCompat sView = findViewById(itSwitch.next());
            sView.setChecked(dateStatePref.getBoolean("dateState" + i, false));
            int finalI = i;
            //Switch Event Listener
            sView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton v, boolean isChecked) {
                    SharedPreferences.Editor editor = getSharedPreferences("dateStates", MODE_PRIVATE).edit();
                    if (isChecked){
                        //When switch checked
                        editor.putBoolean("dateState" + finalI, true);
                    }
                    else {
                        //When switch unchecked
                        editor.putBoolean("dateState" + finalI, false);
                    }
                    editor.apply();
                }
            });
            //---------------------------------------------------------------------
            //TextView Event Listener
            TextView tView = findViewById(itViewText.next());
            tView.setText(timeContentPref.getString("timeContent" + i, "--:--"));
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
        SharedPreferences.Editor prefEditor = getSharedPreferences( "dateStates", MODE_PRIVATE).edit();
        prefEditor.clear();
        prefEditor.apply();
        prefEditor = getSharedPreferences( "timeContent", MODE_PRIVATE).edit();
        prefEditor.clear();
        prefEditor.apply();

        CompoundButton bView = findViewById(R.id.switch0);
        bView.setChecked(false);
        bView = findViewById(R.id.switch1);
        bView.setChecked(false);
        bView = findViewById(R.id.switch2);
        bView.setChecked(false);
        bView = findViewById(R.id.switch3);
        bView.setChecked(false);
        bView = findViewById(R.id.switch4);
        bView.setChecked(false);
        bView = findViewById(R.id.switch5);
        bView.setChecked(false);
        bView = findViewById(R.id.switch6);
        bView.setChecked(false);

        TextView tView = findViewById(R.id.time0);
        tView.setText(R.string.defaultTime);
        tView = findViewById(R.id.time1);
        tView.setText(R.string.defaultTime);
        tView = findViewById(R.id.time2);
        tView.setText(R.string.defaultTime);
        tView = findViewById(R.id.time3);
        tView.setText(R.string.defaultTime);
        tView = findViewById(R.id.time4);
        tView.setText(R.string.defaultTime);
        tView = findViewById(R.id.time5);
        tView.setText(R.string.defaultTime);
        tView = findViewById(R.id.time6);
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

        if(android.text.format.DateFormat.is24HourFormat(ScheduleMorning.this))
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

        SharedPreferences.Editor editor = getSharedPreferences("timeContent", MODE_PRIVATE).edit();
        editor.putString("timeContent" + day, timeString);
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