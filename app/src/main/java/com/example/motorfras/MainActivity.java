package com.example.motorfras;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import java.io.InputStream;
import java.io.OutputStream;
import static java.lang.Float.parseFloat;

public class MainActivity extends AppCompatActivity{
    public static TextView tempSens;
    ImageView lampIv;
    Button launchSchedule, launchSettings;
    SwitchCompat switchCompat;

    private static final String TAG = "MainActivity";
    private static boolean isFahrenheit = false;
    BluetoothDevice mBTDevice = null;

    public static BluetoothSocket mmSocket;
    public static ConnectedThread connectedThread;
    public static CreateConnectThread createConnectThread;

    private final static int CONNECTING_STATUS = 1; // used in bluetooth handler to identify message status
    private final static int MESSAGE_READ = 2; // used in bluetooth handler to identify message update
    private static SharedPreferences dateStatePref;


    //When the app is closed
    @Override
    public void onBackPressed() {
        // Terminate Bluetooth Connection and close app
        if (createConnectThread != null)
        {
            createConnectThread.cancel();
        }
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    //[11:44,11:43]

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
        tempSens            = findViewById(R.id.tempSensTv);
        dateStatePref       = getSharedPreferences("tempSwitchState", MODE_PRIVATE);

        isFahrenheit = dateStatePref.getBoolean("tempSwitchState", false);

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        SharedPreferences hardwareAddressPref = getSharedPreferences("hardwareAddress", MODE_PRIVATE);
        String hwAddress = hardwareAddressPref.getString("hardwareAddress", "");

        for(BluetoothDevice device : pairedDevices)
        {
            Log.d(TAG, device.getName());
            Log.d("--hwAddress--", hwAddress);
            if(hwAddress.equals(device.getAddress()))
            {
                mBTDevice = device;
            }
        }

        if(mBTDevice != null)
        {
            createConnectThread = new CreateConnectThread(mBluetoothAdapter, mBTDevice.getAddress());
            createConnectThread.start();
        }
        else
        {
            showToast("No Paired Devices");
        }



        launchSchedule.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // Terminate Bluetooth Connection and close app
                if (createConnectThread != null)
                {
                    createConnectThread.cancel();
                }
                launchSchedule();
            }
        });

        launchSettings.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // Terminate Bluetooth Connection and close app
                if (createConnectThread != null)
                {
                    createConnectThread.cancel();
                }
                launchSettings();
            }
        });

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){

                if(buttonView.isPressed() && mBTDevice != null)
                {
                    if(isChecked)
                    {
                        if(mmSocket.isConnected())
                        {
                            String msg = "BT_LED_ENABLE";
                            connectedThread.write(msg);

                            lampIv.setImageResource(R.drawable.ic_baseline_lightbulb_on);
                            showToast("Your engine heater is on");
                        }
                    }
                    else
                    {
                        if(mmSocket.isConnected())
                        {
                            String msg = "BT_LED_DISABLE";
                            connectedThread.write(msg);

                            lampIv.setImageResource(R.drawable.ic_baseline_lightbulb_off);
                            showToast("Your engine heater is off");
                        }
                    }
                }
                else
                {
                    buttonView.setChecked(false);
                    showToast("No Connection");
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

    //Display a toast message in the app that vanishes after a short while
    private void showToast(String msg)
    {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    /* ============================ Thread to Create Bluetooth Connection =================================== */
    public static class CreateConnectThread extends Thread {

        public CreateConnectThread(BluetoothAdapter bluetoothAdapter, String address) {
            /*
            Use a temporary object that is later assigned to mmSocket
            because mmSocket is final.
             */
            BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(address);
            BluetoothSocket tmp = null;
            UUID uuid = bluetoothDevice.getUuids()[0].getUuid();

            try {
                /*
                Get a BluetoothSocket to connect with the given BluetoothDevice.
                Due to Android device varieties,the method below may not work fo different devices.
                You should try using other methods i.e. :
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
                 */
                tmp = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(uuid);

            } catch (IOException e) {
                Log.e(TAG, "Socket's create() method failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it otherwise slows down the connection.
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            bluetoothAdapter.cancelDiscovery();
            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                mmSocket.connect();
                Log.e("Status", "Device connected");
                //handler.obtainMessage(CONNECTING_STATUS, 1, -1).sendToTarget();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and return.
                try {
                    mmSocket.close();
                    Log.e("Status", "Cannot connect to device");
                    //handler.obtainMessage(CONNECTING_STATUS, -1, -1).sendToTarget();
                } catch (IOException closeException) {
                    Log.e(TAG, "Could not close the client socket", closeException);
                }
                return;
            }

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
            connectedThread = new ConnectedThread(mmSocket);
            connectedThread.run();
        }

        // Closes the client socket and causes the thread to finish.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the client socket", e);
            }
        }
    }

    /* =============================== Thread for Data Transfer =========================================== */
    public static class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes = 0; // bytes returned from read()
            // Keep listening to the InputStream until an exception occurs
            while(true)
            {
                try
                {
                    /*
                    Read from the InputStream from Arduino until termination character is reached.
                     */
                    buffer[bytes] = (byte) mmInStream.read();
                    String readMessage;
                    if (buffer[bytes] == '\n')
                    {
                        readMessage = new String(buffer,0, bytes);

                        float n = parseFloat(readMessage);
                        if(isFahrenheit)
                        {
                            n = (n * (9/5) ) + 32;
                            tempSens.setText("" + n + "°F");
                        }
                        else
                        {
                            tempSens.setText("" + n + "°C");
                        }
                        Log.e("Arduino Message",readMessage);

                        //handler.obtainMessage(MESSAGE_READ,readMessage).sendToTarget();
                        bytes = 0;
                    }
                    else
                    {
                        bytes++;
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(String input) {
            byte[] bytes = input.getBytes(); //converts entered String into bytes
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
                Log.e("Send Error","Unable to send message",e);
            }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }
}