package com.flat20.gui.textures;

public abstract class Texture {

	public int ID = -1;
	public int width;
	public int height;

	//public Bitmap bitmap; // Why are we saving bitmap?

	public Texture(int width, int height) {
		//this.bitmap = bitmap;
		this.width = width;
		this.height = height;
	}

	// Called from Sprite when Bitmap needs to be loaded
	// from the system.
	/*
	public boolean load(GL10 gl) {

		if (bitmap == null) {
			Log.e("Texture", "Bitmap was null.");
			return false;
		}

		ID = TextureManager.createTextureFromBitmap(gl, bitmap);
		// Need to make sure we only load Texture once.

		//textureID = loadBitmap(this, gl, 2);
		return (ID != -1);
	}*/
/*	
	public void free() {
		Log.i("Texture", "free");
	}
*/
}