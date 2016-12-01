package edu.csumb.cst438.router;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by pico on 11/1/16.
 */

public class Route {

    private boolean isLocal = false;
    private int routeIdRemote = -1;
    private String route = "";
    private String startPointLat = "";
    private String startPointLon = "";
    private int userId = -1;
    private String routeName = "";
    private JSONArray jsonArray = new JSONArray();
    private float distance;
    private ArrayList<LatLng> latLngs = new ArrayList<>();
    private int MIN_DISTANCE = 10;

    public Route(boolean isLocal, int routeIdRemote, String route, String startPointLat, String startPointLon, int userId, String routeName) {
        this.isLocal = isLocal;
        this.route = route;
        this.routeIdRemote = routeIdRemote;
        this.startPointLat = startPointLat;
        this.startPointLon = startPointLon;
        this.userId = Integer.parseInt(UserServices.getUserId());
        this.routeName = routeName;
    }

    public Route() {

    }

    public double getDistance(LatLng left, LatLng right) {
        double latDist = Math.toRadians(right.latitude - left.latitude);
        double lonDist = Math.toRadians(right.longitude - left.longitude);
        double a = Math.sin(latDist / 2) * Math.sin(latDist / 2) +
                Math.cos(Math.toRadians(left.latitude)) * Math.cos(Math.toRadians(right.latitude))
                * Math.sin(lonDist / 2) * Math.sin(lonDist / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = 6371 * c;
        return d;
    }

    public void add(LatLng latLng) {
        if(latLng.latitude == 0.0 && latLng.longitude == 0.0) {
            return;
        }
        if(latLngs.size() > 0) {
            // check for if the distance is way too much
            if(getDistance(latLng, this.latLngs.get(latLngs.size() - 1)) > MIN_DISTANCE) {
                Log.d("route", "distance was too long!: " + Double.toString(getDistance(latLng, this.latLngs.get(latLngs.size() - 1))));
                return;
            }
        }
        try {
            Log.d("route", "here");
            Log.d("route", route);
            JSONObject temp = new JSONObject();
            temp.put("lat", latLng.latitude);
            temp.put("lon", latLng.longitude);
            jsonArray.put(temp);
            route = jsonArray.toString();
            latLngs.add(latLng);
        }
        catch (Exception e) {
            Log.d("route", e.toString());
        }
    }

    public String toString() {
        return String.format("isLocal: %s \nroute: %s\n routeIdRemote: %s\nstartPointLat: %s\nStartPointLon: %s\nUserId: %s\nRouteName: %s\n",
                isLocal, route, routeIdRemote, startPointLat, startPointLon, userId, routeName);
    }

    public void setDistance(float distance)
    {
        this.distance = distance;
    }

    public float getDistance() {return distance;}

    public void setLocal(boolean local) {
        this.isLocal = local;
    }

    public void setRouteIdRemote(int id) {
        this.routeIdRemote = id;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public void setStartPointLat(String lat) {
        this.startPointLat = lat;
    }

    public void setStartPointLon(String lon) {
        this.startPointLon = lon;
    }

    public void setUserId(int id) {
        this.userId = id;
    }

    public void setRouteName(String name) {
        this.routeName = name;
    }

    public boolean isLocal() {return isLocal;}

    public int getRouteIdRemote() {
        return routeIdRemote;
    }

    public String getRoute() {
        return route;
    }
    public ArrayList<LatLng> getRouteList() {
        try {
            jsonArray = new JSONArray(route.toString());
        }
        catch (Exception e) {
            Log.d("route", e.toString());
        }
        ArrayList<LatLng> locs = new ArrayList<>();
        try {
            for(int i = 0; i < jsonArray.length(); i++) {
                locs.add(new LatLng(Double.parseDouble(new JSONObject(jsonArray.get(i).toString()).get("lat").toString()),
                        Double.parseDouble(new JSONObject(jsonArray.get(i).toString()).get("lon").toString())));
                Log.d("locs", locs.toString());
            }
        }
        catch (Exception e) {
            Log.d("route", e.toString());
        }
        return locs;

    }

    public String getStartPointLat() {
        return startPointLat;
    }

    public String getStartPointLon() {
        return startPointLon;
    }

    public int getUserId() {
        return userId;
    }

    public String getRouteName() {
        return routeName;
    }

}
