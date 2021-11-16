package com.flat20.gui.sprites;

import javax.microedition.khronos.opengles.GL10;

import com.flat20.gui.Renderer;
import com.flat20.gui.sprites.Sprite;

public class SpriteContainer extends Sprite {

	protected Sprite[] mSprites = new Sprite[0];
	protected int mNumSprites = 0;

	public SpriteContainer() {
	}

	public void addSprite(Sprite sprite) {

		sprite.parent = this;

		Sprite[] array = new Sprite[mSprites.length + 1];
		System.arraycopy(mSprites, 0, array, 0, mSprites.length);
		array[mSprites.length] = sprite;
		mSprites = array;
		mNumSprites++;
	}
/*
	@Override
	public void buildDisplayList(RenderList renderList) {//, int globalX, int globalY, float globalZ) {

		if (!visible)
			return;

		updateGlobalProperties();

		final Sprite[] sprites = mSprites;
		final int len = mNumSprites;
		for (int i=0; i<len; i++) {
			final Sprite sprite = sprites[i];
			//TODO Make this ||
			if (sprite.y+globalY < Renderer.VIEWPORT_HEIGHT && sprite.y+globalY+sprite.height > 0)
				sprite.buildDisplayList(renderList);//, globalX+sprite.x, globalY+sprite.y, globalZ+sprite.z);
		}

	}

	@Override
	public void updateGlobalProperties() {

		if (parent != null) {
			globalX = parent.globalX + x;
			globalY = parent.globalY + y;
			globalZ = parent.globalZ + z;
		}

		final Sprite[] sprites = mSprites;
		final int len = mNumSprites;
		for (int i=0; i<len; i++) {
			final Sprite sprite = sprites[i];
			sprite.updateGlobalProperties();
		}
	}
*/
	/*
	@Override
	public void setY(int newY) {

		if (parent != null)
			globalY = parent.globalY + y;

		final Sprite[] sprites = mSprites;
		final int len = mNumSprites;
		for (int i=0; i<len; i++) {
			final Sprite sprite = sprites[i];
			sprite.setY(sprite.y);
		}

	}
*/
	@Override
	public void draw(GL10 gl) {
		if (!visible)
			return;

/*
		if (globalY+height < 0 || globalY > Renderer.VIEWPORT_HEIGHT) {
			Log.i("SpriteContainer", " no draw " + this + " y:" + y + " " + this.height + " " + Renderer.VIEWPORT_HEIGHT + ", globalY: " + globalY);
			return;
		}
*/
		gl.glPushMatrix();
		gl.glTranslatef(x, y, z);

		if (parent != null) {
			globalX = parent.globalX + x;
			globalY = parent.globalY + y;
		}

		final Sprite[] sprites = mSprites;
		final int len = mNumSprites;
		for (int i=0; i<len; i++) {
			final Sprite sprite = sprites[i];
			if (sprite.y+globalY < Renderer.VIEWPORT_HEIGHT && sprite.y+globalY+sprite.height > 0) {
				sprite.draw(gl);
			}
		}

		gl.glPopMatrix();

	}

	@Override
	public void load(GL10 gl) {
		for (Sprite sprite : mSprites) {
			sprite.load(gl);
		}
	}

	@Override
	public void free(GL10 gl) {
		for (Sprite sprite : mSprites) {
			sprite.free(gl);
		}
	}

}
