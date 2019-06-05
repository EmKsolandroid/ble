# Ble Library V1.1
![alt text](https://camo.githubusercontent.com/f955549c1c2a3f18777beabc31d20728dbf6f62a/68747470733a2f2f696d672e6761646765746861636b732e636f6d2f696d672f37302f32372f36333537343930353934343038312f302f616e64726f69642d6261736963732d636f6e6e6563742d626c7565746f6f74682d6465766963652e31323830783630302e6a7067)

Simple and Mechanized Library to develop the BLE applications faster .

## Getting Started

Create an Android Ble Project and Follow the below steps.


## How To Add dunadevAndroid:bleLib To Your Project .. ?

## Using Gradle: <br>
Step 1 :Add it in your root build.gradle at the end of repositories .

```
allprojects {
		repositories {
			
			maven { url 'https://jitpack.io' }
		}
	}
```
Step 2  Add the dependency

```
dependencies {
	        
	    implementation 'com.github.EmKsolandroid:ble:1.1'

	}
```

## Using Maven :<br>

Step 1 : 
```
<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
 ```
 
 Step 2:
 ```
 <dependency>
	    <groupId>com.github.EmKsolandroid</groupId>
	    <artifactId>ble</artifactId>
	    <version>1.1</version>
	</dependency>
```


Once you have finished adding this library you are all set to work with Ble Lib.

### Few Important Notes :
<ul>
<li>This Library has already added all the premissions you required . so you need not to bother about it<br>

```
 <uses-permission android:name="android.permission.BLUETOOTH_ADMIN"/>
<uses-permission android:name="android.permission.BLUETOOTH"/>
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
    <!-- Blutooth Permission -->
<uses-feature android:name="android.hardware.bluetooth_le" android:required="true"/>
  ```
  </li>
  <li> You need to create the boradcast Reciever to get  the  Notification </li>
<ul>


### Initialization

Create An Broadcast Reciver and Declare it in android Manifest:
```
<receiver
            android:name="com.androidBle.maturis.blelib.MyReciever"
            android:enabled="true"
            android:exported="true">
            <intent-filter android:priority="9">
                <action android:name="com.example.maturis.blelib.CUSTOM_BROADCAST" />
            </intent-filter>
        </receiver>
        
```

Create An Object Of Ble this will enable the ble On device.

```
Ble  ble = new Ble(Activity, Contxet,Android_Name_Reciver);
```

Check for the permission and it will call the on request premission if permissions are not granted.

```
Boolean status = ble.checkPermission(PERMISSION_REQUEST_COARSE_LOCATION);
        if (status){
            Log.w(TAG,"Permission Granted");


        }else {
            Log.w(TAG,"Permission Not Granted");

        }
```

Override the onRequestPermissionsResult

```
  @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, yay! Start the Bluetooth device scan.
                    Log.d("TAG", "RUNTIIME PERMISSION OK");
                    Log.w("TAG","SCANNING STARTED");

                } else {
                    // Alert the user that this application requires the location permission to perform the scan.
                    Log.d("TAG", "RUNTIIME PERMISSION NOTOK");
                    Toast.makeText(this, "NO PERMISSION ", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
```

To, start the scan of Devices. The Device will stop the scan after a particular Scan duration. 
```
 ble.scanLeDevice(true,SCAN_DURATION);
 ```

To, stop the Scan the manually .
```
 ble.scanLeDevice(false,SCAN_DURATION);
 ```
 
 To get the list of devices .This will return an array list of type String
 ```
  ble.getDevices();
  ```
  
  To connect  and to create a bond with Ble device.
  ```
  ble.connectBLE(MAC_ADDRESS);
  ```
  To disconnect with Ble Device
  ```
  ble.disConnectBLE();
  ```
  
  To perform write on characteristics.
   ```
   ble.writeCharacteristics(YOUR_BYTE_ARRAY,BLE_SERVICE,BLE_WRITE_CHARACTERISTICS);
   ```
   
  To enable Notification.
   ```
   ble.enableNotification(BLE_SERVICE,BLE_NOTIFY_CHARACTER,CLIENT_CCD,true);
   ```
   
   To get the Notified data you have to create a BroadCastReciver and add the following code snippet in onRecieve.
    
    ```
    @Override
    public void onReceive(Context context, Intent intent) {
        String data = intent.getStringExtra("data");

        Log.w(TAG, "Request Recieved On MyReciever ::" + data);

    }
    ```
    
    
   To check the logs. type Ble in logcat.
    
    
    
## Conclusion

Feel free to modify the library as per your need.<br>
Happy coding



## Versioning

We use [Github](https://github.com/) for versioning. For the versions available, see the [tags on this repository](https://github.com/EmKsolandroid/ble/releases). 

## Authors

* **Maturi Karthik ** - maturikarthik31@gmail.com



## License

This project is licensed under MIT - 




## Built With


* [Maven](https://maven.apache.org/) - Dependency Management





