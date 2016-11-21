package edu.csumb.cst438.router;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;

public class Profile extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private UserServices userServices;
    private Button mSaveButton;
    private EditText mUsername;
    private EditText mEmail;
    private EditText mBio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userServices = new UserServices();
        mSaveButton = (Button)findViewById(R.id.ProfileSaveButton);
        mUsername = (EditText)findViewById(R.id.UsernameEditText);
        mEmail = (EditText)findViewById(R.id.EmailTextEdit);
        mBio = (EditText)findViewById(R.id.ProfileBioEditText);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.profile_drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.profile_left_drawer);

        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.menu_string_array)));

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        getProfile();


    }

    public void getProfile() {
        userServices.CreateLocalUser("test user", "BIO", "PRIVATE", "email@rmail.jdsk", "321");

        EditText mUsername = (EditText) findViewById(R.id.UsernameEditText);
        mUsername.setHint(userServices.getUserName());

        EditText email = (EditText) findViewById(R.id.EmailTextEdit);
        email.setHint(userServices.getUserEmail());

        EditText bio = (EditText) findViewById(R.id.ProfileBioEditText);
        bio.setHint(userServices.getUserBio());

        Log.d("poop", userServices.getUserPrivacy());
        // todo: do button swapping here
        if(userServices.getUserPrivacy().equals("PUBLIC")) {
            findViewById(R.id.ProfilePrivacyButton_Private).setBackgroundColor(000000);
        } else {
            findViewById(R.id.ProfilePrivacyButton_Public).setBackgroundColor(000000);
        }

        // TODO: unit changes
        //userServices.getUnitPref();
    }

    public void saveProfile(View view) {

        if(!TextUtils.isEmpty(mBio.getText().toString())) {
            userServices.updateUserBio(mBio.getText().toString());
        }
        if(!TextUtils.isEmpty(mUsername.getText().toString())) {
            userServices.updateUserName(mUsername.getText().toString());
        }
        if(!TextUtils.isEmpty(mEmail.getText().toString())) {
            userServices.updateUserEmail(mEmail.getText().toString());
        }
    }

    public void logOut(View view) {
        userServices.deleteLocalUser();
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}
