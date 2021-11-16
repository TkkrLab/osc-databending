package com.flat20.gui.widgets;

import com.flat20.gui.sprites.SpriteContainer;

// Widget is an interactive SpriteGroup. Adds touch events

public class Widget extends SpriteContainer implements IWidget {

	protected boolean mHasFocus = false;

	public Widget(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public Widget() {
		super();
	}

	public boolean getVisible() {
		return visible;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean hitTest(int localX, int localY) {
		return (visible && localX > 0 && localX < width && localY > 0 && localY < height);
	}

	// Returns true if event was caught.
	public boolean onTouchMove(int touchX, int touchY, float pressure, int pointerId) {
		//Log.i("Widget", "onTouchMove");
		return false;
	}

	// Returns true if event was caught.
	public boolean onTouchDown(int touchX, int touchY, float pressure, int pointerId) {
		//Log.i("Widget", "onTouchDown");
		return true;
	}

	// Returns true if event was caught.
	public boolean onTouchUp(int touchX, int touchY, float pressure, int pointerId) {
		//Log.i("Widget", "onTouchUp");
		return false;
	}

	// Returns true if event was caught.
	public boolean onTouchUpOutside(int touchX, int touchY, float pressure, int pointerId) {
		//Log.i("Widget", "onTouchUpOutside");
		return false;
	}

	public void onFocusChanged(boolean focus) {
		//Log.i("Widget", "onFocusChanged " + focus);
		mHasFocus = focus;
	}

}
