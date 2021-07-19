package com.christophedurand.go4lunch.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.christophedurand.go4lunch.R;
import com.christophedurand.go4lunch.ui.mapView.MapFragment;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import java.util.Collections;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;


@RuntimePermissions
public class HomeActivity extends AppCompatActivity {

    public static final String apiKey = "AIzaSyD6FndQ_yMQLDPOYVzaeLt1rIuJ72Ntg_M";


    private ActivityResultLauncher<Intent> activityResultLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Places.initialize(getApplication(), apiKey);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        Button menuButton = findViewById(R.id.activity_home_menu_button);
        menuButton.setOnClickListener(v -> {

        });

        Button searchButton = findViewById(R.id.activity_home_search_button);
        searchButton.setOnClickListener(this::startAutocompleteActivity);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);

        HomeActivityPermissionsDispatcher.showCameraWithPermissionCheck(this);

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {

                    if (result.getResultCode() == RESULT_OK) {
                        Place place = Autocomplete.getPlaceFromIntent(result.getData());

                    } else if (result.getResultCode() == AutocompleteActivity.RESULT_ERROR) {
                        Status status = Autocomplete.getStatusFromIntent(result.getData());

                    } else if (result.getResultCode() == RESULT_CANCELED) {
                        // The user canceled the operation
                    }

                });
    }

    public void startAutocompleteActivity(View view) {
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY,
                Collections.singletonList(Place.Field.NAME))
                .build(this);
        activityResultLauncher.launch(intent);
    }

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    void showCamera() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.map, MapFragment.newInstance())
                .addToBackStack("location")
                .commitAllowingStateLoss();
    }

    @OnShowRationale(Manifest.permission.ACCESS_FINE_LOCATION)
    void showRationaleForCamera(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setMessage(R.string.permission_location_rationale)
                .setPositiveButton(R.string.button_allow, (dialog, button) -> request.proceed())
                .setNegativeButton(R.string.button_deny, (dialog, button) -> request.cancel())
                .show();
    }

    @OnPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)
    void showDeniedForCamera() {
        Toast.makeText(this, R.string.permission_location_denied, Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain(Manifest.permission.ACCESS_FINE_LOCATION)
    void showNeverAskForCamera() {
        Toast.makeText(this, R.string.permission_location_neverask, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        HomeActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

}