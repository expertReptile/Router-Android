package edu.csumb.cst438.router;


import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Pearce on 11/3/16.
 */

public class SearchEngine {
    public static ArrayList<User> findFriends(String query, ArrayList<User> data) {
        String pattern = "(?i).*" + query + ".*";
        ArrayList<User> results = new ArrayList<>();

        for(User entry: data) {
            if(entry.username.matches(pattern)) {
                results.add(entry);
            }
        }
        Log.d("SearchEngine", "findFriends completed");
        return results;
    }

    public static ArrayList<Route> findRoutes(String query, ArrayList<Route> data) {
        String pattern = "(?i).*" + query + ".*";
        ArrayList<Route> results = new ArrayList<>();

        for(Route entry: data) {
            if(entry.getRouteName().matches(pattern)) {
                results.add(entry);
            }
        }
        Log.d("SearchEngine", "findRoutes completed");
        return results;
    }
}
