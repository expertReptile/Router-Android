package edu.csumb.cst438.router;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;


/**
 * Created by pico on 11/8/16.
 */

public class RoutesServices extends Services{

    public RoutesServices(SQLiteDatabase db) {
        super(db);
    }

    public void insertRoute(Route route) {

        final ContentValues values = new ContentValues();
        values.put(SQLiteHelper.Routes.COLLUMN_NAME_ROUTEID, route.getRouteIdRemote());
        values.put(SQLiteHelper.Routes.COLLUMN_NAME_ROUTE, route.getRoute());
        values.put(SQLiteHelper.Routes.COLLUMN_NAME_ROUTE_NAME, route.getRouteName());
        values.put(SQLiteHelper.Routes.COLLUMN_NAME_START_POINT_LAT, route.getStartPointLat());
        values.put(SQLiteHelper.Routes.COLLUMN_NAME_START_POINT_LON, route.getStartPointLon());
        values.put(SQLiteHelper.Routes.COLLUMN_NAME_USER_ID, route.getUserId());

        insert(SQLiteHelper.Routes.TABLE_NAME, values);

    }

    private void insert(final String tableName, final ContentValues values) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.insert(tableName, "null", values);
            }
        }).start();
    }

    public void deleteRoute(int id) {
        String query = String.format("DELETE FROM Routes WHERE RouteId = %s", Integer.toString(id));
        db.execSQL(query);
    }

    public Route getRouteById(int id) {
        String query = String.format("SELECT * FROM Routes WHERE RouteId = %s", id);

        Cursor c = db.rawQuery(query, null);

        if(c.getCount() == 0) { return null; }

        c.moveToFirst();

        // _id, RouteId, Route, StartPointLat, StartPointLon, RouteName, UserId
        return new Route(false, c.getInt(1), c.getString(2), c.getString(3), c.getString(4), c.getInt(6), c.getString(5));
    }

    public ArrayList<Route> getAllLocalRoutes() {
        String query = "SELECT * FROM Routes WHERE 1";

        ArrayList<Route> routes = new ArrayList<>();

        Cursor c = db.rawQuery(query, null);

        c.moveToFirst();
        while(c.moveToNext()) {
            routes.add(new Route(false, c.getInt(1), c.getString(2), c.getString(3), c.getString(4), c.getInt(6), c.getString(5)));
        }

        return routes;
    }

    public void update(final String query) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.rawQuery(query, null);
            }
        }).start();
    }

    public void UpdateRouteRoute(String newRoute, int id) {
        String query = String.format("UPDATE Routes SET Route = '%s' WHERE RouteId = %d", newRoute, id );
        update(query);
    }

    public void updateStartPointLat(String newLat, int id) {
        String query = String.format("UPDATE Routes SET StartPointLat = '%s' WHERE RouteId = %d", newLat , id );
        update(query);
    }

    public void updateStartPointLon(String newLon, int id) {
        String query = String.format("UPDATE Routes SET StartPointLon = '%s' WHERE RouteId = %d", newLon, id );
        update(query);
    }

    public void updateRouteName(String newName, int id) {
        String query = String.format("UPDATE Routes SET RouteName = '%s' WHERE RouteId = %d", newName, id );
        update(query);
    }

    public void updateRouteUserId(String newId, int id) {
        String query = String.format("UPDATE Routes SET UserId = '%s' WHERE RouteId = %d", newId, id );
        update(query);
    }
}
