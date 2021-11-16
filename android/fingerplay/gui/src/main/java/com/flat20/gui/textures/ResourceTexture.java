package com.flat20.gui.textures;

public class ResourceTexture extends Texture {

	protected int mResourceId;

    //final private static BitmapFactory.Options sBitmapOptions = new BitmapFactory.Options();

	// TODO check resource width/height in constructor.
    protected ResourceTexture(int resourceId, int width, int height) {
		super(width, height);
    	mResourceId = resourceId;
		//Bitmap bitmap = context.getResources().openRawResource(resourceId);
	}

    public int getResourceId() {
    	return mResourceId;
    }
/*
    @Override
	public boolean load(GL10 gl) {
    	ID = TextureManager.createTextureFromResource(gl, mResourceId);
    	return (ID != -1);
	}
*/

}
