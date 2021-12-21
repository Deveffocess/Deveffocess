package com.livo.nuo.lib.mapcurves.interfaces;

import android.os.Message;

/**
 * Interface definition for worker thread to send messages to the UI thread.
 */
public interface UiThreadCallback {

    void publishToUiThread(Message message);
}
