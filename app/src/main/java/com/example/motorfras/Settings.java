package com.example.motorfras;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.Objects;

public class Settings extends AppCompatActivity {
    private Button buttonDevice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setSupportActionBar(findViewById(R.id.mainToolbar));
        getSupportActionBar().setTitle("Settings");

        buttonDevice = (Button) findViewById(R.id.buttonDevices);
        buttonDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDevices();
            }
        });
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