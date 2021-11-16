package com.flat20.gui;

import java.util.ArrayList;

import android.view.MotionEvent;

import com.flat20.gui.sprites.Sprite;
import com.flat20.gui.widgets.IWidget;
import com.flat20.gui.widgets.Widget;
import com.flat20.gui.widgets.WidgetContainer;
import com.flat20.util.multitouch.TouchManager;
import com.flat20.util.multitouch.TouchEvent;
import com.flat20.util.multitouch.TouchEvent.TouchType;

/**
 * TODO Shouldn't extend Renderer but decorate its addSprite() method.
 * Currently the TestRenderer needs to change its parent class
 * depending on whether it wants to be interactive or not.
 * 
 * If BasicActivity had a addSprite() then InteractiveActivity
 * could add the Widgets there, but that means the developer
 * would have to extends an Activity (or code it himself??)
 *
 * Might be a good trade off actually..
 * 
 * @author andreas
 * 
 */
public class WidgetRenderer extends Renderer {

	final protected WidgetContainer mRootWidgetContainer;

	//final int mWidth;
	//final int mHeight;

	// Requires height
	public WidgetRenderer(int viewportWidth, int viewportHeight) {
		super(viewportWidth, viewportHeight);

		//mWidth = width;
		//mHeight = height;

		// width and height isn't used in mRootWidgetContainer but might as well set it
		// to something better than 0,0
		mRootWidgetContainer = new WidgetContainer(viewportWidth, viewportHeight);
	}

	@Override
	public void addSprite(Sprite sprite) {
		if (sprite instanceof Widget) {
			addWidget((Widget) sprite);
		}
		// Both SpriteContainer and WidgetContainer sets the parent
		// and SpriteContainer needs to be last.
		super.addSprite(sprite);
	}

	// For touch events.
	private void addWidget(Widget widget) {
		mRootWidgetContainer.addSprite(widget);
	}

	public WidgetContainer getWidgetContainer() {
		return mRootWidgetContainer;
	}

	public IWidget[] getWidgets() {
		return mRootWidgetContainer.getWidgets();
	}

	public void onTouchEvent(MotionEvent event) {

		// We need to be backwards compatible so all new methods
		// are stored in an external class which only gets called
		// if we're on SDK version 2.0 or higher.

		final ArrayList<TouchEvent> events = TouchManager.handleMotionEvent(event);

		for (TouchEvent te : events) {

	    	float fTouchX = te.x;
	    	float fTouchY = Renderer.VIEWPORT_HEIGHT - te.y;
	    	float pressure = te.pressure;
			int touchX = (int) (fTouchX+0.5f);
			int touchY = (int) (fTouchY+0.5f); // convert to opengl y.
			int ptrId = te.pointerId;

			TouchType type = te.type;

			switch (type) {
	
				case MOVE:
					mRootWidgetContainer.onTouchMove(touchX, touchY, pressure, ptrId);
					break;
	
				case DOWN:
					mRootWidgetContainer.onTouchDown(touchX, touchY, pressure, ptrId);
					break;
	
				case UP:
					mRootWidgetContainer.onTouchUp(touchX, touchY, pressure, ptrId);
					break;
			}

		}

	}

}
