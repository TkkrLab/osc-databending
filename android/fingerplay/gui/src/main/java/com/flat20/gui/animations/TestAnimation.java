package com.flat20.gui.animations;

import com.flat20.gui.sprites.Sprite;

public class TestAnimation extends Animation {

	protected Sprite mSprite;

	protected int mDestWidth;
	protected int mDestHeight;
	protected float mCurrentWidth;
	protected float mCurrentHeight;

	protected int timer = 0;

	public TestAnimation(Sprite sprite, int destWidth, int destHeight) {
		mSprite = sprite;
		mDestWidth = destWidth;
		mDestHeight = destHeight;
		mCurrentWidth = sprite.width;
		mCurrentHeight = sprite.height;
	}

	@Override
	public boolean update() {
		mCurrentWidth += (mDestWidth - mCurrentWidth) / 10.0f;
		mCurrentHeight += (mDestHeight - mCurrentHeight) / 10.0f;
		mSprite.setSize((int)mCurrentWidth, (int)mCurrentHeight);
		//mSprite.setY(mSprite.y++);
		return (++timer < 20);
	}

}
