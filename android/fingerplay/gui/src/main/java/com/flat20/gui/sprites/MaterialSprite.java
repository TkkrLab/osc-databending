package com.flat20.gui.sprites;

import com.flat20.gui.textures.Grid;
import com.flat20.gui.textures.Material;

/**
 * TODO Remove SimpleSprite and add a constructor in MaterialSprite which defaults
 * to StretchedMaterial if only the Texture is supplied.
 * 
 * @author andreas
 *
 */
public class MaterialSprite extends TexturedSprite {

	public MaterialSprite(Material material) {
		super(material.texture, material.texture.width, material.texture.height);
		mMesh = material.createGrid();
	}

	public MaterialSprite(Material material, int width, int height) {
		super(material.texture, width, height);
		mMesh = material.createGrid();
		setSize(width, height);
	}

	@Override
	public void setSize(int width, int height) {
		super.setSize(width, height);
		mMesh.setSize(width, height);
	}

}