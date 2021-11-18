package com.christophedurand.go4lunch.ui.homeView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.christophedurand.go4lunch.R;
import com.christophedurand.go4lunch.ui.MainActivity;
import com.christophedurand.go4lunch.ui.settingsView.SettingsActivity;
import com.christophedurand.go4lunch.ui.ViewModelFactory;
import com.christophedurand.go4lunch.ui.detailsView.RestaurantDetailsActivity;
import com.christophedurand.go4lunch.utils.Utils;
import com.firebase.ui.auth.AuthUI;
import com.google.android.libraries.places.api.Places;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String apiKey = "AIzaSyD6FndQ_yMQLDPOYVzaeLt1rIuJ72Ntg_M";


    private View headerView;
    private DrawerLayout drawerLayout;
    public Toolbar toolbar;

    private TextView userNameTextView;
    private TextView userMailTextView;
    private ImageView userAvatarImageView;

    private String restaurantId;

    private HomeViewModel homeViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        configureToolBar();
        configureDrawerLayout();
        configureNavigationView();
        configureBottomNavigationView();

        Places.initialize(getApplication(), apiKey);

        configureViewModel();
        subscribe();
    }

    @Override
    protected void onResume() {
        super.onResume();
        configureCurrentUserProfileUI(headerView);
        homeViewModel.getUpdatedRestaurantId();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        homeViewModel.getHomeViewStateMediatorLiveData().removeObservers(this);
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        item.getItemId();
        return false;
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

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.nav_lunch:
                showMyLunch();
                break;
            case R.id.nav_settings:
                showSettingsActivity();
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
        headerView = navigationView.getHeaderView(0);
        configureCurrentUserProfileUI(headerView);
    }

    private void configureCurrentUserProfileUI(View headerView) {
        userNameTextView = headerView.findViewById(R.id.user_name_text_view);
        userMailTextView = headerView.findViewById(R.id.user_mail_text_view);
        userAvatarImageView = headerView.findViewById(R.id.user_avatar_image_view);
    }


    private void showMyLunch() {
        if (restaurantId != null && !restaurantId.equals("") ) {
            Intent intent = new Intent(this, RestaurantDetailsActivity.class);
            intent.putExtra("restaurantId", restaurantId);
            startActivity(intent);
        } else {
            Toast.makeText(this, getString(R.string.choose_restaurant_title), Toast.LENGTH_SHORT).show();
        }
    }

    private void showSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void signOut() {
        AuthUI.getInstance().signOut(this).addOnCompleteListener(task -> {
            startActivity(new Intent(HomeActivity.this, MainActivity.class));
            finish();
        });
    }


    private void configureViewModel() {
        ViewModelFactory viewModelFactory = new ViewModelFactory("");
        homeViewModel = new ViewModelProvider(this, viewModelFactory).get(HomeViewModel.class);
    }

    private void subscribe() {
        Drawable avatarPlaceHolder = Utils.getAvatarPlaceHolder(this);

        homeViewModel.getHomeViewStateMediatorLiveData().observe(this, homeViewState -> {
            if (homeViewState.getCurrentUser() != null && homeViewState.getCurrentUser().getRestaurant() != null) {
                String username = homeViewState.getCurrentUser().getName();
                userNameTextView.setText(username);

                String email = homeViewState.getCurrentUser().getEmail();
                userMailTextView.setText(email);

                if (homeViewState.getCurrentUser().getAvatarURL() != null) {
                    Glide.with(this)
                            .load(homeViewState.getCurrentUser().getAvatarURL())
                            .apply(RequestOptions.circleCropTransform())
                            .placeholder(avatarPlaceHolder)
                            .into(userAvatarImageView);
                }

                restaurantId = homeViewState.getChosenRestaurantId();
            }
        });
    }

}