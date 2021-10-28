package com.christophedurand.go4lunch.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.christophedurand.go4lunch.R;
import com.christophedurand.go4lunch.ui.workmatesView.Restaurant;
import com.christophedurand.go4lunch.ui.workmatesView.UserManager;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    public static int AUTH_REQUEST_CODE = 1;


    private final UserManager userManager = UserManager.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        configureSignIn();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AUTH_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                userManager.createCurrentUser(new Restaurant("", "", ""), new ArrayList<>(), false);
                startHomeActivity();
            }
        }
    }

    private void configureSignIn() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build(),
                new AuthUI.IdpConfig.TwitterBuilder().build()
        );

        FirebaseUser user = userManager.getCurrentUser();
        if (user != null) {
            startHomeActivity();
        } else {
            Intent signInIntent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .setTheme(R.style.SignInTheme)
                    .setLogo(R.drawable.ic_restaurant)
                    .build();
            startActivityForResult(signInIntent, AUTH_REQUEST_CODE);
        }

    }

    private void startHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

}