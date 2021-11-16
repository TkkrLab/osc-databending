package com.flat20.util.multitouch;

import java.util.ArrayList;

import android.util.Log;
import android.view.MotionEvent;

public class SingleTouchHandler extends TouchHandler {

	public ArrayList<TouchEvent> handleMotionEvent(MotionEvent event) {

		clearEvents();

		final int action = event.getAction();
		final int pointerId = 0;

		if ( action == MotionEvent.ACTION_DOWN ) {
			final TouchEvent te = setPointerState(pointerId, TouchEvent.TouchType.DOWN, event.getX(), event.getY(), event.getPressure());
			addEvent(te);
		}

		else if ( action == MotionEvent.ACTION_MOVE ) {
			final TouchEvent te = setPointerState(pointerId, TouchEvent.TouchType.MOVE, event.getX(), event.getY(), event.getPressure());
			addEvent(te);
		}

		else if (action == MotionEvent.ACTION_UP) {
			final TouchEvent te = setPointerState(pointerId, TouchEvent.TouchType.UP, event.getX(), event.getY(), event.getPressure());
			addEvent(te);
		}

		else {
			Log.i("SingleTouchHandler", "Unhandled action: " + action);
		}

		return getEvents();
	}

}
