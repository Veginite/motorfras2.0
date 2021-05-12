package com.example.motorfras;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;



public class Schedule extends AppCompatActivity {
    private Button scheduleMorning;
    private Button scheduleAfterNoon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        scheduleMorning = (Button) findViewById(R.id.scheduleMorning);
        scheduleMorning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openScheduleMorning();
            }
        });
        scheduleAfterNoon = (Button) findViewById(R.id.scheduleAfterNoon);
        scheduleAfterNoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openScheduleAfterNoon();
            }
        });
    }
    public void openScheduleMorning() {
        Intent intent = new Intent(this, ScheduleMorning.class);
        startActivity(intent);
    }

    public void openScheduleAfterNoon() {
        Intent intent1 = new Intent(this, ScheduleAfterNoon.class);
        startActivity(intent1);
    }
}
