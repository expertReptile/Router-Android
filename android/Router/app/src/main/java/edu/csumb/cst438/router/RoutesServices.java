package edu.csumb.cst438.router;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;


/**
 * Created by pico on 11/8/16.
 */

public class RoutesServices extends Services{

    public RoutesServices() {
        super(Application.db);
    }

    public static void insertRoute(Route route) {
        Log.d("services", route.toString());

        final ContentValues values = new ContentValues();
        values.put(SQLiteHelper.Routes.COLLUMN_NAME_ROUTEID, route.getRouteIdRemote());
        values.put(SQLiteHelper.Routes.COLLUMN_NAME_ROUTE, route.getRoute());
        values.put(SQLiteHelper.Routes.COLLUMN_NAME_ROUTE_NAME, route.getRouteName());
        values.put(SQLiteHelper.Routes.COLLUMN_NAME_START_POINT_LAT, route.getStartPointLat());
        values.put(SQLiteHelper.Routes.COLLUMN_NAME_START_POINT_LON, route.getStartPointLon());
        values.put(SQLiteHelper.Routes.COLLUMN_NAME_USER_ID, route.getUserId());

        insert(SQLiteHelper.Routes.TABLE_NAME, values);

    }

    private static void insert(final String tableName, final ContentValues values) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.insert(tableName, "null", values);
            }
        }).start();
    }

    public static void deleteRoute(int id) {
        String query = String.format("DELETE FROM Routes WHERE RouteId = %s", Integer.toString(id));
        db.execSQL(query);
    }

    public static Route getRouteById(int id) {
        String query = String.format("SELECT * FROM Routes WHERE RouteId = %s", id);

        Cursor c = db.rawQuery(query, null);

        if(c.getCount() == 0) { return null; }

        c.moveToFirst();

        // _id, RouteId, Route, StartPointLat, StartPointLon, RouteName, UserId
        return new Route(false, c.getInt(1), c.getString(2), c.getString(3), c.getString(4), c.getInt(6), c.getString(5));
    }

    public static ArrayList<Route> getAllLocalRoutes() {

        ArrayList<Route> routes = new ArrayList<>();

        Cursor c = db.query("Routes", null, null, null, null, null, null);

        //c.moveToFirst();
        Log.d("RoutesServices", Integer.toString(c.getCount()));
        Log.d("RoutesServices", "about to add routes");
        while(c.moveToNext()) {
            Log.d("RoutesServices", "adding route");
            routes.add(new Route(false, c.getInt(1), c.getString(2), c.getString(3), c.getString(4), c.getInt(6), c.getString(5)));
        }

        Log.d("RoutesServices", routes.toString());
        return routes;
    }

    public static void update(final ContentValues values, final String where) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    db.update(SQLiteHelper.Routes.TABLE_NAME, values, where, null);
                }
                catch (Exception e) {
                    Log.d("update", e.toString());
                }
            }
        }).start();
    }

    public static void updateRouteRoute(String newRoute, int id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("Route", newRoute);
        String where = "RouteId=" + Integer.toString(id);
        Log.d("recording", "updated " + newRoute);
        update(contentValues, where);
    }

    public static void updateStartPointLat(String newLat, String name) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("StartPointLat", newLat);
        String where = "RouteName=" + name;
        update(contentValues, where);
    }

    public static void updateStartPointLon(String newLon, String name) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("StartPointLon", newLon);
        String where = "RouteName=" + name;
        update(contentValues, where);
    }

    public static void updateRouteName(String newName, String name) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("RouteName", newName);
        String where = "RouteName=" + name;
        update(contentValues, where);
    }

    public static void updateRouteUserId(String newId, String name) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("UserId", newId);
        String where = "RouteName=" + name;
        update(contentValues, where);
    }
}
