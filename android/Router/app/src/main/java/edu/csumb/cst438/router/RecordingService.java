package edu.csumb.cst438.router;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import static edu.csumb.cst438.router.Application.cont;

/**
 * Created by pico on 11/11/16.
 */

public class RecordingService extends IntentService {

    private LocationService gps;
    private Route route = new Route();
    private Object lock = new Object();
    public static String name = "Recording.Service";
    private RoutesServices routesServices;
    private static int SLEEP_TIME = 2000; // 2 seconds
    private Thread recording;
    private Thread checking;

    public RecordingService() {
        super("RecordingService");
        gps = new LocationService(this);
        routesServices = new RoutesServices();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // get data from intent
        //do work in here
        route.setRouteName(intent.getStringExtra("name"));
        route.setStartPointLat(intent.getStringExtra("StartLat"));
        route.setStartPointLon(intent.getStringExtra("StartLon"));
        routesServices.insertRoute(route);
        cont = true;

        recording = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("recording", "starting recording");
                startRecording();
                Log.d("recording", "stopped recording");
                Log.d("recording", route.getRoute().toString());
            }
        });

        checking = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("recording", "starting gps");
                startGPSing();
                Log.d("recording", "stopped gpsing");
            }
        });

        recording.start();
        checking.start();
    }

    @Override
    public void onDestroy() {
    }

    public void startRecording() {

        while(Application.cont) {
            Log.d("recording", "here1");
            synchronized (lock) {

                while(!gps.hasChanged())
                {
                    if(!Application.cont) {
                        Log.d("recording", "dying");
                        return;
                    }
                    try {
                        Log.d("recording", "waiting");
                        lock.wait();
                        Log.d("recording", "waited");
                    }
                    catch (Exception e) {
                        Log.d("recording", e.toString());
                    }
                    if(!Application.cont) {
                        return;
                    }
                }
                route.add(gps.getLocation());
                routesServices.updateRouteRoute(route.getRoute(), route.getRouteIdRemote());
                Application.currentRoute = route;
                Log.d("recording", "route: " + route);
            }
            Log.d("recording", "BLA4");
            Log.d("recording", Boolean.toString(Application.cont));
        }
        Log.d("recording", Boolean.toString(Application.cont));

    }

    public void startGPSing() {
        Log.d("recording", "starting gps!");

        while(Application.cont) {
            Log.d("recording", "here2");
            synchronized (lock) {

                while(!gps.hasChanged()) {
                    if(!Application.cont) {
                        Log.d("recording", "dying");
                        return;
                    }
                    try {
                        Log.d("recording", "sleeping");
                        Log.d("recording", "loc: " + gps.getLocation().toString());
                        Thread.sleep(SLEEP_TIME);
                        lock.notify();
                        Log.d("recording", "waking");
                    }
                    catch (Exception e) {
                        Log.d("recording", e.toString());
                    }
                    if(!Application.cont) {
                        return;
                    }
                }
                lock.notify();
                Log.d("recording", "notified");
            }
            Log.d("recording", "BLA3");
            Log.d("recording", Boolean.toString(Application.cont));
        }
        Log.d("recording", Boolean.toString(Application.cont));
    }
}
