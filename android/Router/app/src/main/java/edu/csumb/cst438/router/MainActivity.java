package edu.csumb.cst438.router;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private FloatingSearchView mSearchView;
    private LocationService loc;
    private RoutesServices routesServices;
    private Marker marker;
    private boolean isRecording = false;
    private Intent recordingService;
    private String routeName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.main_left_drawer);
        mSearchView = (FloatingSearchView) findViewById(R.id.floating_search_view);

        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.menu_string_array)));

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mSearchView.attachNavigationDrawerToMenuButton(mDrawerLayout);

        loc = new LocationService(this);
        routesServices = Application.routesService;
        recordingService = new Intent(this, RecordingService.class);
        Log.d("map", "finished onCreate");
    }

    public void startRecording() {

        if(!isRecording) {
            recordingService.putExtra("name", "TEMPORARY");
            recordingService.putExtra("StartLat", Double.toString(loc.getLocation().latitude));
            recordingService.putExtra("StartLon", Double.toString(loc.getLocation().longitude));
            this.startService(recordingService);
            isRecording = true;
        }
        else {
            this.stopService(recordingService);
            isRecording = false;

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    routeName = input.getText().toString();
                }
            });

            builder.show();

            RoutesServices.updateRouteName(routeName, "TEMPORARY");
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("map", "onMapReady start");
        mMap = googleMap;

        // Add a marker in monterey and move the camera
        marker = mMap.addMarker(new MarkerOptions().position(loc.getLocation()).title("You"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc.getLocation(), 16));

        updateLocation();
    }

    public void updateLocation() {
        if(marker != null) {
            marker.remove();
        }
        LatLng newPos = loc.getLocation();
        Log.d("update", "New Location: " + newPos.toString());
        CameraUpdate center = CameraUpdateFactory.newLatLngZoom(newPos, 20);
        mMap.moveCamera(center);
        Log.d("update", "Moved camera to " + center.toString());
        marker = mMap.addMarker(new MarkerOptions()
        .position(newPos)
        .alpha(0.8f)
        .anchor(0.0f, 1.0f)
        .title("Your Location"));
        new LocationChangedListener().execute(null, null);
    }


    private class LocationChangedListener extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            Log.d("update", "Starting new thread");
            while(!loc.hasChanged()) {
                //Log.d("update", loc.getLocation().toString());
                Log.d("update", "waiting");
                try {
                    Thread.sleep(1000);
                }
                catch (Exception e) {
                    Log.d("thread", e.toString());
                }
            }
            return null;
        }

        protected void onPostExecute(Void params) {
            Log.d("post", "Location Changed");
            updateLocation();
        }
    }
}


