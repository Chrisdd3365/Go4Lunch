package com.christophedurand.go4lunch.ui;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.christophedurand.go4lunch.R;


public class SettingsActivity extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        notificationsSwitchCompatIsTapped();

    }


    private void notificationsSwitchCompatIsTapped() {
        SwitchCompat notificationsSwitchCompat = findViewById(R.id.notifications_switch_compat);
        SharedPreferences sharedPreferences = getSharedPreferences("Go4LunchNotification", Context.MODE_PRIVATE);
        boolean remindMe = sharedPreferences.getBoolean("remindMe", true);
        notificationsSwitchCompat.setChecked(remindMe);
        notificationsSwitchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("remindMe", isChecked);
            editor.apply();
        });
    }

}
