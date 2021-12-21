package com.livo.nuo.lib.mapcurves.models;

import android.graphics.Point;

public class PixelControlPoints {
    private Point firstPixelControlPoint;
    private Point secondPixelControlPoint;

    public PixelControlPoints(Point firstPixelControlPoint, Point secondPixelControlPoint) {
        this.firstPixelControlPoint = firstPixelControlPoint;
        this.secondPixelControlPoint = secondPixelControlPoint;
    }

    public Point getFirstPixelControlPoint() {
        return firstPixelControlPoint;
    }

    public Point getSecondPixelControlPoint() {
        return secondPixelControlPoint;
    }
}
