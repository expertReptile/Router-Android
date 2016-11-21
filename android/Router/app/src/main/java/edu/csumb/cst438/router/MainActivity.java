package edu.csumb.cst438.router;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.widget.ArrayAdapter;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.net.URL;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private FloatingSearchView mSearchView;
    private LocationService loc;
    private RoutesServices routesServices;
    private Marker marker;

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

        loc = Application.locationService;
        routesServices = Application.routesService;
        Log.d("map", "finished onCreate");
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
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc.getLocation(), 16));

        Route route = RoutesServices.getAllLocalRoutes().get(0);
        Log.d("test", DrawingService.createLine(route).toString());
        googleMap.addPolyline(DrawingService.createLine(route));
        LatLng pos = loc.getLocation();
        marker = mMap.addMarker(new MarkerOptions().position(pos).title("Your Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 16));

        Polyline line = googleMap.addPolyline(new PolylineOptions()
        .add(new LatLng(51.5, -0.1), new LatLng(40.7, -74.0))
        .width(5)
        .color(Color.RED));
        updateLocation();
    }

    public void updateLocation() {
        if(marker != null) {
            marker.remove();
        }
        LatLng newPos = loc.getLocation();
        Log.d("update", "New Location: " + newPos.toString());
        CameraUpdate center = CameraUpdateFactory.newLatLngZoom(newPos, 15);
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


