package com.example.motorfras;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import static java.lang.Integer.parseInt;

public class ScheduleAfterNoon extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    int timePickerButtonID = -1;

    private static final String TAG = "ScheduleAfterNoon";
    BluetoothDevice mBTDevice;

    public static BluetoothSocket mmSocket;
    public static ConnectedThread connectedThread;
    public static CreateConnectThread createConnectThread;

    private final static int CONNECTING_STATUS = 1; // used in bluetooth handler to identify message status
    private final static int MESSAGE_READ = 2; // used in bluetooth handler to identify message update

    Button resetScheduleButton;
    Button uploadScheduleButton;
    Button Back;

    //When the app is closed
    @Override
    public void onBackPressed() {
        // Terminate Bluetooth Connection and close app
        if (createConnectThread != null) {
            createConnectThread.cancel();
        }
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_after_noon);

        resetScheduleButton = findViewById(R.id.buttonReset);
        uploadScheduleButton = findViewById(R.id.buttonUpload);

        setSupportActionBar(findViewById(R.id.mainToolbar));
        getSupportActionBar().setTitle("Afternoon schedule");

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        //FUTURE WORK: CACHE THE HARDWARE ADDRESS WHICH IS ACQUIRED
        //FROM A LIST THAT THE USER CAN SELECT WHAT DEVICE TO USE
        for (BluetoothDevice device : pairedDevices) {
            Log.d(TAG, device.getName());
            mBTDevice = device;
        }

        if (pairedDevices.size() > 0) {
            createConnectThread = new CreateConnectThread(mBluetoothAdapter, mBTDevice.getAddress());
            createConnectThread.start();
        } else {
            showToast("No Paired Devices");
            mBTDevice = null;
        }

        Set<Integer> switchID = new HashSet<Integer>();
        switchID.addAll(Arrays.asList(
                R.id.switch0,
                R.id.switch1,
                R.id.switch2,
                R.id.switch3,
                R.id.switch4,
                R.id.switch5,
                R.id.switch6));

        Set<Integer> textViewID = new HashSet<Integer>();
        textViewID.addAll(Arrays.asList(
                R.id.time0,
                R.id.time1,
                R.id.time2,
                R.id.time3,
                R.id.time4,
                R.id.time5,
                R.id.time6));

        //---------------------------------------------------------------------

        SharedPreferences dateStatePref = getSharedPreferences("dateStates1"
                , MODE_PRIVATE);
        SharedPreferences timeContentPref = getSharedPreferences("timeContent1"
                , MODE_PRIVATE);

        Iterator<Integer> itViewText = textViewID.iterator();
        Iterator<Integer> itSwitch = switchID.iterator();
        for (int i = 0; i < 7; i++) {
            //---------------------------------------------------------------------
            SwitchCompat sView = findViewById(itSwitch.next());
            sView.setChecked(dateStatePref.getBoolean("dateState1" + i, false));
            int finalI = i;
            //Switch Event Listener
            sView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton v, boolean isChecked) {
                    SharedPreferences.Editor editor = getSharedPreferences("dateStates1", MODE_PRIVATE).edit();
                    if (isChecked) {
                        //When switch checked
                        editor.putBoolean("dateState1" + finalI, true);
                    } else {
                        //When switch unchecked
                        editor.putBoolean("dateState1" + finalI, false);
                    }
                    editor.apply();
                }
            });
            //---------------------------------------------------------------------
            //TextView Event Listener
            TextView tView = findViewById(itViewText.next());
            tView.setText(timeContentPref.getString("timeContent1" + i, "--:--"));
            tView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Save the view's ID so we know what was pressed. Used in onTimeSet() so we know where to place the text
                    timePickerButtonID = v.getId();
                    DialogFragment timePicker = new TimePickerFragment();
                    timePicker.show(getSupportFragmentManager(), "Time Picker");
                }
            });
        }

        resetScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Are you sure you wish to clear the schedule?").setPositiveButton("Yes",
                        dialogClickListener).setNegativeButton("No", dialogClickListener).show();
            }
        });

        uploadScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mmSocket.isConnected()) {
                    String msg = "SCH_NIGHT~";

                    Set<Integer> textViewID = new HashSet<Integer>();
                    textViewID.addAll(Arrays.asList(
                            R.id.time0,
                            R.id.time1,
                            R.id.time2,
                            R.id.time3,
                            R.id.time4,
                            R.id.time5,
                            R.id.time6));

                    Iterator<Integer> itViewText = textViewID.iterator();
                    for (short i = 0; i < 7; i++) {
                        TextView tView = findViewById(itViewText.next());
                        msg += tView.getText();

                        if (i < 6) {
                            msg += ",";
                        }
                    }

                    connectedThread.write(msg);
                }
            }
        });

    }

    //------------- MENU OPTIONS -------------

    private void resetSchedule() {
        SharedPreferences.Editor prefEditor = getSharedPreferences("dateStates1", MODE_PRIVATE).edit();
        prefEditor.clear();
        prefEditor.apply();
        prefEditor = getSharedPreferences("timeContent1", MODE_PRIVATE).edit();
        prefEditor.clear();
        prefEditor.apply();

        CompoundButton bView = findViewById(R.id.switch0);
        bView.setChecked(false);
        bView = findViewById(R.id.switch1);
        bView.setChecked(false);
        bView = findViewById(R.id.switch2);
        bView.setChecked(false);
        bView = findViewById(R.id.switch3);
        bView.setChecked(false);
        bView = findViewById(R.id.switch4);
        bView.setChecked(false);
        bView = findViewById(R.id.switch5);
        bView.setChecked(false);
        bView = findViewById(R.id.switch6);
        bView.setChecked(false);

        TextView tView = findViewById(R.id.time0);
        tView.setText(R.string.defaultTime);
        tView = findViewById(R.id.time1);
        tView.setText(R.string.defaultTime);
        tView = findViewById(R.id.time2);
        tView.setText(R.string.defaultTime);
        tView = findViewById(R.id.time3);
        tView.setText(R.string.defaultTime);
        tView = findViewById(R.id.time4);
        tView.setText(R.string.defaultTime);
        tView = findViewById(R.id.time5);
        tView.setText(R.string.defaultTime);
        tView = findViewById(R.id.time6);
        tView.setText(R.string.defaultTime);
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE: {
                    resetSchedule();
                    break;
                }
                case DialogInterface.BUTTON_NEGATIVE: {
                    //No clicked
                    break;
                }
            }
        }
    };

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView textView = findViewById(timePickerButtonID);

        String timeString = String.format(hourOfDay + ":" + minute);

        if (android.text.format.DateFormat.is24HourFormat(ScheduleAfterNoon.this)) {
            timeString = "";
            if (hourOfDay < 10) {
                timeString += "0";
            }
            timeString += String.format(hourOfDay + ":");
            if (minute < 10) {
                timeString += "0";
            }
            timeString += String.format("" + minute);
        }
        textView.setText(timeString);

        //The ID of the view as it appears in a layout XML file.
        //The final byte represents the day in question (Mon-Sun --- 0-6)
        String idAsResourceString = view.getResources().getResourceName(timePickerButtonID);
        int day = parseInt(String.format("" + idAsResourceString.charAt(idAsResourceString.length() - 1)));

        SharedPreferences.Editor editor = getSharedPreferences("timeContent1", MODE_PRIVATE).edit();
        editor.putString("timeContent1" + day, timeString);
        editor.apply();

        //Reset the pressed button
        timePickerButtonID = -1;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.basic_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.about) {
            //Do stuff
            return true;
        } else if (item.getItemId() == R.id.support) {
            //Do stuff
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //----------------------------------------

    //Display a toast message in the app that vanishes after a short while
    private void showToast(String msg) {
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
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes = 0; // bytes returned from read()
            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    /*
                    Read from the InputStream from Arduino until termination character is reached.
                    Then send the whole String message to GUI Handler.
                     */
                    buffer[bytes] = (byte) mmInStream.read();
                    String readMessage;
                    if (buffer[bytes] == '\n') {
                        readMessage = new String(buffer, 0, bytes);
                        Log.e("Arduino Message", readMessage);
                        //handler.obtainMessage(MESSAGE_READ,readMessage).sendToTarget();
                        bytes = 0;
                    } else {
                        bytes++;
                    }
                } catch (IOException e) {
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
                Log.e("Send Error", "Unable to send message", e);
            }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }


}