package com.flat20.gui.sprites;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.util.Log;

import com.flat20.gui.ZRenderList;

public class ZSpriteContainer {

	final ZRenderList mRenderList = new ZRenderList();
	
	final ArrayList<ZSprite> mSprites = new ArrayList<ZSprite>();

	public void addSprite(ZSprite sprite) {
		// Do we need to do this?
		//super.addSprite(sprite);

			// Should only be called once per texture.
			//mRenderList.addTexture(((TexturedSprite)sprite).getTexture());
			//mRenderList.add((TexturedSprite) sprite);
		mSprites.add((ZSprite) sprite);
		
	}

	public void loadResources(GL10 gl) {

		mRenderList.clearTextures();

		//mRenderList.setTextures( TextureManager.getTextures() );
		for (ZSprite sprite : mSprites) {
			mRenderList.addTexture(sprite.getTexture());
			mRenderList.add(sprite);
			sprite.getGrid().generateHardwareBuffers(gl);
		}

	}

	public void draw(GL11 gl) {
		Log.e("ZSpriteContainer" ," has stuff commented out don't use..");
		//throw new Exception("ZSPRITECONTAINER has stuff commented out.. don't use?");
		
		//drawRenderList(gl, mRenderList);
	}

	/*
	 * Texture
	 *   Grid
	 *     Sprite(Texture, Grid) 
	 * 
	 * Texture
	 *   //previous
	 *   //next
	 *   mGrids
	 * 
	 * Grid
	 *   //previous
	 *   //next
	 *   mSprites
	 *   
	 * Sprite
	 *   //previous
	 *   //next
	 * 
	 * addSprite
	 *   
	 */
/*
	// List<Textures, List<Grid, List<Sprite>
    private void drawRenderList(final GL11 gl, final ZRenderList renderList) {

    	final int numUsedTextureIds = renderList.numUsedTextureIds;
    	for (int i=0; i<numUsedTextureIds; i++) {
    		final int textureID = renderList.usedTexturesIds[i];

    		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureID);
    		//System.out.println("Render texture " + i + " ID: " + textureID);

    		final int numSprites = renderList.mNumSprites[textureID];
    		final ZSprite[] sprites = renderList.mSprites[textureID];

    		gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, sprites[0].grid.mVertBufferIndex);
            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, 0);

            gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, sprites[0].grid.mTextureCoordBufferIndex);
            gl.glTexCoordPointer(2, GL11.GL_FLOAT, 0, 0);

            gl.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, sprites[0].grid.mIndexBufferIndex);

    		for (int s = 0; s < numSprites; s++) {
    			final ZSprite sprite = sprites[s];

        		//System.out.println("  sprite " + s + " = " + sprite.x + "," + sprite.y + "," + sprite.z);

        		gl.glPushMatrix();
	    		gl.glTranslatex(sprite.x, sprite.y, sprite.z);

	            gl.glDrawElements(GL11.GL_TRIANGLES, sprite.grid.mIndexCount, GL11.GL_UNSIGNED_SHORT, 0);

	            // Are these needed?
	            //gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
	            //gl.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);

	    		// crashes
	    		//sprite.getGrid().draw(gl);

	    		gl.glPopMatrix();
    		}
    	}
    }
*/
}
