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
        Log.d("DrawingService", "DrawingService constructor completed");
    }

    public static PolylineOptions createLine(Route route) {
        PolylineOptions polyLine = new PolylineOptions();
        if(route == null) { return polyLine; }
        polyLine.width(5).color(Color.RED);
        for(LatLng latLg : route.getNormalizedRoute()) {
            Log.d("test", latLg.toString());
            polyLine.add(latLg);
        }
        Log.d("polyline", polyLine.toString());
        Log.d("DrawingService", "createLine completed");
        return polyLine;
    }
}
