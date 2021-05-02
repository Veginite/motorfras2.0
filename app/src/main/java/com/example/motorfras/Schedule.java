package com.example.motorfras;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

public class Schedule extends AppCompatActivity {

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

        SharedPreferences dateStatePref = getSharedPreferences( "dateStates", MODE_PRIVATE);
        SharedPreferences dateContentPref = getSharedPreferences( "dateContent", MODE_PRIVATE);

        String days[] = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

        Iterator<Integer> itViewText = textViewID.iterator();
        Iterator<Integer> itSwitch = switchID.iterator();
        for(int i = 0; i < 7; i++)
        {
            //---------------------------------------------------------------------
            SwitchCompat sView = findViewById(itSwitch.next());
            TextView tView = findViewById(itViewText.next());

            sView.setChecked(dateStatePref.getBoolean("dateState" + days[i], false));
            tView.setText(dateContentPref.getString("dateContent" + days[i], ""));

            int finalI = i;
            //Switch Event Listener
            sView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (sView.isChecked()){
                        //When switch checked
                        SharedPreferences.Editor editor = getSharedPreferences("dateStates", MODE_PRIVATE).edit();
                        editor.putBoolean("dateState" + days[finalI], true);
                        editor.apply();
                        sView.setChecked(true);
                    }
                    else {
                        //When switch unchecked
                        SharedPreferences.Editor editor = getSharedPreferences("dateStates", MODE_PRIVATE).edit();
                        editor.putBoolean("dateState" + days[finalI], false);
                        editor.apply();
                        sView.setChecked(false);
                    }
                }
            });
            //---------------------------------------------------------------------
            //TextView Event Listener
            tView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (sView.isChecked()){
                        //When switch checked
                        SharedPreferences.Editor editor = getSharedPreferences("dateStates", MODE_PRIVATE).edit();
                        editor.putBoolean("dateState" + days[finalI], true);
                        editor.apply();
                        sView.setChecked(true);
                    }
                    else {
                        //When switch unchecked
                        SharedPreferences.Editor editor = getSharedPreferences("dateStates", MODE_PRIVATE).edit();
                        editor.putBoolean("dateState" + days[finalI], false);
                        editor.apply();
                        sView.setChecked(false);
                    }
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