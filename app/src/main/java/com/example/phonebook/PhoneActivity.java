package com.example.phonebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class PhoneActivity extends AppCompatActivity {
    private final int PHONE_CALL = 1;
    private final int SEND_SMS = 2;
    String[] phones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        RadioButton newRadioButton;
        int type;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        TextView textView = findViewById(R.id.infoTextView);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        Button buttonOk = findViewById(R.id.buttonOk);
        Button buttonCancel = findViewById(R.id.buttonCancel);

        Intent intent = getIntent();
        type = intent.getIntExtra("type", -1);
        phones = intent.getStringArrayExtra("phones");

        if (type == PHONE_CALL) {
            textView.setText("для звонка");
            buttonOk.setText("Позвонить");
        } else if (type == SEND_SMS) {
            textView.setText("для отправки SMS");
            buttonOk.setText("Отправить SMS");
        }
        for (int i = 0; i < phones.length; i++) {
            newRadioButton = new RadioButton(this);
            newRadioButton.setText(phones[i]);
            radioGroup.addView(newRadioButton);
        }

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                if (checkedRadioButtonId != -1) {
                    RadioButton myRadioButton = (RadioButton) findViewById(checkedRadioButtonId);
                    intent.putExtra("type", type);
                    intent.putExtra("phone", myRadioButton.getText());
                    setResult(RESULT_OK, intent);
                } else {
                    setResult(RESULT_CANCELED, intent);
                }
                finish();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
    }

}