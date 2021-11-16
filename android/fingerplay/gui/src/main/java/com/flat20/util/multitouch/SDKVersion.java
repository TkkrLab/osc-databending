package com.flat20.util.multitouch;

import android.os.Build;
import android.util.Log;

public class SDKVersion {

	private static int sVersion;

	static {
		String sdk = Build.VERSION.SDK;
		sVersion = Integer.parseInt(sdk);
		Log.i("SDKVersion", " SDK Version = " + sVersion);
	}

	public static int getVersion() {
		return sVersion;
	}

	public static boolean hasMultiTouch() {
		return (sVersion > 5);
	}
}
