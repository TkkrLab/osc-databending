package com.flat20.util.multitouch;

import java.util.ArrayList;

import android.view.MotionEvent;


public class TouchManager {

	private final static TouchHandler sTouchHandler = (SDKVersion.hasMultiTouch()) ? new MultiTouchHandler() : new SingleTouchHandler();

	/**
	 * Parses a MotionEvent for any single or multi touch events and returns them
	 * in an easier to use format. Handles up to MAX_POINTERS events at a time.
	 * It skips the history events so only the last event for each pointer is used.
	 * 
	 * @param event
	 * @return List of found TouchEvents.
	 */
	public final static ArrayList<TouchEvent> handleMotionEvent(MotionEvent event) {

		ArrayList<TouchEvent> res = sTouchHandler.handleMotionEvent(event);
		//Log.i("MultiTouchManager", sTouchHandler.toString());
		
		return res;
	}

}