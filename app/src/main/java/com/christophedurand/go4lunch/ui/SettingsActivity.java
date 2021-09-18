package com.christophedurand.go4lunch.ui;


import android.os.Bundle;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.christophedurand.go4lunch.R;
import com.google.firebase.messaging.FirebaseMessaging;


public class SettingsActivity extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        notificationsSwitchCompatIsTapped();

    }


    private void notificationsSwitchCompatIsTapped() {
        SwitchCompat notificationsSwitchCompat = findViewById(R.id.notifications_switch_compat);
        notificationsSwitchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    FirebaseMessaging.getInstance().subscribeToTopic("Go4Lunch Messages");
                } else {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("Go4Lunch Messages");
                }
            }
        });
    }

}
