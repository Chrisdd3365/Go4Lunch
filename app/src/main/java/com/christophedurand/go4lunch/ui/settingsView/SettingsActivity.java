package com.christophedurand.go4lunch.ui.settingsView;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.ViewModelProvider;

import com.christophedurand.go4lunch.R;
import com.christophedurand.go4lunch.ui.ViewModelFactory;


public class SettingsActivity extends AppCompatActivity {

    private SwitchCompat _notificationsSwitchCompat;

    private SettingsViewModel settingsViewModel;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        _notificationsSwitchCompat = findViewById(R.id.notifications_switch_compat);

        configureViewModel();
        subscribe();
    }


    private void notificationsSwitchCompatIsTapped(String userId) {
        _notificationsSwitchCompat.setOnCheckedChangeListener((buttonView, isChecked) ->
                settingsViewModel.updateHasSetNotifications(isChecked, userId));
    }


    private void configureViewModel() {
        ViewModelFactory viewModelFactory = new ViewModelFactory("");
        settingsViewModel = new ViewModelProvider(this, viewModelFactory).get(SettingsViewModel.class);
    }

    private void subscribe() {
        settingsViewModel.getSettingsViewStateMediatorLiveData().observe(this, settingsViewState -> {
            if (settingsViewState.getCurrentUser() != null) {
                _notificationsSwitchCompat.setChecked(settingsViewState.getCurrentUser().isHasSetNotifications());
                notificationsSwitchCompatIsTapped(settingsViewState.getCurrentUser().getUid());
            }
        });
    }

}
