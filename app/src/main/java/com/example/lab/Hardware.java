//package com.example.lab;
//
//import android.bluetooth.BluetoothAdapter;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.media.MediaPlayer;
//import android.media.MediaRecorder;
//import android.net.wifi.WifiManager;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//import android.widget.VideoView;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//
//import java.io.IOException;
//
//public class Hardware extends AppCompatActivity implements SurfaceHolder.Callback {
//
//    // WiFi
//    private WifiManager wifiManager;
//    private Button wifiSwitchButton;
//    private TextView wifiStatusTextView;
//
//    // Bluetooth
//    private BluetoothAdapter mBluetoothAdapter;
//
//    // MediaPlayer
//    private MediaPlayer mediaPlayer;
//
//    // MediaRecorder
//    private MediaRecorder mediaRecorder;
//    private SurfaceHolder surfaceHolder;
//    private Button buttonRecord;
//    private Button buttonPlayback;
//    private boolean isRecording = false;
//    private String outputFile;
//
//    // Camera
//    private static final int pic_id = 123;
//    Button camera_open_id;
//    ImageView click_image_id;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.hardware);
//
//        // WiFi
//        wifiSwitchButton = findViewById(R.id.wifiSwitch);
//        wifiStatusTextView = findViewById(R.id.tv);
//        wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
//
//        // Bluetooth
//        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//
//        // MediaPlayer
//        mediaPlayer = MediaPlayer.create(this, R.raw.song);
//
//        // MediaRecorder
//        SurfaceView surfaceView = findViewById(R.id.surfaceView);
//        surfaceHolder = surfaceView.getHolder();
//        surfaceHolder.addCallback(this);
//        buttonRecord = findViewById(R.id.button_record);
//        buttonPlayback = findViewById(R.id.button_playback);
//        outputFile = getExternalCacheDir().getAbsolutePath() + "/recorded_video.mp4";
//
//        // Camera
//        camera_open_id = findViewById(R.id.camera_button);
//        click_image_id = findViewById(R.id.click_image);
//
//        // Set initial Wi-Fi status text
//        updateWifiStatusText();
//    }
//
//    // WiFi
//    public void toggleWifi(View view) {
//        if (wifiManager.isWifiEnabled()) {
//            wifiManager.setWifiEnabled(false);
//        } else {
//            wifiManager.setWifiEnabled(true);
//        }
//        updateWifiStatusText();
//    }
//
//    private void updateWifiStatusText() {
//        if (wifiManager.isWifiEnabled()) {
//            wifiStatusTextView.setText(R.string.wi_fi_is_on);
//        } else {
//            wifiStatusTextView.setText(R.string.wi_fi_is_off);
//        }
//    }
//
//    // Bluetooth
//    public void toggleBluetooth(View view) {
//        if (mBluetoothAdapter.isEnabled()) {
//            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
//                return;
//            }
//            mBluetoothAdapter.disable();
//            showToast("Bluetooth is OFF");
//        } else {
//            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableBtIntent, 1);
//            showToast("Bluetooth is ON");
//        }
//    }
//
//    // MediaPlayer
//    public void playAudio(View view) {
//        mediaPlayer.start();
//    }
//
//    // MediaRecorder
//    @Override
//    public void surfaceCreated(SurfaceHolder holder) {
//        surfaceHolder = holder;
//        if (mediaRecorder != null) {
//            mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
//        }
//    }
//
//    @Override
//    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//        surfaceHolder = holder;
//    }
//
//    @Override
//    public void surfaceDestroyed(SurfaceHolder holder) {
//        if (isRecording) {
//            stopRecording();
//        }
//        releaseMediaRecorder();
//    }
//
//    public void toggleRecording(View view) {
//        if (!isRecording) {
//            startRecording();
//        } else {
//            stopRecording();
//        }
//    }
//
//    private void startRecording() {
//        if (prepareMediaRecorder()) {
//            try {
//                mediaRecorder.start();
//                isRecording = true;
//                buttonRecord.setText("Stop Recording");
//                buttonPlayback.setVisibility(View.GONE);
//                showToast("Recording started");
//            } catch (IllegalStateException e) {
//                e.printStackTrace();
//                releaseMediaRecorder();
//                isRecording = false;
//                buttonRecord.setText("Record");
//                buttonPlayback.setVisibility(View.VISIBLE);
//                showToast("Failed to start recording");
//            }
//        } else {
//            showToast("Failed to prepare MediaRecorder");
//        }
//    }
//
//    private void stopRecording() {
//        try {
//            mediaRecorder.stop();
//            mediaRecorder.reset();
//            mediaRecorder.release();
//            mediaRecorder = null;
//            isRecording = false;
//            buttonRecord.setText("Record");
//            buttonPlayback.setVisibility(View.VISIBLE);
//            showToast("Recording stopped");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private boolean prepareMediaRecorder() {
//        mediaRecorder = new MediaRecorder();
//        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
//        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
//        mediaRecorder.setVideoSize(320, 240
//        ); // Adjust size as needed
//        mediaRecorder.setVideoFrameRate(30);
//        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
//        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
//        mediaRecorder.setOutputFile(outputFile);
//        mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
//        mediaRecorder.setOrientationHint(90); // Set orientation
//        try {
//            mediaRecorder.prepare();
//            return true;
//        } catch (IOException e) {
//            e.printStackTrace();
//            releaseMediaRecorder();
//            return false;
//        }
//    }
//
//    private void releaseMediaRecorder() {
//        if (mediaRecorder != null) {
//            mediaRecorder.reset();
//            mediaRecorder.release();
//            mediaRecorder = null;
//        }
//    }
//
//    // Camera
//    public void openCamera(View view) {
//        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(camera_intent, pic_id);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == pic_id) {
//            Bitmap photo = (Bitmap) data.getExtras().get("data");
//            click_image_id.setImageBitmap(photo);
//        }
//    }
//
//    // Helper method to show toast messages
//    private void showToast(String message) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//    }
//}

package com.example.lab;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Set;

public class Hardware extends AppCompatActivity {
    ImageView img;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button b1, b2, b3, b4, b5,on,off,discover;
        TextView t2;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hardware);
        b1 = findViewById(R.id.capture);
        b2 = findViewById(R.id.On);
        b3 = findViewById(R.id.discover);
        b4 = findViewById(R.id.off);
        b5 = findViewById(R.id.display);
        t2 = findViewById(R.id.textView2);
        on=findViewById(R.id.on_w);
        off=findViewById(R.id.off_w);
        discover=findViewById(R.id.discover_wifi);
        img = findViewById(R.id.imageView);
        int REQUEST_ENABLE_BT = 0;
        int REQUEST_DISCOVER_BT = 1;
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        WifiManager wifi= (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivityForResult(intent, REQUEST_ENABLE_BT);
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.BLUETOOTH_ADVERTISE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivityForResult(intent, REQUEST_DISCOVER_BT);
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                adapter.disable(); // Corrected line
            }
        });
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Set<BluetoothDevice> devices = adapter.getBondedDevices();
                for(BluetoothDevice device:devices){
                    t2.append(device.getName()+"\n");
                }
            }
        });
        on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wifi.setWifiEnabled(true);
            }
        });
        off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wifi.setWifiEnabled(false);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap map=(Bitmap) data.getExtras().get("data");
        img.setImageBitmap(map);
    }
}