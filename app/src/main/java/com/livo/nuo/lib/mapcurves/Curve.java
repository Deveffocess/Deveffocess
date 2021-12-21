package com.livo.nuo.lib.mapcurves;

import com.google.android.gms.maps.model.Cap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PatternItem;

import java.util.List;

public interface Curve {

    int getColor();

    Object getData();

    Cap getEndCap();

    String getId();

    int getJointType();

    List<PatternItem> getPattern();

    List<LatLng> getPoints();

    Cap getStartCap();

    Object getTag();

    float getWidth();

    float getZIndex();

    boolean isClickable();

    boolean isGeodesic();

    boolean isVisible();

    void remove();

    void setClickable(boolean clickable);

    void setColor(int color);

    void setData(Object data);

    void setEndCap(Cap endCap);

    void setGeodesic(boolean geodesic);

    void setJointType(int jointType);

    void setPattern(List<PatternItem> pattern);

    void setPoints(List<LatLng> points);

    void setStartCap(Cap startCap);

    void setTag(Object tag);

    void setVisible(boolean visible);

    void setWidth(float width);

    void setZIndex(float zIndex);
}
