package edu.csumb.cst438.router;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


/**
 * Created by lachavez on 10/27/16.
 */

public class Register extends AppCompatActivity{
    private static final String TAG = "RegisterActivity";

    private EditText email_to_create;
    private EditText username_to_create;
    private EditText password_to_create;
    private EditText passwordAgain_to_create;
    private EditText bio_to_create;
    private Button register_button;
    private Button login_button;

    private Connector connector = new Connector();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setupVariables();

        // If they already have an account they can choose to login
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });

        register_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String email = email_to_create.getText().toString();
                String username = username_to_create.getText().toString();
                String bio = bio_to_create.getText().toString();
                String password = password_to_create.getText().toString();
                String passwordAgain = passwordAgain_to_create.getText().toString();

                if (email.trim().length() > 0 && username.trim().length() > 0 && password.trim().length() > 0 && passwordAgain.trim().length() >0)
                    createUser(username, password, bio, email);
                else {
                    Snackbar.make(v, "Please Enter Credentials!", Snackbar.LENGTH_LONG)
                            .show();
                }
            }
        });

    }

    public void createUser(final String username, final String password, final String bio, final String email) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int userId = connector.createUser(username, password, bio, email);
                Log.d(TAG, " createUser: " + Integer.toString(userId));
            }
        });
    }

    private void setupVariables() {
        email_to_create = (EditText) findViewById(R.id.email_to_create);
        username_to_create = (EditText) findViewById(R.id.username_to_create);
        bio_to_create = (EditText) findViewById(R.id.bio_to_create);
        password_to_create = (EditText) findViewById(R.id.password_to_create);
        passwordAgain_to_create = (EditText) findViewById(R.id.passwordAgain_to_create);
        register_button = (Button) findViewById(R.id.register_button);
        login_button = (Button) findViewById(R.id.login_button);
    }

}