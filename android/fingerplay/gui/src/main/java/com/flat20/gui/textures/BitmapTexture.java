package com.flat20.gui.textures;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.opengl.GLUtils;

/**
 * Texture with a bitmap you can draw on to generate
 * dynamic textures.
 * 
 * @author andreas
 *
 */
public class BitmapTexture extends Texture {

	final public String name;
	protected Bitmap mBitmap;

    protected BitmapTexture(String name, int width, int height) {
		super(width, height);
		this.name = name;
		mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
	}

    protected BitmapTexture(String name, Bitmap bitmap) {
		super(bitmap.getWidth(), bitmap.getHeight());
		this.name = name;
		mBitmap = bitmap;
	}

    public Bitmap getBitmap() {
    	return mBitmap;
    }

    public void refresh(GL10 gl) {
    	gl.glBindTexture(GL10.GL_TEXTURE_2D, ID);
    	GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, mBitmap, 0);
    }
/*
    @Override
    public void free() {
    	mBitmap.recycle();
    	mBitmap = null;
    }
*/
}
