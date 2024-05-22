package com.example.lab;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class BroadcastHandler extends AppCompatActivity {

    private static final int REQUEST_BLUETOOTH_PERMISSION = 1;

    private BroadcastHandle broadcastHandler;
    private TextView tvStatus;
    private BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.broadcast);

        tvStatus = findViewById(R.id.tvStatus);

        // Initialize BroadcastHandler
        broadcastHandler = new BroadcastHandle();

        // Register the BroadcastHandler with intent filters
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        filter.addAction(Intent.ACTION_BOOT_COMPLETED);

        registerReceiver(broadcastHandler, filter);

        // Initialize BluetoothAdapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Button to simulate Airplane Mode toggle
        Button btnToggleAirplaneMode = findViewById(R.id.btnToggleAirplaneMode);
        btnToggleAirplaneMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BroadcastHandler.this, "Cannot toggle Airplane Mode directly due to Android restrictions.", Toast.LENGTH_LONG).show();
            }
        });

        // Button to toggle Bluetooth
        Button btnToggleBluetooth = findViewById(R.id.btnToggleBluetooth);
        btnToggleBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleBluetooth();
            }
        });

        // Button to check connectivity
        Button btnCheckConnectivity = findViewById(R.id.btnCheckConnectivity);
        btnCheckConnectivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnectivity();
            }
        });

        // Button to check battery level
        Button btnCheckBattery = findViewById(R.id.btnCheckBattery);
        btnCheckBattery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBatteryLevel();
            }
        });
    }

    private void toggleBluetooth() {
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth not supported", Toast.LENGTH_SHORT).show();
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_BLUETOOTH_PERMISSION);
                return;
            }
        }
        if (bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.disable();
            tvStatus.setText("Bluetooth is off");
        } else {
            bluetoothAdapter.enable();
            tvStatus.setText("Bluetooth is on");
        }
    }

    private void checkConnectivity() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isConnected = cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
        tvStatus.setText(isConnected ? "Connected" : "Disconnected");
    }

    private void checkBatteryLevel() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = registerReceiver(null, ifilter);

        int level = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) : -1;
        int scale = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1) : -1;

        float batteryPct = level * 100 / (float) scale;
        if(batteryPct==100) {
            tvStatus.setText("Battery Level: " + batteryPct + "%");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastHandler);
    }

    public class BroadcastHandle extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_AIRPLANE_MODE_CHANGED.equals(action)) {
                boolean state = intent.getBooleanExtra("state", false);
                String message = state ? "Airplane mode is on" : "Airplane mode is off";
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                tvStatus.setText(message);
            } else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                String message = (state == BluetoothAdapter.STATE_ON) ? "Bluetooth is on" : "Bluetooth is off";
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                tvStatus.setText(message);
            } else if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
                boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
                String message = noConnectivity ? "Disconnected" : "Connected";
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                tvStatus.setText(message);
            } else if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                float batteryPct = level * 100 / (float) scale;
                String message = "Battery Level: " + batteryPct + "%";
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                tvStatus.setText(message);
            } else if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
                String message = "Boot Completed";
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                tvStatus.setText(message);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_BLUETOOTH_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                toggleBluetooth();
            } else {
                Toast.makeText(this, "Bluetooth permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
