package com.me.test.game2.item;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.me.test.game2.BaseItem;
import com.me.test.game2.ItemContainer;

public class Metal extends BaseItem{

	public Metal(ItemContainer game) {
		super(game);
		
		TextureRegion region = new TextureRegion(game.getGame().getManager().getAtlas().findRegion("metal"));
		setDrawable(new TextureRegionDrawable(region));
	}




}
