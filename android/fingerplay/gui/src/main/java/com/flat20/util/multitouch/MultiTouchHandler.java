package com.flat20.util.multitouch;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;

import android.util.Log;
import android.view.MotionEvent;

public class MultiTouchHandler extends TouchHandler {
	public static final String TAG = MultiTouchHandler.class.getSimpleName();

	public void handleMove(final MotionEvent event, TouchEvent.TouchType touchType) {
		try {
			final int numPointers = event.getPointerCount();

			for (int i = 0; i < numPointers; i++) {
				final TouchEvent te = setPointerState(event.getPointerId(i), touchType, event.getX(i), event.getY(i), event.getPressure(i));
				addEvent(te);
			}
		} catch (Exception e) {
			Writer writer = new StringWriter();
			e.printStackTrace(new PrintWriter(writer));
			String s = writer.toString();
			Log.e(TAG, s);
		}
	}

	public ArrayList<TouchEvent> handleMotionEvent(MotionEvent event) {

		clearEvents();

		final int action = event.getAction();

		if ( action == MotionEvent.ACTION_DOWN ) {
			handleMove(event, TouchEvent.TouchType.DOWN);
		}
		else if ( (action & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_POINTER_DOWN ) {
			handleMove(event, TouchEvent.TouchType.DOWN);
		}
		else if ( action == MotionEvent.ACTION_MOVE ) {
			handleMove(event, TouchEvent.TouchType.MOVE);
				//final TouchEvent last = getPointerState(pointerId, 1);
/*
					float x = te.x;
					float y = te.y;
					int dx = (int)Math.abs(x - last.x);
					int dy = (int)Math.abs(y - last.y);
					int distance = (int)Math.sqrt(dx*dx + dy*dy);

					Log.i("MTH", pointerId + " distance: " + distance + " last: " + (int)last.x + "," + (int)last.y + " new: " + (int)te.x + "," + (int)te.y);

					if (distance > 40) {
						te.y = last.y;
						te.x = last.x;
						Log.i("MTH", " Large jump reverting y.." + te.x + ", " + te.y);
					}
					
					//if ((dx > 40 && dy < 10) || (dy > 40 && dx < 10)) {
						//Log.i("MTH", "Large jump! " + dx + ", " + dy);
					//}
				//}
	*/
		}
		else if ( (action & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_POINTER_UP ) {
			handleMove(event, TouchEvent.TouchType.UP);
		}
		else if (action == MotionEvent.ACTION_UP) {
			handleMove(event, TouchEvent.TouchType.UP);
		}
		else {
			Log.i("MultiTouchManager", "Unhandled action: " + action);
		}
		return getEvents();
	}
}
