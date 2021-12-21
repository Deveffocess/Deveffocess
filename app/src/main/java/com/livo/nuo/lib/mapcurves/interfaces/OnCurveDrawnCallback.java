package com.livo.nuo.lib.mapcurves.interfaces;

import com.livo.nuo.lib.mapcurves.Curve;

/**
 * Interface definition for a callback to be invoked when a curve is drawn on map.
 */
public interface OnCurveDrawnCallback {

    void onCurveDrawn(Curve curve);
}
