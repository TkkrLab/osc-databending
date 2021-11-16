package com.flat20.gui.sprites;

import javax.microedition.khronos.opengles.GL10;

import com.flat20.gui.textures.Mesh;
import com.flat20.gui.textures.Texture;

// IS abstract because it doesn't create the Grid which causes nullpointer exception in free() or load(), etc.
public abstract class TexturedSprite extends Sprite {

	protected Texture mTexture = null;
	protected Mesh mMesh = null;

	protected TexturedSprite(Texture texture, int width, int height) {
		mTexture = texture;
		this.width = width;
		this.height = height;
	}

	protected TexturedSprite(Texture texture) {
		mTexture = texture;
		width = texture.width;
		height = texture.height;
	}

	protected TexturedSprite(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public Texture getTexture() {
		return mTexture;
	}

	public Mesh getGrid() {
		return mMesh;
	}
/*
	@Override
	public void buildDisplayList(RenderList displayList) {//, int globalX, int globalY, float globalZ) {
		super.buildDisplayList(displayList);
		displayList.add(this);
	}

	@Override
	public void updateGlobalProperties() {
		if (parent != null) {
			globalX = parent.globalX + x;
			globalY = parent.globalY + y;
			globalZ = parent.globalZ + z;
		}
	}
*/

	@Override
	public void load(GL10 gl) {
        mMesh.generateHardwareBuffers(gl);
	}

	@Override
	public void free(GL10 gl) {
        mMesh.forgetHardwareBuffers();
	}

	@Override
	public void draw(GL10 gl) {

		if (!visible)
    		return;

		// Do we need this?
		globalX = parent.globalX + x;
		globalY = parent.globalY + y;

		gl.glBindTexture(GL10.GL_TEXTURE_2D, mTexture.ID);

		gl.glPushMatrix();
		gl.glTranslatef(x, y, z);

		if (rotation != 0)
			gl.glRotatef(rotation, 0, 0, 1);

		gl.glTranslatef(offsetX, offsetY, 0);

		mMesh.draw(gl);

		gl.glPopMatrix();

	}
}
