package com.christophedurand.go4lunch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;
import java.util.Objects;

public class SignInActivity extends AppCompatActivity {

    // -- PROPERTIES
    // STATICS
    // FACEBOOK LOGIN REQUEST CODE
    public static final int FACEBOOK_LOGIN = 0;
    // GOOGLE SIGN IN REQUEST CODE
    public static final int RC_SIGN_IN = 1;

    // MODEL
    private CallbackManager callbackManager;
    public GoogleSignInClient mGoogleSignInClient;

    // UI
    private ImageView go4LunchImageView;
    private TextView go4LunchTextView;
    private TextView descriptionTextView;
    private LoginButton facebookLoginButton;
    private SignInButton googleSignInButton;


    // -- VIEW LIFE CYCLE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); // will hide the title
        Objects.requireNonNull(getSupportActionBar()).hide(); // will hide the title bar

        // INIT LAYOUT
        setContentView(R.layout.sign_in_activity);

        // FACEBOOK INIT CALLBACK MANAGER
        callbackManager = CallbackManager.Factory.create();

//        AccessToken accessToken = AccessToken.getCurrentAccessToken();
//        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        // GOOGLE SIGN IN OPTIONS INIT
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // INIT UI
        go4LunchImageView = findViewById(R.id.go4LunchImageView);
        go4LunchTextView = findViewById(R.id.go4LunchTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);

        // FACEBOOK LOGIN BUTTON
        facebookLoginButton = findViewById(R.id.facebook_login_button);
        // FACEBOOK LOGIN BUTTON REGISTER CALLBACK
        facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("LOGIN FACEBOOK", "LOGIN IS SUCCESSFUL");
            }

            @Override
            public void onCancel() {
                Log.d("LOGIN FACEBOOK", "LOGIN CANCELLED");
            }

            @Override
            public void onError(FacebookException e) {
                Log.d("LOGIN FACEBOOK", "LOGIN ERROR");
            }
        });

        // GOOGLE SIGN IN BUTTON
        googleSignInButton = findViewById(R.id.google_sign_in_button);
        // GOOGLE SIGN IN BUTTON SET ON CLICK LISTENER
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        // Set the dimensions of the sign-in button.
        googleSignInButton.setSize(SignInButton.SIZE_STANDARD);

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // FACEBOOK
        if (requestCode == FACEBOOK_LOGIN)
            callbackManager.onActivityResult(requestCode, resultCode, data);

        // GOOGLE
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    // -- GOOGLE METHODS
    private void updateUI(GoogleSignInAccount account) {

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

//            assert account != null;
//            account.getDisplayName();
//            account.getGivenName(); // first name of user
//            account.getFamilyName(); // last name of user
//            account.getEmail(); // email of user

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Sign In Result", "signInResult: failed code= " + e.getStatusCode());
            updateUI(null);
        }
    }

}