package com.livo.nuo.lib.mapcurves.impl;

import android.graphics.Point;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;
import com.livo.nuo.lib.mapcurves.CurveOptions;
import com.livo.nuo.lib.mapcurves.interfaces.UiThreadCallback;
import com.livo.nuo.lib.mapcurves.models.LatLngControlPoints;
import com.livo.nuo.lib.mapcurves.models.MessageQueueData;
import com.livo.nuo.lib.mapcurves.models.PixelControlPoints;
import com.livo.nuo.lib.mapcurves.utils.BezierCurveUtils;
import com.livo.nuo.lib.mapcurves.utils.Constants;

import java.util.List;

/**
 * WorkerHandlerThread responsible for computing all intermediate points
 * between two LatLng coordinates
 */

public final class WorkerHandlerThread extends HandlerThread implements Handler.Callback {

    private static Handler workerHandler;
    private UiThreadCallback uiThreadCallback;

    WorkerHandlerThread(String name) {
        super(name);
    }

    /*
     * Ui thread sends messages to the worker thread's message queue
     */
    public void addMessage(Message message) {
        if (workerHandler != null) {
            workerHandler.sendMessage(message);
        } else {
            workerHandler = new Handler(getLooper(), this);
            workerHandler.sendMessage(message);
        }
    }

    public void setUiThreadCallback(UiThreadCallback callback) {
        this.uiThreadCallback = callback;
    }

    /*
     * Function computes all intermediate points between two latLng coordinates
     * using Bezier cubic equation and returns CurveOptions object
     */
    private CurveOptions computePoints(CurveOptions curveOptions, Projection mapProjection) {

        List<LatLng> latLongArrayList = curveOptions.getLatLngList();
        Long start = System.currentTimeMillis();
        for (int index = 0; index < latLongArrayList.size() - 1; index++) {
            LatLng source = latLongArrayList.get(index);
            LatLng destination = latLongArrayList.get(index + 1);

            if (curveOptions.isComputePointsBasedOnScreenPixels()) {

                // Convert start and end LatLng coordinates to screen pixel points.
                // Compute all intermediate screen pixel points using bezier cubic equation.
                // Finally, screen pixel points will be converted back to geographic locations.

                Point startPoint = mapProjection.toScreenLocation(source);
                Point endPoint = mapProjection.toScreenLocation(destination);

                PixelControlPoints PixelControlPoints = BezierCurveUtils.
                        computeCurveControlPoints(curveOptions.getAlpha(), startPoint, endPoint);
                for (double step = 0.0; step < 1.005; step += 0.005) {
                    Point curveXYPoint = BezierCurveUtils.computeCurvePoints(startPoint,
                            PixelControlPoints.getFirstPixelControlPoint(),
                            PixelControlPoints.getSecondPixelControlPoint(), endPoint, step);
                    LatLng curveLatLng = mapProjection.fromScreenLocation(curveXYPoint);
                    curveOptions.getReal().add(curveLatLng);
                }
            } else {

                LatLngControlPoints latLngControlPoints = BezierCurveUtils.
                        computeCurveControlPoints(curveOptions.getAlpha(), source, destination);
                for (double step = 0.0; step < 1.005; step += 0.005) {
                    LatLng curveLatLng = BezierCurveUtils.computeCurvePoints(source,
                            latLngControlPoints.getFirstLatLngControlPoint(),
                            latLngControlPoints.getSecondLatLngControlPoint(), destination, step);
                    curveOptions.getReal().add(curveLatLng);
                }
            }
        }
        Long end = System.currentTimeMillis();
        Log.d("WorkerHandlerThread", "Curve-Fit workerThread finished, took: " + (end - start) + " ms");
        return curveOptions;
    }

    @Override
    public boolean quit() {
        cleanupHandler();
        return super.quit();
    }

    /*
     * Removes any pending messages and sets handler to null
     */
    private void cleanupHandler() {
        if (workerHandler != null) {
            workerHandler.removeMessages(Constants.TASK_START, null);
            workerHandler = null;
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        // Sends message back to Ui thread
        if (msg.what == Constants.TASK_START) {
            MessageQueueData messageQueueData = (MessageQueueData) msg.obj;
            CurveOptions options = computePoints(messageQueueData.getCurveOptions(),
                    messageQueueData.getProjection());
            Message message = Message.obtain(null, Constants.TASK_COMPLETE, options);
            uiThreadCallback.publishToUiThread(message);
        }
        return false;
    }
}

