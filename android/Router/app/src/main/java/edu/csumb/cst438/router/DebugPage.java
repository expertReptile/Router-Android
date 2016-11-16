package edu.csumb.cst438.router;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

public class DebugPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug_page);
        ActivityCompat.requestPermissions(this, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
                LocationService.MY_PERMISSION_ACCESS_COURSE_LOCATION );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_debug_page, menu);

        return true;
    }

    public void openRecordActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openMyRoutesActivity(View view) {
        Intent intent = new Intent(this, MyRoutes.class);
        startActivity(intent);
    }

    public void openFriendsActivity(View view) {
        Intent intent = new Intent(this, Friends.class);
        startActivity(intent);
    }

    public void openProfileActivity(View view) {
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }

    public void openLoginActivity(View view) {
        this.stopService(new Intent(this, RecordingService.class));
        //Intent intent = new Intent(this, Login.class);
        //startActivity(intent);
    }

    public void MartinTest(View view) {
        Intent intent = new Intent(this, RecordingService.class);
        intent.putExtra("name", "routeName");
        intent.putExtra("userId", "1");
        this.startService(intent);
    }
}
