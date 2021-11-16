package com.flat20.gui.widgets;

public class Button extends Widget {

	protected IListener mListener = null;
	
	public Button(int width, int height) {
		super(width, height);
	}

	@Override
	public boolean onTouchDown(int touchX, int touchY, float pressure, int pointerId) {
		onPress();
		return true;//super.onTouchDown(touchX, touchY, pressure);
	}

	@Override
	public boolean onTouchUp(int touchX, int touchY, float pressure, int pointerId) {
		onRelease();
		onClick(touchX, touchY);
		return true;
	}

	@Override
	public boolean onTouchUpOutside(int touchX, int touchY, float pressure, int pointerId) {
		onRelease();
		return true;
	}

	protected void onPress() {
		if (mListener != null)
			mListener.onPress(this);
	}

	protected void onRelease() {
		if (mListener != null)
			mListener.onRelease(this);
	}

	public void onClick(int touchX, int touchY) {
		if (mListener != null)
			mListener.onClick(this);
	}

	public void setListener(IListener listener) {
		mListener = listener;
	}

	public interface IListener {
		public void onClick(Button button);
		public void onPress(Button button);
		public void onRelease(Button button);
	}
/*
	public abstract class Listener implements IListener {
		public void onClick(Button button) {}
		public void onPress(Button button) {}
		public void onRelease(Button button) {}
	}
*/
}
