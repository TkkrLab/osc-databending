package com.flat20.gui.textures;

/**
 * Repeates a Texture over width and height of the Grid.
 * @author andreas
 *
 */
public class TiledMaterial extends Material {

	public TiledMaterial(Texture texture) {
		super(texture);
	}

	@Override
	public Grid createGrid() {
		return new TiledGrid(texture);
	}

	
	// Grid

	final protected class TiledGrid extends Grid {

		public TiledGrid(Texture texture) {
			super(2, 2);

	        createSimpleGrid(texture);
		}

		// Should be in Grid class?
	    protected void createSimpleGrid(Texture texture) {
	    	set(0, 0, 0.0f, 0.0f, 					 0.0f,	0.0f, 1.0f);
	    	set(1, 0, texture.width, 0.0f, 			 0.0f, 	1.0f, 1.0f);
	    	set(0, 1, 0.0f, texture.height, 		 0.0f, 	0.0f, 0.0f);
	    	set(1, 1, texture.width, texture.height, 0.0f, 	1.0f, 0.0f );
	    }

	    @Override
		public void setSize(int width, int height) {
			//final float[] vertexArray = mVertexArray;
			mVertexBuffer.put(3, width);//vertexArray[3] = width;
			mVertexBuffer.put(9, width);
			mVertexBuffer.put(6+1, height);
			mVertexBuffer.put(9+1, height);

			//Log.i("TiledMaterial.Grid", " TiledGridrewind?? w = " + width);

			mNeedsVertexBufferUpdate = true;

			//final float[] texCoordArray = mTexCoordArray;
			float tEndX = (float)width / texture.width;
			float tEndY = (float)height / texture.height;
			//texCoordArray[2] = tEndX;
			mTexCoordBuffer.put(1, tEndY); // tex are upside down?

			mTexCoordBuffer.put(2, tEndX);
			mTexCoordBuffer.put(3, tEndY); // tex are upside down?

			mTexCoordBuffer.put(6, tEndX);

			mNeedsTexCoordBufferUpdate = true;
			//updateVertexBuffer();
		}
	}
}
