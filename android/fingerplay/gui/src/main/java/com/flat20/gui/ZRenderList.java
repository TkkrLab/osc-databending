package com.flat20.gui;

import java.util.Collection;

import android.util.Log;

import com.flat20.gui.sprites.ZSprite;
import com.flat20.gui.textures.Texture;

/**
 * 
 * A list of sprites sorted by texture.
 * 
 * There's little error checking when you add a Sprite
 * to keep that method as fast as possible.
 * So ZRenderList will generate an error if you try
 * to add a Sprite before adding its Textures
 * 
 * @author andreas
 *
 */
public class ZRenderList {

	// Max Texture ID
	private final static int MAX_TEXTURE_ID = 100;

	// Max Textures ( almost same as max IDs ) 
	//private final static int MAX_TEXTURES = 100; 

	// Max Sprites per Texture. Could be set by TextureManager?
	private final static int MAX_SPRITES = 100; 

	// Textures stored by texture ID. max textureID = 100 and max 100 sprites per texture..?
	final public ZSprite[][] mSprites = new ZSprite[MAX_TEXTURE_ID][MAX_SPRITES];

	// Lookup array for texture Ids used.
	final public int[] usedTexturesIds = new int[MAX_TEXTURE_ID];
	public int numUsedTextureIds = 0;

	final public int[] mNumSprites = new int[MAX_SPRITES]; // num sprites per texture. 
	//public Sprite[] mSprites = new Sprite[100];

	// Nice version
	//public HashMap<Texture, ArrayList<TexturedSprite>> mTextureSpritesList = new HashMap<Texture, ArrayList<TexturedSprite>>();

	/**
	 * Populates the internal Texture list with the
	 * specified textures.
	 */
	final public void setTextures(Collection<Texture> textures) {
		numUsedTextureIds = 0;
		for (Texture texture : textures) {
			addTexture(texture);
		}
	}

	final public boolean addTexture(final Texture texture) {
		if (hasTexture(texture))
			return false;

		final int id = texture.ID;
		Log.i("ZRenderList", "addTexture id = " + id + " max = " + MAX_TEXTURE_ID + ", maxsprites = " + MAX_SPRITES);
		usedTexturesIds[numUsedTextureIds++] = id;
		mNumSprites[id] = 0;
		return true;
	}

	final public boolean hasTexture(final Texture texture) {
		final int id = texture.ID;	
		for (int i=0; i<numUsedTextureIds; i++) {
			if (usedTexturesIds[i] == id)
				return true;
		}
		return false;
	}

	final public void clearTextures() {
		clearSprites();
		numUsedTextureIds = 0;
	}

	final public void clearSprites() {
		for (int i=0; i<numUsedTextureIds; i++) {
    		int textureID = usedTexturesIds[i];
    		mNumSprites[textureID] = 0;
		}
	}

	final public void add(final ZSprite sprite) {

		final int id = sprite.getTexture().ID;
		mSprites[id][mNumSprites[id]] = sprite;
		mNumSprites[id]++;

		/*
		 * Nice version
		ArrayList<TexturedSprite> spriteList = mTextureSpritesList.get(texture);
		if (spriteList == null) {
			spriteList = new ArrayList<TexturedSprite>();
			mTextureSpritesList.put(texture, spriteList);
		}

		spriteList.add(sprite);*/
	}

	final public void remove(ZSprite sprite) {
		final int id = sprite.getTexture().ID;
		ZSprite stored = mSprites[id][mNumSprites[id]];
		if (stored == sprite) {
			mSprites[id][mNumSprites[id]] = null;
			mNumSprites[id]--;

			//TODO We could remove the Texture from the manager here.
		}
	}

}
