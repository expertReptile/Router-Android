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

    public Route(boolean isLocal, int routeIdRemote, String route, String startPointLat, String startPointLon, int userId, String routeName) {
        this.isLocal = isLocal;
        this.route = route;
        this.routeIdRemote = routeIdRemote;
        this.startPointLat = startPointLat;
        this.startPointLon = startPointLon;
        this.userId = userId;
        this.routeName = routeName;
    }

    public Route() {

    }

    public void add(LatLng latLng) {
        if(latLng.latitude == 0.0 && latLng.longitude == 0.0) {
            return;
        }
        try {
            Log.d("route", "here");
            Log.d("route", route);
            JSONObject temp = new JSONObject();
            temp.put("lat", latLng.latitude);
            temp.put("lon", latLng.longitude);
            jsonArray.put(temp);
            route = jsonArray.toString();
        }
        catch (Exception e) {
            Log.d("route", e.toString());
        }
    }

    public String toString() {
        return String.format("isLocal: %s \nroute: %s\n routeIdRemote: %s\nstartPointLat: %s\nStartPointLon: %s\nUserId: %s\nRouteName: %s\n",
                isLocal, route, routeIdRemote, startPointLat, startPointLon, userId, routeName);
    }

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
