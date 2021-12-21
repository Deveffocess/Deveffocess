package com.livo.nuo.lib.mapcurves.models;

import com.google.android.gms.maps.Projection;
import com.livo.nuo.lib.mapcurves.CurveOptions;

public class MessageQueueData {
    private CurveOptions curveOptions;
    private Projection projection;

    public MessageQueueData(CurveOptions curveOptions, Projection projection) {
        this.curveOptions = curveOptions;
        this.projection = projection;
    }

    public CurveOptions getCurveOptions() {
        return curveOptions;
    }

    public Projection getProjection() {
        return projection;
    }
}
