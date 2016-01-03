package com.me.test.game2.item;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.me.test.game2.BaseItem;
import com.me.test.game2.ItemContainer;

public class Box extends BaseItem{

	public Box(ItemContainer game) {
		super(game);
	
		TextureRegion region = new TextureRegion(game.getGame().getManager().getAtlas().findRegion("box"));

		setDrawable(new TextureRegionDrawable(region));
	}


/*	public boolean isBinded(){
		return _game.getTable().get(getLocationX(), getLocationY())==this;
	}*/
}
