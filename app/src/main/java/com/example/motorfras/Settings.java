package com.example.motorfras;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Settings extends AppCompatActivity {
    private Button Bluetooth, Tid, Tillbaka;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Tid = findViewById(R.id.Tid);
        Bluetooth = findViewById(R.id.Bluetooth);
        Tillbaka = findViewById(R.id.Tillbaka);

        Bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBluetooth();
            }
        });

        Tid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTimer();
            }
        });

        Tillbaka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMainactivity();
            }
        });
    }

    public void openTimer() {
        Intent intent = new Intent(this, Timer.class);
        startActivity(intent);
    }
    public void openBluetooth() {
        Intent intent = new Intent(this, Bluetooth.class);
        startActivity(intent);
    }
    public void openMainactivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}