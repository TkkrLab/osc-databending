package com.flat20.gui.animations;

public abstract class Animation {

	private static int ID_COUNTER = 0;
	public int ID = ++ID_COUNTER;
	
	//public boolean isFinished = false;
/*
	public Animation() {
	}
*/
	public boolean update() {
		return false;
	}

}
