package com.example.motorfras;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Objects;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    android.widget.Button switchToSettings;
    SwitchCompat switchCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar(findViewById(R.id.mainToolbar));
        Objects.requireNonNull(getSupportActionBar()).setTitle("Welcome!");

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

        //Assign Variable
        switchCompat = findViewById(R.id.switchOnOff);

        //save switch state in shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences( "save", MODE_PRIVATE);
        switchCompat.setChecked(sharedPreferences.getBoolean("value", true));
        switchCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchCompat.isChecked()){
                    //When switch checked
                    SharedPreferences.Editor editor = getSharedPreferences("save", MODE_PRIVATE).edit();
                    editor.putBoolean("value", true);
                    editor.apply();
                    switchCompat.setChecked(true);
                }
                else{
                    //When switch unchecked
                    SharedPreferences.Editor editor = getSharedPreferences("save", MODE_PRIVATE).edit();
                    editor.putBoolean("value", false);
                    editor.apply();
                    switchCompat.setChecked(false);
                }
            }
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