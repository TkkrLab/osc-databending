package com.flat20.gui;

import com.flat20.gui.animations.AnimationManager;
import com.flat20.gui.textures.TextureManager;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * Using a GLSurfaceView with a Renderer uses 11kb every restart.
 * @author andreas
 *
 */
public class BasicActivity extends Activity {

	//protected DummyRenderer mRenderer;
	protected Renderer mRenderer;
	protected GLSurfaceView mView;

	protected int mWidth;
	protected int mHeight;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextureManager.clear();
        TextureManager.setResources( getResources() );

        // Get dimensions
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        mWidth = dm.widthPixels;
        mHeight = dm.heightPixels;

        onCreateView();

        AnimationManager.getInstance().create(mRenderer, false);
        
        onCreateGraphics();

        setContentView(mView);

    }

    protected void onCreateView() {
        Log.i("BasicActivity", "onCreateView");
    	//mRenderer = new DummyRenderer();
    	mRenderer = new Renderer(mWidth, mHeight);
        mView = new GLSurfaceView(this); // GLSurfaceView with renderer takes 11k and doesn't clear mem onResume
        mView.setRenderer(mRenderer);
        Log.i("BasicActivity", "onCreateView Done");
    }

	protected void onCreateGraphics() {
	}

    protected void onDestroyView() {
        mRenderer = null;
        mView = null;
    }

    protected void onDestroyGraphics() {
    	
    }

	@Override
	protected void onDestroy() {

		onDestroyView();
		onDestroyGraphics();

		AnimationManager.getInstance().destroy();

		TextureManager.setResources( null );
		
		super.onDestroy();

		System.exit(0);
	}

    @Override
    protected void onResume() {
        super.onResume();

        //Performance.startMemoryTrace();

        mView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        //Performance.stopMemoryTrace();

        mView.onPause();
    }

}