package com.flat20.util.multitouch;

import java.util.ArrayList;

import com.flat20.util.multitouch.TouchEvent.TouchType;

import android.view.MotionEvent;

public abstract class TouchHandler {

	// Android has a maximum of 256?
	private final int NUM_POINTERS = 8;

	// Store MAX_HISTORY-1 events in history. In other words, store one event in history  
	private final int MAX_HISTORY = 2;

	private final int MAX_POINTERS = NUM_POINTERS*MAX_HISTORY;

	// Using the last TouchEvent to store the state for each pointer with a possible
	// backlog of MAX_HISTORY
	private final TouchEvent[] mPointers = new TouchEvent[MAX_POINTERS];

	// Number of pointers currently pressed. Pretty useless and never read.
	//protected int sNumPointers = 0;

	// List of TouchEvents passed back from handleMotionEvent.
	private final ArrayList<TouchEvent> mEvents = new ArrayList<TouchEvent>(NUM_POINTERS);

	// Points to the current history index in mPointers.
	// Can safely be left at 0 if we don't need history. 
	protected int mPointersIndex = 0;

	public TouchHandler() {
		for (int i=0; i<MAX_POINTERS; i++) {
			mPointers[i] = new TouchEvent();
			mPointers[i].pointerId = i % NUM_POINTERS;
		}
	}

	/**
	 * Parses a MotionEvent for any single or multi touch events and returns them
	 * in an easier to use format. Handles up to MAX_POINTERS events at a time.
	 * It skips the history events so only the last event for each pointer is used.
	 * 
	 * @param event
	 * @return List of found TouchEvents.
	 */
	public ArrayList<TouchEvent> handleMotionEvent(MotionEvent event) {
		return mEvents;
	}

	protected void clearEvents() {
		mEvents.clear();

		mPointersIndex = (mPointersIndex + NUM_POINTERS) % MAX_POINTERS;
	}

	protected void addEvent(TouchEvent event) {
		mEvents.add(event);
	}

	protected ArrayList<TouchEvent> getEvents() {
		return mEvents;
	}

	/**
	 * 
	 * Update the state of a pointer with the latest TouchEvent.
	 * 
	 * @param pointerId
	 * @param type
	 * @param x
	 * @param y
	 * @return TouchEvent for convenience.
	 */
	public TouchEvent setPointerState(int pointerId, TouchType type, float x, float y, float pressure) {
		final int index = mPointersIndex + pointerId;
/*
		if (mPointers[index] == null) {
			mPointers[index] = new TouchEvent();
			mPointers[index].pointerId = pointerId;
		}
*/
		mPointers[index].type = type;
		mPointers[index].x = x;
		mPointers[index].y = y;
		mPointers[index].pressure = pressure;
		return mPointers[index];
	}

	/**
	 * 
	 * @param pointerId
	 * @param history	Fetch TouchEvent for X updates ago.
	 * @return
	 */
	public TouchEvent getPointerState(int pointerId, int history) {
		final int historyIndex = (history % MAX_HISTORY) * NUM_POINTERS;

		int index = mPointersIndex - historyIndex;
		if (index < 0)
			index += MAX_POINTERS;

		//Log.i("TH", "pointerId = " + pointerId + " history = " + history + " historyIndex = " + historyIndex + " mPointersIndex = " + mPointersIndex + " index = " + index);

		return mPointers[index + pointerId];
	}

	public String toString() {
		String ret = "TouchHandler ";
		for (int i=0; i<NUM_POINTERS; i++) {
			TouchEvent lastPointerEvent = mPointers[i];
			if (lastPointerEvent != null)
				ret += " " + lastPointerEvent.type;
			else
				ret += " x";
		}
		return ret;
	}


}
