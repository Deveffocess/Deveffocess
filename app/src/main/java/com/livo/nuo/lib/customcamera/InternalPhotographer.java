package com.livo.nuo.lib.customcamera;

import android.app.Activity;

interface InternalPhotographer extends Photographer {

    void initWithViewfinder(Activity activity, CameraView preview);
}
