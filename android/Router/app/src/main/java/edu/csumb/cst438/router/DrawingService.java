package edu.csumb.cst438.router;

import android.graphics.Color;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Created by pico on 11/20/16.
 */

public class DrawingService {

    private RoutesServices routesServices;

    public DrawingService() {
        routesServices = Application.routesService;
    }

    public static PolylineOptions createLine(Route route) {
        PolylineOptions polyLine = new PolylineOptions();
        polyLine.width(5).color(Color.RED);
        for(LatLng latLg : route.getRouteList()) {
            Log.d("test", latLg.toString());
            polyLine.add(latLg);
        }
        Log.d("polyline", polyLine.toString());
        return polyLine;
    }
}
