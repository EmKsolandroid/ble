package com.ble.dunasys.bleand;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class Ble {

    public static final String TAG = "Ble";


    private static final int NOT_CONNECTED = 0;
    private static final int CONNECTING = 1;
    private static final int CONNECTED = 2;
    public static Context mContext;
    public static Activity mActivity;
    public static int REQUEST_ENABLE_BT = 102;
    private static BluetoothAdapter mBluetoothAdapter;
    public ArrayList<String> devices;
    private BluetoothGatt mBluetoothGatt;
    private BluetoothManager bluetoothManager;
    private BluetoothDevice mDevice;
    private String ACTION_INTENT ;


    /**
     * Constrcutin initialization ..
     *
     * @param mActivity
     * @param mContext
     */
    public Ble(Activity mActivity, Context mContext,String RECIVER_ACTION_INTENT) {
        this.mActivity = mActivity;
        this.mContext = mContext;
        this.ACTION_INTENT = RECIVER_ACTION_INTENT;

        devices = new ArrayList<>();
        enableBle();

    }



    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private final BluetoothGattCallback mBluetoothGattCallBack = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            Log.d(TAG, "THIS CALL BACK IS CALLED" + status + "---" + newState + "---");
            switch (newState) {
                case NOT_CONNECTED:

                    Log.d(TAG, "NOT CONNECTED" + status);
                    break;
                case CONNECTING:
                    Log.d(TAG, "CONNECTING" + status);
                    break;
                case CONNECTED:
                    Log.d(TAG, "CONNECTED" + status);
                    mBluetoothGatt.discoverServices();
                    break;

                default:
                    Log.d(TAG, "CONNECTED" + status);
                    break;

            }


        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            Log.d(TAG, "SERVICE DISCOVER  CALL BACK IS CALLED" + status + "---");


            if (status == BluetoothGatt.GATT_SUCCESS) {
                displayBleService(gatt.getServices());
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {

            super.onCharacteristicRead(gatt, characteristic, status);
            Log.d(TAG, "OnChracteristicsRead-------->OOOOOOO1");
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);

            Log.d(TAG, "OnCharacteristicsWrite------> OOOOOOO2---------->" + characteristic.getUuid());

            //broadcastUpdate(characteristic);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            Log.d(TAG, "OncharacteristicsChange--------->" + characteristic.getUuid());

            broadcastUpdate(characteristic);
            // Log.d(TAG, "CALLED ENABLE NOTIFICATION ");
            // enableNotification(characteristic, true);
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
            Log.d(TAG, "onDescriptorRead");
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
            Log.d(TAG, "OnDescriptorWrite--ON WRITE DES CALLBACK");
        }
    };


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            Log.d(TAG, "DEVICE ::" + device.getAddress() + "--" + device.getName());
            devices.add(device.getAddress());
        }
    };





    /**
     * Enable Bluetooth
     */

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void enableBle() {
        bluetoothManager = (BluetoothManager) mContext.getSystemService(mContext.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            mActivity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);

        }


    }

    /**
     * Check Permission
     * @param permission
     * @return
     */
    public boolean checkPermission(int permission) {
        Log.d(TAG, "Enterd Checked Permission");
        int permission_ACCESS_COARSE_LOCATION = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION);
        //int permission_BLUETOOTH = ContextCompat.checkSelfPermission(mContext,Manifest.permission.BLUETOOTH);
        if (permission_ACCESS_COARSE_LOCATION == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "CHECK PERMISSION - TRUE");
            return true;

        } else {
            Log.d(TAG, "CHECK PERMISSION - FALSE");
            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, permission);
            return false;

        }
    }

    /**
     * scan Device
     * @param enable
     * @param SCAN_PERIOD
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void scanLeDevice(boolean enable, int SCAN_PERIOD) {
        Log.d(TAG, "Scan period::" + SCAN_PERIOD);
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    Log.d(TAG, "Stop Scan Post Delay");
                }
            }, 1000);


            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {

            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            Log.d(TAG, "Stop Scan");
        }
    }


    /**
     * Get The List Of Scanned Device
     * @return
     */
    public ArrayList<String> getDevices() {
        // Removing  All Duplicates
        HashSet<String> hashSet = new HashSet<>();
        hashSet.addAll(devices);
        devices.clear();
        if (devices.addAll(hashSet)) {
            return devices;
        } else {
            return null;
        }

    }

    /**
     * Connecting To BlueTooth ..
     */

    // Connect Bluetooth .. !
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void connectBLE(String device_ADDR) {


        if (device_ADDR != null) {
            mDevice = mBluetoothAdapter.getRemoteDevice(device_ADDR);
            Log.d(TAG, "DEVICE==" + mDevice.getName());


            mBluetoothGatt = mDevice.connectGatt(mContext, false, mBluetoothGattCallBack);
            // creating a Bond
            //  mDevice.setPin(new byte[]{(byte)123456});


            boolean bondState = mDevice.createBond();


            Log.d(TAG, "COONCETION _ STATE ==" + bondState);

        }
    }

    /**
     * Disconnect BLe ..
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void disConnectBLE() {
        mBluetoothGatt.disconnect();
    }

    /**
     * Get Services ..
     *
     * @param gattServices
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void displayBleService(List<BluetoothGattService> gattServices) {
        Log.d(TAG, "display Services");
        List<BluetoothGattCharacteristic> characteristics = new ArrayList<BluetoothGattCharacteristic>();
        for (BluetoothGattService services : gattServices) {
            Log.d(TAG, "SERVICES == ." + services.getUuid());

            characteristics = services.getCharacteristics();
            for (BluetoothGattCharacteristic characteristic : characteristics) {
                Log.d(TAG, "CAHRACTERISTICS  == ." + characteristic.getUuid() + "--" + characteristic.getProperties());


                //enableNotification(characteristic, true);
            }
        }

    }


    /**
     * Enable Notification
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void enableNotification(UUID DLE_SERVICE, UUID characteristics,UUID CLIENT_CCD ,boolean enable) {


        BluetoothGattService Service = mBluetoothGatt.getService(DLE_SERVICE);
        if (Service == null) {
            Log.d(TAG, "service not found!");
            //return false;
        }


        BluetoothGattCharacteristic characteristic = Service.getCharacteristic(characteristics);


        //FOR SPECIFIC CHARACTETISTIC .. !

        List<BluetoothGattDescriptor> bluetoothGattDescriptors = characteristic.getDescriptors();
        for (BluetoothGattDescriptor descriptor1 : bluetoothGattDescriptors) {
            Log.d(TAG, "DESCRIPTOR==" + descriptor1.getUuid());
        }
        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(CLIENT_CCD);


        // CHAR ONLY TO NOTIFY ..
        Log.d(TAG, "descriptor==" + descriptor.getUuid());
        mBluetoothGatt.setCharacteristicNotification(characteristic, true);
        descriptor.setValue(true ? BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE : BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
        //descriptor.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
        boolean result = mBluetoothGatt.writeDescriptor(descriptor);


        Log.d(TAG, "Notification-UUID::"+ characteristic.getUuid()+"-->"+ result);


    }


    /**
     * write On Characteristics
     */

    // Function To Write Characteristics .. !
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void writeCharacteristics(byte[] data, UUID DLE_SERVICE, UUID DLE_WRITE_CHAR) {
        BluetoothGattService Service = mBluetoothGatt.getService(DLE_SERVICE);
        if (Service == null) {
            Log.d(TAG, "service not found!");
            //return false;
        }


        BluetoothGattCharacteristic charc1 = Service.getCharacteristic(DLE_WRITE_CHAR);
        if (charc1 == null) {
            Log.d(TAG, "char not found!");
            Log.d(TAG, "CHARAC_-TRST" + charc1.getUuid());
            // return false;
        }
        // charc1.setValue(new byte[]{0x00, 0x05, 0x10, 0x01, 0x3E, 0x01, 0x23});

        charc1.setValue(data);
        boolean stat = mBluetoothGatt.writeCharacteristic(charc1);

        Log.d(TAG, "FINISHED WRITTING CHAR 1 status write :(status)" + stat);

        if (data != null && data.length > 0) {
            final StringBuilder stringBuilder = new StringBuilder(data.length);
            for (byte byteChar : data)
                stringBuilder.append(String.format("%02X ", byteChar));


        }


    }


    /**
     * BroadCsating Notification
     */

    // BroadCastUpdate ..
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void broadcastUpdate(BluetoothGattCharacteristic characteristic) {




        final byte[] data = characteristic.getValue();
        if (data != null && data.length > 0) {
            final StringBuilder stringBuilder = new StringBuilder(data.length);
            for (byte byteChar : data)
                stringBuilder.append(String.format("%02X ", byteChar));
            Log.d(TAG, "DATA==" + new String(data) + "\n" + stringBuilder.toString());

            Intent intent = new Intent(ACTION_INTENT);
            intent.putExtra("data",stringBuilder.toString());
            mContext.sendBroadcast(intent);
        }




    }
}