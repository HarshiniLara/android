package com.example.lab;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;

public class Notification extends AppCompatActivity {
    Button b1,b2,b3,b4;
    ProgressBar progressBar;
    DatePicker picker;
    TimePicker time;
    TextView t1;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);
        b1=findViewById(R.id.alert);
        b2=findViewById(R.id.status);
        b3=findViewById(R.id.prog);
        progressBar=findViewById(R.id.progressBar);
        picker=findViewById(R.id.calendarView);
        b4=findViewById(R.id.get);
        t1=findViewById(R.id.date);
        time=findViewById(R.id.time);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert=new AlertDialog.Builder(Notification.this);
                alert.setTitle("Alert");
                alert.setMessage("Data is going to over");
                alert.setPositiveButton("Recharge", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"Recharing",Toast.LENGTH_LONG).show();
                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog= alert.create();
                alertDialog.show();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String channel_id="CHANNEL_ID_NOTIFICATION";
                NotificationCompat.Builder builder=new NotificationCompat.Builder(Notification.this,channel_id);
                builder.setSmallIcon(R.drawable.ic_launcher_background);
                builder.setContentTitle("SOS");
                builder.setContentText("Someone is in Alert");
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("data","Alert");
                PendingIntent pendingIntent=PendingIntent.getActivity(getApplicationContext(),0,intent,PendingIntent.FLAG_MUTABLE);
                builder.setContentIntent(pendingIntent);
                NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                NotificationChannel notificationChannel= null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    notificationChannel = notificationManager.getNotificationChannel(channel_id);

                    if(notificationChannel==null){
                        notificationChannel=new NotificationChannel(channel_id,"Some Description",NotificationManager.IMPORTANCE_HIGH);
                        notificationChannel.setLightColor(Color.GREEN);
                        notificationChannel.enableVibration(true);
                        notificationManager.createNotificationChannel(notificationChannel);
                    }
                }
                notificationManager.notify(0,builder.build());
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler handler=new Handler();
                progressBar.setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for(int i=0;i<=100;i++){
                            int finalI = i;
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(finalI);
                                }
                            },i*100);
                        }
                    }
                }).start();
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t1.setText(changeDate());
            }
        });

    }
    public String changeDate(){
        StringBuilder builder=new StringBuilder();
        //builder.append(picker.getDayOfMonth()+"\n"+picker.getMonth()+"\n"+picker.getYear());
        builder.append(time.getHour()+"\n"+time.getMinute()+"\n");
        return builder.toString();
    }
}