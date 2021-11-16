package com.flat20.util;

import android.view.MotionEvent;

/**
 * Only use this class if SDK is >5.
 *   
 * @author andreas
 *
 */
public class MouseUtils {

    public static int getPointerCount(MotionEvent event) {
        return event.getPointerCount();
    }

    public static int getPointerId(MotionEvent event, int pointerIndex) {
        return event.getPointerId(pointerIndex);
    }
}
