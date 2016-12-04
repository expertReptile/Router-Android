package edu.csumb.cst438.router;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private EditText usernameToLogin;
    private EditText passwordToLogin;
    private SignInButton signInButton;
    private GoogleApiClient mGoogleApiClient;
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
        ActivityCompat.requestPermissions(this, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
                LocationService.MY_PERMISSION_ACCESS_COURSE_LOCATION );
        Log.d("Login", "onCreate completed");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        Log.d("Login", "onActivityResult completed");
    }

    private void handleSignInResult(GoogleSignInResult result) {
        this.connector = new Connector();
        this.userServices = Application.userService;
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            Log.d(TAG, "signed in: success");
            GoogleSignInAccount acct = result.getSignInAccount();
            String username = acct.getEmail().substring(0,acct.getEmail().indexOf('@'));
            int userId = connector.createUser(username,acct.getEmail(), " ", acct.getEmail());
            if(userId == -1){
                User user = connector.checkLogin(username, acct.getEmail());
                userServices.CreateLocalUser(user.username, user.bio, user.privacy, user.email, user.id, "METRIC");
            }
            else{
                userServices.CreateLocalUser(username, " ", "PRIVATE", acct.getEmail(), Integer.toString(userId), "METRIC");
            }
            moveToMain();
        } else {
            Log.d(TAG, "signed in: failed");
        }
        Log.d("Login", "handleSignInResult completed");
    }

    private void signIn() {
        if(mGoogleApiClient != null && mGoogleApiClient.isConnected()){
            signOut();
        }
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        Log.d("Login", "signIn completed");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Log.d("Login", "onConnectionFailed completed");
    }

    // Whenever a login succeeds it moves to the main activity
    private void moveToMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        Log.d("Login", "moveToMain completed");
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
        Log.d("Login", "logIn completed");
    }

    public void register(View view){
        Intent intent = new Intent(Login.this, Register.class);
        startActivity(intent);
        Log.d("Login", "register completed");
    }

    private void authenticateLogin(final String username, final String password, View view) {
        this.connector = new Connector();
        this.userServices = Application.userService;
        User user = connector.checkLogin(username,password);
        if(user == null) {
            Log.d(TAG, "signed in: failed");
            Snackbar.make(view, "Sign In Failed", Snackbar.LENGTH_LONG)
                    .show();
        }
        else{
            userServices.CreateLocalUser(user.username, user.bio, "private", user.email, user.id, "METRIC");
            moveToMain();
        }
        Log.d("Login", "authenticateLogin completed");
    }

    private void setupVariables() {
        usernameToLogin = (EditText) findViewById(R.id.usernameToLogin);
        passwordToLogin = (EditText) findViewById(R.id.passwordToLogin);
        signInButton = (SignInButton) findViewById(R.id.signInButton);
        Log.d("Login", "setupVariables completed");
    }

    private void signOut() {
        Log.d(TAG, "Google signed out");
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Log.d(TAG, "status: " + status.toString());
                    }
                });
        Log.d("Login", "signOut completed");
    }
}