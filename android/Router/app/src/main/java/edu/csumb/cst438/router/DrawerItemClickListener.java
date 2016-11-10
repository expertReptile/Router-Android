package edu.csumb.cst438.router;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * This class listens for clicks from the menu drawer and starts the activity the user clicked.
 *
 * Created by Pearce on 11/3/16.
 */

public class DrawerItemClickListener extends AppCompatActivity implements ListView.OnItemClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        switch(position) {
            case 0: Intent mainActivityIntent = new Intent(parent.getContext(), MainActivity.class);
                parent.getContext().startActivity(mainActivityIntent);
                break;
            case 1: Intent myRoutesIntent = new Intent(parent.getContext(), MyRoutes.class);
                parent.getContext().startActivity(myRoutesIntent);
                break;
            case 2: Intent friendsIntent = new Intent(parent.getContext(), Friends.class);
                parent.getContext().startActivity(friendsIntent);
                break;
            case 3: Intent profileIntent = new Intent(parent.getContext(), Profile.class);
                parent.getContext().startActivity(profileIntent);
                break;
            default:
        }
    }
}