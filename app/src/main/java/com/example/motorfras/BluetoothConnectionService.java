package com.example.motorfras;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.UUID;

public class BluetoothConnectionService {
    //THIS UUID IS INSECURE AND NEEDS TO BE BASED ON V1 GENERATION USING DEVICE MAC ID
    private static final UUID INSECURE_UUID = UUID.fromString("041de592-ab3c-11eb-bcbc-0242ac130002");
    private final BluetoothAdapter mBluetoothAdapter;
    Context mContext;

    private AcceptThread mInsecureAcceptThread;
    private ConnectThread mConnectThread;
    private BluetoothDevice mDevice;
    private UUID deviceUUID;

    private ConnectedThread mConnectedThread;

    public BluetoothConnectionService(Context context) {
        mContext = context;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mServerSocket;

        public AcceptThread(){
            BluetoothServerSocket tmp = null;
            try{
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("motorfras", INSECURE_UUID);
            }
            catch(IOException e){
                //Handle error
            }
            mServerSocket = tmp;
        }
        public void run(){
            BluetoothSocket socket = null;

            try{
                socket = mServerSocket.accept();
            }
            catch(IOException e){
                //Handle error
            }

            if(socket != null)
            {
                connected(socket, mDevice);
            }
        }

        public void cancel()
        {
            try{
                mServerSocket.close();
            }
            catch (IOException e){
                //Handle error
            }
        }
    }
    private class ConnectThread extends Thread {
        private BluetoothSocket mSocket;

        public ConnectThread(BluetoothDevice device, UUID uuid){
            mDevice = device;
            deviceUUID = uuid;
        }

        public void run() {
            BluetoothSocket tmp = null;
            try{
                tmp = mDevice.createRfcommSocketToServiceRecord(deviceUUID);
            }
            catch(IOException e){
                //Handle error
            }
            mSocket = tmp;

            mBluetoothAdapter.cancelDiscovery();

            try {
                mSocket.connect();
            }
            catch (IOException e) {
                try {
                    mSocket.close();
                }
                catch (IOException e1) {
                    //Handle error
                }
            }
            connected(mSocket, mDevice);
        }
        public void cancel() {
            try {
                mSocket.close();
            }
            catch (IOException e){
                //Handle error
            }
        }
    }

    public synchronized void start(){
        if(mConnectThread != null)
        {
            mConnectThread.cancel();
            mConnectThread = null;
        }
        if(mInsecureAcceptThread == null)
        {
            mInsecureAcceptThread = new AcceptThread();
            mInsecureAcceptThread.start();
        }
    }

    public void startClient(BluetoothDevice device, UUID uuid){
        //Progressdialog if desired
        mConnectThread = new ConnectThread(device, uuid);
        mConnectThread.start();
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mSocket;
        private final InputStream mInStream;
        private final OutputStream mOutStream;

        public ConnectedThread(BluetoothSocket socket){
            mSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            //Dismiss progress dialog if there is one

            try{
                tmpIn = mSocket.getInputStream();
                tmpOut = mSocket.getOutputStream();
            }
            catch(IOException e)
            {
                //Handle error
            }
            mInStream = tmpIn;
            mOutStream = tmpOut;
        }
        public void run(){
            byte[] buffer = new byte[1024];

            int bytes = 0;

            while(true)
            {
                try {
                    bytes = mInStream.read(buffer);
                }
                catch (IOException e) {
                    //Handle error
                    break;
                }
                String incomingMessage = new String(buffer, 0, bytes);
            }
        }

        public void write(byte[] bytes){
            String text = new String(bytes, Charset.defaultCharset());

            try {
                mOutStream.write(bytes);
            }
            catch (IOException e){
                //Handle error
            }
        }

        public void cancel(){
            try {
                mSocket.close();
            }
            catch (IOException e){
                //Handle error
            }
        }
    }
    private void connected(BluetoothSocket mSocket, BluetoothDevice mDevice) {
        mConnectedThread = new ConnectedThread(mSocket);
        mConnectedThread.start();
    }

    public void write(byte[] out){
        ConnectedThread r;

        mConnectedThread.write(out);
    }
}
