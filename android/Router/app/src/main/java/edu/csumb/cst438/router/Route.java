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
    private double MIN_DISTANCE = 0.06;

    public Route(boolean isLocal, int routeIdRemote, String route, String startPointLat, String startPointLon, int userId, String routeName) {
        this.isLocal = isLocal;
        this.route = route;
        this.routeIdRemote = routeIdRemote;
        this.startPointLat = startPointLat;
        this.startPointLon = startPointLon;
        this.userId = Integer.parseInt(UserServices.getUserId());
        this.routeName = routeName;
        Log.d("Route", "Route constructor completed");
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
        Log.d("Route", "getDistance completed");
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
        Log.d("Route", "add completed");
    }

    public String toString() {
        Log.d("Route", "toString completed");
        return String.format("isLocal: %s \nroute: %s\n routeIdRemote: %s\nstartPointLat: %s\nStartPointLon: %s\nUserId: %s\nRouteName: %s\n",
                isLocal, route, routeIdRemote, startPointLat, startPointLon, userId, routeName);
    }

    public void setDistance(float distance) {
        this.distance = distance;
        Log.d("Route", "setDistance completed");
    }

    public float getDistance() {
        Log.d("Route", "getDistance completed");
        return distance;
    }

    public void setLocal(boolean local) {
        this.isLocal = local;
        Log.d("Route", "setLocal completed");
    }

    public void setRouteIdRemote(int id) {
        this.routeIdRemote = id;
        Log.d("Route", "setRouteIdRemote completed");
    }

    public void setRoute(String route) {
        this.route = route;
        Log.d("Route", "setRoute completed");
    }

    public void setStartPointLat(String lat) {
        this.startPointLat = lat;
        Log.d("Route", "setStartPointLat completed");
    }

    public void setStartPointLon(String lon) {
        this.startPointLon = lon;
        Log.d("Route", "setStartPointLon completed");
    }

    public void setUserId(int id) {
        this.userId = id;
        Log.d("Route", "setUserId completed");
    }

    public void setRouteName(String name) {
        this.routeName = name;
        Log.d("Route", "setRouteName completed");
    }

    public boolean isLocal() {
        Log.d("Route", "isLocal completed");
        return isLocal;
    }

    public int getRouteIdRemote() {
        Log.d("Route", "getRouteIdRemote completed");
        return routeIdRemote;
    }

    public ArrayList<LatLng> getNormalizedRoute() {
        Log.d("Route", "getNormalizedRoute completed");
        return normalize(getRouteList());
    }

    public String getRoute() {
        Log.d("Route", "getRoute completed");
        return route;
    }

    public ArrayList<LatLng> getRouteList() {
        try {
            Log.d("LIST", "BEFORE " + route.toString());
            jsonArray = new JSONArray(route);
            Log.d("LIST", "AFTER" + jsonArray.toString());
        }
        catch (Exception e) {
            String temp = route.replace("[", "");
            temp = temp.replace("]", "");

            temp = temp.replaceAll("\\}\\,\\{", "} {");
            temp = temp.replaceAll("\\\\", "");
            temp = temp.substring(1, temp.length() - 2);
            Log.d("LIST", temp);
            try {
                for(String item: temp.split(" ")) {
                    JSONObject object = new JSONObject(item);
                    jsonArray.put(object);
                }
            }
            catch (Exception e2) {
                Log.d("ERROR", e.toString());
            }

            Log.d("ERROR", e.toString());
            Log.d("ERROR", route.toString());
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
        Log.d("Route", "getRouteList completed");
        return locs;
    }

    public ArrayList<LatLng> normalize(ArrayList<LatLng> locs) {
        ArrayList<LatLng>  norms = new ArrayList<>();
        for(int i = 0; i < locs.size() - 1; i++) {
            Log.d("DIST", Double.toString(getDistance(locs.get(i), locs.get(i+1))));
            if(getDistance(locs.get(i), locs.get(i+1)) < MIN_DISTANCE) {
                norms.add(locs.get(i));
            }
        }
        Log.d("Route", "normalize completed");
        return norms;
    }

    public String getStartPointLat() {
        Log.d("Route", "setStartPointLat completed");
        return startPointLat;
    }

    public String getStartPointLon() {
        Log.d("Route", "setStartPointLon completed");
        return startPointLon;
    }

    public int getUserId() {
        Log.d("Route", "getUserId completed");
        return userId;
    }

    public String getRouteName() {
        Log.d("Route", "getRouteName completed");
        return routeName;
    }
}