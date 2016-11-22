package edu.csumb.cst438.router;


import java.util.ArrayList;

/**
 * Created by Pearce on 11/3/16.
 */

public class SearchEngine {
    public static ArrayList<Route> findFriends(String query) {
        //TODO: implement search for friends functionality (may need to change format)

        String pattern = ".*" + query + ".*";
        ArrayList<Route> results = new ArrayList<>();
        return results;
    }

    public static ArrayList<Route> findRoutes(String query, ArrayList<Route> data) {
        //TODO: implement search for routes functionality (may need to change format)
        String pattern = ".*" + query + ".*";
        ArrayList<Route> results = new ArrayList<>();
        int ctr = 0;

        for(Route entry: data) {
            if(entry.getRouteName().matches(pattern)) {
                results.add(entry);
            }
        }

        return results;
    }
}
