package com.flat20.gui;

import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class InteractiveActivity extends BasicActivity {

	protected WidgetRenderer mWidgetRenderer;

	@Override
    protected void onCreateView() {
		mWidgetRenderer = new WidgetRenderer(mWidth, mHeight);
		mRenderer = mWidgetRenderer;
        mView = new GLSurfaceView(this);
        mView.setRenderer(mWidgetRenderer);
    }

	@Override
	public boolean onTouchEvent(MotionEvent event) {
        mWidgetRenderer.onTouchEvent(event);
		return true;
	}

	/*
	@Override
	public boolean onTouchEvent(MotionEvent event) {

    	float fTouchX = event.getX();
    	float fTouchY = mHeight - event.getY();
    	float pressure = event.getPressure();
		int touchX = (int) (fTouchX+0.5f);
		int touchY = (int) (fTouchY+0.5f); // convert to opengl y.

		switch (event.getAction()) {

			case MotionEvent.ACTION_MOVE:
				mWidgetContainer.onTouchMove(touchX, touchY, pressure);
				break;

			case MotionEvent.ACTION_DOWN:
				mWidgetContainer.onTouchDown(touchX, touchY, pressure);
				break;

			case MotionEvent.ACTION_UP:
				mWidgetContainer.onTouchUp(touchX, touchY, pressure);
				break;
		}

		return super.onTouchEvent(event);
	}
*/
}
