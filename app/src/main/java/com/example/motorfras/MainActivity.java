package com.example.motorfras;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.Set;

public class MainActivity extends AppCompatActivity {
    android.widget.Button switchToSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        switchToSettings=findViewById(R.id.Settings);
        switchToSettings.setOnClickListener(new View.OnClickListener(){
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