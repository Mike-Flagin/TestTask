package com.mike.testtask;

import static com.mike.testtask.User.BIRTHDAY_PARAM;
import static com.mike.testtask.User.DESCRIPTION_PARAM;
import static com.mike.testtask.User.NAME_PARAM;
import static com.mike.testtask.User.dateToString;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AddUserActivity extends AppCompatActivity {
    private EditText nameEditText;
    private DatePicker birthdayDatePicker;
    private EditText descriptionEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        nameEditText = findViewById(R.id.name_edittext);
        birthdayDatePicker = findViewById(R.id.birthday_datepicker);
        descriptionEditText = findViewById(R.id.description_edittext);
        Button addUserButton = findViewById(R.id.add_user_button);

        addUserButton.setOnClickListener(v -> {
            Intent data = new Intent();
            String name = nameEditText.getText().toString();
            String description = descriptionEditText.getText().toString();
            Calendar birthday = Calendar.getInstance();
            birthday.set(birthdayDatePicker.getYear(), birthdayDatePicker.getMonth() + 1, birthdayDatePicker.getDayOfMonth());
            data.putExtra(NAME_PARAM, name);
            data.putExtra(BIRTHDAY_PARAM, dateToString(birthday));
            data.putExtra(DESCRIPTION_PARAM, description);
            setResult(RESULT_OK, data);
            finish();
        });
    }
}