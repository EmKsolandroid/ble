package com.ble.ellamma.ble;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ble.ellamma.bleand.Ble;
import com.ble.ellamma.ble.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Ble ble = new Ble(this,getApplicationContext(),null);
        ble.enableBle();
    }
}
