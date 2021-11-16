package com.flat20.gui.textures;

/**
 * Material determines how the texture is draw on the sprite.
 * MATERIALS CAN BE RE-USED. Think of them as GridFactories
 * @author andreas
 *
 */
public abstract class Material {

	public Texture texture;

	public Material(Texture texture) {
		this.texture = texture;
	}

	public Mesh createGrid() {
		return null;
	}
}
