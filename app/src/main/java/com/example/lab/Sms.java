package com.example.lab;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import java.util.ArrayList;

public class Sms extends Activity {

    private static final int PERMISSION_REQUEST_SEND_SMS = 123;
    private static final int REQUEST_SELECT_CONTACTS = 1;
    private EditText editTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms);

        // Initialize EditText field
        editTextMessage = findViewById(R.id.editTextMessage);
    }

    public void selectContacts(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // Enable multiple selection
        startActivityForResult(intent, REQUEST_SELECT_CONTACTS);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_CONTACTS && resultCode == RESULT_OK) {
            ArrayList<String> selectedPhoneNumbers = new ArrayList<>();
            Cursor cursor = null;
            try {
                Uri uri = data.getData();
                cursor = getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int phoneNumberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    do {
                        String phoneNumber = cursor.getString(phoneNumberIndex);
                        selectedPhoneNumbers.add(phoneNumber);
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

            if (!selectedPhoneNumbers.isEmpty()) {
                // Here you can do whatever you want with the selected phone numbers,
                // such as displaying them in a TextView or using them to send messages.
                sendMessage(selectedPhoneNumbers);
            } else {
                Toast.makeText(getApplicationContext(), "No contacts selected", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendMessage(ArrayList<String> phoneNumbers) {
        String message = editTextMessage.getText().toString().trim();

        if (message.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter a message.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_SEND_SMS);
            return;
        }

        if (phoneNumbers.size() == 1) {
            sendSMS(phoneNumbers.get(0), message);
        } else {
            sendGroupSMS(phoneNumbers, message);
        }
    }

    private void sendSMS(String phoneNumber, String message) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
        Toast.makeText(getApplicationContext(), "SMS sent to " + phoneNumber, Toast.LENGTH_SHORT).show();
    }

    private void sendGroupSMS(ArrayList<String> phoneNumbers, String message) {
        SmsManager smsManager = SmsManager.getDefault();
        for (String phoneNumber : phoneNumbers) {
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
        }
        Toast.makeText(getApplicationContext(), "Group SMS sent to all recipients", Toast.LENGTH_SHORT).show();
    }
}
