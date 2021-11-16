package com.flat20.util;

import com.flat20.gui.animations.AnimationManager;

import android.util.Log;

public class Performance {
/*
	private static long nanoStart;

	final public static void start() {
		nanoStart = System.nanoTime();
	}

	final public static void end() {
		long time = System.nanoTime() - nanoStart;
		System.out.println("Performance: " + time);
	}
*/

	private static Thread sMemoryThread = null;

	public static void startMemoryTrace() {
		if (sMemoryThread == null) {
            ProfileRecorder.sSingleton.resetAll();
			sMemoryThread = new Thread(mMemoryTrace);
			sMemoryThread.start();
		}
	}

	public static void stopMemoryTrace() {
		// probably only works IF the runnable is sleeping.
		sMemoryThread.interrupt();
		try {
			sMemoryThread.join();
		} catch (InterruptedException e) {
			
		}
		sMemoryThread = null;
	}

	public static Runnable mMemoryTrace = new Runnable() {

		public boolean isRunning = false;

		public void run() {
			isRunning = true;
			while (isRunning) {
		        Runtime r = Runtime.getRuntime();
				//Log.i("Performance", "__ memory ______________");
		        Log.i("Performance", "freeMem: " + r.freeMemory());
		        //Log.i("Performance", "maxMem: " + r.maxMemory());
		        //Log.i("Performance", "totalMem: " + r.totalMemory());
		        //Log.i("Performance", "usedMem: " + (r.totalMemory() - r.freeMemory()));

		        Log.i("Performance", "Render Min: " + ProfileRecorder.sSingleton.getMinTime(ProfileRecorder.PROFILE_FRAME) + "ms / frame");
	        	Log.i("Performance", "Render Max: " + ProfileRecorder.sSingleton.getMaxTime(ProfileRecorder.PROFILE_FRAME) + "ms / frame");
	        	Log.i("Performance", "Render Average: " + ProfileRecorder.sSingleton.getAverageTime(ProfileRecorder.PROFILE_FRAME) + "ms / frame");
	        	Log.i("Performance", "FPS: " + 1000/ProfileRecorder.sSingleton.getAverageTime(ProfileRecorder.PROFILE_FRAME));

	        	Log.i("Performance", "Animations: " + AnimationManager.getInstance().getNumAnimations());

	        	ProfileRecorder.sSingleton.resetAll();

		        try {
		        	Thread.sleep(10000);
		        } catch (InterruptedException e) {
		        	isRunning = false;
		        }
			}
		}
	};
	
}
