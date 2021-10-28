package com.christophedurand.go4lunch.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.christophedurand.go4lunch.R;
import com.christophedurand.go4lunch.ui.workmatesView.UserManager;
import com.christophedurand.go4lunch.ui.detailsView.RestaurantDetailsActivity;
import com.christophedurand.go4lunch.utils.Utils;
import com.google.android.libraries.places.api.Places;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String apiKey = "AIzaSyD6FndQ_yMQLDPOYVzaeLt1rIuJ72Ntg_M";


    private View headerView;
    public Toolbar toolbar;
    private DrawerLayout drawerLayout;

    private String restaurantId;

    private final UserManager userManager = UserManager.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        configureToolBar();
        configureDrawerLayout();
        configureNavigationView();
        configureBottomNavigationView();

        Places.initialize(getApplication(), apiKey);

    }

    @Override
    protected void onResume() {
        super.onResume();

        setUserProfileUI(headerView);
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
        setUserProfileUI(headerView);
    }

    private void setUserProfileUI(View headerView) {
        TextView userNameTextView = headerView.findViewById(R.id.user_name_text_view);
        TextView userMailTextView = headerView.findViewById(R.id.user_mail_text_view);
        ImageView userAvatarImageView = headerView.findViewById(R.id.user_avatar_image_view);

        Drawable avatarPlaceHolder = Utils.getAvatarPlaceHolder(this);

        // Using handler to delay this method's call because callback from firebase is too slow
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() ->
                userManager.getCurrentUserData().addOnSuccessListener(user -> {
                    String username = user.getName();
                    userNameTextView.setText(username);

                    String email = user.getEmail();
                    userMailTextView.setText(email);

                    if (user.getAvatarURL() != null) {
                        Glide.with(this)
                                .load(user.getAvatarURL())
                                .apply(RequestOptions.circleCropTransform())
                                .placeholder(avatarPlaceHolder)
                                .into(userAvatarImageView);
                    }

                    restaurantId = user.getRestaurant().getId();
                }), 3000);
    }

    private void showMyLunch() {
        if (!restaurantId.equals("")) {
            Intent intent = new Intent(this, RestaurantDetailsActivity.class);
            intent.putExtra("restaurantId", restaurantId);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Veuillez choisir votre restaurant !", Toast.LENGTH_SHORT).show();
        }
    }

    private void showSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void signOut() {
        userManager.signOut(this).addOnCompleteListener(task -> {
            startActivity(new Intent(HomeActivity.this, MainActivity.class));
            finish();
        });
    }

}