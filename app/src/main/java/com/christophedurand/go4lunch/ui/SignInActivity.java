package com.christophedurand.go4lunch.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.christophedurand.go4lunch.R;
import com.christophedurand.go4lunch.model.User;
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


public class SignInActivity extends AppCompatActivity {

    public static final int FACEBOOK_LOGIN = 0;
    public static final int RC_SIGN_IN = 1;

    private ImageView go4LunchImageView;
    private TextView go4LunchTextView;
    private TextView descriptionTextView;
    private LoginButton facebookLoginButton;
    private SignInButton googleSignInButton;

    private CallbackManager callbackManager;
    public GoogleSignInClient mGoogleSignInClient;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_in);

        go4LunchImageView = findViewById(R.id.go4LunchImageView);
        go4LunchTextView = findViewById(R.id.go4LunchTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);

        callbackManager = CallbackManager.Factory.create();

//        AccessToken accessToken = AccessToken.getCurrentAccessToken();
//        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        facebookLoginButton = findViewById(R.id.facebook_login_button);
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

        googleSignInButton = findViewById(R.id.google_sign_in_button);
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

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        GoogleSignInAccount googleSignInAccount;

        if (requestCode == FACEBOOK_LOGIN) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            googleSignInAccount = handleSignInResult(task);
            user = new User(googleSignInAccount.getDisplayName(), googleSignInAccount.getEmail(), googleSignInAccount.getPhotoUrl() != null ? googleSignInAccount.getPhotoUrl().toString() : "");

        }

        Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }


    // -- GOOGLE METHODS
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private GoogleSignInAccount handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            return completedTask.getResult(ApiException.class);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Google Sign In", "signInResult: failed code= " + e.getStatusCode());
        }
        return null;
    }

}