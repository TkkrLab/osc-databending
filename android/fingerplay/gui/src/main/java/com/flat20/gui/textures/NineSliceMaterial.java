package com.flat20.gui.textures;

/**
 * Material determines how the texture is draw on the sprite.
 * store vertex grid.
 * store text coords here?  
 * @author andreas
 *
 */
public class NineSliceMaterial extends Material {

	public int outerStartX;
	public int outerStartY;
	public int outerEndX;
	public int outerEndY;
	public int innerStartX;
	public int innerStartY;
	public int innerEndX;
	public int innerEndY;

	// MATERIALS CAN BE RE-USED. They're more like GridFactories
	public NineSliceMaterial(Texture texture,
		int outerStartX, int innerStartX, int innerEndX, int outerEndX,
		int outerStartY, int innerStartY, int innerEndY, int outerEndY) {

		super(texture);

		if (texture == null) {
			throw new Error("Texture can not be null");
		}

		this.outerStartX = outerStartX;
		this.outerStartY = outerStartY;
		this.outerEndX = outerEndX;
		this.outerEndY = outerEndY;
		this.innerStartX = innerStartX;
		this.innerStartY = innerStartY;
		this.innerEndX = innerEndX;
		this.innerEndY = innerEndY;
	}

	public NineSliceMaterial(Texture texture, int innerStartX, int innerStartY, int innerEndX, int innerEndY) {
		super(texture);

		this.outerStartX = 0;
		this.outerStartY = 0;
		this.outerEndX = texture.width;
		this.outerEndY = texture.height;
		this.innerStartX = innerStartX;
		this.innerStartY = innerStartY;
		this.innerEndX = innerEndX;
		this.innerEndY = innerEndY;
	}

	@Override
	public Grid createGrid() {
		return new NineSliceGrid(texture, innerStartX, innerStartY, innerEndX, innerEndY, outerStartX, outerStartY, outerEndX, outerEndY);
	}

	
	// Grid

	protected class NineSliceGrid extends Grid {

		protected int mInnerStartX;
		protected int mInnerStartY;
		protected int mInnerEndX;
		protected int mInnerEndY;
		protected int mOuterStartX;
		protected int mOuterStartY;
		protected int mOuterEndX;
		protected int mOuterEndY;

		public NineSliceGrid(Texture texture,
	    		int innerStartX, int innerStartY, int innerEndX, int innerEndY) {
			super(4, 4);

			//final int[] textureCoords = {0, innerStartX, innerEndX, texture.width, 0, innerStartY, innerEndY, texture.height};
	        create9SliceGrid(texture, 	0, innerStartX, innerEndX, texture.width, 
	        							0, innerStartY, innerEndY, texture.height);
		}

		//texture is 32x32. no spriteHeight here????
		public NineSliceGrid(Texture texture, 
				int innerStartX, int innerStartY, int innerEndX, int innerEndY,
				int outerStartX, int outerStartY, int outerEndX, int outerEndY) {
			super(4, 4);

			//final int[] textureCoords = {outerStartX, innerStartX, innerEndX, outerEndX, outerStartY, innerStartY, innerEndY, outerEndY};
			create9SliceGrid(texture, 	outerStartX, innerStartX, innerEndX, outerEndX,
										outerStartY, innerStartY, innerEndY, outerEndY);
		}

		protected void create9SliceGrid(Texture texture,
				int outerStartX, int innerStartX, int innerEndX, int outerEndX,
				int outerStartY, int innerStartY, int innerEndY, int outerEndY) {

			mInnerStartX = innerStartX;
			mInnerStartY = innerStartY;
			mInnerEndX = innerEndX;
			mInnerEndY = innerEndY;
			mOuterStartX = outerStartX;
			mOuterStartY = outerStartY;
			mOuterEndX = outerEndX;
			mOuterEndY = outerEndY;

			//Log.i("nsg", "texture w: " + texture.width + ", h: " + texture.height);
			//Log.i("nsg", mOuterStartX + ", " + mInnerStartX + ", " + mInnerEndX + ", " + mOuterEndX + ", " + mOuterStartY + ", " + mInnerStartY + ", " + mInnerEndX + ", " + mOuterEndY);
	/*
	        // sprite coordinates
	        int[] sx = {
	        		textureCoords[0],
	        		textureCoords[1] - textureCoords[0],
	        		textureCoords[3] - textureCoords[2],
	        		textureCoords[3]
	        };
	        int[] sy = {
	        		textureCoords[4],
	        		textureCoords[5] - textureCoords[4],
	        		textureCoords[7] - textureCoords[6],
	        		textureCoords[7]
	        };

	        // openGL textures coordinates
	        float[] tx = {
	        		(float)textureCoords[0] / texture.width,
	        		(float)textureCoords[1] / texture.width,
	        		(float)textureCoords[2] / texture.width,
	        		(float)textureCoords[3] / texture.width
	        };

	        // openGL y has 0.0 at the bottom and 1.0 at the top so we flip the values.
	        float[] ty = {
	        		(float)textureCoords[7] / texture.height,
	        		(float)textureCoords[6] / texture.height,
	        		(float)textureCoords[5] / texture.height,
	        		(float)textureCoords[4] / texture.height
	        };
	*/

	        // sprite coordinates
			// ???? 
	        int[] sx = {
	        		0,
	        		innerStartX - outerStartX,
	        		innerEndX - outerStartX,
	        		outerEndX - outerStartX
	        };
	        int[] sy = {
	        		0,
	        		innerStartY-outerStartY,
	        		innerEndY-outerStartY,
	        		outerEndY-outerStartY
	        };

	        // openGL textures coordinates
	        float[] tx = {
	        		(float)outerStartX / texture.width,
	        		(float)innerStartX / texture.width,
	        		(float)innerEndX / texture.width,
	        		(float)outerEndX / texture.width
	        };

	        // openGL y has 0.0 at the bottom and 1.0 at the top so we flip the values.
	        //float one = 1.0f / texture.height;

	        float[] ty = {
	        		(float)outerEndY / texture.height,
	        		(float)innerEndY / texture.height,
	        		(float)innerStartY / texture.height,
	        		(float)outerStartY / texture.height
	        };

	        //Log.i("nsg", "sx: " + sx[0] + ", " + sx[1] + ", " + sx[2] + ", " + sx[3]);
	        //Log.i("nsg", "sy: " + sy[0] + ", " + sy[1] + ", " + sy[2] + ", " + sy[3]);

	        //Log.i("nsg", "tx: " + tx[0] + ", " + tx[1] + ", " + tx[2] + ", " + tx[3]);
	        //Log.i("nsg", "ty: " + ty[0] + ", " + ty[1] + ", " + ty[2] + ", " + ty[3]);

	        set(0, 0, sx[0], sy[0], 0.0f, tx[0], ty[0]);
	        set(1, 0, sx[1], sy[0], 0.0f, tx[1], ty[0]);
	        set(2, 0, sx[2], sy[0], 0.0f, tx[2], ty[0]);
	        set(3, 0, sx[3], sy[0], 0.0f, tx[3], ty[0]);

	        set(0, 1, sx[0], sy[1], 0.0f, tx[0], ty[1]);
	        set(1, 1, sx[1], sy[1], 0.0f, tx[1], ty[1]);
	        set(2, 1, sx[2], sy[1], 0.0f, tx[2], ty[1]);
	        set(3, 1, sx[3], sy[1], 0.0f, tx[3], ty[1]);

	        set(0, 2, sx[0], sy[2], 0.0f, tx[0], ty[2]);
	        set(1, 2, sx[1], sy[2], 0.0f, tx[1], ty[2]);
	        set(2, 2, sx[2], sy[2], 0.0f, tx[2], ty[2]);
	        set(3, 2, sx[3], sy[2], 0.0f, tx[3], ty[2]);

	        set(0, 3, sx[0], sy[3], 0.0f, tx[0], ty[3]);
	        set(1, 3, sx[1], sy[3], 0.0f, tx[1], ty[3]);
	        set(2, 3, sx[2], sy[3], 0.0f, tx[2], ty[3]);
	        set(3, 3, sx[3], sy[3], 0.0f, tx[3], ty[3]);
	    }

		@Override
		public void setSize(int width, int height) {

			//final float[] vertexArray = mVertexArray;

			// outer x
			mVertexBuffer.put(9, width);
			mVertexBuffer.put(21, width);
			mVertexBuffer.put(33, width);
			mVertexBuffer.put(45, width);

			// inner x in opengl space (as opposed to innerEndX which is on the Texture.
			final int vertexInnerEndX = width - (mOuterEndX - mInnerEndX);
			mVertexBuffer.put(6, vertexInnerEndX);
			mVertexBuffer.put(18, vertexInnerEndX);
			mVertexBuffer.put(30, vertexInnerEndX);
			mVertexBuffer.put(42, vertexInnerEndX);

			// outer y
			mVertexBuffer.put(36+1, height); //12*3 + 1
			mVertexBuffer.put(39+1, height);
			mVertexBuffer.put(42+1, height);
			mVertexBuffer.put(45+1, height); // 4*3 per rad

			// inner y in opengl space.
			final int vertexInnerEndY = height - (mOuterEndY - mInnerEndY);
			mVertexBuffer.put(24+1, vertexInnerEndY); //12*3 + 1
			mVertexBuffer.put(27+1, vertexInnerEndY);
			mVertexBuffer.put(30+1, vertexInnerEndY);
			mVertexBuffer.put(33+1, vertexInnerEndY);
			

			mNeedsVertexBufferUpdate = true;
			//updateVertexBuffer();
		}

	}
}
