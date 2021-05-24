package com.example.motorfras;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import java.util.Objects;

public class Settings extends AppCompatActivity {
    private Button buttonDevice;
    SwitchCompat switchCompat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setSupportActionBar(findViewById(R.id.mainToolbar));
        getSupportActionBar().setTitle("Settings");

        //-----------SWITCH FOR FAHRENHEIT----------
        SharedPreferences dateStatePref = getSharedPreferences("tempSwitchState", MODE_PRIVATE);

        switchCompat = findViewById(R.id.switch11);
        switchCompat.setChecked(dateStatePref.getBoolean("tempSwitchState", false));
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton v, boolean isChecked) {
                SharedPreferences switchState = getSharedPreferences("tempSwitchState", MODE_PRIVATE);
                SharedPreferences.Editor editor = getSharedPreferences("tempSwitchState", MODE_PRIVATE).edit();
                if (isChecked) {
                    editor.putBoolean("tempSwitchState", true);
                    showToast("Changed Celsius to Fahrenheit");
                }
                else {

                    editor.putBoolean("tempSwitchState", false);
                }
                editor.apply();
            }
        });

        //-----------------------------------------



        buttonDevice = (Button) findViewById(R.id.buttonDevices);
        buttonDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDevices();
            }
        });
    }

    private void showToast(String msg)
    {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    public void openDevices() {
        Intent intent = new Intent(this, SelectBluetoothDevices.class);
        startActivity(intent);
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