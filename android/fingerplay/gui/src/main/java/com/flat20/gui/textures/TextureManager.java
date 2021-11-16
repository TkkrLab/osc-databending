package com.flat20.gui.textures;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;

import javax.microedition.khronos.opengles.GL10;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.util.Log;

public class TextureManager {

	//public static GL10 mFreeGl;

	protected static Resources mResources;

    //final protected static ArrayList<Texture> mTextures = new ArrayList<Texture>();

    //final protected static HashMap<Integer, ResourceTexture> mResourceTextures = new HashMap<Integer, ResourceTexture>();

    final protected static HashMap<String, Texture> mTextures = new HashMap<String, Texture>();

    // For Bitmap processing
    final private static int[] mTextureNameWorkspace = new int[1];
    final private static BitmapFactory.Options sBitmapOptions = new BitmapFactory.Options();


    public static void setResources(Resources resources) {
    	mResources = resources;
    }
    public static Resources getResources() {
    	return mResources;
    }

    public static Collection<Texture> getTextures() {
    	return mTextures.values();
    }

    // TODO: ResourceTexture can load straight away and convert itself to a BitmapTexture?
    // this way we don't have to keep two lists? 

    // TODO: Only create one resource texture per resourceId.
    // need to throw error if it's called before TextureManager has been initialized (as in the main activity) 
    public static ResourceTexture createResourceTexture(int resourceId, int width, int height) {
    	//TODO Do loading of bitmap here:
    	String name = "_ResourceTexture " + resourceId;
    	ResourceTexture texture = (ResourceTexture) mTextures.get(name);
    	if (texture == null) {
    		//Bitmap bitmap = createBitmapFromResource(resourceId);
    		//texture = createBitmapTexture(name, bitmap);
    		texture = new ResourceTexture(resourceId, width, height);
    		mTextures.put(name, texture);

    		// bitmap needs to be recycled after texture is put in texture memory!
    		//createTextureFromResource(int resourceId);
    	}
/*
    	ResourceTexture texture = mResourceTextures.get(resourceId);
    	//Log.i("TM", "createResourceTexture NEED TO CHECK WIDTH HERE" + resourceId + ", " + width + ", " + height);
    	if (texture == null) {
        	//Log.i("TM", "createResourceTexture " + resourceId + ", " + width + "," + height);
    		texture = new ResourceTexture(resourceId, width, height);
    		mResourceTextures.put(resourceId, texture);
    	}
    	*/
    	//Log.i("TM", "createResourceTexture found: " + texture.ID + ", " + texture.width + ", " + texture.height);
    	return texture;
    }

    public static BitmapTexture createBitmapTexture(String name, int width, int height) {
    	BitmapTexture texture = (BitmapTexture) mTextures.get(name);
    	//Log.i("TM", "createResourceTexture NEED TO CHECK WIDTH HERE" + resourceId + ", " + width + ", " + height);
    	if (texture == null) {
        	//Log.i("TM", "createBitmapTexture " + name + " " + width + "," + height);
    		texture = new BitmapTexture(name, width, height);
    		mTextures.put(name, texture);
    	} else {
        	Log.i("TM", "did not create a bitmap texture.. is this good?");
    	}
    	return texture;
    }
/*
    private static BitmapTexture createBitmapTexture(String name, Bitmap bitmap) {
    	BitmapTexture texture = mBitmapTextures.get(name);
    	if (texture == null) {
    		texture = new BitmapTexture(name, bitmap);
    		mBitmapTextures.put(name, texture);
    	} else {
        	Log.i("TM", "did not create a bitmap texture.. not good?");
    	}
    	return texture;
    }
*/
    public static void loadTextures(GL10 gl) {
    	/*
    	Log.i("TM", "IS LOAD TEXTURES CALLED?");
    	if (mFreeGl != null) {
    		Log.i("TM", "ERROR ERRROR mFreeGl wasnt null so not free?");
    	}*/
    	/*
		for (ResourceTexture texture : mResourceTextures.values()) {
			//Log.i("TM", "Load resource texture.ID = " + texture.ID);
	    	texture.ID = createTextureFromResource(gl, texture.getResourceId());
    	}*/

		for (Texture texture : mTextures.values()) {
			//Log.i("TM", "Load bitmap texture.ID = " + texture.ID);
			if (texture instanceof ResourceTexture) {
				texture.ID = createTextureFromResource(gl, ((ResourceTexture) texture).getResourceId());
			} else if (texture instanceof BitmapTexture) {
				texture.ID = createTextureFromBitmap(gl, ((BitmapTexture) texture).getBitmap() );
			}
    	}

		//mFreeGl = gl;
	}

	public static void freeTextures(GL10 gl) {

		//gl = mFreeGl;
		final int[] textureToDelete = new int[1];
		
		/*
    	//Texture texture = sprite.getTexture();
		for (ResourceTexture texture : mResourceTextures.values()) {
			//Log.i("TM", "Free Resource texture.ID = " + texture.ID);
			if (texture.ID != -1) {
				textureToDelete[0] = texture.ID;
				gl.glDeleteTextures(1, textureToDelete, 0);
				//texture.free();
				texture.ID = -1;
			}
    	}*/

		//mResourceTextures.clear();


		for (Texture texture : mTextures.values()) {
			//Log.i("TM", "Free Bitmap texture.ID " + texture.ID);
			if (texture.ID != -1) {
				textureToDelete[0] = texture.ID;
				gl.glDeleteTextures(1, textureToDelete, 0);

				//texture.free();

				texture.ID = -1;
			}
    	}

		//mBitmapTextures.clear();

		//mFreeGl = null;
	}
	
	public static void clear() {
		//mResourceTextures.clear();
		mTextures.clear();
	}
/*
	public static void recycle() {

		for (BitmapTexture texture : mBitmapTextures.values()) {
			texture.getBitmap().recycle();
    	}

		mBitmapTextures.clear();

		//mFreeGl = null;
	}
*/
/*
	private static Bitmap createBitmapFromResource(int resourceId) {
		Bitmap bitmap = null;
        if (mResources != null) {

            InputStream is = mResources.openRawResource(resourceId);

            try {
                bitmap = BitmapFactory.decodeStream(is, null, sBitmapOptions);
            } catch (Exception e) { 
            	Log.i("TextureManager", "createTextureFromResource " + e);
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                	Log.e("TextureManager", "createTextureFromResource " + e);
                }
            }

        }

        return bitmap;
    }
*/
	
	private static int createTextureFromResource(GL10 gl, int resourceId) {
        int textureId = -1;
        if (mResources != null && gl != null) {

            InputStream is = mResources.openRawResource(resourceId);

            try {
                final Bitmap bitmap = BitmapFactory.decodeStream(is, null, sBitmapOptions);
                textureId = createTextureFromBitmap(gl, bitmap);
                bitmap.recycle();
            } catch (Exception e) { 
            	Log.i("TextureManager", "createTextureFromResource " + e);
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                	Log.e("TextureManager", "createTextureFromResource " + e);
                }
            }

        }

        return textureId;
    }
/*
	private static int createTextureFromBitmap(GL10 gl, Bitmap bitmap) {
		return createTextureFromBitmap(gl, bitmap, GL10.GL_REPEAT);
	}
*/
	/**
	 * 
	 * @param gl
	 * @param bitmap
	 * @param textureWrapType GL10.GL_REPEAT or GL10.CLAMP_TO_EDGE 
	 * @return
	 */
    private static int createTextureFromBitmap(GL10 gl, Bitmap bitmap) {
    	int er;
        int textureId = -1;
        if (gl != null) {
            gl.glGenTextures(1, mTextureNameWorkspace, 0);

            Log.i("opengl es", "version = " + gl.glGetString(GL10.GL_VERSION));
            textureId = mTextureNameWorkspace[0];
            gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
            er = gl.glGetError();
            if (er != GL10.GL_NO_ERROR)
                Log.e("TextureManager", " A ERROR " + er);

            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
            er = gl.glGetError();
            if (er != GL10.GL_NO_ERROR)
                Log.e("TextureManager", " B ERROR " + er);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
            er = gl.glGetError();
            if (er != GL10.GL_NO_ERROR)
                Log.e("TextureManager", " C ERROR " + er);
/*
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_NEAREST);//GL_REPEAT
            er = gl.glGetError();
            if (er != GL10.GL_NO_ERROR)
                Log.e("TextureManager", " D ERROR " + er);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_NEAREST);//GL10.GL_CLAMP_TO_EDGE
            er = gl.glGetError();
            if (er != GL10.GL_NO_ERROR)
                Log.e("TextureManager", " E ERROR " + er);
*/
            gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_REPLACE);
            er = gl.glGetError();
            if (er != GL10.GL_NO_ERROR)
                Log.e("TextureManager", " F ERROR " + er);

            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);


            int error = gl.glGetError();
            if (error != GL10.GL_NO_ERROR) {
                Log.e("TextureManager", "Texture Load GLError: " + error + " bitmap w: " + bitmap.getWidth() + " h: " + bitmap.getHeight());
            }
        
        }

        return textureId;
    }
}
