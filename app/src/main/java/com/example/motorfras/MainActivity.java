package com.example.motorfras;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.app.TimePickerDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.tv.TvContract;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TimePicker;

import java.nio.charset.Charset;
import java.util.Objects;
import java.util.UUID;

public class MainActivity extends AppCompatActivity{
    ImageView lampIv;
    Button launchSchedule, launchSettings;
    SwitchCompat switchCompat;

    private static final UUID INSECURE_UUID = UUID.fromString("041de592-ab3c-11eb-bcbc-0242ac130002");
    BluetoothAdapter mBluetoothAdapter;
    BluetoothConnectionService mBluetoothConnection;
    BluetoothDevice mBTDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar(findViewById(R.id.mainToolbar));
        getSupportActionBar().setTitle("Welcome!");

        lampIv              = findViewById(R.id.lampIv);
        launchSchedule      = findViewById(R.id.schedule);
        launchSettings      = findViewById(R.id.Settings);
        switchCompat        = findViewById(R.id.switchOnOff);


        launchSchedule.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                launchSchedule();
            }
        });

        launchSettings.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                launchSettings();
            }
        });

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked)
                {
                    /*startBTConnection(mBTDevice, INSECURE_UUID);

                    String msg = "1";
                    mBluetoothConnection.write(msg.getBytes(Charset.defaultCharset()));*/

                    lampIv.setImageResource(R.drawable.ic_baseline_lightbulb_on);
                }
                else
                {
                   /* String msg = "1";
                    mBluetoothConnection.write(msg.getBytes(Charset.defaultCharset()));*/

                    lampIv.setImageResource(R.drawable.ic_baseline_lightbulb_off);
                }
            }
        });

        //---------------------------------------------------------------------
        //save switch state in shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences( "save", MODE_PRIVATE);
        switchCompat.setChecked(sharedPreferences.getBoolean("value", false));
        switchCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchCompat.isChecked()){
                    //When switch checked
                    SharedPreferences.Editor editor = getSharedPreferences("save"
                            ,MODE_PRIVATE).edit();
                    editor.putBoolean("value", true);
                    editor.apply();
                    switchCompat.setChecked(true);
                }
                else{
                    //When switch unchecked
                    SharedPreferences.Editor editor = getSharedPreferences("save"
                            ,MODE_PRIVATE).edit();
                    editor.putBoolean("value", false);
                    editor.apply();
                    switchCompat.setChecked(false);
                }
            }
            //---------------------------------------------------------------------

        });
    }

    public void launchSchedule(){
        android.content.Intent intent= new Intent(this, Schedule.class);
        startActivity(intent);

    }

    public void launchSettings() {
        android.content.Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    /*public void startBTConnection(BluetoothDevice device, UUID uuid){
        mBluetoothConnection.startClient(device, uuid);
    }*/

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