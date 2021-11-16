package com.flat20.gui.animations;

import com.flat20.gui.Renderer;

//TODO only allow one instance of each animation in list. remove won't work otherwise. 
public class AnimationManager {

	// Run internal update thread.
	private boolean mIsThreaded = false;

	protected Animation[] mRunningAnimations;
	protected int mNumAnimations = 0;
	protected Renderer mRenderer; // We use this to synchronize with.

	protected UpdateThread mUpdateThread = null;


	private static AnimationManager sSingleton = new AnimationManager();

	public static AnimationManager getInstance() {
		return sSingleton;
	}

	private AnimationManager() {
	}
/*
	public void add(Animation animation) {
		if (mUpdateThread != null)
			mUpdateThread.add(animation);
	}
	
	public void remove(Animation animation) {
		if (mUpdateThread != null)
			mUpdateThread.remove(animation);
	}

	public boolean hasAnimation(Animation animation) {
		if (mUpdateThread != null)
			return mUpdateThread.hasAnimation(animation);
		return false;
	}

	public int getNumAnimations() {
		if (mUpdateThread != null)
			return mUpdateThread.getNumAnimations();
		return -1;
	}
*/
	public void create(Renderer renderer, boolean isThreaded) {
		if (mUpdateThread != null)
			destroy();

		mRenderer = renderer;
		mIsThreaded = isThreaded;

		mRunningAnimations = new Animation[0];

		if (mIsThreaded) {
			mUpdateThread = new UpdateThread();
			mUpdateThread.start();
		}
	}

	public void destroy() {
		if (mIsThreaded) {
			mUpdateThread.isRunning = false;
	
			try {
				mUpdateThread.join();
			} catch (InterruptedException e) {
			}
		}

		mRunningAnimations = null;

	}

	public void add(Animation animation) {
		synchronized (mRunningAnimations) {
			Animation[] array = new Animation[mNumAnimations + 1];
			System.arraycopy(mRunningAnimations, 0, array, 0, mNumAnimations);
			array[mNumAnimations] = animation;
			mRunningAnimations = array;
			mNumAnimations++;
		}
	}

	// TODO shrink new array
	public void remove(Animation animation) {
		synchronized (mRunningAnimations) {
			Animation[] array = new Animation[mNumAnimations];
			int c=0;
			for (int i=0; i<mNumAnimations; i++) {
				if (mRunningAnimations[i] != null && mRunningAnimations[i].ID == animation.ID) {
				} else {
					array[c] = mRunningAnimations[i];
					c++; 
				}
			}
	
			if (c != mNumAnimations) {
				mRunningAnimations = array;
				mNumAnimations--;
				//animation.isFinished = true;
			}
		}
	}

	public boolean hasAnimation(Animation animation) {
		synchronized (mRunningAnimations) {
			for (int i=0; i<mNumAnimations; i++) {
				Animation a = mRunningAnimations[i];
				if (a != null && a.ID == animation.ID)
					return true;
			}
		}
		return false;
	}

	public int getNumAnimations() {
		return mNumAnimations;
	}

	/**
	 * Updates animations one frame. Can be called from our internal
	 * UpdateThread or from the renderer on each drawFrame().
	 */
	public void updateFrame() {
		//synchronized (mRenderer.getSpriteContainer()) {
			synchronized (mRunningAnimations) {
				for (int i=0; i<mNumAnimations; i++) {
					Animation animation = mRunningAnimations[i];
					if (animation != null && !animation.update())
						remove(animation);
				}
	
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					
				}
			}
		//}
	}

	protected class UpdateThread extends Thread {

		public boolean isRunning = true;

		@Override
		public void run() {
			while (isRunning) {
				updateFrame();
			}
		}

	
	}
}
