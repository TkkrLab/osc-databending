package com.flat20.util.multitouch;

public class TouchEvent {

	public enum TouchType { UP, DOWN, MOVE }

	public int pointerId;
	public TouchType type;
	public float x;
	public float y;
	public float pressure;

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getPressure() {
		return pressure;
	}

	public String toString() {
		return "TouchEvent pointerId:" + pointerId + ", TouchType:" + type + ", x:" + x + ", y:" + y + " pressure:" + pressure;
	}
}