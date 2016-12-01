package edu.csumb.cst438.router;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


/**
 * Created by lachavez on 10/27/16.
 */

public class Register extends AppCompatActivity{
    private static final String TAG = "RegisterActivity";

    private EditText emailToCreate;
    private EditText usernameToCreate;
    private EditText passwordToCreate;
    private EditText repasswordToCreate;
    private EditText bioToCreate;

    private Connector connector;
    private UserServices userServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setupVariables();
    }

    public void register(View view){
        String email = emailToCreate.getText().toString();
        String username = usernameToCreate.getText().toString();
        String bio = bioToCreate.getText().toString();
        String password = passwordToCreate.getText().toString();
        String passwordAgain = repasswordToCreate.getText().toString();

        if (email.trim().length() > 0 && username.trim().length() > 0 && password.trim().length() > 0 && passwordAgain.trim().length() >0)
            if(password.equals(passwordAgain))
                createUser(username, password, bio, email, view);
            else{
                Snackbar.make(view, "Passwords don't match", Snackbar.LENGTH_LONG)
                        .show();
            }
        else {
            Snackbar.make(view, "Please Enter Credentials!", Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    public void logIn(View view){
        Intent intent = new Intent(Register.this, Login.class);
        startActivity(intent);
    }

    public void createUser(final String username, final String password, final String bio, final String email, final View view) {
        this.connector = new Connector();
        this.userServices = new UserServices();
        int userId = connector.createUser(username, password, bio, email);
        Log.d(TAG, " createUser: " + Integer.toString(userId));
        if(userId == -1){
            Snackbar.make(view, "Can't create user", Snackbar.LENGTH_LONG)
                            .show();
        }
        else{
            userServices.CreateLocalUser(username, bio, "PRIVATE", email, Integer.toString(userId), "METRIC");
            moveToMain();
        }
    }

    private void moveToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void setupVariables() {
        emailToCreate = (EditText) findViewById(R.id.emailToCreate);
        usernameToCreate = (EditText) findViewById(R.id.usernameToCreate);
        bioToCreate = (EditText) findViewById(R.id.bioToCreate);
        passwordToCreate = (EditText) findViewById(R.id.passwordToCreate);
        repasswordToCreate = (EditText) findViewById(R.id.repasswordToCreate);
    }

}