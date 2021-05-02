package com.example.motorfras;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Objects;

public class Schedule extends AppCompatActivity {
    SwitchCompat sMonday;
    SwitchCompat sTuesday;
    SwitchCompat sWednesday;
    SwitchCompat sThursday;
    SwitchCompat sFriday;
    SwitchCompat sSaturday;
    SwitchCompat sSunday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        setSupportActionBar(findViewById(R.id.mainToolbar));
        getSupportActionBar().setTitle("Schedule");


        //----------SWITCHES------------------
        sMonday = findViewById(R.id.switchMonday);
        sTuesday = findViewById(R.id.switchTuesday);
        sWednesday = findViewById(R.id.switchWednesday);
        sThursday = findViewById(R.id.switchThursday);
        sFriday = findViewById(R.id.switchFriday);
        sSaturday = findViewById(R.id.switchSaturday);
        sSunday = findViewById(R.id.switchSunday);
        //---------------------------------------------------------------------
        //MONDAY
        SharedPreferences sharedPrefMon = getSharedPreferences( "saveMonday", MODE_PRIVATE);
        sMonday.setChecked(sharedPrefMon.getBoolean("valueMonday", false));
        sMonday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sMonday.isChecked()){
                    //When switch checked
                    SharedPreferences.Editor editor = getSharedPreferences("saveMonday"
                            ,MODE_PRIVATE).edit();
                    editor.putBoolean("valueMonday", true);
                    editor.apply();
                    sMonday.setChecked(true);
                }
                else{
                    //When switch unchecked
                    SharedPreferences.Editor editor = getSharedPreferences("saveMonday"
                            ,MODE_PRIVATE).edit();
                    editor.putBoolean("valueMonday", false);
                    editor.apply();
                    sMonday.setChecked(false);
                }
            }
            //---------------------------------------------------------------------
        });
        //TUESDAY
        SharedPreferences sharedPrefTues = getSharedPreferences( "saveTuesday", MODE_PRIVATE);
        sTuesday.setChecked(sharedPrefTues.getBoolean("valueTuesday", false));
        sTuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sTuesday.isChecked()){
                    //When switch checked
                    SharedPreferences.Editor editor = getSharedPreferences("saveTuesday"
                            ,MODE_PRIVATE).edit();
                    editor.putBoolean("valueTuesday", true);
                    editor.apply();
                    sTuesday.setChecked(true);
                }
                else{
                    //When switch unchecked
                    SharedPreferences.Editor editor = getSharedPreferences("saveTuesday"
                            ,MODE_PRIVATE).edit();
                    editor.putBoolean("valueTuesday", false);
                    editor.apply();
                    sTuesday.setChecked(false);
                }
            }
            //---------------------------------------------------------------------
        });
        //WEDNESDAY
        SharedPreferences sharedPrefWednes = getSharedPreferences( "saveWednesday", MODE_PRIVATE);
        sWednesday.setChecked(sharedPrefWednes.getBoolean("valueWednesday", false));
        sWednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sWednesday.isChecked()){
                    //When switch checked
                    SharedPreferences.Editor editor = getSharedPreferences("saveWednesday"
                            ,MODE_PRIVATE).edit();
                    editor.putBoolean("valueWednesday", true);
                    editor.apply();
                    sWednesday.setChecked(true);
                }
                else{
                    //When switch unchecked
                    SharedPreferences.Editor editor = getSharedPreferences("saveWednesday"
                            ,MODE_PRIVATE).edit();
                    editor.putBoolean("valueWednesday", false);
                    editor.apply();
                    sWednesday.setChecked(false);
                }
            }
            //---------------------------------------------------------------------
        });
        //THURSDAY
        SharedPreferences sharedPrefThurs = getSharedPreferences( "saveThursday", MODE_PRIVATE);
        sThursday.setChecked(sharedPrefThurs.getBoolean("valueThursday", false));
        sThursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sThursday.isChecked()){
                    //When switch checked
                    SharedPreferences.Editor editor = getSharedPreferences("saveThursday"
                            ,MODE_PRIVATE).edit();
                    editor.putBoolean("valueThursday", true);
                    editor.apply();
                    sThursday.setChecked(true);
                }
                else{
                    //When switch unchecked
                    SharedPreferences.Editor editor = getSharedPreferences("saveThursday"
                            ,MODE_PRIVATE).edit();
                    editor.putBoolean("valueThursday", false);
                    editor.apply();
                    sThursday.setChecked(false);
                }
            }
            //---------------------------------------------------------------------
        });
        //FRIDAY
        SharedPreferences sharedPrefFri = getSharedPreferences( "saveFriday", MODE_PRIVATE);
        sFriday.setChecked(sharedPrefFri.getBoolean("valueFriday", false));
        sFriday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sFriday.isChecked()){
                    //When switch checked
                    SharedPreferences.Editor editor = getSharedPreferences("saveFriday"
                            ,MODE_PRIVATE).edit();
                    editor.putBoolean("valueFriday", true);
                    editor.apply();
                    sFriday.setChecked(true);
                }
                else{
                    //When switch unchecked
                    SharedPreferences.Editor editor = getSharedPreferences("saveFriday"
                            ,MODE_PRIVATE).edit();
                    editor.putBoolean("valueFriday", false);
                    editor.apply();
                    sFriday.setChecked(false);
                }
            }
            //---------------------------------------------------------------------
        });
        //SATURDAY
        SharedPreferences sharedPrefSatur = getSharedPreferences( "saveSaturday", MODE_PRIVATE);
        sSaturday.setChecked(sharedPrefSatur.getBoolean("valueSaturday", false));
        sSaturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sSaturday.isChecked()){
                    //When switch checked
                    SharedPreferences.Editor editor = getSharedPreferences("saveSaturday"
                            ,MODE_PRIVATE).edit();
                    editor.putBoolean("valueSaturday", true);
                    editor.apply();
                    sSaturday.setChecked(true);
                }
                else{
                    //When switch unchecked
                    SharedPreferences.Editor editor = getSharedPreferences("saveSaturday"
                            ,MODE_PRIVATE).edit();
                    editor.putBoolean("valueSaturday", false);
                    editor.apply();
                    sSaturday.setChecked(false);
                }
            }
            //---------------------------------------------------------------------
        });
        //SUNDAY
        SharedPreferences sharedPrefSun = getSharedPreferences( "saveSunday", MODE_PRIVATE);
        sSunday.setChecked(sharedPrefSun.getBoolean("valueSunday", false));
        sSunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sSunday.isChecked()){
                    //When switch checked
                    SharedPreferences.Editor editor = getSharedPreferences("saveSunday"
                            ,MODE_PRIVATE).edit();
                    editor.putBoolean("valueSunday", true);
                    editor.apply();
                    sSunday.setChecked(true);
                }
                else{
                    //When switch unchecked
                    SharedPreferences.Editor editor = getSharedPreferences("saveSunday"
                            ,MODE_PRIVATE).edit();
                    editor.putBoolean("valueSunday", false);
                    editor.apply();
                    sSunday.setChecked(false);
                }
            }
            //---------------------------------------------------------------------
        });
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