package edu.csumb.cst438.router;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class Profile extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private UserServices userServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.profile_drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.profile_left_drawer);

        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.menu_string_array)));

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        getProfile();
    }

    public void getProfile() {
        this.userServices = new UserServices();

        EditText username = (EditText) findViewById(R.id.UsernameEditText);
        username.setHint(userServices.getUserName());

        EditText email = (EditText) findViewById(R.id.EmailTextEdit);
        email.setHint(userServices.getUserEmail());

        EditText bio = (EditText) findViewById(R.id.ProfileBioEditText);
        bio.setHint(userServices.getUserBio());


        // todo: do button swapping here
        /*
        if(userServices.getUserPrivacy().equals("PUBLIC")) {

        } else {

        }*/

        // TODO: unit changes
        //userServices.getUnitPref();
    }
}
