package com.livo.nuo.lib.mapcurves.impl;

import com.google.android.gms.maps.model.Cap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.livo.nuo.lib.mapcurves.Curve;

import java.util.List;

public class DelegatingCurve implements Curve {

    private Polyline polyline;
    private CurveManager curveManager;

    private Object data;

    DelegatingCurve(Polyline polyline, CurveManager curveManager) {
        this.polyline = polyline;
        this.curveManager = curveManager;
    }

    @Override
    public int getColor() {
        return polyline.getColor();
    }

    @Override
    public Object getData() {
        return data;
    }

    @Override
    public Cap getEndCap() {
        return polyline.getEndCap();
    }

    @Override
    public String getId() {
        return polyline.getId();
    }

    @Override
    public int getJointType() {
        return polyline.getJointType();
    }

    @Override
    public List<PatternItem> getPattern() {
        return polyline.getPattern();
    }

    @Override
    public List<LatLng> getPoints() {
        return polyline.getPoints();
    }

    @Override
    public Cap getStartCap() {
        return polyline.getStartCap();
    }

    @Override
    public Object getTag() {
        return polyline.getTag();
    }

    @Override
    public float getWidth() {
        return polyline.getWidth();
    }

    @Override
    public float getZIndex() {
        return polyline.getZIndex();
    }

    @Override
    public boolean isClickable() {
        return polyline.isClickable();
    }

    @Override
    public boolean isGeodesic() {
        return polyline.isGeodesic();
    }

    @Override
    public boolean isVisible() {
        return polyline.isVisible();
    }

    @Override
    public void remove() {
        this.curveManager.onRemove(polyline);
        polyline.remove();
    }

    @Override
    public void setClickable(boolean clickable) {
        polyline.setClickable(clickable);
    }

    @Override
    public void setColor(int color) {
        polyline.setColor(color);
    }

    @Override
    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public void setEndCap(Cap endCap) {
        polyline.setEndCap(endCap);
    }

    @Override
    public void setGeodesic(boolean geodesic) {
        polyline.setGeodesic(geodesic);
    }

    @Override
    public void setJointType(int jointType) {
        polyline.setJointType(jointType);
    }

    @Override
    public void setPattern(List<PatternItem> pattern) {
        polyline.setPattern(pattern);
    }

    @Override
    public void setPoints(List<LatLng> points) {
        polyline.setPoints(points);
    }

    @Override
    public void setStartCap(Cap startCap) {
        polyline.setStartCap(startCap);
    }

    @Override
    public void setTag(Object tag) {
        polyline.setTag(tag);
    }

    @Override
    public void setVisible(boolean visible) {
        polyline.setVisible(visible);
    }

    @Override
    public void setWidth(float width) {
        polyline.setWidth(width);
    }

    @Override
    public void setZIndex(float zIndex) {
        polyline.setZIndex(zIndex);
    }

    @Override
    public int hashCode() {
        return polyline.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DelegatingCurve)) {
            return false;
        }
        DelegatingCurve other = (DelegatingCurve) obj;
        return polyline.equals(other.polyline);
    }

    @Override
    public String toString() {
        return polyline.toString();
    }
}
