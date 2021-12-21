package com.livo.nuo.lib.mapcurves.utils;

import static java.lang.Math.asin;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;

import com.google.android.gms.maps.model.LatLng;

public class MercatorUtils {

    /**
     * The earth's radius, in meters.
     */
    private static final double RADIUS = 6371009;

    /**
     * Returns the LatLng resulting from moving a distance from a given LatLng
     * in the specified heading
     *
     * @param from     The LatLng from which to start.
     * @param distance The distance to travel.
     * @param heading  The heading in degrees.
     */
    public static LatLng latLngOffset(LatLng from, double distance, double heading) {
        // https://www.movable-type.co.uk/scripts/latlong.html
        // φ2 = asin( sin φ1 ⋅ cos δ + cos φ1 ⋅ sin δ ⋅ cos θ )
        // λ2 = λ1 + atan2( sin θ ⋅ sin δ ⋅ cos φ1, cos δ − sin φ1 ⋅ sin φ2 ) */
        distance /= RADIUS;
        heading = toRadians(heading);
        double fromLatitude = toRadians(from.latitude);
        double fromLongitude = toRadians(from.longitude);
        double newLng = asin(sin(fromLatitude) * cos(distance) +
                cos(fromLatitude) * sin(distance) * cos(heading));
        double newLat = fromLongitude + atan2(sin(heading) * sin(distance) * cos(fromLatitude),
                cos(distance) - sin(fromLatitude) * sin(newLng));
        return new LatLng(toDegrees(newLng), toDegrees(newLat));
    }

    /**
     * Returns the distance between two LatLng points.
     *
     * @param from Starting point.
     * @param to   Ending point.
     */
    public static double distanceBetween(LatLng from, LatLng to) {
        // https://www.movable-type.co.uk/scripts/latlong.html
        // a = sin²(Δφ/2) + cos φ1 ⋅ cos φ2 ⋅ sin²(Δλ/2)
        // c = 2 ⋅ atan2( √a, √(1−a) )
        // d = R ⋅ c
        double fromLatitude = toRadians(from.latitude);
        double fromLongitude = toRadians(from.longitude);
        double toLatitude = toRadians(to.latitude);
        double toLongitude = toRadians(to.longitude);

        double dLat = toLatitude - fromLatitude;
        double dLon = toLongitude - fromLongitude;
        double a = pow(sin(dLat * 0.5), 2) + cos(fromLatitude) * cos(toLatitude) *
                pow(sin(dLon * 0.5), 2);
        double c = 2 * atan2(sqrt(a), sqrt(1 - a));
        return RADIUS * c;
    }
}
