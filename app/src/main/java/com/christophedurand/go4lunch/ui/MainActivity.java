package com.christophedurand.go4lunch.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.christophedurand.go4lunch.R;
import com.christophedurand.go4lunch.model.UserManager;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    public static int AUTH_REQUEST_CODE = 1;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener listener;
    private List<AuthUI.IdpConfig> providers;
    private final UserManager userManager = UserManager.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        configureSignIn();
    }

    @Override
    protected void onStart() {
        super.onStart();

        firebaseAuth.addAuthStateListener(listener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (listener != null) {
            firebaseAuth.removeAuthStateListener(listener);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AUTH_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                userManager.createUser();
            }
        }

    }

    private void configureSignIn() {
        providers = Arrays.asList(
                    new AuthUI.IdpConfig.GoogleBuilder().build(),
                    new AuthUI.IdpConfig.FacebookBuilder().build(),
                    new AuthUI.IdpConfig.TwitterBuilder().build()
        );

        firebaseAuth = FirebaseAuth.getInstance();

        listener = firebaseAuth -> {
            FirebaseUser user = userManager.getCurrentUser();

            if (user != null) {
                startHomeActivity();
            } else {
                startActivityForResult(AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.SignInTheme)
                        .setLogo(R.drawable.ic_restaurant)
                        .build(), AUTH_REQUEST_CODE);
            }
        };

    }

    private void startHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

}