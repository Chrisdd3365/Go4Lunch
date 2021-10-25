package com.christophedurand.go4lunch.ui;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.christophedurand.go4lunch.R;
import com.christophedurand.go4lunch.ui.workmatesView.UserManager;


public class SettingsActivity extends AppCompatActivity {

    private SwitchCompat _notificationsSwitchCompat;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        _notificationsSwitchCompat = findViewById(R.id.notifications_switch_compat);

        UserManager.getInstance().getCurrentUserData().addOnSuccessListener(currentUser -> {
            _notificationsSwitchCompat.setChecked(currentUser.isHasSetNotifications());
            notificationsSwitchCompatIsTapped(currentUser.getUid());
        });
    }


    private void notificationsSwitchCompatIsTapped(String userId) {
        _notificationsSwitchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            UserManager.getInstance().updateHasSetNotifications(isChecked, userId);
        });
    }

}
