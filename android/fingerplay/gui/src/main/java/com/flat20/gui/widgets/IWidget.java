package com.flat20.gui.widgets;

public interface IWidget {

	public boolean getVisible();
	public int getX();
	public int getY();

	public boolean hitTest(int localX, int localY);

	// Returns true if event was caught.
	public boolean onTouchMove(int touchX, int touchY, float pressure, int pointerId);

	// Returns true if event was caught.
	public boolean onTouchDown(int touchX, int touchY, float pressure, int pointerId);

	// Returns true if event was caught.
	public boolean onTouchUp(int touchX, int touchY, float pressure, int pointerId);

	// Returns true if event was caught.
	public boolean onTouchUpOutside(int touchX, int touchY, float pressure, int pointerId);

	public void onFocusChanged(boolean getsFocus);
}
