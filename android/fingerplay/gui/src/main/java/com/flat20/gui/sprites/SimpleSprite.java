package com.flat20.gui.sprites;

import com.flat20.gui.textures.StretchedMaterial;
import com.flat20.gui.textures.Texture;

public class SimpleSprite extends TexturedSprite {

	public SimpleSprite(Texture texture) {
		super(texture);

		mMesh = new StretchedMaterial(texture).createGrid();//new StretchedGrid(texture, 0, 0, texture.width, texture.height);
		mMesh.setSize(width, height);
	}

	public SimpleSprite(Texture texture, int width, int height) {
		super(texture, width, height);
		mMesh = new StretchedMaterial(texture).createGrid();
		mMesh.setSize(width, height);
	}

	@Override
	public void setSize(int width, int height) {
		super.setSize(width, height);
		mMesh.setSize(width, height);
	}
	
	

}
