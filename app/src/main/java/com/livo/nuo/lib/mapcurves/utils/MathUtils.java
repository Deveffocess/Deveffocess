package com.livo.nuo.lib.mapcurves.utils;

import static java.lang.Math.abs;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toDegrees;

import android.graphics.Point;

import com.google.android.gms.maps.model.LatLng;

public class MathUtils {

    /**
     * Returns the angle between two LatLongs, in degrees.
     */
    public static double angleBetween(LatLng from, LatLng to) {
        double angle = toDegrees(atan2((to.longitude - from.longitude),
                (to.latitude - from.latitude)));
        if (angle < 0) {
            angle = 360 + angle;
        }
        return angle;
    }

    /**
     * Returns the angle between two points, in degrees.
     */
    public static double angleBetween(Point from, Point to) {
        double angle = toDegrees(atan2((double) (to.y - from.y), (double) (to.x - from.x)));
        if (angle < 0) {
            angle = 360 + angle;
        }
        return angle;
    }

    /**
     * Returns the distance between two points.
     */
    public static double distanceBetween(Point from, Point to) {
        return sqrt(pow(abs(to.x - from.x), 2) + pow(abs(to.y - from.y), 2));
    }

    /**
     * Returns the Point resulting from moving a distance from an origin
     * in the specified heading
     *
     * @param from     The Point from which to start.
     * @param distance The distance to travel.
     * @param heading  The heading in degrees.
     */
    public static Point pixelOffset(Point from, double distance, double heading) {
        double x = from.x + (distance * cos(heading));
        double y = from.y + (distance * sin(heading));
        return new Point((int) x, (int) y);
    }
}
