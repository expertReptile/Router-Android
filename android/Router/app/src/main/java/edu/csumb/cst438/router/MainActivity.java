
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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ToggleButton;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;
import java.util.HashMap;

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
    private Polyline currentPath;
    private ArrayList<Marker> nearMe;
    private Connector connector = new Connector();
    private LatLng curPos;
    private HashMap<String, Route> routesNearMe;
    private OnMarkerClickListener markerListener;
    private float bearing = 0f;
    private float curZoomLevel = 15f;
    private float tilt = 45f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null) {
            this.isRecording = savedInstanceState.getBoolean("isRecording");
        }
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
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

    public void startRecording(View view) {
        if(!isRecording) {
            ToggleButton toggleButton = (ToggleButton) view;
            recordingService.putExtra("name", "TEMPORARY");
            recordingService.putExtra("StartLat", Double.toString(loc.getLocation().latitude));
            recordingService.putExtra("StartLon", Double.toString(loc.getLocation().longitude));
            toggleButton.setBackgroundResource(R.drawable.record_button_recording);
            this.startService(recordingService);
            isRecording = true;
        }
        else {
            ToggleButton toggleButton = (ToggleButton) view;
            toggleButton.setBackgroundResource(R.drawable.record_button_default_state);
            this.stopService(recordingService);
            Application.cont = false;
            isRecording = false;

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Name Your Route");

            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    routeName = input.getText().toString();
                    RoutesServices.updateRouteName(routeName, "TEMPORARY");
                    Log.d("saved", RoutesServices.getAllLocalRoutes().toString());
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    RoutesServices.deleteRoute("TEMPORARY");
                    for(Route route: RoutesServices.getAllLocalRoutes()) {
                        Log.d("DELETE", route.toString());
                    }
                }
            });

            builder.show();
        }
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        if(marker != null) {
            marker.remove();
        }
        curZoomLevel = 15f;
        savedInstanceState.putBoolean("isRecording", this.isRecording);
        curPos = loc.getLocation();
        marker = mMap.addMarker(new MarkerOptions().position(curPos).title("Your Location"));

        CameraUpdate center = CameraUpdateFactory.newCameraPosition(new CameraPosition(curPos, curZoomLevel, tilt,bearing));
        mMap.moveCamera(center);

        super.onSaveInstanceState(savedInstanceState);
    }

    public void removeNearMe() {
        if(nearMe != null) {
            for(Marker marker: nearMe) {
                marker.remove();
            }
        }
        if(routesNearMe != null) {
            routesNearMe.clear();
        }

        routesNearMe = null;
        nearMe = null;
        return;
    }

    public void getNearMe(View view) {
        removeNearMe();

        LatLng helper;

        nearMe = new ArrayList<>();
        routesNearMe = new HashMap<>();
        if(curPos != null) {
            ArrayList<Route> allTheRoute = connector.getNearMe(String.valueOf(curPos.latitude), String.valueOf(curPos.longitude), 10);
            if (allTheRoute.size() != 0) {
                for (Route route : allTheRoute) {
                    helper = new LatLng(Double.parseDouble(route.getStartPointLat()), Double.parseDouble(route.getStartPointLon()));
                    nearMe.add(mMap.addMarker(new MarkerOptions().position(helper).title(route.getRouteName())));
                    routesNearMe.put(route.getRouteName(), route);
                }
            }
        }

        return;
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
        mMap.getUiSettings().setMapToolbarEnabled(false);
        if(Application.currentRoute != null) {
            currentPath = mMap.addPolyline(DrawingService.createLine(Application.currentRoute));
        }

        markerListener = new OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String name = marker.getTitle();

                Log.d("marker", "Marker name: " + name);
                if(name.equals("Your Location") == false)
                    drawRoute(name);
                return false;
            }
        };


        ;
        curPos = loc.getLocation();
        marker = mMap.addMarker(new MarkerOptions().position(curPos).title("Your Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curPos, curZoomLevel));
        mMap.setOnMarkerClickListener(markerListener);
        CameraUpdate center = CameraUpdateFactory.newCameraPosition(new CameraPosition(curPos, curZoomLevel, tilt,bearing));
        mMap.moveCamera(center);

        updateLocation();
    }

    public void updateLocation() {
        if(marker != null) {
            marker.remove();
        }
        curZoomLevel = mMap.getCameraPosition().zoom;
        if(curZoomLevel < 9) {
            curZoomLevel = 17;
        }
        bearing = mMap.getCameraPosition().bearing;
        tilt = mMap.getCameraPosition().tilt;
        curPos = loc.getLocation();
        Log.d("update", "New Location: " + curPos.toString());
        CameraUpdate center = CameraUpdateFactory.newCameraPosition(new CameraPosition(curPos, curZoomLevel, tilt,bearing));
        mMap.animateCamera(center);
        Log.d("update", "Moved camera to " + center.toString());
        marker = mMap.addMarker(new MarkerOptions()
        .position(curPos)
        .alpha(0.8f)
        .anchor(0.0f, 1.0f)
        .title("Your Location"));
        new LocationChangedListener().execute(null, null);
    }

    public void updateDraw() {
        if(currentPath == null) {
            currentPath = mMap.addPolyline(DrawingService.createLine(Application.currentRoute));
        }
        else {
            currentPath.remove();
            currentPath = mMap.addPolyline(DrawingService.createLine(Application.currentRoute));
        }
    }

    public void drawRoute(String routeName) {
        if(routesNearMe != null) {
            Log.d("drawRoute", "starting");
            if (currentPath == null) {
                Log.d("drawRoute", routesNearMe.get(routeName).getRouteList().toString());
                currentPath = this.mMap.addPolyline(DrawingService.createLine(routesNearMe.get(routeName)));
                Log.d("drawRoute", currentPath.getPoints().toString());
            } else {
                Log.d("drawRoute", routesNearMe.get(routeName).getRouteList().toString());
                currentPath.remove();
                currentPath = this.mMap.addPolyline(DrawingService.createLine(routesNearMe.get(routeName)));
                Log.d("drawRoute", currentPath.getPoints().toString());
            }
            Log.d("drawRoute", "ending");
        }
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
            if(isRecording) {
                updateDraw();
            }
            updateLocation();
        }
    }
}