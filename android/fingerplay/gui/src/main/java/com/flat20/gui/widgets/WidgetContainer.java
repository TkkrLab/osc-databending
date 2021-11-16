package com.flat20.gui.widgets;

import java.util.HashMap;

import com.flat20.gui.sprites.Sprite;

public class WidgetContainer extends Widget implements IWidget {

	protected IWidget[] mWidgets = new IWidget[0];
	protected int mNumWidgets = 0;

	// Need to be an array now
	final protected HashMap<Integer, IWidget> mPressedWidgets = new HashMap<Integer, IWidget>();

	//protected IWidget mFocusedWidget = null;

	public WidgetContainer(int width, int height) {
		super(width, height);
	}

	@Override
	public void addSprite(Sprite sprite) {
		super.addSprite(sprite);

		if (sprite instanceof IWidget) {
			addWidget((IWidget) sprite);
		}
	}

	private void addWidget(IWidget widget) {
		IWidget[] array = new IWidget[mWidgets.length + 1];
		System.arraycopy(mWidgets, 0, array, 0, mWidgets.length);
		array[mWidgets.length] = widget;
		mWidgets = array;
		mNumWidgets++; 
	}

	public IWidget[] getWidgets() {
		return mWidgets;
	}
/*
	public IWidget getFocusedWidget() {
		return mFocusedWidget;
	}
*/
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean onTouchDown(int touchX, int touchY, float pressure, int pointerId) {

		if (!visible)
			return false;

    	IWidget[] widgets = mWidgets;
		int i = mNumWidgets;
		while (--i >= 0) {
			IWidget widget = widgets[i];
			int localX = touchX - widget.getX();
			int localY = touchY - widget.getY();

			if (widget.hitTest(localX, localY)) {
				if (widget.onTouchDown(localX, localY, pressure, pointerId)) {
					mPressedWidgets.put(pointerId, widget);

					widget.onFocusChanged(true);
					//mFocusedWidget = widget;
					//mFocusedWidget.onFocusChanged(true);
					return true;
				}
			}
		}
		return false;
	}

	// Returns true if event was caught.
	public boolean onTouchMove(int touchX, int touchY, float pressure, int pointerId) {
		// We only care about focused widget.
		//if (mFocusedWidget != null) {
		final IWidget widget = mPressedWidgets.get(pointerId);
		if (widget != null) {

			int localX = touchX - widget.getX();
			int localY = touchY - widget.getY();
			if (widget.getVisible()) {// && mFocusedWidget.hitTest(localX, localY)) {
				widget.onTouchMove(localX, localY, pressure, pointerId);
				return true;
			}
		}
		return false;
	}

	// Returns true if event was caught.
	public boolean onTouchUp(int touchX, int touchY, float pressure, int pointerId) {
		
		final IWidget widget = mPressedWidgets.get(pointerId);
		if (widget != null) {

			int localX = touchX - widget.getX();
			int localY = touchY - widget.getY();

			if (widget.hitTest(localX, localY)) {
				widget.onTouchUp(localX, localY, pressure, pointerId);
			} else if (widget.getVisible()) {
				widget.onTouchUpOutside(touchX, touchY, pressure, pointerId);
			}

			widget.onFocusChanged(false);
			mPressedWidgets.remove(pointerId);

			return true;
		}
		return false;
	}

	// TODO Pass on touch outside to children.
	public boolean onTouchUpOutside(int touchX, int touchY, float pressure, int pointerId) {
		return false;
	}

	public void onFocusChanged(boolean newFocus) {
		mHasFocus = newFocus;
	}

}
