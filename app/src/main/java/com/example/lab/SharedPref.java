package com.example.lab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SharedPref extends AppCompatActivity {

    EditText editName, editSurname, editMarks, editTextId;
    Button btnAddData;
    Button btnviewAll;
    Button btnDelete;
    Button btnviewUpdate;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shared_pref); // Assuming you have the same layout file

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        editName = findViewById(R.id.editText_name);
        editSurname = findViewById(R.id.editText_surname);
        editMarks = findViewById(R.id.editText_Marks);
        editTextId = findViewById(R.id.editText_id);
        btnAddData = findViewById(R.id.button_add);
        btnviewAll = findViewById(R.id.button_viewAll);
        btnviewUpdate = findViewById(R.id.button_update);
        btnDelete = findViewById(R.id.button_delete);

        AddData();
        viewAll();
        UpdateData();
        DeleteData();
    }

    public void sendNotification() {
        String channel_id = "CHANNEL_ID_NOTIFICATION";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(SharedPref.this, channel_id);
        builder.setSmallIcon(R.drawable.notify);
        builder.setContentTitle("Delete data!");
        builder.setContentText("Attempting to delete a record");
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = notificationManager.getNotificationChannel(channel_id);

            if (notificationChannel == null) {
                notificationChannel = new NotificationChannel(channel_id, "Some Description", NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.setLightColor(Color.GREEN);
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
        notificationManager.notify(0, builder.build());
    }

    public void DeleteData() {
        btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(SharedPref.this)
                    .setTitle("Delete Record")
                    .setMessage("Are you sure you want to delete this?")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        String id = editTextId.getText().toString();
                        if (sharedPreferences.contains(id)) {
                            editor.remove(id);
                            editor.apply();
                            Toast.makeText(SharedPref.this, "Data Deleted", Toast.LENGTH_LONG).show();
                            sendNotification();
                        } else {
                            Toast.makeText(SharedPref.this, "Data not found", Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        });
    }

    public void UpdateData() {
        btnviewUpdate.setOnClickListener(v -> {
            String id = editTextId.getText().toString();
            String name = editName.getText().toString();
            String surname = editSurname.getText().toString();
            String marks = editMarks.getText().toString();

            if (sharedPreferences.contains(id)) {
                editor.putString(id, name + "," + surname + "," + marks);
                editor.apply();
                Toast.makeText(SharedPref.this, "Data Updated", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(SharedPref.this, "Data not found", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void AddData() {
        btnAddData.setOnClickListener(v -> {
            String id = editTextId.getText().toString();
            String name = editName.getText().toString();
            String surname = editSurname.getText().toString();
            String marks = editMarks.getText().toString();

            if (!sharedPreferences.contains(id)) {
                editor.putString(id, name + "," + surname + "," + marks);
                editor.apply();
                Toast.makeText(SharedPref.this, "Data Inserted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(SharedPref.this, "Data already exists", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void viewAll() {
        btnviewAll.setOnClickListener(v -> {
            StringBuffer buffer = new StringBuffer();
            for (String key : sharedPreferences.getAll().keySet()) {
                String value = sharedPreferences.getString(key, null);
                if (value != null) {
                    String[] parts = value.split(",");
                    buffer.append("Id :" + key + "\n");
                    buffer.append("Name :" + parts[0] + "\n");
                    buffer.append("Surname :" + parts[1] + "\n");
                    buffer.append("Marks :" + parts[2] + "\n\n");
                }
            }

            if (buffer.length() == 0) {
                showMessage("Error", "Nothing found");
            } else {
                showMessage("Data", buffer.toString());
            }
        });
    }

    public void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }
}