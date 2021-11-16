package com.flat20.gui.textures;

import javax.microedition.khronos.opengles.GL10;

/**
 * Is likely to choke on anything but square textures 
 * 
 * @author andreas
 *
 */
public class CircleMesh extends Mesh {

    private int mSegments;
    private float mTextureCenterX;
    private float mTextureCenterY;
    private float mTextureRadius;

	private int mVisibleIndexCount;


	public CircleMesh(Texture texture, int segments, int centerX, int centerY, int radius) {
		System.out.println("CircleMesh " + segments + "," + centerX + ", " + centerY + ", " +  radius);
		init(segments, centerX/(float)texture.width, centerY/(float)texture.height, radius/(float)texture.width);
	}

    public CircleMesh(int segments, float textureCenterX, float textureCenterY, float textureRadius) {
    	init(segments, textureCenterX, textureCenterY, textureRadius);
    }
    
    private void init(int segments, float textureCenterX, float textureCenterY, float textureRadius) {
    	
    	System.out.println("CircleMesh init()" + segments + ", " + textureCenterX + ", " + textureCenterY + ", " + textureRadius);

    	mSegments = segments;
    	mTextureCenterX = textureCenterX;
    	mTextureCenterY = textureCenterY;
    	mTextureRadius = textureRadius;

		int size = (segments+1);

		final int indexCount = size*3;
        char[] indexArray = new char[indexCount];
		int indPos = 0;
		for (int i=0; i<segments; i++) {

			indexArray[indPos] = 0;
			indPos++;
			indexArray[indPos] = (char)(i+1);
			indPos++;
			if (i == (segments-1))
				indexArray[indPos] = 1;
			else
				indexArray[indPos] = (char)(i+2);
			indPos++;
		}

		createBuffers(size, indexArray);

        mVisibleIndexCount = mIndexCount;

        createCircle(64, 64);
    }

    private void createCircle(int width, int height) {

		final double angFull = 360.0;
		final double angChunk = angFull/mSegments;
		final double centX = (double)(width/2);
		final double centY = (double)(height/2);

		// set first vertex to centre.
		set(0, 0, 0, 0, mTextureCenterX, mTextureCenterY);

    	int i;
    	int segmentIndex = mSegments + ((mSegments/8)*2); // to make it start from the top. /4 is the same? 
    	for (i=0; i<mSegments; i++) {

    		double curAng = (segmentIndex%mSegments)*angChunk;
    		double rad = curAng*(Math.PI/180);

    		float x = (float) (Math.cos(rad)*centX);
    		float y = (float) (Math.sin(rad)*centY);

            float u = mTextureCenterX + (float)(Math.cos(rad) * mTextureRadius);
            float v = mTextureCenterY + (float)(Math.sin(rad) * mTextureRadius);

            set(i+1, x, y, 0.0f, u, v);
            System.out.println("CircleMesh " + i + ": " + x + ", " + y + " -- " + u + ", " + v);

            segmentIndex++;
    	}

    }

    public void setVisibleSegments(int numVisibleSegments) {
    	mVisibleIndexCount = numVisibleSegments*3;
    }
    
    public int getVisibleSegments() {
    	return mVisibleIndexCount;
    }

    public int getNumSegments() {
    	return mSegments;
    }


	public void setSize(int width, int height) {
		createCircle(width, height);
		mNeedsVertexBufferUpdate = true;
		mNeedsTexCoordBufferUpdate = true;
	}

    @Override
    public void draw(GL10 gl) {

    	// Only draw X visible segments. 
    	int oldCount = mIndexCount;
    	mIndexCount = mVisibleIndexCount;

    	super.draw(gl);

    	mIndexCount = oldCount;

    }

}
