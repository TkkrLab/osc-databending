package com.flat20.gui.textures;

public class CircleMaterial extends Material {

	private int mSegments;
	private int mCenterX;
	private int mCenterY;
	private int mRadius;

	public CircleMaterial(Texture texture, int segments, int centerX, int centerY, int radius) {
		super(texture);
		mSegments = segments;
		mCenterX = centerX;
		mCenterY = centerY;
		mRadius = radius;
	}

	@Override
	public Mesh createGrid() {
		return new CircleMesh(texture, mSegments, mCenterX, mCenterY, mRadius); 
	}

/*
	
	// Grid

	protected class StretchedGrid extends Grid {
		protected int mStartX;
		protected int mStartY;
		protected int mEndX;
		protected int mEndY;

		public StretchedGrid(Texture texture, int startX, int startY, int endX, int endY) {
			super(2, 2);

			//final int[] textureCoords = {0, innerStartX, innerEndX, texture.width, 0, innerStartY, innerEndY, texture.height};
	        createStretchedGrid(texture, startX, startY, endX, endY);
		}

	    // Creates a repeated pattern but with texture offsets.
	    public void createStretchedGrid(Texture texture, int startX, int startY, int endX, int endY) {
	    	float tStartX = (float) startX / texture.width;
	    	float tStartY = (float) startY / texture.height;
	    	float tEndX = (float) endX / texture.width;
	    	float tEndY = (float) endY / texture.height;
	    	
	    	//----s----e
	    	
	    	//Log.i("sgA", this + ", " + tStartX + ", " + tStartY + ", " + tEndX + ", " + tEndY + " tw: " + texture.width + ", th: " + texture.height);
	    	//Log.i("sgB", startX + ", " + startY + ", " + endX + ", " + endY);
	    	//Log.i("sgC", "0,0, " + (endX-startX) + ",0, 0," + (endY-startY));
	    	set(0, 0, 0.0f, 0.0f, 				0.0f,	tStartX, tEndY);
	    	set(1, 0, endX-startX, 0.0f, 		0.0f, 	tEndX, tEndY);
	    	set(0, 1, 0.0f, endY-startY, 		0.0f, 	tStartX, tStartY);
	    	set(1, 1, endX-startX, endY-startY, 0.0f, 	tEndX, tStartY );

	    }

	    @Override
		public void setSize(int width, int height) {
			//final float[] vertexArray = mVertexArray;
			mVertexBuffer.put(3, width);
			mVertexBuffer.put(9, width);
			mVertexBuffer.put(6+1, height);
			mVertexBuffer.put(9+1, height);

			mNeedsVertexBufferUpdate = true;
			//updateVertexBuffer();
		}
	}
*/
}
