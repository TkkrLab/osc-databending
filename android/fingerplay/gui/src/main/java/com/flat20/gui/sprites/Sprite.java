package com.flat20.gui.sprites;

import javax.microedition.khronos.opengles.GL10;

// rename to Sprite and call Sprite TexturedSprite ?
// TODO Rename to DisplayObject? or Component according to Composite pattern.
public abstract class Sprite {
/*
	// Component properties
	public Sprite parent; // TODO Important for globalX?
	public int mNumSprites = 0;

	public int globalX;
	public int globalY;
	public float globalZ;
*/
	public boolean visible = true;
	public int x;
	public int y;
	public float z = 0; // Only used by Zrenderer..?
	
	public Sprite parent;
	public int globalX;
	public int globalY;

	public float rotation;

	// offset the center for rotation
	public int offsetX;
	public int offsetY;


	public int width;
	public int height;

	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public void load(GL10 gl) {
	}

	public void free(GL10 gl) {
	}

	public void draw(GL10 gl) {
	}

	/*
	 * These belong to our ZRenderer.
	public void buildDisplayList(RenderList displayList) {//, int globalX, int globalY, float globalZ) {
		updateGlobalProperties();
	}

	public void updateGlobalProperties() {
	}

	public void setX(int newX) {
		x = newX;
		if (parent != null)
			globalX = parent.globalX + x;
	}

	public void setY(int newY) {
		y = newY;
		//if (parent != null)
			//globalY = parent.globalY + y;
			TestRenderer.mRebuild.add(this);
		//}
	}

	public void setZ(float newZ) {
		z = newZ;
		if (parent != null)
			globalZ = parent.globalZ + z;
	}
*/
}
