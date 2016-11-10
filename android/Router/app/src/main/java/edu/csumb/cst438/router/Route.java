package edu.csumb.cst438.router;

/**
 * Created by pico on 11/1/16.
 */

public class Route {

    private boolean isLocal = false;
    private int routeIdRemote;
    private String route;
    private String startPointLat;
    private String startPointLon;
    private int userId;
    private String routeName;

    public Route(boolean isLocal, int routeIdRemote, String route, String startPointLat, String startPointLon, int userId, String routeName) {
        this.isLocal = isLocal;
        this.route = route;
        this.routeIdRemote = routeIdRemote;
        this.startPointLat = startPointLat;
        this.startPointLon = startPointLon;
        this.userId = userId;
        this.routeName = routeName;
    }

    public String toString() {
        return String.format("isLocal: %s \nroute: %s\n routeIdRemote: %s\nstartPointLat: %s\nStartPointLon: %s\nUserId: %s\nRouteName: %s\n",
                isLocal, route, routeIdRemote, startPointLat, startPointLon, userId, routeName);
    }

    public boolean isLocal() {return isLocal;}

    public int getRouteIdRemote() {
        return routeIdRemote;
    }

    public String getRoute() {
        return route;
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
