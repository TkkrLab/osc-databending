package com.flat20.gui;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.flat20.gui.animations.AnimationManager;
import com.flat20.gui.sprites.Sprite;
import com.flat20.gui.sprites.SpriteContainer;
import com.flat20.gui.textures.TextureManager;

import android.opengl.GLSurfaceView;

public class Renderer implements GLSurfaceView.Renderer {

	public static int VIEWPORT_WIDTH;
	public static int VIEWPORT_HEIGHT;

	// cache gl
	//protected GL10 mGl;

	// Our display list root 
	final protected SpriteContainer mSpriteContainer;

    public Renderer(int viewportWidth, int viewportHeight) {
    	VIEWPORT_WIDTH = viewportWidth;
    	VIEWPORT_HEIGHT = viewportHeight;

    	mSpriteContainer = new SpriteContainer();
    	//mCamera = new Camera(0,0);
    	//mContext = context;
    	//TextureManager.setResources(context.getResources());
    	//mSprites = new ArrayList<Sprite>();
    }

    public void addSprite(Sprite sprite) {
    	mSpriteContainer.addSprite(sprite);
    }

	public SpriteContainer getSpriteContainer() {
		return mSpriteContainer;
	}

    public void onDrawFrame(GL10 gl) {

    	//ProfileRecorder.sSingleton.start(ProfileRecorder.PROFILE_FRAME);

    	// TODO Push all sprites to a render list sorted by their texture
    	// so we can draw all sprites with the same texture at once.
    	// need to store their globalX and Y because we can't use
    	// nested "gl.glTranslatef(x, y, 0)"
    	// Will need to use Z as well.
/*
    	ArrayList<Sprite> displayList = new ArrayList<Sprite>();
    	mSpriteContainer.buildDisplayList(displayList, mSpriteContainer.x, mSpriteContainer.y);

    	Log.i("Renderer", "displayList.size = " + displayList.size());
*/
    	AnimationManager.getInstance().updateFrame();

    	mSpriteContainer.globalX = mSpriteContainer.x;
    	mSpriteContainer.globalY = mSpriteContainer.y;
    	mSpriteContainer.draw(gl);

		//ProfileRecorder.sSingleton.stop(ProfileRecorder.PROFILE_FRAME);
    	//ProfileRecorder.sSingleton.endFrame(); 

		//Performance.end(); // ~72 00000
    }
/*
	public int[] getConfigSpec() {
        int[] configSpec = { EGL10.EGL_DEPTH_SIZE, 0, EGL10.EGL_NONE };
        return configSpec;
	}
*/


	public void onSurfaceChanged(GL10 gl, int width, int height) {

        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrthof(0.0f, width, 0.0f, height, 0.0f, 256.0f); // z can be 0.0f to -256.0f

        //gl.glColor4x(0x10000, 0x10000, 0x10000, 0x10000); //Fixed numbers, so 1.0f. alpha is last.

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

	public void onSurfaceCreated(GL10 gl, EGLConfig arg1) {

		// Free old resources and create new ones.



		//mGl = gl;
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);

        // Doesn't work in onDraw?? 
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

        gl.glShadeModel(GL10.GL_FLAT);
        gl.glDisable(GL10.GL_DEPTH_TEST);
        gl.glEnable(GL10.GL_TEXTURE_2D);
        /*
         * By default, OpenGL enables features that improve quality but reduce
         * performance. One might want to tweak that especially on software
         * renderer.
         */
        gl.glDisable(GL10.GL_DITHER);
        gl.glDisable(GL10.GL_LIGHTING);

        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        // Blending
        gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

        // The problem is that all Grids and sprites gets re-created before
        // we can free the old ones.

        // what about calling freeSprites onResume() or onCreate()?
        
        // We have to free resources IF they've been set already.
        freeResources(gl);

        // Then load them.
		loadResources(gl);
		
		//from spritemethodtest
		
		// oncreated()
		// grid.forgethardwarebuffers
		// reload sprites 
		// grid.generatehardwarebuffers
		
		// shutdown method
		//  gl.deleteTextures.
		// grid.freeHardwarebuffers
		
    }

	protected void loadResources(GL10 gl) {
		TextureManager.loadTextures(gl);
		//GridManager.loadGrids(gl);
		mSpriteContainer.load(gl);
		//mGl = gl;
	}

	protected void freeResources(GL10 gl) {
		TextureManager.freeTextures(gl);

		//no point. mSpritecontainer gets cleared before freeResources ever gets called.

		mSpriteContainer.free(gl);

	}
}
