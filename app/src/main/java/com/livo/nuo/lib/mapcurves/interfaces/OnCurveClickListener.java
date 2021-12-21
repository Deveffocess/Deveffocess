package com.livo.nuo.lib.mapcurves.interfaces;


import com.livo.nuo.lib.mapcurves.Curve;

/**
 * Interface definition for a callback to be invoked when a curve is clicked on map.
 */
public interface OnCurveClickListener {

    void onCurveClick(Curve curve);
}