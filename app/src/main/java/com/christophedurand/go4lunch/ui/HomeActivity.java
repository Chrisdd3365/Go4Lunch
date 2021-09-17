package com.christophedurand.go4lunch.ui;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.christophedurand.go4lunch.R;
import com.christophedurand.go4lunch.model.UserManager;
import com.christophedurand.go4lunch.ui.mapView.MapFragment;
import com.christophedurand.go4lunch.utils.Utils;
import com.google.android.libraries.places.api.Places;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;


@RuntimePermissions
public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String apiKey = "AIzaSyD6FndQ_yMQLDPOYVzaeLt1rIuJ72Ntg_M";

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;

    //private ActivityResultLauncher<Intent> activityResultLauncher;

    private final UserManager userManager = UserManager.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        configureToolBar();
        configureDrawerLayout();
        configureNavigationView();
        configureBottomNavigationView();

        HomeActivityPermissionsDispatcher.showCameraWithPermissionCheck(this);

        Places.initialize(getApplication(), apiKey);

//        activityResultLauncher = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(), result -> {
//
//                    if (result.getResultCode() == RESULT_OK) {
//                        Place place = Autocomplete.getPlaceFromIntent(result.getData());
//
//                    } else if (result.getResultCode() == AutocompleteActivity.RESULT_ERROR) {
//                        Status status = Autocomplete.getStatusFromIntent(result.getData());
//
//                    } else if (result.getResultCode() == RESULT_CANCELED) {
//                        // The user canceled the operation
//                    }
//
//                });

    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            drawerLayout.openDrawer(GravityCompat.START);
        }

        moveTaskToBack(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_lunch:
                break;
            case R.id.nav_settings:
                break;
            case R.id.nav_logout:
                signOut();
                break;
            default:
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
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


    private void configureToolBar() {
        toolbar = findViewById(R.id.activity_home_toolbar);
        toolbar.setTitle(R.string.hungry_title);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);
    }

    private void configureBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

    private void configureDrawerLayout() {
        drawerLayout = findViewById(R.id.activity_home_parent_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorWhite));
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void configureNavigationView() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        updateUIWithUserData(headerView);
    }

    private void updateUIWithUserData(View headerView) {
        setUserProfileUI(headerView);
    }

    private void setUserProfileUI(View headerView) {

        TextView userNameTextView = headerView.findViewById(R.id.user_name_text_view);
        TextView userMailTextView = headerView.findViewById(R.id.user_mail_text_view);
        ImageView userAvatarImageView = headerView.findViewById(R.id.user_avatar_image_view);

        Drawable avatarPlaceHolder = Utils.getAvatarPlaceHolder(this);

        userManager.getCurrentUserData().addOnSuccessListener(user -> {
            String username = TextUtils.isEmpty(user.getName()) ? "No User Name Found" : user.getName();
            userNameTextView.setText(username);

            String email = TextUtils.isEmpty(user.getEmail()) ? "No Email Found" : user.getEmail();
            userMailTextView.setText(email);

            if (user.getAvatarURL() != null) {
                Glide.with(this)
                        .load(user.getAvatarURL())
                        .apply(RequestOptions.circleCropTransform())
                        .placeholder(avatarPlaceHolder)
                        .into(userAvatarImageView);
            }
        });
    }

    private void signOut() {
        userManager.signOut(this).addOnCompleteListener(task -> {
            startActivity(new Intent(HomeActivity.this, MainActivity.class));
            finish();
        });
    }

}