package edu.csumb.cst438.router;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

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

    public RecordingService() {
        super("RecordingService");
        gps = new LocationService(this);
        routesServices = new RoutesServices();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // get data from intent
        //do work in here
        route.setRouteName("test");

        route.setUserId(Integer.parseInt(intent.getStringExtra("userId")));
        route.setRouteIdRemote(10);
        routesServices.insertRoute(route);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("recording", "starting recording");
                startRecording();
                Log.d("recording", "stopped recording");
                Log.d("recording", route.getRoute().toString());
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("recording", "starting gps");
                startGPSing();
                Log.d("recording", "stopped gpsing");
            }
        }).start();
    }

    @Override
    public void onDestroy() {
    }

    public void startRecording() {

        while(true) {
            Log.d("loop", "here1");
            synchronized (lock) {

                while(!gps.hasChanged())
                {
                    try {
                        Log.d("recording", "waiting");
                        lock.wait();
                        Log.d("recording", "waited");
                    }
                    catch (Exception e) {
                        Log.d("recording", e.toString());
                    }
                }
                route.add(gps.getLocation());
                routesServices.updateRouteRoute(route.getRoute(), route.getRouteIdRemote());
                Log.d("recording", "route: " + route);
            }
        }

    }

    public void startGPSing() {
        Log.d("recording", "starting gps!");

        while(true) {
            Log.d("loop", "here2");
            synchronized (lock) {

                while(!gps.hasChanged()) {
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
                }
                lock.notify();
                Log.d("recording", "notified");
            }
        }
    }
}
