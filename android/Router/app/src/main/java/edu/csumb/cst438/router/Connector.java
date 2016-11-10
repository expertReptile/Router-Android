package edu.csumb.cst438.router;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by pico on 10/15/16.
 */

public class Connector {

    URL url;
    HttpURLConnection conn;
    static String AWS = "http://router-dev.us-west-2.elasticbeanstalk.com";
    static String checkLogin = "/checkLogin/";
    static String downloadRoute = "/downloadRoute/";
    static String uploadRoute = "/uploadRoute/";
    static String createUser = "/createUser/";
    static String getNearMe = "/getNearMe/";
    static ExecutorService executorService = Executors.newSingleThreadExecutor();

    Object result = null;


    public Connector() {

        try {
            conn = buildConnection("");
        }
        catch (Exception e) {
            Log.e("error", e.toString());
        }
    }


    public HashMap<String, String> downloadRoute(final int routeId) {

        Callable<HashMap<String, String>> callable = new Callable<HashMap<String, String>>() {
            @Override
            public HashMap<String, String> call() throws Exception {
                return downloadRoute_internal(routeId);
            }
        };
        Future<HashMap<String, String>> future = executorService.submit(callable);
        executorService.shutdown();
        try {
            return future.get();
        }
        catch (Exception e) {
            Log.e("error", e.toString());
        }
        return null;
    }

    public int createUser(final String username, final String password, final String bio, final String email) {

        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return createUser_internal(username, password, bio, email);
            }
        };
        Future<Integer> future = executorService.submit(callable);
        executorService.shutdown();
        try {
            return future.get();
        }
        catch (Exception e) {
            Log.e("error", e.toString());
        }
        return -1;
    }

    public int uploadRoute(final int userId, final String lat, final String lon, final String name, final String path) {

        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return uploadRoute_intenal(userId, lat, lon, name, path);
            }
        };
        Future<Integer> future = executorService.submit(callable);
        executorService.shutdown();
        try {
            return future.get();
        }
        catch (Exception e) {
            Log.e("error", e.toString());
        }
        return -1;
    }

    public ArrayList<HashMap<String, String>> getNearMe(final String lat, final String lon, final double distance) {

        Callable<ArrayList<HashMap<String, String>>> callable = new Callable<ArrayList<HashMap<String, String>>>() {
            @Override
            public ArrayList<HashMap<String, String>> call() throws Exception {
                return getNearMe_internal(lat, lon, distance);
            }
        };

        Future<ArrayList<HashMap<String, String>>> future = executorService.submit(callable);
        executorService.shutdown();
        try {
            return future.get();
        }
        catch (Exception e) {
            Log.e("error", e.toString());
        }
        return null;
    }

    public boolean checkLogin(final String username, final String password) {

        Callable<Boolean> callable = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return checkLogin_internal(username, password);
            }
        };
        Future<Boolean> future = executorService.submit(callable);
        executorService.shutdown();
        try {
            return future.get();
        }
        catch (Exception e) {
            Log.e("error", e.toString());
        }
        return false;

    }

    private HttpURLConnection buildConnection(String endpoint) {
        try {
            url = new URL(AWS + endpoint);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
        }
        catch (Exception e) {
            Log.e("error", e.toString());
        }

        return conn;
    }

    private String getResponse(String json, String endpoint) {
        try {
            conn = buildConnection(endpoint);
            OutputStream os = conn.getOutputStream();
            os.write(json.getBytes());
            os.flush();

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed to connect: Code " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = "";
            String temp = "";
            while ((temp = br.readLine()) != null)
            {
                response += temp;
            }
                // will keep appending the json response to response var

            conn.disconnect();
            return response;
        } catch (Exception e) {
            Log.e("error", e.toString());
        }
        return "";
    }

    public static String sha1(String item) {
        byte[] b = item.getBytes();
        String result = "";
        for(int i = 0; i < item.length(); i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }


    private int uploadRoute_intenal(int userId, String lat, String lon, String name, String path) {
        HashMap<String, String> result = new HashMap<String, String>();
        String json = "";

        try {
            json = (new JSONObject()
                    .put("userId", Integer.toString(userId))
                    .put("route", (new JSONObject()
                                .put("name", name)
                                .put("path", path)
                                .put("startingPoint", new JSONObject()
                                                    .put("lat", lat)
                                                    .put("lon", lon))))).toString();
        }
        catch (Exception e) {
            Log.e("error", e.toString());
        }

        try {
            JSONObject response = new JSONObject(getResponse(json.toString(), uploadRoute));
            Iterator<?> keys = response.keys();

            while(keys.hasNext()) {
                String key = keys.next().toString();
                result.put(key, response.get(key).toString());
            }
        }
        catch (Exception e) {
            Log.e("error", e.toString());
        }
        try {
            return Integer.parseInt(result.get("idroutes"));
        }
        catch (Exception e) {
            return -1;
        }

    }


    private ArrayList<HashMap<String, String>> getNearMe_internal(String lat, String lon, double distance) {
        ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
        String json = "";

        try {
            json = (new JSONObject()
                    .put("userLat", lat.toString())
                    .put("userLon", lon.toString())
                    .put("dist", Double.toString(distance))).toString();
        }
        catch (Exception e) {
            Log.e("error", e.toString());
        }

        try {
            JSONArray response = new JSONArray(getResponse(json.toString(), getNearMe));

            for(int i = 0; i < response.length(); i++) {
                HashMap<String, String> temp = new HashMap<>();
                JSONObject item = (JSONObject)response.get(i);
                Iterator<?> keys = item.keys();

                while(keys.hasNext()) {
                    String key = keys.next().toString();
                    temp.put(key, item.get(key).toString());
                }
                result.add(temp);
            }
        }
        catch (Exception e) {
            Log.e("error", e.toString());
        }

        return result;
    }

    private HashMap<String, String> downloadRoute_internal(int routeId) {
        HashMap<String, String> result = new HashMap<>();
        String json = "";
        try {
            json = (new JSONObject()
                    .put("routeId", Integer.toString(routeId))).toString();
        }
        catch (Exception e) {
            Log.e("error", e.toString());
        }

        try {
            JSONObject response = new JSONObject(getResponse(json.toString(), downloadRoute));
            Iterator<?> keys = response.keys();

            while(keys.hasNext()) {
                String key = keys.next().toString();
                result.put(key, response.get(key).toString());
            }
        }
        catch (Exception e) {
            Log.e("error", e.toString());
        }

        return result;
    }

    private int createUser_internal(String username, String password, String bio, String email) {
        password = sha1(password);
        String json = "";
        try {
            json = (new JSONObject()
                    .put("username", username)
                    .put("password", password)
                    .put("bio", bio)
                    .put("email", email)).toString();
        }
        catch (Exception e) {
            Log.d("error", e.toString());
        }

        try {
            JSONObject response = new JSONObject(getResponse(json.toString(), createUser));
            return Integer.parseInt((response.get("userId").toString()));
        }
        catch (Exception e) {
            Log.e("error", e.toString());
        }
        return -1;
    }

    private Boolean checkLogin_internal(String username, String password) {
        password = sha1(password);
        String json = "";
        try {
            json = (new JSONObject()
                    .put("username", username)
                    .put("password", password)).toString();
        }
        catch (Exception e) {
            Log.d("error", e.toString());
        }

        try {
            JSONObject response = new JSONObject(getResponse(json.toString(), checkLogin));
            if(response.get("status").toString().equals("success")) {
                return true;
            }
            return false;
        }
        catch (Exception e) {
            Log.e("error", e.toString());
        }
        return false;

    }
}
