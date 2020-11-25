package com.example.phonebook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final int PHONE_CALL = 1;
    private final int SEND_SMS = 2;
    private final int REQUEST_CODE_PERMISSION_READ = 100;
    private MyAdapter myAdapter;
    Intent intentPhoneActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        intentPhoneActivity = new Intent(this, PhoneActivity.class);

        ActivityCompat.requestPermissions(
                MainActivity.this,
                new String[]{
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.SEND_SMS
                },
                REQUEST_CODE_PERMISSION_READ
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        ArrayList<Contact> contactsList;
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION_READ &&
                permissions[0].equals(Manifest.permission.READ_CONTACTS) &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            Cursor cursor = getContentResolver()
                    .query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

            if (cursor.moveToFirst()) {

                contactsList = getContactsList(cursor);
                myAdapter = new MyAdapter(contactsList);

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                RecyclerView recyclerView = findViewById(R.id.recyclerView);
                TextView textView = findViewById(R.id.textView);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(myAdapter);
                textView.setText("Количество контактов: " + Integer.toString(contactsList.size()));


                myAdapter.setOnClickListener(new MyAdapter.MyOnClickListener() {
                    @Override
                    public void onClickCall(Contact contact) {
                        ArrayList<String> phones = contact.getPhones();
                        int countPhones = phones.size();
                        if (countPhones == 1) {
                            phone_call(contact.getPhone(0));
                        } else if (countPhones > 1) {
                            intentPhoneActivity.putExtra("type", PHONE_CALL);
                            intentPhoneActivity.putExtra("phones", phones.toArray(new String[0]));
                            startActivityForResult(intentPhoneActivity, 1);
                        }

                    }

                    @Override
                    public void onClickSms(Contact contact) {
                        ArrayList<String> phones = contact.getPhones();
                        int countPhones = phones.size();
                        if (countPhones == 1) {
                            send_sms(contact.getPhone(0));
                        } else if (countPhones > 1) {
                            intentPhoneActivity.putExtra("type", SEND_SMS);
                            intentPhoneActivity.putExtra("phones", phones.toArray(new String[0]));
                            startActivityForResult(intentPhoneActivity, 1);
                        }

                    }
                });
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            int type = data.getIntExtra("type", -1);
            String phone = data.getStringExtra("phone");
            if (type == PHONE_CALL) {
                phone_call(phone);
            } else if (type == SEND_SMS) {
                send_sms(phone);
            }
        }
    }

    private void phone_call(String phone) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel: " + phone));
        startActivity(intent);
    }

    private void send_sms(String phone) {
        String toSms = "smsto:" + phone;
        String messageText = "Привет!";
        Intent sms = new Intent(Intent.ACTION_SENDTO, Uri.parse(toSms));

        sms.putExtra("sms_body", messageText);
        startActivity(sms);
    }

    private ArrayList<Contact> getContactsList(Cursor cursor) {
        ArrayList<Contact> contacts = new ArrayList<>();

        do {
            int idIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            String id = cursor.getString(idIndex);

            int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            String name = cursor.getString(nameIndex);

            int hasPhoneNumberIndex = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
            String hasPhoneNumber = cursor.getString(hasPhoneNumberIndex);

            if (Integer.parseInt(hasPhoneNumber) > 0) {
                Cursor phoneCursor = getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        new String[]{id},
                        null
                );
                if (phoneCursor.moveToFirst()) {
                    contacts.add(new Contact(R.drawable.ic_contact, name, getPhoneNumbers(phoneCursor)));
                }
            }
        } while (cursor.moveToNext());

        Collections.sort(contacts, new Comparator<Contact>() {
            public int compare(Contact o1, Contact o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });
        return contacts;
    }

    private ArrayList<String> getPhoneNumbers(Cursor phoneCursor) {
        String phoneInfo;
        ArrayList<String> phoneNumbers = new ArrayList<>();
        do {
            int phoneNumberIndex = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            phoneInfo = phoneCursor.getString(phoneNumberIndex);
            phoneNumbers.add(phoneInfo);
        } while (phoneCursor.moveToNext());
        phoneCursor.close();
        return phoneNumbers;
    }

}