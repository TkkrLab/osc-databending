package com.flat20.gui.sprites;

import com.flat20.gui.textures.Grid;
import com.flat20.gui.textures.Texture;
import com.flat20.gui.textures.ZMaterial;

public class ZSprite {

	public boolean visible = true;
	public int x;
	public int y;
	public int z = 0;

	public Grid grid;
	public ZMaterial material;

	public ZSprite(ZMaterial material, int width, int height) {
		this.material = material;
		grid = material.createGrid();
		grid.setSize(width, height);
	}

	public Texture getTexture() {
		return material.texture;
	}

	public Grid getGrid() {
		return grid;
	}

}
