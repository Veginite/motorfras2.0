package com.example.motorfras;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Timer extends AppCompatActivity {
    android.widget.Button backToSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        backToSettings=findViewById(R.id.Settings);
        backToSettings.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                openSettings();
            }
        });
    }
    public void openSettings(){
        android.content.Intent intent= new Intent(this, Settings.class);
        startActivity(intent);
    }
}