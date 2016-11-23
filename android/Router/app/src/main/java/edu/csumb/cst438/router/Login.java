package edu.csumb.cst438.router;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private EditText usernameToLogin;
    private EditText passwordToLogin;
    private SignInButton signInButton;
    private GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView;
    private Connector connector;
    private UserServices userServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupVariables();
        //  Configure sign-in to request the user's ID, email address, and basic profile. ID and
        //  basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to GoogleSignIn.API and the options above.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(gso.getScopeArray());

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               signIn();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            Log.d(TAG, "signed in: success");
            // TODO: Send user to a different activity to enter `User` info.
            moveToMain();
        } else {
            Log.d(TAG, "signed in: failed");
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    // Whenever a login succeeds it moves to the main activity
    private void moveToMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void logIn(View view) {
        String username = usernameToLogin.getText().toString();
        String password = passwordToLogin.getText().toString();

        if (username.trim().length() > 0 && password.trim().length() > 0) {
            authenticateLogin(username, password, view);
        } else {
            Snackbar.make(view, "Please Enter Credentials!", Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    public void register(View view){
        Intent intent = new Intent(Login.this, Register.class);
        startActivity(intent);
    }

    public void authenticateLogin(final String username, final String password, View view) {
        this.connector = new Connector();
        this.userServices = new UserServices();
        User user = connector.checkLogin(username,password);
        if(user == null) {
            Log.d(TAG, "signed in: failed");
            Snackbar.make(view, "Sign In Failed", Snackbar.LENGTH_LONG)
                    .show();
        }
        else{
            userServices.createLocalUser(user.username, user.bio, "private", user.email, user.id, "METRIC");
            moveToMain();
        }
    }

    private void setupVariables() {
        usernameToLogin = (EditText) findViewById(R.id.usernameToLogin);
        passwordToLogin = (EditText) findViewById(R.id.passwordToLogin);
        signInButton = (SignInButton) findViewById(R.id.signInButton);
    }
}