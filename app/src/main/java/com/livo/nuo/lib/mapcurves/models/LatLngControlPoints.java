package com.livo.nuo.lib.mapcurves.models;

import com.google.android.gms.maps.model.LatLng;

public class LatLngControlPoints {
    private LatLng firstLatLngControlPoint;
    private LatLng secondLatLngControlPoint;

    public LatLngControlPoints(LatLng firstLatLngControlPoint, LatLng secondLatLngControlPoint) {
        this.firstLatLngControlPoint = firstLatLngControlPoint;
        this.secondLatLngControlPoint = secondLatLngControlPoint;
    }

    public LatLng getFirstLatLngControlPoint() {
        return firstLatLngControlPoint;
    }

    public LatLng getSecondLatLngControlPoint() {
        return secondLatLngControlPoint;
    }
}
