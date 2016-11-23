package edu.csumb.cst438.router;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import static edu.csumb.cst438.router.Application.userService;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserServices userService = new UserServices();
        Intent intent;

        //TODO uncomment this for deployment functionality
        /*
        if(userService.getUserId() == null){
            intent = new Intent(this, Login.class);
        } else {
            intent = new Intent(this, MainActivity.class);
        }*/

        intent = new Intent(this, DebugPage.class);
        startActivity(intent);
        finish();
    }
}
